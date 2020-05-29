package com.example.backend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.repository.PenalidadRepository;
import com.example.backend.repository.bean.Penalidad;

@Service
public class PenalidadDAO {
	
	@Autowired
	private PenalidadRepository repository;
	
	public Penalidad recuperarPenalidadActivaPorCodigo(String codigo, Integer estado) {
		return repository.findFirstByCodigoAndEstado(codigo, estado);
	}

}
