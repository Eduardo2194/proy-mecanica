package com.example.backend.repository.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="Problema")
@EntityListeners(AuditingEntityListener.class)
public class Problema {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idProblema;
	private Integer idUsuarioEmi;
	private Integer idUsuarioRecep;
	private String detalles;
	private String foto;
	private String estado;
	private Date fechaRegistro;
	private Date fechaFin;
	private String latitud;
	private String longitud;
	
	public Integer getIdProblema() {
		return idProblema;
	}
	public void setIdProblema(Integer idProblema) {
		this.idProblema = idProblema;
	}
	public Integer getIdUsuarioEmi() {
		return idUsuarioEmi;
	}
	public void setIdUsuarioEmi(Integer idUsuarioEmi) {
		this.idUsuarioEmi = idUsuarioEmi;
	}
	public Integer getIdUsuarioRecep() {
		return idUsuarioRecep;
	}
	public void setIdUsuarioRecep(Integer idUsuarioRecep) {
		this.idUsuarioRecep = idUsuarioRecep;
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
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
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
}
