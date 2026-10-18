package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.UsuarioDto;
import com.taller.tallerbackend.exceptions.AccesoDenegadoException;
import com.taller.tallerbackend.persistencias.entidades.Usuario;
import com.taller.tallerbackend.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody UsuarioDto datos, HttpServletRequest request) {
        validarSuperAdmin(request);
        Usuario usuario = usuarioService.crearUsuario(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(HttpServletRequest request) {
        validarSuperAdmin(request);
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    private void validarSuperAdmin(HttpServletRequest request) {
        String rol = (String) request.getAttribute("rol");
        if (!"SUPERADMIN".equals(rol)) {
            throw new AccesoDenegadoException();
        }
    }
}