package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.TipoVehiculoDto;
import com.taller.tallerbackend.exceptions.TipoVehiculoDuplicadoException;
import com.taller.tallerbackend.exceptions.TipoVehiculoNotFoundException;
import com.taller.tallerbackend.persistencias.entidades.TipoVehiculo;
import com.taller.tallerbackend.persistencias.repositorio.TipoVehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TipoVehiculoService {

    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;

    public TipoVehiculo crear(TipoVehiculoDto datos) {
        tipoVehiculoRepository.findByNombreIgnoreCase(datos.getNombre())
                .ifPresent(t -> { throw new TipoVehiculoDuplicadoException(); });

        TipoVehiculo tipo = new TipoVehiculo(datos.getNombre().trim(), datos.getPrecioDia());
        return tipoVehiculoRepository.save(tipo);
    }

    public List<TipoVehiculo> listarTodos() {
        return tipoVehiculoRepository.findAll();
    }

    public List<TipoVehiculo> listarActivos() {
        return tipoVehiculoRepository.findByActivoTrue();
    }

    public TipoVehiculo actualizar(UUID id, TipoVehiculoDto datos) {
        TipoVehiculo tipo = tipoVehiculoRepository.findById(id)
                .orElseThrow(TipoVehiculoNotFoundException::new);

        if (datos.getNombre() != null && !datos.getNombre().isBlank()) {
            tipoVehiculoRepository.findByNombreIgnoreCase(datos.getNombre()).ifPresent(t -> {
                if (!t.getId().equals(id)) throw new TipoVehiculoDuplicadoException();
            });
            tipo.setNombre(datos.getNombre().trim());
        }
        if (datos.getPrecioDia() != null) {
            tipo.setPrecioDia(datos.getPrecioDia());
        }
        return tipoVehiculoRepository.save(tipo);
    }

    public void desactivar(UUID id) {
        TipoVehiculo tipo = tipoVehiculoRepository.findById(id)
                .orElseThrow(TipoVehiculoNotFoundException::new);
        tipo.setActivo(false);
        tipoVehiculoRepository.save(tipo);
    }
}