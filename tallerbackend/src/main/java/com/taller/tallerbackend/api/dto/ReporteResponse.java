package com.taller.tallerbackend.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class ReporteResponse {
    private long totalPrestamos;
    private long prestamosActivos;
    private long prestamosDevueltos;
    private BigDecimal montoTotalRecaudado;
    private List<ReporteSede> porSede;
    private List<ReporteTipoVehiculo> porTipoVehiculo;

    public static class ReporteSede {
        public String sedeNombre;
        public long cantidadPrestamos;
        public BigDecimal montoRecaudado;

        public ReporteSede(String sedeNombre, long cantidadPrestamos, BigDecimal montoRecaudado) {
            this.sedeNombre = sedeNombre;
            this.cantidadPrestamos = cantidadPrestamos;
            this.montoRecaudado = montoRecaudado;
        }
    }

    public static class ReporteTipoVehiculo {
        public String tipo;
        public long cantidadPrestamos;
        public BigDecimal montoRecaudado;

        public ReporteTipoVehiculo(String tipo, long cantidadPrestamos, BigDecimal montoRecaudado) {
            this.tipo = tipo;
            this.cantidadPrestamos = cantidadPrestamos;
            this.montoRecaudado = montoRecaudado;
        }
    }
    public long getTotalPrestamos() { return totalPrestamos; }
    public void setTotalPrestamos(long totalPrestamos) { this.totalPrestamos = totalPrestamos; }

    public long getPrestamosActivos() { return prestamosActivos; }
    public void setPrestamosActivos(long prestamosActivos) { this.prestamosActivos = prestamosActivos; }

    public long getPrestamosDevueltos() { return prestamosDevueltos; }
    public void setPrestamosDevueltos(long prestamosDevueltos) { this.prestamosDevueltos = prestamosDevueltos; }

    public BigDecimal getMontoTotalRecaudado() { return montoTotalRecaudado; }
    public void setMontoTotalRecaudado(BigDecimal montoTotalRecaudado) { this.montoTotalRecaudado = montoTotalRecaudado; }

    public List<ReporteSede> getPorSede() { return porSede; }
    public void setPorSede(List<ReporteSede> porSede) { this.porSede = porSede; }

    public List<ReporteTipoVehiculo> getPorTipoVehiculo() { return porTipoVehiculo; }
    public void setPorTipoVehiculo(List<ReporteTipoVehiculo> porTipoVehiculo) { this.porTipoVehiculo = porTipoVehiculo; }
}