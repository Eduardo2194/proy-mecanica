package com.example.backend.engine.context;

import com.example.backend.rest.type.request.ValoracionRequest;
import com.example.backend.rest.type.response.ValoracionResponse;

public class ValoracionContext extends Context {

	private ValoracionRequest  valoracionRequest;
	private ValoracionResponse valoracionResponse;
	
	public ValoracionRequest getValoracionRequest() {
		return valoracionRequest;
	}
	public void setValoracionRequest(ValoracionRequest valoracionRequest) {
		this.valoracionRequest = valoracionRequest;
	}
	public ValoracionResponse getValoracionResponse() {
		return valoracionResponse;
	}
	public void setValoracionResponse(ValoracionResponse valoracionResponse) {
		this.valoracionResponse = valoracionResponse;
	}
}
