package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.*;
import com.taller.tallerbackend.persistencias.entidades.Prestamo;
import com.taller.tallerbackend.persistencias.entidades.Reserva;
import com.taller.tallerbackend.persistencias.entidades.Vehiculo;
import com.taller.tallerbackend.persistencias.repositorio.MantenimientoRepository;
import com.taller.tallerbackend.persistencias.repositorio.PrestamoRepository;
import com.taller.tallerbackend.persistencias.repositorio.ReservaRepository;
import com.taller.tallerbackend.persistencias.repositorio.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    PrestamoRepository prestamoRepository;
    @Autowired
    ReservaRepository reservaRepository;
    @Autowired
    VehiculoRepository vehiculoRepository;
    @Autowired
    MantenimientoRepository mantenimientoRepository;

    public ReporteResponse generarReporte(OffsetDateTime inicio, OffsetDateTime fin) {
        List<Prestamo> prestamos = prestamoRepository.findByFechaPrestamoBetween(inicio, fin);

        ReporteResponse reporte = new ReporteResponse();
        reporte.setTotalPrestamos(prestamos.size());
        reporte.setPrestamosActivos(prestamos.stream().filter(Prestamo::isEstado).count());
        reporte.setPrestamosDevueltos(prestamos.stream().filter(p -> !p.isEstado()).count());

        BigDecimal montoTotal = prestamos.stream()
                .filter(p -> !p.isEstado()) // solo los devueltos tienen monto real
                .map(Prestamo::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        reporte.setMontoTotalRecaudado(montoTotal);


        Map<String, List<Prestamo>> agrupadosPorSede = prestamos.stream()
                .collect(Collectors.groupingBy(p -> p.getSedePrestamoId().getNombre()));

        List<ReporteResponse.ReporteSede> porSede = agrupadosPorSede.entrySet().stream()
                .map(entry -> new ReporteResponse.ReporteSede(
                        entry.getKey(),
                        entry.getValue().size(),
                        entry.getValue().stream()
                                .filter(p -> !p.isEstado())
                                .map(Prestamo::getMontoTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .collect(Collectors.toList());
        reporte.setPorSede(porSede);


        Map<String, List<Prestamo>> agrupadosPorTipo = prestamos.stream()
                .collect(Collectors.groupingBy(p -> p.getVehiculoId().getTipo().getNombre()));

        List<ReporteResponse.ReporteTipoVehiculo> porTipo = agrupadosPorTipo.entrySet().stream()
                .map(entry -> new ReporteResponse.ReporteTipoVehiculo(
                        entry.getKey(),
                        entry.getValue().size(),
                        entry.getValue().stream()
                                .filter(p -> !p.isEstado())
                                .map(Prestamo::getMontoTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .collect(Collectors.toList());
        reporte.setPorTipoVehiculo(porTipo);

        return reporte;
    }

    public ReporteVehiculosResponse reporteVehiculos() {
        LocalDate hoy = LocalDate.now();

        List<VehiculoResponse> enMantenimiento = vehiculoRepository.findAll().stream()
                .filter(v -> mantenimientoRepository.existeParaVehiculoEnFecha(v.getPlaca(), hoy))
                .map(this::toVehiculoResponse).toList();

        List<VehiculoResponse> disponibles = vehiculoRepository.findByEstado(true).stream()
                .filter(v -> enMantenimiento.stream().noneMatch(m -> m.getPlaca().equals(v.getPlaca())))
                .map(this::toVehiculoResponse).toList();

        List<VehiculoResponse> alquilados = vehiculoRepository.findByEstado(false).stream()
                .map(this::toVehiculoResponse).toList();

        return new ReporteVehiculosResponse(disponibles, alquilados, enMantenimiento);
    }

    private VehiculoResponse toVehiculoResponse(Vehiculo v) {
        return new VehiculoResponse(
                v.getPlaca(),
                v.getTipo().getNombre(),
                v.getPrecioDia(),
                v.getEstado(),
                new SedeResponse(v.getSedeId().getId(), v.getSedeId().getNombre()));
    }

    public ReporteAgendaResponse generarReporteAgenda(OffsetDateTime inicio, OffsetDateTime fin) {
        List<Reserva> reservas = reservaRepository.findByFechaInicioBetween(inicio, fin);

        ReporteAgendaResponse reporte = new ReporteAgendaResponse();
        reporte.setTotalReservas(reservas.size());
        reporte.setConfirmadas(reservas.stream().filter(r -> r.getEstado() == com.taller.tallerbackend.persistencias.entidades.EstadoReserva.CONFIRMADA).count());
        reporte.setPendientes(reservas.stream().filter(r -> r.getEstado() == com.taller.tallerbackend.persistencias.entidades.EstadoReserva.PENDIENTE).count());
        reporte.setFinalizadas(reservas.stream().filter(r -> r.getEstado() == com.taller.tallerbackend.persistencias.entidades.EstadoReserva.FINALIZADA).count());
        reporte.setCanceladas(reservas.stream().filter(r -> r.getEstado() == com.taller.tallerbackend.persistencias.entidades.EstadoReserva.CANCELADA).count());

        // Agrupado por dia de la semana, en orden Lunes -> Domingo (no alfabetico)
        Map<DayOfWeek, Long> conteoPorDia = reservas.stream()
                .collect(Collectors.groupingBy(r -> r.getFechaInicio().getDayOfWeek(), Collectors.counting()));

        List<ReporteAgendaResponse.ReporteAgendaItem> porDiaSemana = new ArrayList<>();
        for (DayOfWeek dia : DayOfWeek.values()) {
            porDiaSemana.add(new ReporteAgendaResponse.ReporteAgendaItem(nombreDia(dia), conteoPorDia.getOrDefault(dia, 0L)));
        }
        reporte.setPorDiaSemana(porDiaSemana);

        // Agrupado por mes (yyyy-MM), ordenado cronologicamente
        Map<String, Long> conteoPorMes = reservas.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getFechaInicio().getYear() + "-" + String.format("%02d", r.getFechaInicio().getMonthValue()),
                        Collectors.counting()));

        List<ReporteAgendaResponse.ReporteAgendaItem> porMes = conteoPorMes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new ReporteAgendaResponse.ReporteAgendaItem(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        reporte.setPorMes(porMes);

        return reporte;
    }

    private String nombreDia(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miercoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sabado";
            case SUNDAY -> "Domingo";
        };
    }


}