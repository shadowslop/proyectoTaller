package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.LoginRequest;
import com.taller.tallerbackend.api.dto.LoginResponse;
import com.taller.tallerbackend.exceptions.CredencialesInvalidasException;
import com.taller.tallerbackend.persistencias.entidades.Usuario;
import com.taller.tallerbackend.persistencias.repositorio.UsuarioRepository;
import com.taller.tallerbackend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest datos) {
        Usuario usuario = usuarioRepository.findByNombre(datos.getNombre())
                .orElseThrow(CredencialesInvalidasException::new);

        if (!encoder.matches(datos.getPassword(), usuario.getPassword())) {
            throw new CredencialesInvalidasException();
        }

        String token = jwtUtil.generarToken(usuario.getId(), usuario.getNombre(), usuario.getRol().name());

        LoginResponse response = new LoginResponse(
                token, usuario.getId(), usuario.getNombre(), usuario.getRol().name(), usuario.getSedeId().getId()
        );

        return ResponseEntity.ok(response);
    }
}