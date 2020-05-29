package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.repository.bean.Problema;

@Repository
public interface ProblemaRepository extends JpaRepository<Problema, Integer> {

	public List<Problema> findFirstByIdUsuarioEmi(int idUsuarioEmi);

	public List<Problema> findFirstByIdUsuarioRecep(int idUsuarioRecep);

	public Problema findFirstByIdUsuarioRecepOrderByIdProblemaDesc(int idUsuarioRecep);

	public Problema findFirstByIdUsuarioEmiOrderByIdProblemaDesc(int idUsuarioEmi);
	
	public List<Problema> findFirstByIdUsuarioRecepAndEstado(int idUsuarioRecep, String estado);
}
