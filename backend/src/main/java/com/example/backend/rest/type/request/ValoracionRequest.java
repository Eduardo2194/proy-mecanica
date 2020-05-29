package com.example.backend.rest.type.request;

import java.io.Serializable;

import com.example.backend.rest.type.business.ValoracionRest;

public class ValoracionRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ValoracionRest valoracion;

	public ValoracionRest getValoracion() {
		return valoracion;
	}

	public void setValoracion(ValoracionRest valoracion) {
		this.valoracion = valoracion;
	}
	
}
