package com.example.backend.rest.type.business;

public class ProblemaRest {

	private int codigoProblema;
	private String conductor;
	private String mecanico;
	private String detalles;
	private String foto;
	private String accion;
	private String latitud;
	private String longitud;

	private UsuarioRest datosConductor;
	private UsuarioRest datosMecanico;
	
	public int getCodigoProblema() {
		return codigoProblema;
	}
	public void setCodigoProblema(int codigoProblema) {
		this.codigoProblema = codigoProblema;
	}
	public String getConductor() {
		return conductor;
	}
	public void setConductor(String conductor) {
		this.conductor = conductor;
	}
	public String getMecanico() {
		return mecanico;
	}
	public void setMecanico(String mecanico) {
		this.mecanico = mecanico;
	}
	public String getDetalles() {
		return detalles;
	}
	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public UsuarioRest getDatosConductor() {
		return datosConductor;
	}
	public void setDatosConductor(UsuarioRest datosConductor) {
		this.datosConductor = datosConductor;
	}
	public UsuarioRest getDatosMecanico() {
		return datosMecanico;
	}
	public void setDatosMecanico(UsuarioRest datosMecanico) {
		this.datosMecanico = datosMecanico;
	}
	
	
}
