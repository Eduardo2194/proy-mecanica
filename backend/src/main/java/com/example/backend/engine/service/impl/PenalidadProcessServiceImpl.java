package com.example.backend.engine.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.dao.PenalidadDAO;
import com.example.backend.dao.SuscripcionDAO;
import com.example.backend.dao.UsuarioDAO;
import com.example.backend.dao.UsuarioPenalidadDAO;
import com.example.backend.engine.context.ContextHolder;
import com.example.backend.engine.context.UsuarioPenalidadContext;
import com.example.backend.engine.service.PenalidadProcessService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.repository.bean.Penalidad;
import com.example.backend.repository.bean.Suscripcion;
import com.example.backend.repository.bean.Usuario;
import com.example.backend.repository.bean.UsuarioPenalidad;
import com.example.backend.repository.enums.EstadoGeneral;
import com.example.backend.repository.enums.EstadoUsuarioEnum;
import com.example.backend.rest.type.base.ResponseStatusBase;
import com.example.backend.rest.type.request.RegistrarPagoPenalidadRequest;
import com.example.backend.rest.type.request.RegistrarUsuarioPenalidadRequest;
import com.example.backend.rest.type.response.RegistrarPagoPenalidadResponse;
import com.example.backend.rest.type.response.RegistrarUsuarioPenalidadResponse;

@Service
public class PenalidadProcessServiceImpl implements PenalidadProcessService {

	private static final Logger LOGGER = LogManager.getLogger(PenalidadProcessServiceImpl.class);
	
	@Autowired
	private UsuarioPenalidadDAO usuarioPenalidadDAO;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private SuscripcionDAO suscripcionDAO;
	
	@Autowired
	private PenalidadDAO penalidadDAO;
	
	@Override
	public void registrarPago() {
		final String methodName = "registrarPago";
		LOGGER.traceEntry(methodName);
		
		UsuarioPenalidadContext context = ContextHolder.get(UsuarioPenalidadContext.class);
		RegistrarPagoPenalidadRequest request = context.getRegistrarPagoRequest();
		RegistrarPagoPenalidadResponse response = context.getRegistrarPagoResponse();
		
		//Recuperando datos del usuario
		Usuario usuario = usuarioDAO.recuperarUsuarioPorDNIyConPenalidad(request.getUsuario().getDni(), EstadoUsuarioEnum.BLOQUEADO_X_PENALIDAD.getCode());
		if (usuario == null) {
			LOGGER.info("No existe el usuario con penalidad para el dni enviado");
			throw new BackEndException("No existe el usuario con penalidad para el dni enviado");
		} 
		
		
		//Buscando y actualizando penalidad del usuario
		UsuarioPenalidad usuarioPenalidad = usuarioPenalidadDAO.recuperarPenalidadActivaPorDni(request.getUsuario().getDni(), EstadoGeneral.ACTIVO.getCode());
		if (usuarioPenalidad == null) {
			LOGGER.info("No existe penalidad para el dni ingresado");
			throw new BackEndException("No existe penalidad para el dni ingresado");
		}
		
		usuarioPenalidad.setEstado(EstadoGeneral.ELIMINADO.getCode());
		usuarioPenalidad = usuarioPenalidadDAO.actualizarUsuarioPenalidad(usuarioPenalidad);
		if (usuarioPenalidad == null){
			LOGGER.info("No se pudo registrar el pago");
			throw new BackEndException("No se pudo registrar el pago");
		}
		
		
		//Activando al usuario
		List<Suscripcion> suscripciones = suscripcionDAO.recuperarSuscripcionesUsuario(usuario.getDni());
		if(suscripciones == null || suscripciones.isEmpty()) {
			//Si el usuario no contaba con suscripciones regresa a su estado de "En prueba"
			usuario.setEstado(EstadoUsuarioEnum.EN_PRUEBA.getCode());
		} else {
			//Si registra al menos una suscripcion, se le devuelve el estado activo.
		
			usuario.setEstado(EstadoUsuarioEnum.ACTIVO.getCode());
		}
		
		usuario = usuarioDAO.actualizarUsuario(usuario);
		if (usuario == null) {
			LOGGER.info("No se pudo actualizar el estado del usuario");
			throw new BackEndException("No se pudo actualizar el estado del usuario");
		}
		
		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Registro de pago OK");
			
		LOGGER.traceExit();
	}

	@Override
	public void registrarUsuarioPenalidad() {
		final String methodName = "registrarUsuarioPenalidad";
		LOGGER.traceEntry(methodName);
		
		UsuarioPenalidadContext context = ContextHolder.get(UsuarioPenalidadContext.class);
		RegistrarUsuarioPenalidadRequest request = context.getRegistrarUsuarioPenalidadRequest();
		RegistrarUsuarioPenalidadResponse response = context.getRegistrarUsuarioPenalidadResponse();
		
		
		List<Integer> estadoInvalidos = Arrays.asList(new Integer[] {
				EstadoUsuarioEnum.BLOQUEADO_X_FIN_SUSCRIPCION.getCode(),
				EstadoUsuarioEnum.BLOQUEADO_X_FIN_PRUEBA.getCode(),
				EstadoUsuarioEnum.ELIMINADO.getCode(),
				EstadoUsuarioEnum.BLOQUEADO_X_PENALIDAD.getCode()
			});
		Usuario usuario = usuarioDAO.recuperarUsuarioPorDNI(request.getUsuario().getDni(), estadoInvalidos);
		if (usuario == null) {
			LOGGER.info("No existe el usuario para el dni enviado, o el estado del usuario no permite la operacion");
			throw new BackEndException("No existe el usuario para el dni enviado, o el estado del usuario no permite la operacion");
		}
		
		Penalidad penalidad = penalidadDAO.recuperarPenalidadActivaPorCodigo(request.getPenalidad().getCodigo(), EstadoGeneral.ACTIVO.getCode());
		
		if(penalidad == null) {
			LOGGER.info("Codigo de penalidad enviado invalido");
			throw new BackEndException("Codigo de penalidad enviado invalido");
		}
		
		UsuarioPenalidad usuarioPenalidad = new UsuarioPenalidad();
		usuarioPenalidad.setCodigoPenalidad(request.getPenalidad().getCodigo());
		usuarioPenalidad.setDniUsuario(request.getUsuario().getDni());
		usuarioPenalidad.setEstado(EstadoGeneral.ACTIVO.getCode());
		
		usuarioPenalidad = usuarioPenalidadDAO.registrarUsuarioPenalidad(usuarioPenalidad);
		
		if (usuarioPenalidad == null) {
			LOGGER.info("No se pudo registrar la penalidad al usuario");
			throw new BackEndException("No se pudo registrar la penalidad al usuario");
		}
		
		usuario.setEstado(EstadoUsuarioEnum.BLOQUEADO_X_PENALIDAD.getCode());
		
		usuario = usuarioDAO.actualizarUsuario(usuario);
		if (usuario == null) {
			LOGGER.info("No se pudo actualizar el estado del usuario");
			throw new BackEndException("No se pudo actualizar el estado del usuario");
		} 
		
		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Registro de penalidad OK");
			
		LOGGER.traceExit();
	}

}
