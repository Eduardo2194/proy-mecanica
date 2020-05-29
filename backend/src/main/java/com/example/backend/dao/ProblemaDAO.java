package com.example.backend.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.repository.ProblemaRepository;
import com.example.backend.repository.bean.Problema;

@Service
public class ProblemaDAO {

	@Autowired
	ProblemaRepository problemaRepository;
		
	/*registrar un problema*/
	public Problema registrarProblema(Problema problema) {
		return problemaRepository.save(problema);
	}
	
	/*buscar un problema por ID*/
	public Problema buscarProblema(int id) {
		return problemaRepository.getOne(id);
	}
	
	/*listar problemas*/
	public List<Problema> listarProblemas() {
		return problemaRepository.findAll();
	}
	
	/*buscar un problema por DNI de conductor*/
	public List<Problema> buscarProblemaPorConductor (int idUsuarioEmi) {
		return problemaRepository.findFirstByIdUsuarioEmi(idUsuarioEmi);
	}

	public List<Problema> buscarProblemaPorMecanico (int idUsuarioRecep) {
		return problemaRepository.findFirstByIdUsuarioRecep(idUsuarioRecep);
	}

	public List<Problema> buscarProblemaPorMecanico2 (int idUsuarioRecep, String estado) {
		return problemaRepository.findFirstByIdUsuarioRecepAndEstado(idUsuarioRecep, estado);
	}
	
	public Problema buscarUltimoProblemaDeConductor (int idUsuarioRecep) {
		return problemaRepository.findFirstByIdUsuarioEmiOrderByIdProblemaDesc(idUsuarioRecep);
	}

	public Problema buscarUltimoProblemaDeMecanico (int idUsuarioRecep) {
		return problemaRepository.findFirstByIdUsuarioRecepOrderByIdProblemaDesc(idUsuarioRecep);
	}
	
}
