package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.ReservaActualizarDto;
import com.taller.tallerbackend.api.dto.ReservaDto;
import com.taller.tallerbackend.api.dto.ReservaResponse;
import com.taller.tallerbackend.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponse> crear(@RequestBody ReservaDto datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.crear(datos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> modificar(@PathVariable UUID id, @RequestBody ReservaActualizarDto datos) {
        return ResponseEntity.ok(reservaService.modificar(id, datos));
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<ReservaResponse> confirmar(@PathVariable UUID id) {
        return ResponseEntity.ok(reservaService.confirmar(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponse> cancelar(@PathVariable UUID id) {
        return ResponseEntity.ok(reservaService.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarTodas() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @GetMapping("/sede/{sedeId}")
    public ResponseEntity<List<ReservaResponse>> listarPorSede(@PathVariable UUID sedeId) {
        return ResponseEntity.ok(reservaService.listarPorSede(sedeId));
    }
}