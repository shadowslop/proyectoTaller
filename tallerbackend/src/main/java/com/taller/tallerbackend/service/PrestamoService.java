package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.*;
import com.taller.tallerbackend.exceptions.*;
import com.taller.tallerbackend.persistencias.entidades.*;
import com.taller.tallerbackend.persistencias.repositorio.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class PrestamoService {

    @Autowired
    PrestamoRepository prestamoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VehiculoRepository vehiculoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    SedeRepository sedeRepository;
    @Autowired
    DocumentoRepository documentoRepository;
    @Autowired
    MantenimientoRepository mantenimientoRepository;
    @Autowired
    AuditoriaService auditoriaService;
    @Autowired
    ReservaRepository reservaRepository;

    @Transactional
    public PrestamoResponse crearPrestamo(PrestamoRequest datos) {

        Cliente cliente = clienteRepository.findById(datos.getClienteCedula())
                .orElseThrow(() -> new ClienteNotFoundException());

        Vehiculo vehiculo = vehiculoRepository.findById(datos.getVehiculoPlaca())
                .orElseThrow(() -> new VehiculoNotFoundException());

        Usuario usuario = usuarioRepository.findById(datos.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException());

        Sede sedePrestamo = sedeRepository.findById(datos.getSedePrestamoId())
                .orElseThrow(() -> new SedeNoEncontrada());

        boolean tieneActivo = prestamoRepository.findByClienteIdCedulaAndEstado(cliente.getCedula(), true)
                .isPresent();
        if (tieneActivo) {
            throw new ClientePestramoAlreadyActivedException();
        }

        if (!vehiculo.getEstado()) {
            throw new VehiculoAlreadyUsedException();
        }

        if (mantenimientoRepository.existeParaVehiculoEnFecha(vehiculo.getPlaca(), java.time.LocalDate.now())) {
            throw new VehiculoEnMantenimientoException();
        }

        List<Documento> documentos = documentoRepository.findByVehiculoIdPlaca(vehiculo.getPlaca());

        List<TipoDocumento> requeridos = List.of(
                TipoDocumento.SOAT, TipoDocumento.TECNOMECANICA, TipoDocumento.SEGURO_TODO_RIESGO);
        boolean faltaAlgunDocumento = requeridos.stream()
                .anyMatch(tipo -> documentos.stream().noneMatch(d -> d.getTipoDocumento() == tipo));
        if (faltaAlgunDocumento) {
            throw new DocumentosIncompletosException();
        }

        OffsetDateTime ahora = OffsetDateTime.now();
        boolean tieneDocumentoVencido = documentos.stream()
                .anyMatch(d -> d.getFechaVencimiento().isBefore(ahora));
        if (tieneDocumentoVencido) {
            throw new DocumentosVencidosException();
        }
        Prestamo prestamo = new Prestamo();
        prestamo.setClienteId(cliente);
        prestamo.setVehiculoId(vehiculo);
        prestamo.setUsuarioId(usuario);
        prestamo.setSedePrestamoId(sedePrestamo);
        prestamo.setSedeDevolucionId(null);
        prestamo.setFechaPrestamo(ahora);
        prestamo.setFechaDevolucion(null);
        prestamo.setEstado(true);
        prestamo.setMontoTotal(BigDecimal.ZERO);

        vehiculo.setEstado(false);
        vehiculoRepository.save(vehiculo);

        Prestamo guardado = prestamoRepository.save(prestamo);

        auditoriaService.registrar(usuario.getNombre(), "CREAR", "Prestamo",
                "Préstamo del vehículo " + vehiculo.getPlaca() + " al cliente " + cliente.getCedula());

        return toResponse(guardado);
    }

    @Transactional
    public PrestamoResponse devolverPrestamo(UUID prestamoId, DevolucionRequest datos) {

        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new PrestamoNotFoundException());

        if (!prestamo.isEstado()) {
            throw new PrestamoYaDevueltoException();
        }

        Sede sedeDevolucion = sedeRepository.findById(datos.getSedeDevolucionId())
                .orElseThrow(() -> new SedeNoEncontrada());
        OffsetDateTime ahora = OffsetDateTime.now();

        // RF-09/HU-03: cobro por día completo, redondeado al día superior (bloques de 24h)
        long horas = ChronoUnit.HOURS.between(prestamo.getFechaPrestamo(), ahora);
        long dias = Math.max(1, (horas + 23) / 24);

        BigDecimal tarifaDia = prestamo.getVehiculoId().getPrecioDia();
        BigDecimal monto = tarifaDia.multiply(BigDecimal.valueOf(dias));

        // RF-46: recargo por mora si viene la fecha/hora esperada de devolución
        BigDecimal recargoMora = BigDecimal.ZERO;
        if (datos.getFechaDevolucionEsperada() != null
                && ahora.isAfter(datos.getFechaDevolucionEsperada())) {
            long horasMora = ChronoUnit.HOURS.between(datos.getFechaDevolucionEsperada(), ahora);
            long diasMora = Math.max(1, (horasMora + 23) / 24);
            recargoMora = tarifaDia.multiply(BigDecimal.valueOf(diasMora))
                    .multiply(new BigDecimal("0.5")); // 50% de la tarifa diaria por día de mora
            monto = monto.add(recargoMora);
        }

        prestamo.setFechaDevolucion(ahora);
        prestamo.setSedeDevolucionId(sedeDevolucion);
        prestamo.setEstado(false);
        prestamo.setMontoTotal(monto);

        Vehiculo vehiculo = prestamo.getVehiculoId();
        vehiculo.setEstado(true);
        if (vehiculo.getSedeId() == null || !vehiculo.getSedeId().getId().equals(sedeDevolucion.getId())) {
            vehiculo.setSedeId(sedeDevolucion);
        }
        vehiculoRepository.save(vehiculo);
        prestamo.setKmFinal(datos.getKmFinal());
        prestamo.setCombFinal(datos.getCombFinal());
        if (prestamo.getKmInicial() != null && datos.getKmFinal() != null) {
            prestamo.setDiferenciaKm(datos.getKmFinal() - prestamo.getKmInicial());
        }

        prestamo.setNovedades(datos.getNovedades());
        EstadoVehiculo estadoFinal = datos.getEstadoFinalVehiculo() != null
                ? datos.getEstadoFinalVehiculo() : EstadoVehiculo.DISPONIBLE;
        prestamo.setEstadoFinalVehiculo(estadoFinal);
        boolean operativo = estadoFinal == EstadoVehiculo.DISPONIBLE;
        vehiculo.setEstado(operativo);
        Prestamo guardado = prestamoRepository.save(prestamo);

        auditoriaService.registrar("DEVOLVER", "Prestamo",
                "Devolucion del vehiculo " + guardado.getVehiculoId().getPlaca()
                        + " del cliente " + guardado.getClienteId().getCedula()
                        + ". Total: $" + guardado.getMontoTotal());

        return toResponse(guardado);
    }

    public List<PrestamoResponse> listarPrestamos() {
        return prestamoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<PrestamoResponse> listarPorSede(UUID sedeId) {
        return prestamoRepository.findBySedePrestamoIdOrSedeDevolucionIdOrVehiculoIdSedeId(sedeId)
                .stream().map(this::toResponse).toList();
    }

    public List<PrestamoResponse> listarPorUsuario(UUID usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId)
                .stream().map(this::toResponse).toList();
    }

    private PrestamoResponse toResponse(Prestamo p) {
        SedeResponse sedePrestamo = new SedeResponse(p.getSedePrestamoId().getId(), p.getSedePrestamoId().getNombre());
        SedeResponse sedeDevolucion = p.getSedeDevolucionId() == null ? null :
                new SedeResponse(p.getSedeDevolucionId().getId(), p.getSedeDevolucionId().getNombre());

        VehiculoResponse vehiculo = new VehiculoResponse(
                p.getVehiculoId().getPlaca(),
                p.getVehiculoId().getTipo().getNombre(),
                p.getVehiculoId().getPrecioDia(),
                p.getVehiculoId().getEstado(),
                new SedeResponse(p.getVehiculoId().getSedeId().getId(), p.getVehiculoId().getSedeId().getNombre())
        );

        ClienteResponse cliente = new ClienteResponse(
                p.getClienteId().getCedula(),
                p.getClienteId().getNombre(),
                p.getClienteId().getApellido(),
                p.getClienteId().getTelefono()
        );

        UsuarioResponse usuario = new UsuarioResponse(
                p.getUsuarioId().getId(),
                p.getUsuarioId().getNombre(),
                p.getUsuarioId().getRol().name()
        );

        return new PrestamoResponse(
                p.getId(), cliente, vehiculo, usuario, sedePrestamo, sedeDevolucion,
                p.getFechaPrestamo(), p.getFechaDevolucion(), p.isEstado(), p.getMontoTotal(),
                p.getReservaId() != null ? p.getReservaId().getId() : null,
                p.getFechaDevolucionEsperada(), p.getRecargoMora()
        );
    }

    public PrestamoResponse buscarActivoPorPlaca(String placa) {
        Prestamo prestamo = prestamoRepository.findByVehiculoIdPlacaAndEstado(placa, true)
                .orElseThrow(() -> new PrestamoNotFoundException());
        return toResponse(prestamo);
    }

    public List<PrestamoResponse> listarPorCliente(String cedula) {
        clienteRepository.findById(cedula).orElseThrow(() -> new ClienteNotFoundException());
        return prestamoRepository.findByClienteIdCedula(cedula)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public PrestamoResponse registrarEntrega(UUID prestamoId, EntregaRequest datos) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new PrestamoNotFoundException());
        prestamo.setKmInicial(datos.getKmInicial());
        prestamo.setCombInicial(datos.getCombInicial());
        Prestamo guardado = prestamoRepository.save(prestamo);

        auditoriaService.registrar("ENTREGA", "Prestamo",
                "Entrega del vehiculo " + guardado.getVehiculoId().getPlaca()
                        + " con km inicial " + datos.getKmInicial());

        return toResponse(guardado);
    }
    // Entrega de una reserva: la convierte en prestamo reutilizando TODAS las validaciones de crearPrestamo
