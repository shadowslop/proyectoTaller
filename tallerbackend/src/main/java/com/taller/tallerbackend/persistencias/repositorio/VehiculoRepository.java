package com.taller.tallerbackend.persistencias.repositorio;

import com.taller.tallerbackend.persistencias.entidades.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

    List<Vehiculo> findBySedeId(UUID sedeId);

    List<Vehiculo> findByEstado(Boolean estado);

    @Query("SELECT COUNT(v) FROM Vehiculo v WHERE v.sedeId.id = :sedeId")
    long countBySedeId(@Param("sedeId") UUID sedeId);

    @Query("""
        SELECT v FROM Vehiculo v
        WHERE v.estado = true
          AND v.sedeId.id = :sedeId
          AND LOWER(v.placa) LIKE LOWER(CONCAT('%', :placa, '%'))
        """)
    List<Vehiculo> findByEstadoTrueAndSedeIdAndPlacaContainingIgnoreCase(@Param("sedeId") UUID sedeId, @Param("placa") String placa);

    @Query("SELECT v FROM Vehiculo v WHERE v.estado = true AND v.sedeId.id = :sedeId AND v.tipo.id = :tipoId")
    List<Vehiculo> findByEstadoTrueAndSedeIdAndTipoId(@Param("sedeId") UUID sedeId, @Param("tipoId") UUID tipoId);
}