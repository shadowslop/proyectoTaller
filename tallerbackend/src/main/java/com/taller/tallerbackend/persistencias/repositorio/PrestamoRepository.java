package com.taller.tallerbackend.persistencias.repositorio;
import com.taller.tallerbackend.persistencias.entidades.Prestamo;
import com.taller.tallerbackend.persistencias.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrestamoRepository extends JpaRepository<Prestamo, UUID> {

    Optional<Prestamo> findByClienteIdCedulaAndEstado(String cedula, boolean estado);

    List<Prestamo> findBySedePrestamoIdOrSedeDevolucionId(UUID sedePrestamoId, UUID sedeDevolucionId);

    List<Prestamo> findByUsuarioId (UUID usuarioId);

    List<Prestamo> findByFechaPrestamoBetween(OffsetDateTime inicio, OffsetDateTime fin);

    @Query("""
        SELECT p FROM Prestamo p
        WHERE p.sedePrestamoId.id = :sedeId
           OR p.sedeDevolucionId.id = :sedeId
           OR p.vehiculoId.sedeId.id = :sedeId
        """)
    List<Prestamo> findBySedePrestamoIdOrSedeDevolucionIdOrVehiculoIdSedeId(
            @org.springframework.data.repository.query.Param("sedeId") UUID sedeId);
    Optional<Prestamo> findByVehiculoIdPlacaAndEstado(String placa, boolean estado);
    boolean existsByVehiculoIdPlacaAndEstado(String placa, boolean estado);
    List<Prestamo> findByClienteIdCedula(String cedula);
    @Query("""
    SELECT p FROM Prestamo p
    WHERE p.estado = true
      AND p.reservaId IS NOT NULL
      AND p.fechaDevolucionEsperada < :limite
    """)
    List<Prestamo> buscarReservasVencidas(@Param("limite") OffsetDateTime limite);
}