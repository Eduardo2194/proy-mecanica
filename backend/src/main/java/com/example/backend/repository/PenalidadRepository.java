package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.repository.bean.Penalidad;

@Repository
public interface PenalidadRepository extends JpaRepository<Penalidad, Integer> {
	
	public Penalidad findFirstByCodigoAndEstado(String codigo, Integer estado);
	

}
