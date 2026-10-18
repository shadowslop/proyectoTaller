package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.MantenimientoRequest;
import com.taller.tallerbackend.api.dto.MantenimientoResponse;
import com.taller.tallerbackend.exceptions.MantenimientoNotFoundException;
import com.taller.tallerbackend.exceptions.VehiculoNotFoundException;
import com.taller.tallerbackend.persistencias.entidades.Mantenimiento;
import com.taller.tallerbackend.persistencias.entidades.Vehiculo;
import com.taller.tallerbackend.persistencias.repositorio.MantenimientoRepository;
import com.taller.tallerbackend.persistencias.repositorio.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MantenimientoService {

    @Autowired
    MantenimientoRepository mantenimientoRepository;
    @Autowired
    VehiculoRepository vehiculoRepository;

    public MantenimientoResponse agendar(MantenimientoRequest datos) {
        Vehiculo vehiculo = vehiculoRepository.findById(datos.getVehiculoPlaca())
                .orElseThrow(() -> new VehiculoNotFoundException());

        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setVehiculo(vehiculo);
        mantenimiento.setTipo(datos.getTipo());
        mantenimiento.setFecha(datos.getFecha());

        return new MantenimientoResponse(mantenimientoRepository.save(mantenimiento));
    }

    public List<MantenimientoResponse> listarTodos() {
        return mantenimientoRepository.findAll().stream()
                .map(MantenimientoResponse::new)
                .sorted((a, b) -> a.getFecha().compareTo(b.getFecha()))
                .toList();
    }

    public List<MantenimientoResponse> listarPorVehiculo(String placa) {
        return mantenimientoRepository.findByVehiculoPlaca(placa).stream()
                .map(MantenimientoResponse::new)
                .toList();
    }

    public void cancelar(UUID id) {
        if (!mantenimientoRepository.existsById(id)) {
            throw new MantenimientoNotFoundException();
        }
        mantenimientoRepository.deleteById(id);
    }

    // Usado por PrestamoService y por la busqueda de disponibles del combo de prestamo.
    public boolean estaEnMantenimientoHoy(String placa) {
        return mantenimientoRepository.existeParaVehiculoEnFecha(placa, LocalDate.now());
    }
}