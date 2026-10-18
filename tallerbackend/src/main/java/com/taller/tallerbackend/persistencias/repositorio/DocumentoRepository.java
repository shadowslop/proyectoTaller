package com.taller.tallerbackend.persistencias.repositorio;

import com.taller.tallerbackend.persistencias.entidades.Documento;
import com.taller.tallerbackend.persistencias.entidades.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentoRepository extends JpaRepository<Documento, UUID> {

    List<Documento> findByVehiculoIdPlaca(String placa);

    List<Documento> findByFechaVencimientoBefore(OffsetDateTime fecha);

    Optional<Documento> findByVehiculoIdPlacaAndTipoDocumento(String placa, TipoDocumento tipoDocumento);

    void deleteByVehiculoIdPlaca(String placa);
    List<Documento> findByFechaVencimientoLessThanEqualAndFechaVencimientoAfter(
            OffsetDateTime limite, OffsetDateTime ahora);
}