package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.ReporteAgendaResponse;
import com.taller.tallerbackend.api.dto.ReporteResponse;
import com.taller.tallerbackend.api.dto.ReporteVehiculosResponse;
import com.taller.tallerbackend.exceptions.AccesoDenegadoException;
import com.taller.tallerbackend.service.ReporteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    ReporteService reporteService;

    @GetMapping
    public ResponseEntity<ReporteResponse> generar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fin,
            HttpServletRequest request) {

        String rol = (String) request.getAttribute("rol");
        if (!"SUPERADMIN".equals(rol)) {
            throw new AccesoDenegadoException();
        }

        return ResponseEntity.ok(reporteService.generarReporte(inicio, fin));
    }
    @GetMapping("/agenda")
    public ResponseEntity<ReporteAgendaResponse> generarAgenda(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fin,
            HttpServletRequest request) {

        String rol = (String) request.getAttribute("rol");
        if (!"SUPERADMIN".equals(rol)) {
            throw new AccesoDenegadoException();
        }

        return ResponseEntity.ok(reporteService.generarReporteAgenda(inicio, fin));
    }
    @GetMapping("/vehiculos")
    public ResponseEntity<ReporteVehiculosResponse> reporteVehiculos() {
        return ResponseEntity.ok(reporteService.reporteVehiculos());
    }
}