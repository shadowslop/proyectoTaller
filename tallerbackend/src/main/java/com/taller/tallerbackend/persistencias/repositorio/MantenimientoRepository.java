package com.taller.tallerbackend.persistencias.repositorio;

import com.taller.tallerbackend.persistencias.entidades.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, UUID> {

    @Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.placa = :placa ORDER BY m.fecha")
    List<Mantenimiento> findByVehiculoPlaca(@Param("placa") String placa);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Mantenimiento m " +
            "WHERE m.vehiculo.placa = :placa AND m.fecha = :fecha")
    boolean existeParaVehiculoEnFecha(@Param("placa") String placa, @Param("fecha") LocalDate fecha);
}