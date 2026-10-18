package com.tallerfrontend.frontend.model;

public class EntregaRequest {
    private Integer kmInicial;   // RF-34
    private Integer combInicial; // RF-37

    public EntregaRequest(Integer kmInicial, Integer combInicial) {
        this.kmInicial = kmInicial;
        this.combInicial = combInicial;
    }

    public Integer getKmInicial() { return kmInicial; }
    public Integer getCombInicial() { return combInicial; }
}