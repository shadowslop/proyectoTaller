package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.*;
import com.taller.tallerbackend.exceptions.*;
import com.taller.tallerbackend.persistencias.entidades.*;
import com.taller.tallerbackend.persistencias.repositorio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {

    @Autowired ReservaRepository reservaRepository;
    @Autowired ClienteRepository clienteRepository;
    @Autowired VehiculoRepository vehiculoRepository;
    @Autowired SedeRepository sedeRepository;
    @Autowired AuditoriaService auditoriaService;

    // RF-29 y RF-30: toda reserva nace PENDIENTE y se confirma luego con el boton
    public ReservaResponse crear(ReservaDto datos) {
        Cliente cliente = clienteRepository.findById(datos.getClienteCedula())
                .orElseThrow(ClienteNotFoundException::new);
        Vehiculo vehiculo = vehiculoRepository.findById(datos.getVehiculoPlaca())
                .orElseThrow(VehiculoNotFoundException::new);
        Sede sede = sedeRepository.findById(datos.getSedeId())
                .orElseThrow(SedeNoEncontrada::new);

        validarPeriodo(datos.getFechaInicio(), datos.getFechaFin());
        verificarDisponibilidad(datos.getVehiculoPlaca(), datos.getFechaInicio(), datos.getFechaFin(), null);

        Reserva reserva = new Reserva(cliente, vehiculo, sede, datos.getFechaInicio(), datos.getFechaFin(), EstadoReserva.PENDIENTE);
        reserva = reservaRepository.save(reserva);

        auditoriaService.registrar("CREAR", "Reserva",
                "Reserva del vehiculo " + reserva.getVehiculoId().getPlaca()
                        + " para el cliente " + reserva.getClienteId().getCedula()
                        + " (" + reserva.getFechaInicio() + " a " + reserva.getFechaFin() + ")");
        return toResponse(reserva);
    }

    // RF-32
    public ReservaResponse modificar(UUID id, ReservaActualizarDto datos) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow(ReservaNotFoundException::new);

        if (reserva.getEstado() == EstadoReserva.CANCELADA || reserva.getEstado() == EstadoReserva.FINALIZADA) {
            throw new ReservaNoCancelableException();
        }

        validarPeriodo(datos.getFechaInicio(), datos.getFechaFin());
        verificarDisponibilidad(reserva.getVehiculoId().getPlaca(), datos.getFechaInicio(), datos.getFechaFin(), id);

        reserva.setFechaInicio(datos.getFechaInicio());
        reserva.setFechaFin(datos.getFechaFin());
        reserva = reservaRepository.save(reserva);

        auditoriaService.registrar("ACTUALIZAR", "Reserva",
                "Nuevo periodo de la reserva del vehiculo " + reserva.getVehiculoId().getPlaca()
                        + ": " + reserva.getFechaInicio() + " a " + reserva.getFechaFin());
        return toResponse(reserva);
    }

    // Confirmar: solo PENDIENTE, desde el dia antes del inicio (00:00) hasta que termine la reserva
    public ReservaResponse confirmar(UUID id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow(ReservaNotFoundException::new);

        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new ReservaNoConfirmableException("Solo se pueden confirmar reservas pendientes");
        }

        // Las fechas se guardan como "hora escrita por el usuario, etiquetada UTC":
        // se comparan como hora de pared contra la hora local actual.
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicio = reserva.getFechaInicio().withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime fin = reserva.getFechaFin().withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime habilitadaDesde = inicio.toLocalDate().minusDays(1).atStartOfDay();

        if (ahora.isBefore(habilitadaDesde)) {
            throw new ReservaNoConfirmableException("Aun no se puede confirmar: se habilita desde el dia antes de la reserva");
        }
        if (ahora.isAfter(fin)) {
            throw new ReservaNoConfirmableException("El plazo para confirmar esta reserva ya vencio");
        }

        reserva.setEstado(EstadoReserva.CONFIRMADA);
        reserva = reservaRepository.save(reserva);

        auditoriaService.registrar("CONFIRMAR", "Reserva",
                "Reserva confirmada del vehiculo " + reserva.getVehiculoId().getPlaca()
                        + " (cliente " + reserva.getClienteId().getCedula() + ")");
        return toResponse(reserva);
    }

    // RF-31
    public ReservaResponse cancelar(UUID id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow(ReservaNotFoundException::new);

        boolean yaPaso = reserva.getEstado() == EstadoReserva.CANCELADA || reserva.getEstado() == EstadoReserva.FINALIZADA;
        boolean yaInicio = !reserva.getFechaInicio().isAfter(OffsetDateTime.now());
        if (yaPaso || yaInicio) {
            throw new ReservaNoCancelableException();
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva = reservaRepository.save(reserva);

        auditoriaService.registrar("CANCELAR", "Reserva",
                "Reserva cancelada del vehiculo " + reserva.getVehiculoId().getPlaca()
                        + " (cliente " + reserva.getClienteId().getCedula() + ")");
        return toResponse(reserva);
    }

    // Eliminar: canceladas, finalizadas o cuya fecha de fin ya paso
    public void eliminar(UUID id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow(ReservaNotFoundException::new);

        boolean terminada = reserva.getEstado() == EstadoReserva.CANCELADA
                || reserva.getEstado() == EstadoReserva.FINALIZADA;
        boolean yaPasoElDia = reserva.getFechaFin().withOffsetSameInstant(ZoneOffset.UTC)
                .toLocalDate().isBefore(LocalDate.now());

        if (!terminada && !yaPasoElDia) {
            throw new ReservaNoEliminableException();
        }

        auditoriaService.registrar("ELIMINAR", "Reserva",
                "Reserva eliminada del vehiculo " + reserva.getVehiculoId().getPlaca()
                        + " (cliente " + reserva.getClienteId().getCedula() + ")");
        reservaRepository.delete(reserva);
    }

    public List<ReservaResponse> listarPorSede(UUID sedeId) {
        return reservaRepository.findBySedeIdId(sedeId).stream().map(this::toResponse).toList();
    }

    public List<ReservaResponse> listarTodas() {
        return reservaRepository.findAll().stream().map(this::toResponse).toList();
    }

    private void validarPeriodo(OffsetDateTime inicio, OffsetDateTime fin) {
        if (inicio == null || fin == null || !fin.isAfter(inicio)) {
            throw new IllegalArgumentException("La fecha/hora de fin debe ser posterior a la de inicio");
        }
    }

    // RF-30
    private void verificarDisponibilidad(String placa, OffsetDateTime inicio, OffsetDateTime fin, UUID reservaExcluidaId) {
        List<Reserva> solapadas = reservaRepository.buscarSolapadas(placa, inicio, fin, reservaExcluidaId);
        if (!solapadas.isEmpty()) {
            throw new VehiculoNoDisponibleEnPeriodoException();
        }
    }

    private ReservaResponse toResponse(Reserva r) {
        ClienteResponse cliente = new ClienteResponse(
                r.getClienteId().getCedula(),
                r.getClienteId().getNombre(),
                r.getClienteId().getApellido(),
                r.getClienteId().getTelefono());
        VehiculoResponse vehiculo = new VehiculoResponse(
                r.getVehiculoId().getPlaca(),
                r.getVehiculoId().getTipo().getNombre(),
                r.getVehiculoId().getPrecioDia(),
                r.getVehiculoId().getEstado(),
                new SedeResponse(r.getVehiculoId().getSedeId().getId(), r.getVehiculoId().getSedeId().getNombre())
        );
        SedeResponse sede = new SedeResponse(r.getSedeId().getId(), r.getSedeId().getNombre());

        return new ReservaResponse(r.getId(), cliente, vehiculo, sede, r.getFechaInicio(), r.getFechaFin(), r.getEstado().name());
    }
}