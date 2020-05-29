package com.example.backend.engine.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.example.backend.engine.context.ContextHolder;
import com.example.backend.engine.context.UserContext;
import com.example.backend.engine.service.ValidationService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.repository.enums.TipoUsuarioEnum;
import com.example.backend.rest.type.business.UsuarioRest;
import com.example.backend.rest.type.request.LoginUsuarioRequest;
import com.example.backend.rest.type.request.GestionarUsuarioRequest;

@Service
public class ValidationServiceImpl implements ValidationService{
	
	private static final Logger LOGGER = LogManager.getLogger(ValidationServiceImpl.class);
	
	@Override
	public void validateLoginParams() {
		final String methodName = "validateLoginParams";
		LOGGER.traceEntry(methodName);
		UserContext context = ContextHolder.get(UserContext.class);
		LoginUsuarioRequest request = context.getLoginRequest();
		
		if(request == null) {
			throw new BackEndException("El request no puede ser nulo");
		}
		
		/*if (request.getUsuario() == null) {
			throw new BackEndException("El objeto usuario no puede ser nulo");
		} else if (request.getUsuario().getDni() == null || request.getUsuario().getDni().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.dni no puede ser nulo o vacio");	
		} else if (request.getUsuario().getContrasena() == null || request.getUsuario().getContrasena().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.contrasena no puede ser nulo o vacio");
		}*/
		this.validateUsuarioBasicData(request.getUsuario());
		
		LOGGER.traceExit(methodName);
	}


	@Override
	public void validateRegistroParams() {
		final String methodName = "validateRegistroParams";
		LOGGER.traceEntry(methodName);
		UserContext context = ContextHolder.get(UserContext.class);
		GestionarUsuarioRequest request = context.getGestionarRequest();
		
		if(request == null) {
			throw new BackEndException("El request no puede ser nulo");
		}
		
		this.validateUsuarioBasicData(request.getUsuario());

		if (request.getUsuario() == null) {
			throw new BackEndException("El objeto usuario no puede ser nulo");
		} else if (request.getUsuario().getNombres() == null || request.getUsuario().getNombres().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.nombre no puede ser nulo o vacio");
		} else if (request.getUsuario().getApellidos() == null || request.getUsuario().getApellidos().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.apellidos no puede ser nulo o vacio");
		} else if (request.getUsuario().getCelular() == null || request.getUsuario().getCelular().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.celular no puede ser nulo o vacio");
		} else if (request.getUsuario().getCorreo() == null || request.getUsuario().getCorreo().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.correo no puede ser nulo o vacio");
		} else if (request.getUsuario().getTipo() == null || request.getUsuario().getTipo().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.tipo no puede ser nulo o vacio");
		}
		/* else if (request.getUsuario().getFirebaseToken() == null || request.getUsuario().getFirebaseToken().trim().isEmpty()){
			throw new BackEndException("El campo usuario.firebaseToken no puede ser nulo o vacio");
		}*/
		
		LOGGER.traceExit(methodName);
	}

	@Override
	public void validateMecanicoExclusiveParams(){
		final String methodName = "validateRegistroParams";
		LOGGER.traceEntry(methodName);
		UserContext context = ContextHolder.get(UserContext.class);
		UsuarioRest usuario = context.getUserRestEntity();
		if (usuario == null) {
			throw new BackEndException("El objeto usuario no puede ser nulo");
		} else if (usuario.getFoto() == null || usuario.getFoto().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.foto no puede ser nulo o vacio para un usuario del tipo "+TipoUsuarioEnum.MECANICO.getCode());
		}
	}

	private void validateUsuarioBasicData(UsuarioRest usuario) {
		if (usuario == null) {
			throw new BackEndException("El objeto usuario no puede ser nulo");
		} else if (usuario.getDni() == null || usuario.getDni().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.dni no puede ser nulo o vacio");	
		} else if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
			throw new BackEndException("El campo usuario.contrasena no puede ser nulo o vacio");
		} 
	}

	



}
