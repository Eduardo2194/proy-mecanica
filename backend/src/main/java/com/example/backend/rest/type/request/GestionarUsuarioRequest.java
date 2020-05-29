package com.example.backend.rest.type.request;

import java.io.Serializable;

import com.example.backend.rest.type.business.UsuarioRest;

public class GestionarUsuarioRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UsuarioRest usuario;

	public UsuarioRest getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioRest usuario) {
		this.usuario = usuario;
	}
}
