package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.TipoVehiculoDto;
import com.taller.tallerbackend.persistencias.entidades.TipoVehiculo;
import com.taller.tallerbackend.service.TipoVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tipos-vehiculo")
public class TipoVehiculoController {

    @Autowired
    TipoVehiculoService tipoVehiculoService;

    @PostMapping
    public ResponseEntity<TipoVehiculo> crear(@RequestBody TipoVehiculoDto datos,
                                              jakarta.servlet.http.HttpServletRequest request) {
        validarSuperAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoVehiculoService.crear(datos));
    }

    @GetMapping
    public ResponseEntity<List<TipoVehiculo>> listarTodos() {
        return ResponseEntity.ok(tipoVehiculoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<TipoVehiculo>> listarActivos() {
        return ResponseEntity.ok(tipoVehiculoService.listarActivos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoVehiculo> actualizar(@PathVariable UUID id,
                                                   @RequestBody TipoVehiculoDto datos,
                                                   jakarta.servlet.http.HttpServletRequest request) {
        validarSuperAdmin(request);
        return ResponseEntity.ok(tipoVehiculoService.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id,
                                           jakarta.servlet.http.HttpServletRequest request) {
        validarSuperAdmin(request);
        tipoVehiculoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    private void validarSuperAdmin(jakarta.servlet.http.HttpServletRequest request) {
        String rol = (String) request.getAttribute("rol");
        if (!"SUPERADMIN".equals(rol)) {
            throw new com.taller.tallerbackend.exceptions.AccesoDenegadoException();
        }
    }
}