package com.example.backend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.repository.PlanesSuscripcionRepository;
import com.example.backend.repository.bean.PlanesSuscripcion;

@Service
public class PlanesSuscripcionDAO {
	
	@Autowired
	private PlanesSuscripcionRepository repository;
	
	public PlanesSuscripcion recuperarPlanActivoPorCodigo(String codigo, Integer estado) {
		return repository.findByCodigoAndEstado(codigo, estado);
	}
}
