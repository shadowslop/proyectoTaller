package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.PagoRequest;
import com.taller.tallerbackend.persistencias.entidades.Pago;
import com.taller.tallerbackend.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    PagoService pagoService;

    @PostMapping
    public ResponseEntity<Pago> registrar(@RequestBody PagoRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.registrarPago(datos));
    }

    @GetMapping("/prestamo/{prestamoId}")
    public ResponseEntity<List<Pago>> listarPorPrestamo(@PathVariable UUID prestamoId) {
        return ResponseEntity.ok(pagoService.listarPorPrestamo(prestamoId));
    }

    @GetMapping("/prestamo/{prestamoId}/comprobante")
    public ResponseEntity<String> comprobante(@PathVariable UUID prestamoId) {
        return ResponseEntity.ok(pagoService.generarComprobante(prestamoId));
    }
}