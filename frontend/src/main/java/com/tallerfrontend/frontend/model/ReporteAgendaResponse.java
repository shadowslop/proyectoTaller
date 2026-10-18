package com.tallerfrontend.frontend.model;

import java.util.List;

public class ReporteAgendaResponse {
    private long totalReservas;
    private long confirmadas;
    private long pendientes;
    private long finalizadas;
    private long canceladas;
    private List<ReporteAgendaItem> porDiaSemana;
    private List<ReporteAgendaItem> porMes;

    public static class ReporteAgendaItem {
        private String etiqueta;
        private long cantidad;

        public String getEtiqueta() { return etiqueta; }
        public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
        public long getCantidad() { return cantidad; }
        public void setCantidad(long cantidad) { this.cantidad = cantidad; }
    }

    public long getTotalReservas() { return totalReservas; }
    public void setTotalReservas(long totalReservas) { this.totalReservas = totalReservas; }
    public long getConfirmadas() { return confirmadas; }
    public void setConfirmadas(long confirmadas) { this.confirmadas = confirmadas; }
    public long getPendientes() { return pendientes; }
    public void setPendientes(long pendientes) { this.pendientes = pendientes; }
    public long getFinalizadas() { return finalizadas; }
    public void setFinalizadas(long finalizadas) { this.finalizadas = finalizadas; }
    public long getCanceladas() { return canceladas; }
    public void setCanceladas(long canceladas) { this.canceladas = canceladas; }
    public List<ReporteAgendaItem> getPorDiaSemana() { return porDiaSemana; }
    public void setPorDiaSemana(List<ReporteAgendaItem> porDiaSemana) { this.porDiaSemana = porDiaSemana; }
    public List<ReporteAgendaItem> getPorMes() { return porMes; }
    public void setPorMes(List<ReporteAgendaItem> porMes) { this.porMes = porMes; }
}