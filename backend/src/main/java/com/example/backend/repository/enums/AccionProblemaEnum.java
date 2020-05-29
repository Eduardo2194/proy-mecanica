package com.example.backend.repository.enums;

public enum AccionProblemaEnum {

	CREAR("CREAR"),
	ASIGNAR("ASIGNAR"),
	ATENDER("ATENDER"),
	RECHAZAR("RECHAZAR"),
	CANCELAR("CANCELAR");
	
	private final String code;
	
	private AccionProblemaEnum(String code) {
		this.code = code;
	}
	
	public static AccionProblemaEnum getByCode(String code) {
		AccionProblemaEnum value = null;
		for (AccionProblemaEnum item : AccionProblemaEnum.values()) {
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
