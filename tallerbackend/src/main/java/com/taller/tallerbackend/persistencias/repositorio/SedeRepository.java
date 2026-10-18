package com.taller.tallerbackend.persistencias.repositorio;

import com.taller.tallerbackend.persistencias.entidades.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SedeRepository extends JpaRepository<Sede, UUID> {
}