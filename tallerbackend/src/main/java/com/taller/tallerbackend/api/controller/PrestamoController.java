package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.DevolucionRequest;
import com.taller.tallerbackend.api.dto.EntregaRequest;
import com.taller.tallerbackend.api.dto.PrestamoRequest;
import com.taller.tallerbackend.api.dto.PrestamoResponse;
import com.taller.tallerbackend.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoResponse> crear(@RequestBody PrestamoRequest datos) {
        PrestamoResponse prestamo = prestamoService.crearPrestamo(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamo);
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<PrestamoResponse> devolver(@PathVariable UUID id, @RequestBody DevolucionRequest datos) {
        PrestamoResponse prestamo = prestamoService.devolverPrestamo(id, datos);
        return ResponseEntity.ok(prestamo);
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> listar() {
        return ResponseEntity.ok(prestamoService.listarPrestamos());
    }

    @GetMapping("/sede/{sedeId}")
    public ResponseEntity<List<PrestamoResponse>> listarPorSede(@PathVariable UUID sedeId) {
        return ResponseEntity.ok(prestamoService.listarPorSede(sedeId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PrestamoResponse>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(prestamoService.listarPorUsuario(usuarioId));
    }
    @GetMapping("/vehiculo/{placa}/activo")
    public ResponseEntity<PrestamoResponse> buscarActivoPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(prestamoService.buscarActivoPorPlaca(placa));
    }
    @GetMapping("/cliente/{cedula}")
    public ResponseEntity<List<PrestamoResponse>> listarPorCliente(@PathVariable String cedula) {
        return ResponseEntity.ok(prestamoService.listarPorCliente(cedula));
    }
    @PutMapping("/{id}/entrega")
    public ResponseEntity<PrestamoResponse> registrarEntrega(
            @PathVariable UUID id, @RequestBody EntregaRequest datos) {
        return ResponseEntity.ok(prestamoService.registrarEntrega(id, datos));
    }
    @PostMapping("/desde-reserva/{reservaId}")
    public ResponseEntity<PrestamoResponse> crearDesdeReserva(@PathVariable UUID reservaId,
                                                              @RequestParam UUID usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prestamoService.crearDesdeReserva(reservaId, usuarioId));
    }

    @GetMapping("/alertas/reservas-vencidas")
    public ResponseEntity<List<PrestamoResponse>> reservasVencidas() {
        return ResponseEntity.ok(prestamoService.listarReservasVencidas());
    }
}