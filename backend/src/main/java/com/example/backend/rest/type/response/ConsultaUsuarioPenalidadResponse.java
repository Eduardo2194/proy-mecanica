package com.example.backend.rest.type.response;

import com.example.backend.rest.type.base.ResponseBase;
import com.example.backend.rest.type.business.PenalidadRest;
import com.example.backend.rest.type.business.UsuarioRest;

public class ConsultaUsuarioPenalidadResponse extends ResponseBase {

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