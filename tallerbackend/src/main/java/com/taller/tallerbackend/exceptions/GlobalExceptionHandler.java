package com.taller.tallerbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<String> handleClienteNotFound(ClienteNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(VehiculoNotFoundException.class)
    public ResponseEntity<String> handleVehiculoNotFound(VehiculoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<String> handleUsuarioNotFound(UsuarioNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(SedeNoEncontrada.class)
    public ResponseEntity<String> handleSedeNoEncontrada(SedeNoEncontrada e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PrestamoNotFoundException.class)
    public ResponseEntity<String> handlePrestamoNotFound(PrestamoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ClientePestramoAlreadyActivedException.class)
    public ResponseEntity<String> handleClienteConPrestamoActivo(ClientePestramoAlreadyActivedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(VehiculoAlreadyUsedException.class)
    public ResponseEntity<String> handleVehiculoYaUsado(VehiculoAlreadyUsedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(PrestamoYaDevueltoException.class)
    public ResponseEntity<String> handlePrestamoYaDevuelto(PrestamoYaDevueltoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(DocumentosVencidosException.class)
    public ResponseEntity<String> handleDocumentosVencidos(DocumentosVencidosException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
    @ExceptionHandler(DocumentosIncompletosException.class)
    public ResponseEntity<String> handleDocumentosIncompletos(DocumentosIncompletosException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<String> handleCredencialesInvalidas(CredencialesInvalidasException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<String> handleAccesoDenegado(AccesoDenegadoException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(DocumentoNotFoundException.class)
    public ResponseEntity<String> handleDocumentoNotFound(DocumentoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DocumentoDuplicadoException.class)
    public ResponseEntity<String> handleDocumentoDuplicado(DocumentoDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(VehiculoConPrestamoActivoException.class)
    public ResponseEntity<String> handleVehiculoConPrestamoActivo(VehiculoConPrestamoActivoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(LicenciaVencidaException.class)
    public ResponseEntity<String> handleLicenciaVencida(LicenciaVencidaException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(VehiculoEnMantenimientoException.class)
    public ResponseEntity<String> handleVehiculoEnMantenimiento(VehiculoEnMantenimientoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MantenimientoNotFoundException.class)
    public ResponseEntity<String> handleMantenimientoNotFound(MantenimientoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(TipoVehiculoNotFoundException.class)
    public ResponseEntity<String> handleTipoVehiculoNotFound(TipoVehiculoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(TipoVehiculoDuplicadoException.class)
    public ResponseEntity<String> handleTipoVehiculoDuplicado(TipoVehiculoDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(ReservaNotFoundException.class)
    public ResponseEntity<String> handleReservaNotFound(ReservaNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(VehiculoNoDisponibleEnPeriodoException.class)
    public ResponseEntity<String> handleVehiculoNoDisponibleEnPeriodo(VehiculoNoDisponibleEnPeriodoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(ReservaNoCancelableException.class)
    public ResponseEntity<String> handleReservaNoCancelable(ReservaNoCancelableException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(ReservaNoConfirmableException.class)
    public ResponseEntity<String> handleReservaNoConfirmable(ReservaNoConfirmableException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(ReservaNoEliminableException.class)
    public ResponseEntity<String> handleReservaNoEliminable(ReservaNoEliminableException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

}