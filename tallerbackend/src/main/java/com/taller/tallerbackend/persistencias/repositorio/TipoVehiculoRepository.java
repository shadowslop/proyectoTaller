package com.taller.tallerbackend.persistencias.repositorio;

import com.taller.tallerbackend.persistencias.entidades.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculo, UUID> {
    Optional<TipoVehiculo> findByNombreIgnoreCase(String nombre);
    List<TipoVehiculo> findByActivoTrue();
}