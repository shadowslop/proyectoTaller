package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.DocumentoDto;
import com.taller.tallerbackend.exceptions.DocumentoDuplicadoException;
import com.taller.tallerbackend.exceptions.DocumentoNotFoundException;
import com.taller.tallerbackend.exceptions.VehiculoNotFoundException;
import com.taller.tallerbackend.persistencias.entidades.Documento;
import com.taller.tallerbackend.persistencias.entidades.Vehiculo;
import com.taller.tallerbackend.persistencias.repositorio.DocumentoRepository;
import com.taller.tallerbackend.persistencias.repositorio.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoService {

    @Autowired
    DocumentoRepository documentoRepository;
    @Autowired
    VehiculoRepository vehiculoRepository;

    public Documento crearDocumento(DocumentoDto datos) {
        Vehiculo vehiculo = vehiculoRepository.findById(datos.getVehiculoPlaca())
                .orElseThrow(() -> new VehiculoNotFoundException());

        documentoRepository.findByVehiculoIdPlacaAndTipoDocumento(datos.getVehiculoPlaca(), datos.getTipoDocumento())
                .ifPresent(d -> { throw new DocumentoDuplicadoException(); });

        Documento documento = new Documento();
        documento.setTipoDocumento(datos.getTipoDocumento());
        documento.setFechaVencimiento(datos.getFechaVencimiento());
        documento.setVehiculoId(vehiculo);
        documento.setEmpresa(datos.getEmpresa());

        return documentoRepository.save(documento);
    }

    public Documento actualizarDocumento(UUID id, DocumentoDto datos) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new DocumentoNotFoundException());


        if (datos.getTipoDocumento() != null && datos.getTipoDocumento() != documento.getTipoDocumento()) {
            documentoRepository.findByVehiculoIdPlacaAndTipoDocumento(
                    documento.getVehiculoId().getPlaca(), datos.getTipoDocumento()
            ).ifPresent(d -> {
                if (!d.getId().equals(id)) {
                    throw new DocumentoDuplicadoException();
                }
            });
            documento.setTipoDocumento(datos.getTipoDocumento());
        }

        if (datos.getFechaVencimiento() != null) {
            documento.setFechaVencimiento(datos.getFechaVencimiento());
        }

        if (datos.getEmpresa() != null) {
            documento.setEmpresa(datos.getEmpresa());
        }

        return documentoRepository.save(documento);
    }

    public void eliminarDocumento(UUID id) {
        if (!documentoRepository.existsById(id)) {
            throw new DocumentoNotFoundException();
        }
        documentoRepository.deleteById(id);
    }

    public List<Documento> listarPorVehiculo(String placa) {
        return documentoRepository.findByVehiculoIdPlaca(placa);
    }

    public List<Documento> listarVencidos() {

        return documentoRepository.findByFechaVencimientoBefore(OffsetDateTime.now());
    }
    public List<Documento> listarPorVencer() {
        OffsetDateTime ahora = OffsetDateTime.now();
        // RF-20/HU-04: vencen dentro de los próximos 30 días calendario
        return documentoRepository
                .findByFechaVencimientoLessThanEqualAndFechaVencimientoAfter(ahora.plusDays(30), ahora);
    }
}