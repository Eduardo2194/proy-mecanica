package com.example.backend.repository.enums;

public enum EstadoGeneral {
	
	ELIMINADO(0),
	ACTIVO(1);
	
	private final Integer code;
	
	private EstadoGeneral(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}
}
