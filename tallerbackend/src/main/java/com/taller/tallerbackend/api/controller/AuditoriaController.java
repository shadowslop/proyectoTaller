package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.persistencias.entidades.Auditoria;
import com.taller.tallerbackend.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<Auditoria>> listar() {
        return ResponseEntity.ok(auditoriaService.listar());
    }
}