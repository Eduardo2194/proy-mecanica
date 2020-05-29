package com.example.backend.rest.type.response;

import com.example.backend.rest.type.base.ResponseBase;
import com.example.backend.rest.type.business.UsuarioRest;

public class GestionarUsuarioResponse extends ResponseBase {

	private UsuarioRest usuario;

	public UsuarioRest getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioRest usuario) {
		this.usuario = usuario;
	}
}
