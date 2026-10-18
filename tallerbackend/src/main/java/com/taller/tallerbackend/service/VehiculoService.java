package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.SedeConteoResponse;
import com.taller.tallerbackend.api.dto.VehiculoDto;
import com.taller.tallerbackend.exceptions.SedeNoEncontrada;
import com.taller.tallerbackend.exceptions.TipoVehiculoNotFoundException;
import com.taller.tallerbackend.exceptions.VehiculoConPrestamoActivoException;
import com.taller.tallerbackend.exceptions.VehiculoNotFoundException;
import com.taller.tallerbackend.persistencias.entidades.Sede;
import com.taller.tallerbackend.persistencias.entidades.TipoVehiculo;
import com.taller.tallerbackend.persistencias.entidades.Vehiculo;
import com.taller.tallerbackend.persistencias.repositorio.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehiculoService {

    @Autowired
    VehiculoRepository vehiculoRepository;
    @Autowired
    SedeRepository sedeRepository;
    @Autowired
    PrestamoRepository prestamoRepository;
    @Autowired
    DocumentoRepository documentoRepository;
    @Autowired
    MantenimientoRepository mantenimientoRepository;
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;

    public Vehiculo crearVehiculo(VehiculoDto datos) {
        Sede sede = sedeRepository.findById(datos.getSedeId())
                .orElseThrow(SedeNoEncontrada::new);
        TipoVehiculo tipo = tipoVehiculoRepository.findById(datos.getTipoId())
                .orElseThrow(TipoVehiculoNotFoundException::new);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(datos.getPlaca());
        vehiculo.setTipo(tipo);
        vehiculo.setPrecioDia(datos.getPrecioDia() != null ? datos.getPrecioDia() : tipo.getPrecioDia());
        vehiculo.setSedeId(sede);
        vehiculo.setEstado(true);

        return vehiculoRepository.save(vehiculo);
    }

    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }

    public List<Vehiculo> listarPorSede(UUID sedeId) {
        return vehiculoRepository.findBySedeId(sedeId);
    }

    public List<Vehiculo> listarDisponibles() {
        return vehiculoRepository.findByEstado(true);
    }

    public Vehiculo obtenerPorPlaca(String placa) {
        return vehiculoRepository.findById(placa)
                .orElseThrow(VehiculoNotFoundException::new);
    }

    public List<Vehiculo> buscarDisponiblesPorSede(UUID sedeId, String texto) {
        List<Vehiculo> disponibles = vehiculoRepository.findByEstadoTrueAndSedeIdAndPlacaContainingIgnoreCase(
                sedeId, texto == null ? "" : texto
        );

        if (texto != null && !texto.isBlank()) {
            String filtro = texto.trim().toLowerCase();
            for (TipoVehiculo t : tipoVehiculoRepository.findAll()) {
                if (t.getNombre().toLowerCase().contains(filtro) || filtro.contains(t.getNombre().toLowerCase())) {
                    List<Vehiculo> porTipo = vehiculoRepository.findByEstadoTrueAndSedeIdAndTipoId(sedeId, t.getId());
                    for (Vehiculo v : porTipo) {
                        if (disponibles.stream().noneMatch(d -> d.getPlaca().equals(v.getPlaca()))) {
                            disponibles.add(v);
                        }
                    }
                }
            }
        }

        var hoy = java.time.LocalDate.now();
        return disponibles.stream()
                .filter(v -> !tieneDocumentoVencido(v))
                .filter(v -> !mantenimientoRepository.existeParaVehiculoEnFecha(v.getPlaca(), hoy))
                .toList();
    }

    private boolean tieneDocumentoVencido(Vehiculo v) {
        var ahora = java.time.OffsetDateTime.now();
        return documentoRepository.findByVehiculoIdPlaca(v.getPlaca()).stream()
                .anyMatch(d -> d.getFechaVencimiento() != null && d.getFechaVencimiento().isBefore(ahora));
    }

    @Transactional
    public void eliminarVehiculo(String placa) {
        Vehiculo vehiculo = obtenerPorPlaca(placa);

        boolean tienePrestamoActivo = prestamoRepository.existsByVehiculoIdPlacaAndEstado(placa, true);
        if (tienePrestamoActivo) {
            throw new VehiculoConPrestamoActivoException();
        }

        documentoRepository.deleteByVehiculoIdPlaca(placa);
        vehiculoRepository.delete(vehiculo);
    }

    public Vehiculo cambiarSede(String placa, UUID nuevaSedeId) {
        Vehiculo vehiculo = obtenerPorPlaca(placa);
        Sede nuevaSede = sedeRepository.findById(nuevaSedeId)
                .orElseThrow(SedeNoEncontrada::new);

        boolean tienePrestamoActivo = prestamoRepository.existsByVehiculoIdPlacaAndEstado(placa, true);
        if (tienePrestamoActivo) {
            throw new VehiculoConPrestamoActivoException();
        }

        vehiculo.setSedeId(nuevaSede);
        return vehiculoRepository.save(vehiculo);
    }

    public List<SedeConteoResponse> contarVehiculosPorSede() {
        return sedeRepository.findAll().stream()
                .map(sede -> new SedeConteoResponse(
                        sede.getId(),
                        sede.getNombre(),
                        vehiculoRepository.countBySedeId(sede.getId())
                ))
                .toList();
    }
    public List<Vehiculo> buscar(String placa, UUID sedeId, UUID tipoId, Boolean estado) {
        return vehiculoRepository.findAll().stream()
                .filter(v -> placa == null || v.getPlaca().toLowerCase().contains(placa.toLowerCase()))
                .filter(v -> sedeId == null || v.getSedeId().getId().equals(sedeId))
                .filter(v -> tipoId == null || v.getTipo().getId().equals(tipoId))
                .filter(v -> estado == null || v.getEstado().equals(estado))
                .toList();
    }
}