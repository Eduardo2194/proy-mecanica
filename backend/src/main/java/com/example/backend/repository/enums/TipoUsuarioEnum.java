package com.example.backend.repository.enums;

public enum TipoUsuarioEnum {
	
	ADMIN("ADMIN"),
	CAJERO("CAJERO"),
	CONDUCTOR("CONDUCTOR"),
	MECANICO("MECANICO");
	
	private final String code;
	
	private TipoUsuarioEnum(String code) {
		this.code = code;
	}
	
	public static TipoUsuarioEnum getByCode(String code) {
		TipoUsuarioEnum value = null;
		for (TipoUsuarioEnum item : TipoUsuarioEnum.values()) {
			if (item.getCode().equals(code)) {
				value = item;
				break;
			}
		}
		return value;
	}

	public String getCode() {
		return code;
	}
	
	

}
