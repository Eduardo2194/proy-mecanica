package com.example.backend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.repository.UsuarioPenalidadRepository;
import com.example.backend.repository.bean.UsuarioPenalidad;

@Service
public class UsuarioPenalidadDAO {
	
	@Autowired
	private UsuarioPenalidadRepository repository;
	
	
	public UsuarioPenalidad registrarUsuarioPenalidad(UsuarioPenalidad usuarioPenalidad) {
		return repository.save(usuarioPenalidad);
	}
	
	public UsuarioPenalidad actualizarUsuarioPenalidad(UsuarioPenalidad usuarioPenalidad) {
		return repository.save(usuarioPenalidad);
	}
	
	public UsuarioPenalidad recuperarPenalidadActivaPorDni(String dni, Integer estado) {
		return repository.findFirstByDniUsuarioAndEstado(dni, estado);
	}
}
