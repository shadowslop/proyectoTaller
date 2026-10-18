package com.taller.tallerbackend.service;

import com.taller.tallerbackend.persistencias.entidades.Auditoria;
import com.taller.tallerbackend.persistencias.entidades.Usuario;
import com.taller.tallerbackend.persistencias.repositorio.AuditoriaRepository;
import com.taller.tallerbackend.persistencias.repositorio.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuditoriaService {

    @Autowired
    AuditoriaRepository auditoriaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    HttpServletRequest request; // Spring inyecta un proxy de la peticion actual

    // RF-49: registrar fecha, hora y usuario responsable de cada modificación
    public void registrar(String usuario, String accion, String entidad, String detalle) {
        auditoriaRepository.save(new Auditoria(usuario, accion, entidad, detalle));
    }

    // Version corta: toma el usuario autenticado de la peticion actual (JWT)
    public void registrar(String accion, String entidad, String detalle) {
        registrar(usuarioActual(), accion, entidad, detalle);
    }

    private String usuarioActual() {
        try {
            Object id = request.getAttribute("usuarioId");
            if (id != null) {
                return usuarioRepository.findById(UUID.fromString(id.toString()))
                        .map(Usuario::getNombre)
                        .orElse(id.toString());
            }
        } catch (Exception ignorado) {
            // fuera de una peticion HTTP o id invalido: se registra como sistema
        }
        return "sistema";
    }

    public List<Auditoria> listar() {
        return auditoriaRepository.findAll();
    }
}