package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.DocumentoDto;
import com.taller.tallerbackend.persistencias.entidades.Documento;
import com.taller.tallerbackend.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    DocumentoService documentoService;

    @PostMapping
    public ResponseEntity<Documento> crear(@RequestBody DocumentoDto datos) {
        Documento documento = documentoService.crearDocumento(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(documento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Documento> actualizar(@PathVariable UUID id, @RequestBody DocumentoDto datos) {
        Documento documento = documentoService.actualizarDocumento(id, datos);
        return ResponseEntity.ok(documento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        documentoService.eliminarDocumento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehiculo/{placa}")
    public ResponseEntity<List<Documento>> listarPorVehiculo(@PathVariable String placa) {
        return ResponseEntity.ok(documentoService.listarPorVehiculo(placa));
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<Documento>> listarVencidos() {
        return ResponseEntity.ok(documentoService.listarVencidos());
    }
    @GetMapping("/por-vencer")
    public ResponseEntity<List<Documento>> listarPorVencer() {
        return ResponseEntity.ok(documentoService.listarPorVencer());
    }
}