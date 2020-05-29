package com.example.backend.rest.type.response;

import com.example.backend.rest.type.base.ResponseBase;
import com.example.backend.rest.type.business.ProblemaRest;

public class GestionarProblemaResponse extends ResponseBase {

	private ProblemaRest problema;

	public ProblemaRest getProblema() {
		return problema;
	}

	public void setProblema(ProblemaRest problema) {
		this.problema = problema;
	}
}
