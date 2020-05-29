package com.example.backend.rest.type.response;

import com.example.backend.rest.type.base.ResponseBase;
import com.example.backend.rest.type.business.PlanesSuscripcionRest;
import com.example.backend.rest.type.business.SuscripcionRest;
import com.example.backend.rest.type.business.UsuarioRest;

public class RegistrarSuscripcionResponse extends ResponseBase {

    private UsuarioRest usuario;
    private PlanesSuscripcionRest plan; 
    private SuscripcionRest suscripcion;

	public UsuarioRest getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioRest usuario) {
		this.usuario = usuario;
	}
	public PlanesSuscripcionRest getPlan() {
		return plan;
	}
	public void setPlan(PlanesSuscripcionRest plan) {
		this.plan = plan;
	}
	public SuscripcionRest getSuscripcion() {
		return suscripcion;
	}
	public void setSuscripcion(SuscripcionRest suscripcion) {
		this.suscripcion = suscripcion;
	}
	
}