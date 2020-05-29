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
import com.example.backend.engine.context.ProblemaContext;
import com.example.backend.engine.context.UserContext;
import com.example.backend.engine.service.impl.ProblemProcessServiceImpl;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.rest.type.request.GestionarProblemaRequest;
import com.example.backend.rest.type.response.GestionarProblemaResponse;
import com.example.backend.rest.type.response.ListarProblemasResponse;
import com.example.backend.utils.Utils;

@RestController
@RequestMapping(ApplicationConstants.APPLICATION_CONTEXT)
public class ProblemaController {

	private final static String BASE_PATH = "/problema";
	
	private static final Logger LOGGER = LogManager.getLogger(ProblemaController.class);
	
	@Autowired 
	private ProblemProcessServiceImpl processService;
		
	@RequestMapping(path = BASE_PATH , method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<GestionarProblemaResponse> processUpdateProblem(@RequestBody GestionarProblemaRequest request) {
		final String methodName = "processUpdateProblem";
		LOGGER.traceEntry(methodName);
		GestionarProblemaResponse response = new GestionarProblemaResponse();
		ResponseEntity<GestionarProblemaResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			ProblemaContext context = ContextHolder.get(ProblemaContext.class);
			context.setRegistrarProblemaRequest(request);
			context.setRegistrarProblemaResponse(response);

			processService.registrar();

			response = context.getRegistrarProblemaResponse();
			httpStatus = HttpStatus.OK; 
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<GestionarProblemaResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}
	
	@RequestMapping(path = BASE_PATH + "/listar" , method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<ListarProblemasResponse> processListProblem() {
		final String methodName = "processListProblem";
		LOGGER.traceEntry(methodName);
		ListarProblemasResponse response = new ListarProblemasResponse();
		ResponseEntity<ListarProblemasResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			UserContext context = ContextHolder.get(UserContext.class);
			context.setListarProblemasResponse(response);

			processService.listar();

			response = context.getListarProblemasResponse();
			httpStatus = HttpStatus.OK; 
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<ListarProblemasResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}

	@RequestMapping(path = BASE_PATH + "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<GestionarProblemaResponse> processGetProblem(@PathVariable(name="id", required=true)String id) {
		final String methodName = "processGetProblem";
		LOGGER.traceEntry(methodName);
		GestionarProblemaResponse response = new GestionarProblemaResponse();
		ResponseEntity<GestionarProblemaResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			ProblemaContext context = ContextHolder.get(ProblemaContext.class);
			context.setProblemaId(id);
			context.setRegistrarProblemaResponse(response);

			processService.consultar();

			response = context.getRegistrarProblemaResponse();
			httpStatus = HttpStatus.OK; 
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<GestionarProblemaResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}

	@RequestMapping(path = BASE_PATH + "/consulta/conductor/{dniConductor}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<GestionarProblemaResponse> processGetProblemXConductor(@PathVariable(name="dniConductor", required=true)String dniConductor) {
		final String methodName = "processGetProblemXConductor";
		LOGGER.traceEntry(methodName);
		GestionarProblemaResponse response = new GestionarProblemaResponse();
		ResponseEntity<GestionarProblemaResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			ProblemaContext context = ContextHolder.get(ProblemaContext.class);
			context.setDniConductor(dniConductor);
			context.setRegistrarProblemaResponse(response);

			processService.consultarXConductor();

			response = context.getRegistrarProblemaResponse();
			httpStatus = HttpStatus.OK; 
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<GestionarProblemaResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}

	@RequestMapping(path = BASE_PATH + "/consulta/mecanico/{dniMecanico}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<GestionarProblemaResponse> processGetProblemXMecanico(@PathVariable(name="dniMecanico", required=true)String dniMecanico) {
		final String methodName = "processGetProblemXMecanico";
		LOGGER.traceEntry(methodName);
		GestionarProblemaResponse response = new GestionarProblemaResponse();
		ResponseEntity<GestionarProblemaResponse> httpResponse = null;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; 
		try {
			ProblemaContext context = ContextHolder.get(ProblemaContext.class);
			context.setDniMecanico(dniMecanico);
			context.setRegistrarProblemaResponse(response);

			processService.consultarXMecanico();

			response = context.getRegistrarProblemaResponse();
			httpStatus = HttpStatus.OK; 
		} catch (BackEndException e) {
			response.setStatus(Utils.buildErrorValidationStatus(e));
		} catch (Exception e) {
			response.setStatus(Utils.buildErrorStatus(e));
		} 
		httpResponse = new ResponseEntity<GestionarProblemaResponse>(response, httpStatus);
		LOGGER.traceExit();
		return httpResponse;
	}

	
}
