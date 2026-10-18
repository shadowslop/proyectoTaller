package com.taller.tallerbackend.persistencias.repositorio;

import com.taller.tallerbackend.persistencias.entidades.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuditoriaRepository extends JpaRepository<Auditoria, UUID> {
}