// (documentos, mantenimiento, vehiculo disponible, cliente sin prestamo activo).
    @Transactional
    public PrestamoResponse crearDesdeReserva(UUID reservaId, UUID usuarioId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(ReservaNotFoundException::new);

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new ReservaNoConfirmableException("Solo se puede entregar una reserva confirmada");
        }

        // Las fechas de la reserva son "hora escrita por el usuario, etiquetada UTC":
        // se comparan como hora de pared contra la hora local actual.
        LocalDateTime ahoraLocal = LocalDateTime.now();
        LocalDateTime inicio = reserva.getFechaInicio().withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime fin = reserva.getFechaFin().withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();

        if (ahoraLocal.isBefore(inicio.toLocalDate().atStartOfDay())) {
            throw new ReservaNoConfirmableException("Aun no es la fecha de la reserva");
        }
        if (ahoraLocal.isAfter(fin)) {
            throw new ReservaNoConfirmableException("La reserva ya vencio sin ser entregada");
        }

        PrestamoRequest req = new PrestamoRequest();
        req.setClienteCedula(reserva.getClienteId().getCedula());
        req.setVehiculoPlaca(reserva.getVehiculoId().getPlaca());
        req.setUsuarioId(usuarioId);
        req.setSedePrestamoId(reserva.getSedeId().getId());

        PrestamoResponse creado = crearPrestamo(req);

        // Fecha pactada de devolucion = fin de la reserva, convertida a un instante real (hora local)
        OffsetDateTime esperada = fin.atZone(ZoneId.systemDefault()).toOffsetDateTime();

        Prestamo prestamo = prestamoRepository.findById(creado.getId())
                .orElseThrow(PrestamoNotFoundException::new);
        prestamo.setReservaId(reserva);
        prestamo.setFechaDevolucionEsperada(esperada);
        prestamo.setRecargoMora(BigDecimal.ZERO);
        prestamo = prestamoRepository.save(prestamo);

        // La reserva ya se atendio: queda FINALIZADA (no se puede cancelar ni volver a entregar)
        reserva.setEstado(EstadoReserva.FINALIZADA);
        reservaRepository.save(reserva);

        auditoriaService.registrar("ENTREGAR", "Reserva",
                "Reserva entregada como prestamo: vehiculo " + reserva.getVehiculoId().getPlaca()
                        + ", cliente " + reserva.getClienteId().getCedula());

        return toResponse(prestamo);
    }

    // Alerta: prestamos de reserva aun sin devolver con mas de 7 dias de retraso
// sobre su fecha de fin de reserva (fecha de devolucion esperada).
    public List<PrestamoResponse> listarReservasVencidas() {
        OffsetDateTime limite = OffsetDateTime.now().minusDays(7);
        return prestamoRepository.findAll().stream()
                .filter(p -> p.isEstado()
                        && p.getReservaId() != null
                        && p.getFechaDevolucionEsperada() != null
                        && p.getFechaDevolucionEsperada().isBefore(limite))
                .map(this::toResponse)
                .toList();
    }
}