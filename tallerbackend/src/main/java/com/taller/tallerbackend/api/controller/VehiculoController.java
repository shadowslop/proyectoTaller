package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.SedeConteoResponse;
import com.taller.tallerbackend.api.dto.VehiculoDto;
import com.taller.tallerbackend.api.dto.VehiculoSedeUpdateRequest;
import com.taller.tallerbackend.persistencias.entidades.Vehiculo;
import com.taller.tallerbackend.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    @Autowired
    VehiculoService vehiculoService;

    @PostMapping
    public ResponseEntity<Vehiculo> crear(@RequestBody VehiculoDto datos, jakarta.servlet.http.HttpServletRequest request) {
        validarSuperAdmin(request);
        Vehiculo vehiculo = vehiculoService.crearVehiculo(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculo);
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> eliminar(@PathVariable String placa, jakarta.servlet.http.HttpServletRequest request) {
        validarSuperAdmin(request);
        vehiculoService.eliminarVehiculo(placa);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{placa}/sede")
    public ResponseEntity<Vehiculo> cambiarSede(@PathVariable String placa,
                                                @RequestBody VehiculoSedeUpdateRequest datos,
                                                jakarta.servlet.http.HttpServletRequest request) {
        validarSuperAdmin(request);
        Vehiculo vehiculo = vehiculoService.cambiarSede(placa, datos.getSedeId());
        return ResponseEntity.ok(vehiculo);
    }

    @GetMapping("/conteo-por-sede")
    public ResponseEntity<List<SedeConteoResponse>> contarPorSede() {
        return ResponseEntity.ok(vehiculoService.contarVehiculosPorSede());
    }

    private void validarSuperAdmin(jakarta.servlet.http.HttpServletRequest request) {
        String rol = (String) request.getAttribute("rol");
        if (!"SUPERADMIN".equals(rol)) {
            throw new com.taller.tallerbackend.exceptions.AccesoDenegadoException();
        }
    }

    @GetMapping
    public ResponseEntity<List<Vehiculo>> listar() {
        return ResponseEntity.ok(vehiculoService.listarVehiculos());
    }

    @GetMapping("/sede/{sedeId}")
    public ResponseEntity<List<Vehiculo>> listarPorSede(@PathVariable UUID sedeId) {
        return ResponseEntity.ok(vehiculoService.listarPorSede(sedeId));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Vehiculo>> listarDisponibles() {
        return ResponseEntity.ok(vehiculoService.listarDisponibles());
    }

    // Usado por el combo de "vehiculo" al registrar un prestamo: filtra disponibles de la sede
    // por texto (placa o tipo, ej. "sub" -> suburbanos disponibles).
    @GetMapping("/sede/{sedeId}/disponibles/buscar")
    public ResponseEntity<List<Vehiculo>> buscarDisponiblesPorSede(@PathVariable UUID sedeId,
                                                                   @RequestParam(required = false, defaultValue = "") String texto) {
        return ResponseEntity.ok(vehiculoService.buscarDisponiblesPorSede(sedeId, texto));
    }

    @GetMapping("/{placa}")
    public ResponseEntity<Vehiculo> obtenerPorPlaca(@PathVariable String placa) {
        Vehiculo vehiculo = vehiculoService.obtenerPorPlaca(placa);
        return ResponseEntity.ok(vehiculo);
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<Vehiculo>> buscar(
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) UUID sedeId,
            @RequestParam(required = false) UUID tipoId,
            @RequestParam(required = false) Boolean estado) {
        return ResponseEntity.ok(vehiculoService.buscar(placa, sedeId, tipoId, estado));
    }
}