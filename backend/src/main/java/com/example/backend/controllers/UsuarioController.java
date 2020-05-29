package com.example.backend.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.constants.ApplicationConstants;
import com.example.backend.engine.context.ContextHolder;
import com.example.backend.engine.context.UserContext;
import com.example.backend.engine.service.UserProcessService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.rest.type.request.GestionarUsuarioRequest;
import com.example.backend.rest.type.request.LoginUsuarioRequest;
import com.example.backend.rest.type.response.ConsultaUsuarioPenalidadResponse;
import com.example.backend.rest.type.response.GestionarUsuarioResponse;
import com.example.backend.rest.type.response.LoginUsuarioResponse;
import com.example.backend.utils.Utils;

@RestController
@RequestMapping(ApplicationConstants.APPLICATION_CONTEXT)
public class UsuarioController {
	
	private final static String BASE_PATH = "/usuario";
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioController.class);
	
	@Autowired 
	private UserProcessService processService;
	
	@RequestMapping(path = BASE_PATH + "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<LoginUsuarioResponse> processLoginUsuario(@RequestBody LoginUsuarioRequest request) {
		final String methodName = "processLoginUsuario";
		LOGGER.traceEntry(methodName);
		LoginUsuarioResponse response = new LoginUsuarioResponse();
		ResponseEntity<LoginUsuarioResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			UserContext context = ContextHolder.get(UserContext.class);
			context.setLoginRequest(request);
			context.setLoginResponse(response);

			processService.login();

			response = context.getLoginResponse();
			httpStatus = HttpStatus.OK;
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<LoginUsuarioResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}
	
	
	@RequestMapping(path = BASE_PATH, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<GestionarUsuarioResponse> processRegistrarUsuario(@RequestBody GestionarUsuarioRequest request) {
		final String methodName = "processRegistrarUsuario";
		LOGGER.traceEntry(methodName);
		GestionarUsuarioResponse response = new GestionarUsuarioResponse();
		ResponseEntity<GestionarUsuarioResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			UserContext context = ContextHolder.get(UserContext.class);
			context.setGestionarRequest(request);
			context.setGestionarResponse(response);

			processService.registrar();

			response = context.getGestionarResponse();
			httpStatus = HttpStatus.OK; 
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<GestionarUsuarioResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}
	
	@RequestMapping(path = BASE_PATH, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<GestionarUsuarioResponse> processActualizarUsuario(@RequestBody GestionarUsuarioRequest request) {
		final String methodName = "processActualizarUsuario";
		LOGGER.traceEntry(methodName);
		GestionarUsuarioResponse response = new GestionarUsuarioResponse();
		ResponseEntity<GestionarUsuarioResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			UserContext context = ContextHolder.get(UserContext.class);
			context.setGestionarRequest(request);
			context.setGestionarResponse(response);

			processService.actualizar();

			response = context.getGestionarResponse();
			httpStatus = HttpStatus.OK; 
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<GestionarUsuarioResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}

	
	@RequestMapping(path = BASE_PATH + "/{dni}/penalidad", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<ConsultaUsuarioPenalidadResponse> consultaUsuarioPenalidad(
    		@PathVariable(name="dni", required=true)String dni){
		final String methodName = "consultaUsuarioPenalidad";
		LOGGER.traceEntry(methodName);
		ConsultaUsuarioPenalidadResponse response = new ConsultaUsuarioPenalidadResponse();
		ResponseEntity<ConsultaUsuarioPenalidadResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			UserContext context = ContextHolder.get(UserContext.class);
			context.setDni(dni);
			context.setConsultaPenalidadResponse(response);

			processService.consultaPenalidad();

			response = context.getConsultaPenalidadResponse();
			httpStatus = HttpStatus.OK; 
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<ConsultaUsuarioPenalidadResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}
	
	@RequestMapping(path = BASE_PATH + "/recuperar", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<GestionarUsuarioResponse> processRecuperarContrasena(@RequestBody GestionarUsuarioRequest request) {
		final String methodName = "processRecuperarContrasena";
		LOGGER.traceEntry(methodName);
		GestionarUsuarioResponse response = new GestionarUsuarioResponse();
		ResponseEntity<GestionarUsuarioResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			UserContext context = ContextHolder.get(UserContext.class);
			context.setGestionarRequest(request);
			context.setGestionarResponse(response);

			processService.recuperarContrasena();

			response = context.getGestionarResponse();
			httpStatus = HttpStatus.OK;
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<GestionarUsuarioResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}

	
	
	
}
