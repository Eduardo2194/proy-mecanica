package com.example.backend.rest.type.request;

import com.example.backend.rest.type.business.PenalidadRest;
import com.example.backend.rest.type.business.UsuarioRest;

public class RegistrarUsuarioPenalidadRequest {
	
	private UsuarioRest usuario;
	private PenalidadRest penalidad;
	public UsuarioRest getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioRest usuario) {
		this.usuario = usuario;
	}
	public PenalidadRest getPenalidad() {
		return penalidad;
	}
	public void setPenalidad(PenalidadRest penalidad) {
		this.penalidad = penalidad;
	}

}
