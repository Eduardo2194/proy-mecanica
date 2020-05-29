package com.example.backend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.repository.ValoracionRepository;
import com.example.backend.repository.bean.Valoracion;

@Service
public class ValoracionDAO {

	@Autowired
	ValoracionRepository valoracionRepository;
	
	public Valoracion registrar (Valoracion valoracion) {
		return valoracionRepository.save(valoracion);
	}
}
