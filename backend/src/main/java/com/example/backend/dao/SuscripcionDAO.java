package com.example.backend.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.repository.PlanesSuscripcionRepository;
import com.example.backend.repository.SuscripcionRepository;
import com.example.backend.repository.bean.PlanesSuscripcion;
import com.example.backend.repository.bean.Suscripcion;

@Service
public class SuscripcionDAO {
	
	@Autowired
	private SuscripcionRepository repository;
	
	@Autowired
	private PlanesSuscripcionRepository planesSuscripcionRespository;
	
	
	public Suscripcion registrarSuscripcion(Suscripcion suscripcion) {
		return repository.save(suscripcion);
	}
	
	public List<Suscripcion> recuperarSuscripcionesUsuario(String dni){
		return repository.findByDniUsuario(dni);
	}
	
	public List<PlanesSuscripcion> consultaPlanesSuscripcion(Integer estadoInvalido){
		return planesSuscripcionRespository.findByEstadoNot(estadoInvalido);
	} 
	
}
