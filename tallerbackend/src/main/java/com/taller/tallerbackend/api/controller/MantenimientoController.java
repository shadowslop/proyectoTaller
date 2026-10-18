package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.MantenimientoRequest;
import com.taller.tallerbackend.api.dto.MantenimientoResponse;
import com.taller.tallerbackend.exceptions.AccesoDenegadoException;
import com.taller.tallerbackend.service.MantenimientoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

    @Autowired
    MantenimientoService mantenimientoService;

    @PostMapping
    public ResponseEntity<MantenimientoResponse> agendar(@RequestBody MantenimientoRequest datos, HttpServletRequest request) {
        validarSuperAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mantenimientoService.agendar(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id, HttpServletRequest request) {
        validarSuperAdmin(request);
        mantenimientoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MantenimientoResponse>> listar() {
        return ResponseEntity.ok(mantenimientoService.listarTodos());
    }

    @GetMapping("/vehiculo/{placa}")
    public ResponseEntity<List<MantenimientoResponse>> listarPorVehiculo(@PathVariable String placa) {
        return ResponseEntity.ok(mantenimientoService.listarPorVehiculo(placa));
    }

    private void validarSuperAdmin(HttpServletRequest request) {
        String rol = (String) request.getAttribute("rol");
        if (!"SUPERADMIN".equals(rol)) {
            throw new AccesoDenegadoException();
        }
    }
}