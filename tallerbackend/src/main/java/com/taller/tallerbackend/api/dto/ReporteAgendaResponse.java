package com.taller.tallerbackend.api.dto;

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
        public String etiqueta;
        public long cantidad;

        public ReporteAgendaItem(String etiqueta, long cantidad) {
            this.etiqueta = etiqueta;
            this.cantidad = cantidad;
        }
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