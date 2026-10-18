package com.taller.tallerbackend.service;

import com.taller.tallerbackend.api.dto.SedeRequest;
import com.taller.tallerbackend.persistencias.entidades.Sede;
import com.taller.tallerbackend.persistencias.repositorio.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SedeService {
    @Autowired
    SedeRepository sedeRepository;

    public Sede crearSede(SedeRequest datos){
        Sede sede = new Sede();
        sede.setNombre(datos.getNombre());
        return sedeRepository.save(sede);
    }

    public List<Sede> listarSedes(){
        return sedeRepository.findAll();
    }
}