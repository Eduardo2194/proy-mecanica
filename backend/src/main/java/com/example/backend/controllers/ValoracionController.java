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
import com.example.backend.engine.context.ValoracionContext;
import com.example.backend.engine.service.ValoracionService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.rest.type.request.ValoracionRequest;
import com.example.backend.rest.type.response.ValoracionResponse;
import com.example.backend.utils.Utils;

@RestController
@RequestMapping(ApplicationConstants.APPLICATION_CONTEXT)
public class ValoracionController {

	private final static String BASE_PATH = "/valoracion";

	private static final Logger LOGGER = LogManager.getLogger(ValoracionController.class);
	
	@Autowired 
	private ValoracionService processService;
	
	@RequestMapping(path = BASE_PATH , method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<ValoracionResponse> processValoracionMecanico(@RequestBody ValoracionRequest request) {
		
		final String methodName = "processValoracionMecanico";
		LOGGER.traceEntry(methodName);
		ValoracionResponse response = new ValoracionResponse();
		ResponseEntity<ValoracionResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		
		try {
			ValoracionContext context = ContextHolder.get(ValoracionContext.class);
			context.setValoracionRequest(request);
			context.setValoracionResponse(response);

			processService.registrar();

			response = context.getValoracionResponse();
			httpStatus = HttpStatus.OK;
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<ValoracionResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
		
	}





}
