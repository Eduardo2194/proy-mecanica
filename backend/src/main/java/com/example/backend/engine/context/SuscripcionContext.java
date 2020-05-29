package com.example.backend.engine.context;

import com.example.backend.rest.type.request.RegistrarSuscripcionRequest;
import com.example.backend.rest.type.response.ConsultaPlanesSuscripcionResponse;
import com.example.backend.rest.type.response.RegistrarSuscripcionResponse;

public class SuscripcionContext extends Context {
	
	private RegistrarSuscripcionRequest registrarRequest;
	private RegistrarSuscripcionResponse registrarResponse;
	
	private ConsultaPlanesSuscripcionResponse consultaPlanesResponse;

	public ConsultaPlanesSuscripcionResponse getConsultaPlanesResponse() {
		return consultaPlanesResponse;
	}
	public void setConsultaPlanesResponse(ConsultaPlanesSuscripcionResponse consultaPlanesResponse) {
		this.consultaPlanesResponse = consultaPlanesResponse;
	}
	public RegistrarSuscripcionRequest getRegistrarRequest() {
		return registrarRequest;
	}
	public void setRegistrarRequest(RegistrarSuscripcionRequest registrarRequest) {
		this.registrarRequest = registrarRequest;
	}
	public RegistrarSuscripcionResponse getRegistrarResponse() {
		return registrarResponse;
	}
	public void setRegistrarResponse(RegistrarSuscripcionResponse registrarResponse) {
		this.registrarResponse = registrarResponse;
	}
	
}
