package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.repository.bean.PlanesSuscripcion;

@Repository
public interface PlanesSuscripcionRepository extends JpaRepository<PlanesSuscripcion, Integer> {

	public List<PlanesSuscripcion> findByEstadoNot(Integer estado);
	
	public PlanesSuscripcion findByCodigoAndEstado(String codigo, Integer estado);
}
