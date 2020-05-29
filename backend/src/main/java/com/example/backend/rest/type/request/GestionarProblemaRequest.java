package com.example.backend.rest.type.request;

import java.io.Serializable;

import com.example.backend.rest.type.business.ProblemaRest;

public class GestionarProblemaRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ProblemaRest problema;

	public ProblemaRest getProblema() {
		return problema;
	}

	public void setProblema(ProblemaRest problema) {
		this.problema = problema;
	}
}
