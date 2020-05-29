package com.example.backend.engine.context;

import com.example.backend.rest.type.business.UsuarioRest;
import com.example.backend.rest.type.request.GestionarProblemaRequest;
import com.example.backend.rest.type.request.GestionarUsuarioRequest;
import com.example.backend.rest.type.request.LoginUsuarioRequest;
import com.example.backend.rest.type.response.ConsultaUsuarioPenalidadResponse;
import com.example.backend.rest.type.response.GestionarProblemaResponse;
import com.example.backend.rest.type.response.GestionarUsuarioResponse;
import com.example.backend.rest.type.response.ListarProblemasResponse;
import com.example.backend.rest.type.response.LoginUsuarioResponse;

public class UserContext extends Context{
	
	private UsuarioRest userRestEntity;
	
	private LoginUsuarioRequest loginRequest;
	private LoginUsuarioResponse loginResponse;
	
	private GestionarUsuarioRequest gestionarRequest;
	private GestionarUsuarioResponse gestionarResponse;
	
	private String dni;
	private ConsultaUsuarioPenalidadResponse consultaPenalidadResponse;
	
	private GestionarProblemaRequest registrarProblemaRequest;
	private GestionarProblemaResponse registrarProblemaResponse;
	
	private ListarProblemasResponse listarProblemasResponse;
	
	public UsuarioRest getUserRestEntity() {
		return userRestEntity;
	}
	public void setUserRestEntity(UsuarioRest userRestEntity) {
		this.userRestEntity = userRestEntity;
	}
	public LoginUsuarioRequest getLoginRequest() {
		return loginRequest;
	}
	public void setLoginRequest(LoginUsuarioRequest loginRequest) {
		this.loginRequest = loginRequest;
	}
	public LoginUsuarioResponse getLoginResponse() {
		return loginResponse;
	}
	public void setLoginResponse(LoginUsuarioResponse loginResponse) {
		this.loginResponse = loginResponse;
	}
	public GestionarUsuarioRequest getGestionarRequest() {
		return gestionarRequest;
	}
	public void setGestionarRequest(GestionarUsuarioRequest gestionarRequest) {
		this.gestionarRequest = gestionarRequest;
	}
	public GestionarUsuarioResponse getGestionarResponse() {
		return gestionarResponse;
	}
	public void setGestionarResponse(GestionarUsuarioResponse gestionarResponse) {
		this.gestionarResponse = gestionarResponse;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public ConsultaUsuarioPenalidadResponse getConsultaPenalidadResponse() {
		return consultaPenalidadResponse;
	}
	public void setConsultaPenalidadResponse(ConsultaUsuarioPenalidadResponse consultaPenalidadResponse) {
		this.consultaPenalidadResponse = consultaPenalidadResponse;
	}
	public GestionarProblemaRequest getRegistrarProblemaRequest() {
		return registrarProblemaRequest;
	}
	public void setRegistrarProblemaRequest(GestionarProblemaRequest registrarProblemaRequest) {
		this.registrarProblemaRequest = registrarProblemaRequest;
	}
	public GestionarProblemaResponse getRegistrarProblemaResponse() {
		return registrarProblemaResponse;
	}
	public void setRegistrarProblemaResponse(GestionarProblemaResponse registrarProblemaResponse) {
		this.registrarProblemaResponse = registrarProblemaResponse;
	}
	public ListarProblemasResponse getListarProblemasResponse() {
		return listarProblemasResponse;
	}
	public void setListarProblemasResponse(ListarProblemasResponse listarProblemasResponse) {
		this.listarProblemasResponse = listarProblemasResponse;
	}
	
	
}
