package com.example.backend.rest.type.request;

import com.example.backend.rest.type.business.UsuarioRest;

public class LoginUsuarioRequest {
	
	private UsuarioRest usuario;

	public UsuarioRest getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioRest usuario) {
		this.usuario = usuario;
	}
}
