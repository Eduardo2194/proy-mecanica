package com.example.backend.rest.type.response;

import com.example.backend.rest.type.base.ResponseBase;
import com.example.backend.rest.type.business.ValoracionRest;

public class ValoracionResponse extends ResponseBase {

	private ValoracionRest valoracion;

	public ValoracionRest getValoracion() {
		return valoracion;
	}

	public void setValoracion(ValoracionRest valoracion) {
		this.valoracion = valoracion;
	}
}
