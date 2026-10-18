package com.taller.tallerbackend.persistencias.repositorio;

import com.taller.tallerbackend.persistencias.entidades.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    List<Reserva> findBySedeIdId(UUID sedeId);

    List<Reserva> findByClienteIdCedula(String cedula);

    List<Reserva> findByVehiculoIdPlaca(String placa);

    // RF-30: solapamiento de fechas contra reservas activas (no canceladas/finalizadas) del mismo vehiculo
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.vehiculoId.placa = :placa
            AND r.estado IN (com.taller.tallerbackend.persistencias.entidades.EstadoReserva.PENDIENTE,
                             com.taller.tallerbackend.persistencias.entidades.EstadoReserva.CONFIRMADA,
                             com.taller.tallerbackend.persistencias.entidades.EstadoReserva.EN_CURSO)
          AND r.fechaInicio < :fechaFin
          AND r.fechaFin > :fechaInicio
          AND (:reservaExcluidaId IS NULL OR r.id <> :reservaExcluidaId)
        """)
    List<Reserva> buscarSolapadas(@Param("placa") String placa,
                                  @Param("fechaInicio") OffsetDateTime fechaInicio,
                                  @Param("fechaFin") OffsetDateTime fechaFin,
                                  @Param("reservaExcluidaId") UUID reservaExcluidaId);
    List<Reserva> findByFechaInicioBetween(OffsetDateTime inicio, OffsetDateTime fin);
}