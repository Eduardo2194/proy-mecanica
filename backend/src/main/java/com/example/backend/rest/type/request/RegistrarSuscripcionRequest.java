package com.example.backend.rest.type.request;

import com.example.backend.repository.bean.PlanesSuscripcion;
import com.example.backend.rest.type.business.UsuarioRest;

public class RegistrarSuscripcionRequest {
	
    private UsuarioRest usuario;
    private PlanesSuscripcion plan;

	public UsuarioRest getUsuario() {
		return usuario;
	}
    public void setUsuario(UsuarioRest usuario) {
		this.usuario = usuario;
    }

    public PlanesSuscripcion getPlan() {
        return plan;
    }
    public void setPlan(PlanesSuscripcion plan) {
        this.plan = plan;
    }
    
}