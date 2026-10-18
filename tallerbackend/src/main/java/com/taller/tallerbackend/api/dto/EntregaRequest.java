package com.taller.tallerbackend.api.dto;

public class EntregaRequest {
    private Integer kmInicial;   // RF-34
    private Integer combInicial; // RF-37

    public Integer getKmInicial() { return kmInicial; }
    public void setKmInicial(Integer kmInicial) { this.kmInicial = kmInicial; }
    public Integer getCombInicial() { return combInicial; }
    public void setCombInicial(Integer combInicial) { this.combInicial = combInicial; }
}