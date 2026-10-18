package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.ClienteDto;
import com.taller.tallerbackend.exceptions.ClienteNotFoundException;
import com.taller.tallerbackend.exceptions.LicenciaVencidaException;
import com.taller.tallerbackend.persistencias.entidades.Cliente;
import com.taller.tallerbackend.persistencias.repositorio.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    public Cliente crearCliente(ClienteDto datos){
        if (datos.getFechaVencimientoLicencia() == null || datos.getFechaVencimientoLicencia().isBefore(LocalDate.now())) {
            throw new LicenciaVencidaException();
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(datos.getNombre());
        cliente.setApellido(datos.getApellido());
        cliente.setCedula(datos.getCedula());
        cliente.setTelefono(datos.getTelefono());
        cliente.setFechaVencimientoLicencia(datos.getFechaVencimientoLicencia());
        cliente.setNumeroLicencia(datos.getNumeroLicencia());
        clienteRepository.save(cliente);
        return cliente;
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }


    public List<Cliente> buscarClientes(String texto) {
        if (texto == null || texto.isBlank()) {
            return clienteRepository.findAll();
        }
        return clienteRepository.findByNombreStartingWithIgnoreCaseOrApellidoStartingWithIgnoreCaseOrCedulaStartingWith(
                texto, texto, texto
        );
    }
    public Cliente editarCliente(String cedula, ClienteDto datos) {
        Cliente cliente = clienteRepository.findById(cedula)
                .orElseThrow(() -> new ClienteNotFoundException());

        if (datos.getFechaVencimientoLicencia() == null || datos.getFechaVencimientoLicencia().isBefore(LocalDate.now())) {
            throw new LicenciaVencidaException();
        }

        cliente.setNombre(datos.getNombre());
        cliente.setApellido(datos.getApellido());
        cliente.setTelefono(datos.getTelefono());
        cliente.setFechaVencimientoLicencia(datos.getFechaVencimientoLicencia());
        cliente.setNumeroLicencia(datos.getNumeroLicencia());
        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(String cedula) {
        if (!clienteRepository.existsById(cedula)) {
            throw new ClienteNotFoundException();
        }
        clienteRepository.deleteById(cedula);
    }
}