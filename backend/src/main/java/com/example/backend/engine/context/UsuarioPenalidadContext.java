package com.example.backend.engine.context;

import com.example.backend.rest.type.request.RegistrarPagoPenalidadRequest;
import com.example.backend.rest.type.request.RegistrarUsuarioPenalidadRequest;
import com.example.backend.rest.type.response.RegistrarPagoPenalidadResponse;
import com.example.backend.rest.type.response.RegistrarUsuarioPenalidadResponse;

public class UsuarioPenalidadContext extends Context {
	
	private RegistrarPagoPenalidadRequest registrarPagoRequest;
	private RegistrarPagoPenalidadResponse registrarPagoResponse;
	
	private RegistrarUsuarioPenalidadRequest registrarUsuarioPenalidadRequest;
	private RegistrarUsuarioPenalidadResponse registrarUsuarioPenalidadResponse;
	
	public RegistrarPagoPenalidadRequest getRegistrarPagoRequest() {
		return registrarPagoRequest;
	}
	public void setRegistrarPagoRequest(RegistrarPagoPenalidadRequest registrarPagoRequest) {
		this.registrarPagoRequest = registrarPagoRequest;
	}
	public RegistrarPagoPenalidadResponse getRegistrarPagoResponse() {
		return registrarPagoResponse;
	}
	public void setRegistrarPagoResponse(RegistrarPagoPenalidadResponse registrarPagoResponse) {
		this.registrarPagoResponse = registrarPagoResponse;
	}
	public RegistrarUsuarioPenalidadRequest getRegistrarUsuarioPenalidadRequest() {
		return registrarUsuarioPenalidadRequest;
	}
	public void setRegistrarUsuarioPenalidadRequest(RegistrarUsuarioPenalidadRequest registrarUsuarioPenalidadRequest) {
		this.registrarUsuarioPenalidadRequest = registrarUsuarioPenalidadRequest;
	}
	public RegistrarUsuarioPenalidadResponse getRegistrarUsuarioPenalidadResponse() {
		return registrarUsuarioPenalidadResponse;
	}
	public void setRegistrarUsuarioPenalidadResponse(RegistrarUsuarioPenalidadResponse registrarUsuarioPenalidadResponse) {
		this.registrarUsuarioPenalidadResponse = registrarUsuarioPenalidadResponse;
	}
}
