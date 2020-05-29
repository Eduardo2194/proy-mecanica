package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.repository.bean.UsuarioPenalidad;

@Repository
public interface UsuarioPenalidadRepository extends JpaRepository<UsuarioPenalidad, Integer> {

	public UsuarioPenalidad findFirstByDniUsuarioAndEstado(String dniUsuario, Integer estado);
	
}
