package com.example.backend.engine.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.dao.ValoracionDAO;
import com.example.backend.engine.context.ContextHolder;
import com.example.backend.engine.context.ValoracionContext;
import com.example.backend.engine.service.ValoracionService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.repository.bean.Valoracion;
import com.example.backend.rest.type.base.ResponseStatusBase;
import com.example.backend.rest.type.business.ValoracionRest;
import com.example.backend.rest.type.request.ValoracionRequest;
import com.example.backend.rest.type.response.ValoracionResponse;

@Service
public class ValoracionServiceImpl implements ValoracionService {

	private static final Logger LOGGER = LogManager.getLogger(ValoracionServiceImpl.class);
	
	@Autowired
	private ValoracionDAO valoracionDAO;
	
	@Override
	public void registrar() {
		// TODO Auto-generated method stub
		
		final String methodName = "registrar";
		LOGGER.traceEntry(methodName);
		
		ValoracionContext context = ContextHolder.get(ValoracionContext.class);
		ValoracionRequest request = context.getValoracionRequest();
		ValoracionResponse response= context.getValoracionResponse();
		
		Valoracion valoracionRegistrar = new Valoracion() ;
		valoracionRegistrar.setIdProblema(request.getValoracion().getIdProblema());
		valoracionRegistrar.setValor(request.getValoracion().getValor());
		valoracionRegistrar.setComentarios(request.getValoracion().getComentario());
		
		Valoracion valoracion = valoracionDAO.registrar(valoracionRegistrar);
		
		if (valoracion == null) {
			LOGGER.info("Error al registrar la valoración al mecánico");
			throw new BackEndException("Error al registrar la valoración al mecánico");
		}
		
		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Guardado OK");
		
		response.setValoracion(new ValoracionRest());
		response.getValoracion().setIdentifier(valoracion.getId());
		response.getValoracion().setIdProblema(valoracion.getIdProblema());
		response.getValoracion().setValor(valoracion.getValor());
		response.getValoracion().setComentario(valoracion.getComentarios());
		
		LOGGER.traceExit(methodName);
	}

}
