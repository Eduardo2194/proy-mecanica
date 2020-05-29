package com.example.backend.rest.type.business;

import java.util.Date;

public class ListProblemaRest {

	private int codigoProblema;
	private String nombreConductor;
	private String detalleProblema;
	private String foto;
	private Date fechaRegistro;
	
	public int getCodigoProblema() {
		return codigoProblema;
	}
	public void setCodigoProblema(int codigoProblema) {
		this.codigoProblema = codigoProblema;
	}
	public String getNombreConductor() {
		return nombreConductor;
	}
	public void setNombreConductor(String nombreConductor) {
		this.nombreConductor = nombreConductor;
	}
	public String getDetalleProblema() {
		return detalleProblema;
	}
	public void setDetalleProblema(String detalleProblema) {
		this.detalleProblema = detalleProblema;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
}
