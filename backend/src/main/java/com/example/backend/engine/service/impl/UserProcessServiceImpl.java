package com.example.backend.engine.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.dao.PenalidadDAO;
import com.example.backend.dao.UsuarioDAO;
import com.example.backend.dao.UsuarioPenalidadDAO;
import com.example.backend.engine.context.ContextHolder;
import com.example.backend.engine.context.UserContext;
import com.example.backend.engine.service.UserProcessService;
import com.example.backend.engine.service.ValidationService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.repository.bean.Penalidad;
import com.example.backend.repository.bean.Usuario;
import com.example.backend.repository.bean.UsuarioPenalidad;
import com.example.backend.repository.enums.EstadoUsuarioEnum;
import com.example.backend.repository.enums.TipoUsuarioEnum;
import com.example.backend.rest.type.base.ResponseStatusBase;
import com.example.backend.rest.type.business.PenalidadRest;
import com.example.backend.rest.type.business.UsuarioRest;
import com.example.backend.rest.type.request.GestionarUsuarioRequest;
import com.example.backend.rest.type.request.LoginUsuarioRequest;
import com.example.backend.rest.type.response.ConsultaUsuarioPenalidadResponse;
import com.example.backend.rest.type.response.GestionarUsuarioResponse;
import com.example.backend.rest.type.response.LoginUsuarioResponse;

@Service
public class UserProcessServiceImpl implements UserProcessService {

	private static final Logger LOGGER = LogManager.getLogger(UserProcessServiceImpl.class);

	@Autowired
	private ValidationService validationService;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private UsuarioPenalidadDAO usuarioPenalidadDAO;
	
	@Autowired
	private PenalidadDAO penalidadDAO;
	
	@Override
	public void login() {
		final String methodName = "login";
		LOGGER.traceEntry(methodName);
		
		validationService.validateLoginParams();
		
		UserContext context = ContextHolder.get(UserContext.class);
		LoginUsuarioRequest request = context.getLoginRequest();
		LoginUsuarioResponse response= context.getLoginResponse();
		
		LOGGER.info("Intentando iniciar sesion con dni : "+request.getUsuario().getDni());
		List<Integer> invalidStates = Arrays.asList(EstadoUsuarioEnum.ELIMINADO.getCode());
		Usuario usuario = usuarioDAO.recuperarUsuarioPorDNI(request.getUsuario().getDni(), invalidStates);
		
		if (usuario == null) {
			LOGGER.info("No se encontro al usuario");
			throw new BackEndException("El usuario no se encuentra registrado");
		} 
		
		LOGGER.info("Se encontro al usuario");
		
		if (usuario.getContrasena() == null || !usuario.getContrasena().equals(request.getUsuario().getContrasena())) {
			LOGGER.info("La contraseña es inválida");
			throw new BackEndException("Contraseña inválida");
		} 
		
		EstadoUsuarioEnum estadoUsuario = EstadoUsuarioEnum.getByCode(usuario.getEstado());
		TipoUsuarioEnum tipoUsuario = TipoUsuarioEnum.getByCode(usuario.getCodigoTipo());
		if (estadoUsuario == null || tipoUsuario == null) {
			throw new BackEndException("La información del usuario es inválida o corrupta");
		} 
		
		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Logueo OK");
		
		response.setUsuario(new UsuarioRest());
		response.getUsuario().setIdentifier(usuario.getId());
		response.getUsuario().setNombres(usuario.getNombres());
		response.getUsuario().setApellidos(usuario.getApellidos());
		response.getUsuario().setCelular(usuario.getCelular());
		response.getUsuario().setDni(usuario.getDni());
		response.getUsuario().setCorreo(usuario.getCorreo());
		response.getUsuario().setEstado(estadoUsuario.name());
		response.getUsuario().setTipo(tipoUsuario.name());
		response.getUsuario().setFoto(usuario.getFoto());
		response.getUsuario().setFirebaseToken(usuario.getFirebaseToken());
		
		LOGGER.traceExit(methodName);
	}

