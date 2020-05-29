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
import com.example.backend.engine.context.SuscripcionContext;
import com.example.backend.engine.service.SuscripcionProcessService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.rest.type.request.RegistrarSuscripcionRequest;
import com.example.backend.rest.type.response.ConsultaPlanesSuscripcionResponse;
import com.example.backend.rest.type.response.RegistrarSuscripcionResponse;
import com.example.backend.utils.Utils;

@RestController
@RequestMapping(ApplicationConstants.APPLICATION_CONTEXT)
public class SuscripcionController {

    private final static String BASE_PATH = "/suscripcion";

    private static final Logger LOGGER = LogManager.getLogger(SuscripcionController.class);

    @Autowired 
	private SuscripcionProcessService processService;
    
    @RequestMapping(path = BASE_PATH+"/registrar", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<RegistrarSuscripcionResponse> registrarSuscripcion (@RequestBody RegistrarSuscripcionRequest request) {
        final String methodName = "registrarSuscripcion";
        LOGGER.traceEntry(methodName);
        RegistrarSuscripcionResponse response = new RegistrarSuscripcionResponse();
        ResponseEntity<RegistrarSuscripcionResponse> httpResponse = null;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			
			SuscripcionContext context = ContextHolder.get(SuscripcionContext.class);
			context.setRegistrarRequest(request);
			context.setRegistrarResponse(response);
			
			processService.registrar();
			
			response = context.getRegistrarResponse();
			httpStatus = HttpStatus.OK;
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<RegistrarSuscripcionResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
    }
    
    
    @RequestMapping(path = BASE_PATH+"/planes", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<ConsultaPlanesSuscripcionResponse> consultaPlanesSuscripcion (){
        final String methodName = "consultaPlanesSuscripcion";
        LOGGER.traceEntry(methodName);
        ConsultaPlanesSuscripcionResponse response = new ConsultaPlanesSuscripcionResponse();
        ResponseEntity<ConsultaPlanesSuscripcionResponse> httpResponse = null;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			
			SuscripcionContext context = ContextHolder.get(SuscripcionContext.class);
			context.setConsultaPlanesResponse(response);
			
			processService.consultaPlanes();
			
			response = context.getConsultaPlanesResponse();
			httpStatus = HttpStatus.OK;
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<ConsultaPlanesSuscripcionResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
    }
    
}