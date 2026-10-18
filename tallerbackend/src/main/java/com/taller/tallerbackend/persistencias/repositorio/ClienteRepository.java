package com.taller.tallerbackend.persistencias.repositorio;

import com.taller.tallerbackend.persistencias.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    List<Cliente> findByNombreStartingWithIgnoreCaseOrApellidoStartingWithIgnoreCaseOrCedulaStartingWith(
            String nombre, String apellido, String cedula);
}