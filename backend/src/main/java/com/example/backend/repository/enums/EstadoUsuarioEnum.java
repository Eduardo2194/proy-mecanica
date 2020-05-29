package com.example.backend.repository.enums;

public enum EstadoUsuarioEnum {
	
	ELIMINADO(0),
	ACTIVO(1),
	EN_PRUEBA(2),
	BLOQUEADO_X_FIN_PRUEBA(3),
	BLOQUEADO_X_PENALIDAD(4),
	BLOQUEADO_X_FIN_SUSCRIPCION(5);
	
	private final Integer code;
	
	private EstadoUsuarioEnum(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static EstadoUsuarioEnum getByCode(Integer code) {
		EstadoUsuarioEnum value = null;
		for (EstadoUsuarioEnum item : EstadoUsuarioEnum.values()) {
			if (item.getCode().equals(code)) {
				value = item;
				break;
			}
		}
		return value;
	}
	
}
