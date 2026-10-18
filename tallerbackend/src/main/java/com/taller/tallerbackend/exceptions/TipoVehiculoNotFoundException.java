package com.taller.tallerbackend.exceptions;

public class TipoVehiculoNotFoundException extends RuntimeException {
  public TipoVehiculoNotFoundException() {
    super("Tipo de vehiculo no encontrado");
  }
}