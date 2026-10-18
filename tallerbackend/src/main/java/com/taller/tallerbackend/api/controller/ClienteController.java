package com.taller.tallerbackend.api.controller;

import com.taller.tallerbackend.api.dto.ClienteDto;
import com.taller.tallerbackend.persistencias.entidades.Cliente;
import com.taller.tallerbackend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody ClienteDto datos) {
        Cliente cliente = clienteService.crearCliente(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscar(@RequestParam(required = false, defaultValue = "") String texto) {
        return ResponseEntity.ok(clienteService.buscarClientes(texto));
    }
    @PutMapping("/{cedula}")
    public ResponseEntity<Cliente> editar(@PathVariable String cedula, @RequestBody ClienteDto datos) {
        return ResponseEntity.ok(clienteService.editarCliente(cedula, datos));
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> eliminar(@PathVariable String cedula) {
        clienteService.eliminarCliente(cedula);
        return ResponseEntity.noContent().build();
    }
}