	@Override
	public void registrar() {
		final String methodName = "registrar";
		LOGGER.traceEntry(methodName);
		
		validationService.validateRegistroParams();
		
		UserContext context = ContextHolder.get(UserContext.class);
		GestionarUsuarioRequest request= context.getGestionarRequest();
		GestionarUsuarioResponse response= context.getGestionarResponse();
		
		List<Integer> invalidStates = Arrays.asList(EstadoUsuarioEnum.ELIMINADO.getCode());
		Usuario usuarioExistente = usuarioDAO.recuperarUsuarioPorDNI(request.getUsuario().getDni(), invalidStates);
		
		if (usuarioExistente != null) {
			LOGGER.info("El usuario ya se encuentra registrado");
			throw new BackEndException("Ya existe un usuario registrado para el dni ingresado");
		} 
		
		
		Usuario usuarioARegistrar = new Usuario();
		usuarioARegistrar.setNombres(request.getUsuario().getNombres());
		usuarioARegistrar.setApellidos(request.getUsuario().getApellidos());
		usuarioARegistrar.setCelular(request.getUsuario().getCelular());
		usuarioARegistrar.setDni(request.getUsuario().getDni());
		usuarioARegistrar.setCorreo(request.getUsuario().getCorreo());
		usuarioARegistrar.setContrasena(request.getUsuario().getContrasena());
		usuarioARegistrar.setFechaRegistro(new Date());
		if (request.getUsuario().getFirebaseToken() != null && !request.getUsuario().getFirebaseToken().trim().isEmpty()){
			usuarioARegistrar.setFirebaseToken(request.getUsuario().getFirebaseToken());
		}
		TipoUsuarioEnum tipoUsuario = TipoUsuarioEnum.getByCode(request.getUsuario().getTipo());
		if (tipoUsuario == null || tipoUsuario == TipoUsuarioEnum.ADMIN) {
			throw new BackEndException("El tipo de usuario es inválido");
		}
		usuarioARegistrar.setCodigoTipo(tipoUsuario.getCode());
		
		switch (tipoUsuario) {
		case CAJERO:
			usuarioARegistrar.setEstado(EstadoUsuarioEnum.ACTIVO.getCode());
			break;
		case MECANICO:
			context.setUserRestEntity(request.getUsuario());
			validationService.validateMecanicoExclusiveParams();
			usuarioARegistrar.setFoto(request.getUsuario().getFoto());
		case CONDUCTOR:
			usuarioARegistrar.setEstado(EstadoUsuarioEnum.EN_PRUEBA.getCode());
			break;
		default:
			throw new BackEndException("El tipo de usuario es inválido");
		}
		
		Usuario usuario = usuarioDAO.registrarUsuario(usuarioARegistrar);
		
		if(usuario!=null) {
			
			response.setStatus(new ResponseStatusBase());
			response.getStatus().setSuccess(Boolean.TRUE);
			response.getStatus().setMessage("Registro OK");
			
			response.setUsuario(new UsuarioRest());
			response.getUsuario().setIdentifier(usuario.getId());
			response.getUsuario().setNombres(usuario.getNombres());
			response.getUsuario().setApellidos(usuario.getApellidos());
			response.getUsuario().setCelular(usuario.getCelular());
			response.getUsuario().setDni(usuario.getDni());
			response.getUsuario().setCorreo(usuario.getCorreo());
			response.getUsuario().setTipo(request.getUsuario().getTipo());
			response.getUsuario().setFirebaseToken(usuario.getFirebaseToken());
			if (tipoUsuario == TipoUsuarioEnum.MECANICO) {
				response.getUsuario().setFoto(usuario.getFoto());
			}

		} else {
			throw new BackEndException("No se pudo registrar al usuario");
		}
		LOGGER.traceExit(methodName);
	}

