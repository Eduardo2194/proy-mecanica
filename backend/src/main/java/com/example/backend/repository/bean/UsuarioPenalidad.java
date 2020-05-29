package com.example.backend.repository.bean;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="UsuarioPenalidad")
@EntityListeners(AuditingEntityListener.class)
public class UsuarioPenalidad {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
    private String dniUsuario;
    private String codigoPenalidad;
    private Integer estado;

	public String getDniUsuario() {
		return dniUsuario;
	}
	public void setDniUsuario(String dniUsuario) {
		this.dniUsuario = dniUsuario;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigoPenalidad() {
		return codigoPenalidad;
	}
	public void setCodigoPenalidad(String codigoPenalidad) {
		this.codigoPenalidad = codigoPenalidad;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}

}