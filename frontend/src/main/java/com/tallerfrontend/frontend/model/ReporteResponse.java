package com.tallerfrontend.frontend.model;

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
        private String sedeNombre;
        private long cantidadPrestamos;
        private BigDecimal montoRecaudado;

        public String getSedeNombre() { return sedeNombre; }
        public void setSedeNombre(String sedeNombre) { this.sedeNombre = sedeNombre; }
        public long getCantidadPrestamos() { return cantidadPrestamos; }
        public void setCantidadPrestamos(long cantidadPrestamos) { this.cantidadPrestamos = cantidadPrestamos; }
        public BigDecimal getMontoRecaudado() { return montoRecaudado; }
        public void setMontoRecaudado(BigDecimal montoRecaudado) { this.montoRecaudado = montoRecaudado; }
    }

    public static class ReporteTipoVehiculo {
        private String tipo;
        private long cantidadPrestamos;
        private BigDecimal montoRecaudado;

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public long getCantidadPrestamos() { return cantidadPrestamos; }
        public void setCantidadPrestamos(long cantidadPrestamos) { this.cantidadPrestamos = cantidadPrestamos; }
        public BigDecimal getMontoRecaudado() { return montoRecaudado; }
        public void setMontoRecaudado(BigDecimal montoRecaudado) { this.montoRecaudado = montoRecaudado; }
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