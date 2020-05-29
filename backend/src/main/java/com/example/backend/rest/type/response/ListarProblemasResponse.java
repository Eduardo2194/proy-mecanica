package com.example.backend.rest.type.response;

import java.util.List;

import com.example.backend.rest.type.base.ResponseBase;
import com.example.backend.rest.type.business.ListProblemaRest;

public class ListarProblemasResponse extends ResponseBase {

	private List<ListProblemaRest> listaProblema;

	public List<ListProblemaRest> getListaProblema() {
		return listaProblema;
	}

	public void setListaProblema(List<ListProblemaRest> listaProblema) {
		this.listaProblema = listaProblema;
	}
}