	@Override
	public void actualizar() {
		final String methodName = "actualizar";
		LOGGER.traceEntry(methodName);
		
		UserContext context = ContextHolder.get(UserContext.class);
		GestionarUsuarioRequest request= context.getGestionarRequest();
		GestionarUsuarioResponse response= context.getGestionarResponse();
		
		List<Integer> invalidStates = Arrays.asList(EstadoUsuarioEnum.ELIMINADO.getCode());
		Usuario usuarioExistente = usuarioDAO.recuperarUsuarioPorDNI(request.getUsuario().getDni(), invalidStates);
		
		if (usuarioExistente == null) {
			LOGGER.info("El usuario enviado a actualizar no existe");
			throw new BackEndException("El usuario enviado a actualizar no existe");
		} 
		
		UsuarioRest usuarioFromRequest = request.getUsuario();
		if (usuarioFromRequest.getNombres() != null && !usuarioFromRequest.getNombres().trim().isEmpty()) {
			usuarioExistente.setNombres(usuarioFromRequest.getNombres());
		}
		
		if (usuarioFromRequest.getApellidos() != null && !usuarioFromRequest.getApellidos().trim().isEmpty()) {
			usuarioExistente.setApellidos(usuarioFromRequest.getApellidos());
		} 
		
		if (usuarioFromRequest.getCelular() != null && !usuarioFromRequest.getCelular().trim().isEmpty()) {
			usuarioExistente.setCelular(usuarioFromRequest.getCelular());
		}
		
		if (usuarioFromRequest.getFoto() != null && !usuarioFromRequest.getFoto().trim().isEmpty()) {
			usuarioExistente.setFoto(usuarioFromRequest.getFoto());
		} 
		
		if (usuarioFromRequest.getCorreo() != null && !usuarioFromRequest.getCorreo().trim().isEmpty()) {
			usuarioExistente.setCorreo(usuarioFromRequest.getCorreo());
		} 
		
		if (usuarioFromRequest.getContrasena() != null && !usuarioFromRequest.getContrasena().trim().isEmpty()) {
			usuarioExistente.setContrasena(usuarioFromRequest.getContrasena());
		} 
		
		if (usuarioFromRequest.getFirebaseToken() != null && !usuarioFromRequest.getFirebaseToken().trim().isEmpty()) {
			usuarioExistente.setFirebaseToken(usuarioFromRequest.getFirebaseToken());
		}
		
		TipoUsuarioEnum tipoUsuario = TipoUsuarioEnum.getByCode(usuarioExistente.getCodigoTipo());
		if (tipoUsuario == null) {
			throw new BackEndException("El tipo de usuario es inválido, data corrupta");
		}
		
		Usuario usuarioActualizado = usuarioDAO.actualizarUsuario(usuarioExistente);
		
		if (usuarioActualizado == null) {
			throw new BackEndException("No se pudo actualizar los datos del usuario");
		}
		
		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Actualizacion OK");
		
		response.setUsuario(new UsuarioRest());
		response.getUsuario().setIdentifier(usuarioActualizado.getId());
		response.getUsuario().setNombres(usuarioActualizado.getNombres());
		response.getUsuario().setApellidos(usuarioActualizado.getApellidos());
		response.getUsuario().setCelular(usuarioActualizado.getCelular());
		response.getUsuario().setDni(usuarioActualizado.getDni());
		response.getUsuario().setCorreo(usuarioActualizado.getCorreo());
		response.getUsuario().setTipo(tipoUsuario.getCode());
		response.getUsuario().setFirebaseToken(usuarioActualizado.getFirebaseToken());
		response.getUsuario().setFoto(usuarioActualizado.getFoto());
		
		LOGGER.traceExit(methodName);
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void consultaPenalidad() {
		final String methodName = "consultaPenalidad";
		LOGGER.traceEntry(methodName);
		
		UserContext context = ContextHolder.get(UserContext.class);
		String dni = context.getDni();
		ConsultaUsuarioPenalidadResponse response= context.getConsultaPenalidadResponse();
		
		UsuarioPenalidad usuarioPenalidad = null;
		
		usuarioPenalidad = usuarioPenalidadDAO.recuperarPenalidadActivaPorDni(dni, 1);
		
		if (usuarioPenalidad == null) {
			LOGGER.info("No existe penalidad para el dni ingresado");
			throw new BackEndException("No existe penalidad para el dni ingresado");
		}
		
		Penalidad penalidad = penalidadDAO.recuperarPenalidadActivaPorCodigo(usuarioPenalidad.getCodigoPenalidad(), 1);
		
		if(penalidad == null) {
			LOGGER.info("No se pudo recuperar la penalidad, datos corruptos");
			throw new BackEndException("No se pudo recuperar la penalidad, datos corruptos");
		}
		
		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Consulta penalidad OK");
		
		response.setUsuario(new UsuarioRest());
		response.getUsuario().setDni(dni);
		
		response.setPenalidad(new PenalidadRest());
		response.getPenalidad().setCodigo(penalidad.getCodigo());
		response.getPenalidad().setDescripcion(penalidad.getDescripcion());
		response.getPenalidad().setMonto(penalidad.getMonto());
		
		LOGGER.traceExit(methodName);
	}

	@Override
	public void recuperarContrasena() {
		final String methodName = "recuperarContrasena";
		LOGGER.traceEntry(methodName);
		
		UserContext context = ContextHolder.get(UserContext.class);
		GestionarUsuarioRequest request = context.getGestionarRequest();
		GestionarUsuarioResponse response = context.getGestionarResponse();
		
		List<Integer> invalidStates = Arrays.asList(EstadoUsuarioEnum.ELIMINADO.getCode());
		Usuario usuarioExistente = usuarioDAO.recuperarUsuarioPorDNI(request.getUsuario().getDni(), invalidStates);
		
		if (usuarioExistente == null) {
			LOGGER.info("El usuario no existe");
			throw new BackEndException("El usuario no existe");
		}
		
		if (usuarioExistente.getCorreo() == null || usuarioExistente.getCorreo().trim().isEmpty()) {
			LOGGER.info("El correo registrado es invalido");
			throw new BackEndException("El correo registrado es invalido");
		}
		
		
		final String username = "servicio.meca.proy.upc@gmail.com";
        final String password = "1234.5678";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("servicio.meca.proy.upc@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(usuarioExistente.getCorreo())
            );
            message.setSubject("Recuperación de Contraseña");
            message.setText("Estimado "+usuarioExistente.getNombres()+","
                    + "\n\n Por favor ingrese a https://web-upc-tp1.azurewebsites.net/RecoveryPassword.aspx?token="+usuarioExistente.getDni()
            		+ "\n\n Atentamente, ServicioMecanicoApp");

            Transport.send(message);

            LOGGER.info("Se envio el mensaje");

        } catch (Exception e) {
        	LOGGER.error("No se pudo enviar el correo de recuperación", e);
            throw new BackEndException("No se pudo enviar el correo de recuperación");
        }
		
		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Se envio un mensaje a su correo electronico");
	}
	
	

	
}
