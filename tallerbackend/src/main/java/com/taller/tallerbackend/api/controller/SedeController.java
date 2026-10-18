package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.SedeRequest;
import com.taller.tallerbackend.exceptions.AccesoDenegadoException;
import com.taller.tallerbackend.persistencias.entidades.Sede;
import com.taller.tallerbackend.service.SedeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sedes")
public class SedeController {

    @Autowired
    SedeService sedeService;

    @PostMapping
    public ResponseEntity<Sede> crear(@RequestBody SedeRequest datos, HttpServletRequest request) {
        String rol = (String) request.getAttribute("rol");
        if (!"SUPERADMIN".equals(rol)) {
            throw new AccesoDenegadoException();
        }
        Sede sede = sedeService.crearSede(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(sede);
    }

    @GetMapping
    public ResponseEntity<List<Sede>> listar() {
        return ResponseEntity.ok(sedeService.listarSedes());
    }
}