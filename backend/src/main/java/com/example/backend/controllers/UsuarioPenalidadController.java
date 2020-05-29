package com.example.backend.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.constants.ApplicationConstants;
import com.example.backend.engine.context.ContextHolder;
import com.example.backend.engine.context.UsuarioPenalidadContext;
import com.example.backend.engine.service.PenalidadProcessService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.rest.type.request.RegistrarPagoPenalidadRequest;
import com.example.backend.rest.type.request.RegistrarUsuarioPenalidadRequest;
import com.example.backend.rest.type.response.RegistrarPagoPenalidadResponse;
import com.example.backend.rest.type.response.RegistrarUsuarioPenalidadResponse;
import com.example.backend.utils.Utils;

@RestController
@RequestMapping(ApplicationConstants.APPLICATION_CONTEXT)
public class UsuarioPenalidadController {
	
	private final static String BASE_PATH = "/usuario/penalidad";
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioPenalidadController.class);
	
	@Autowired
	private PenalidadProcessService processService;
	
	@RequestMapping(path = BASE_PATH + "/registrarPago", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<RegistrarPagoPenalidadResponse> processRegistrarPago(@RequestBody RegistrarPagoPenalidadRequest request) {
		final String methodName = "processRegistrarPago";
		LOGGER.traceEntry(methodName);
		RegistrarPagoPenalidadResponse response = new RegistrarPagoPenalidadResponse();
		ResponseEntity<RegistrarPagoPenalidadResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			UsuarioPenalidadContext context = ContextHolder.get(UsuarioPenalidadContext.class);
			context.setRegistrarPagoRequest(request);
			context.setRegistrarPagoResponse(response);

			processService.registrarPago();

			response = context.getRegistrarPagoResponse();
			httpStatus = HttpStatus.OK;
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<RegistrarPagoPenalidadResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}
	
	@RequestMapping(path = BASE_PATH + "/registrar", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<RegistrarUsuarioPenalidadResponse> processRegistrarPago(@RequestBody RegistrarUsuarioPenalidadRequest request) {
		final String methodName = "processRegistrarPago";
		LOGGER.traceEntry(methodName);
		RegistrarUsuarioPenalidadResponse response = new RegistrarUsuarioPenalidadResponse();
		ResponseEntity<RegistrarUsuarioPenalidadResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			UsuarioPenalidadContext context = ContextHolder.get(UsuarioPenalidadContext.class);
			context.setRegistrarUsuarioPenalidadRequest(request);
			context.setRegistrarUsuarioPenalidadResponse(response);

			processService.registrarUsuarioPenalidad();

			response = context.getRegistrarUsuarioPenalidadResponse();
			httpStatus = HttpStatus.OK;
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<RegistrarUsuarioPenalidadResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}
	
	
}
