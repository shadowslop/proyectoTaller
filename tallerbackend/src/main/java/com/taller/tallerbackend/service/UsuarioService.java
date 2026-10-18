package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.UsuarioDto;
import com.taller.tallerbackend.persistencias.entidades.Sede;
import com.taller.tallerbackend.persistencias.entidades.Usuario;
import com.taller.tallerbackend.persistencias.repositorio.SedeRepository;
import com.taller.tallerbackend.persistencias.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    SedeRepository sedeRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Usuario crearUsuario(UsuarioDto datos) {
        Sede sede = sedeRepository.findById(datos.getSedeId())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        Usuario usuario = new Usuario();
        usuario.setNombre(datos.getNombre());
        usuario.setPassword(encoder.encode(datos.getPassword()));
        usuario.setRol(datos.getRol());
        usuario.setSedeId(sede);

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}