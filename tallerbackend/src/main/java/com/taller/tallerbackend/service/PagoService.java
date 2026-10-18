package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.PagoRequest;
import com.taller.tallerbackend.exceptions.PrestamoNotFoundException;
import com.taller.tallerbackend.persistencias.entidades.Pago;
import com.taller.tallerbackend.persistencias.entidades.Prestamo;
import com.taller.tallerbackend.persistencias.repositorio.PagoRepository;
import com.taller.tallerbackend.persistencias.repositorio.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PagoService {

    @Autowired
    PagoRepository pagoRepository;
    @Autowired
    PrestamoRepository prestamoRepository;

    // RF-42/RF-43/RF-44: registrar el pago del alquiler con su método de pago
    public Pago registrarPago(PagoRequest datos) {
        Prestamo prestamo = prestamoRepository.findById(datos.getPrestamoId())
                .orElseThrow(() -> new PrestamoNotFoundException());

        Pago pago = new Pago();
        pago.setPrestamo(prestamo);
        pago.setMetodoPago(datos.getMetodoPago());
        pago.setMonto(prestamo.getMontoTotal());   // valor ya calculado por RF-45
        pago.setFecha(OffsetDateTime.now());
        pago.setRecargoMora(prestamo.getRecargoMora());
        return pagoRepository.save(pago);
    }
    public List<Pago> listarPorPrestamo(UUID prestamoId) {
        return pagoRepository.findByPrestamoId(prestamoId);
    }

    // RF-44: comprobante con la información principal del alquiler
    public String generarComprobante(UUID prestamoId) {
        Prestamo p = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new PrestamoNotFoundException());
        BigDecimal recargo = p.getRecargoMora() != null ? p.getRecargoMora() : BigDecimal.ZERO;

        return """
                ============ COMPROBANTE DE ALQUILER ============
                Alquiler No: %s
                Cliente: %s %s (C.C. %s)
                Vehículo: %s (%s)
                Sede préstamo: %s | Sede devolución: %s
                Fecha préstamo: %s
                Fecha devolución: %s
                Tarifa/día: $%s
                Subtotal alquiler: $%s
                Recargos por mora: $%s
                TOTAL PAGADO: $%s
                ================================================
                """.formatted(
                p.getId(),
                p.getClienteId().getNombre(), p.getClienteId().getApellido(), p.getClienteId().getCedula(),
                p.getVehiculoId().getPlaca(), p.getVehiculoId().getTipo().getNombre(),
                p.getSedePrestamoId().getNombre(),
                p.getSedeDevolucionId() != null ? p.getSedeDevolucionId().getNombre() : "N/A",
                p.getFechaPrestamo(), p.getFechaDevolucion(),
                p.getVehiculoId().getPrecioDia(),
                p.getMontoTotal().subtract(recargo),   // Subtotal alquiler
                recargo,                               // Recargos por mora
                p.getMontoTotal());                    // TOTAL PAGADO;
    }
}