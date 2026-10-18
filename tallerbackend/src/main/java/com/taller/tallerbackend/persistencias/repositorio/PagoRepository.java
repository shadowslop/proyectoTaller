package com.taller.tallerbackend.persistencias.repositorio;

import com.taller.tallerbackend.persistencias.entidades.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PagoRepository extends JpaRepository<Pago, UUID> {
    List<Pago> findByPrestamoId(UUID prestamoId);
}