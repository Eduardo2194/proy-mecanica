package com.example.backend.engine.context;

import com.example.backend.rest.type.request.GestionarProblemaRequest;
import com.example.backend.rest.type.response.GestionarProblemaResponse;

public class ProblemaContext extends Context{
	
	private String problemaId;
	private String dniConductor;
	private String dniMecanico;
	private GestionarProblemaRequest registrarProblemaRequest;
	private GestionarProblemaResponse registrarProblemaResponse;
	
	public GestionarProblemaRequest getRegistrarProblemaRequest() {
		return registrarProblemaRequest;
	}
	public void setRegistrarProblemaRequest(GestionarProblemaRequest registrarProblemaRequest) {
		this.registrarProblemaRequest = registrarProblemaRequest;
	}
	public GestionarProblemaResponse getRegistrarProblemaResponse() {
		return registrarProblemaResponse;
	}
	public void setRegistrarProblemaResponse(GestionarProblemaResponse registrarProblemaResponse) {
		this.registrarProblemaResponse = registrarProblemaResponse;
	}
	public String getProblemaId() {
		return problemaId;
	}
	public void setProblemaId(String problemaId) {
		this.problemaId = problemaId;
	}
	public String getDniConductor() {
		return dniConductor;
	}
	public void setDniConductor(String dniConductor) {
		this.dniConductor = dniConductor;
	}
	public String getDniMecanico() {
		return dniMecanico;
	}
	public void setDniMecanico(String dniMecanico) {
		this.dniMecanico = dniMecanico;
	}
	
}
