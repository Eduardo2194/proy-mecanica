package com.example.backend.engine.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.client.firebase.FirebasePushClient;
import com.example.backend.client.firebase.bean.FirebaseNotification;
import com.example.backend.client.firebase.bean.FirebasePushNotificationG;
import com.example.backend.dao.PenalidadDAO;
import com.example.backend.dao.ProblemaDAO;
import com.example.backend.dao.UsuarioDAO;
import com.example.backend.dao.UsuarioPenalidadDAO;
import com.example.backend.engine.context.ContextHolder;
import com.example.backend.engine.context.ProblemaContext;
import com.example.backend.engine.context.UserContext;
import com.example.backend.engine.service.ProblemProcessService;
import com.example.backend.engine.service.ValidationService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.repository.bean.Penalidad;
import com.example.backend.repository.bean.Problema;
import com.example.backend.repository.bean.Usuario;
import com.example.backend.repository.bean.UsuarioPenalidad;
import com.example.backend.repository.enums.AccionProblemaEnum;
import com.example.backend.repository.enums.EstadoGeneral;
import com.example.backend.repository.enums.EstadoUsuarioEnum;
import com.example.backend.repository.enums.TipoUsuarioEnum;
import com.example.backend.rest.type.base.ResponseStatusBase;
import com.example.backend.rest.type.business.ListProblemaRest;
import com.example.backend.rest.type.business.ProblemaRest;
import com.example.backend.rest.type.business.UsuarioRest;
import com.example.backend.rest.type.request.GestionarProblemaRequest;
import com.example.backend.rest.type.response.GestionarProblemaResponse;
import com.example.backend.rest.type.response.ListarProblemasResponse;

@Service
public class ProblemProcessServiceImpl implements ProblemProcessService {

	private static final Logger LOGGER = LogManager.getLogger(ProblemProcessServiceImpl.class);

	@Autowired
	ValidationService validationService;

	@Autowired
	ProblemaDAO problemaDAO;

	@Autowired
	UsuarioDAO usuarioDAO;

	@Autowired
	UsuarioPenalidadDAO usuarioPenalidadDAO;

	@Autowired
	PenalidadDAO penalidadDAO;

	@Override
	public void registrar() {
		// TODO Auto-generated method stub
		final String methodName = "registrar";
		LOGGER.traceEntry(methodName);

		// validationService.validateRegistroParams();

		ProblemaContext context = ContextHolder.get(ProblemaContext.class);
		GestionarProblemaRequest request = context.getRegistrarProblemaRequest();
		GestionarProblemaResponse response = context.getRegistrarProblemaResponse();
		int idConductor = 0;
		String estado = "";

		// Validar acción
		AccionProblemaEnum accion = AccionProblemaEnum.getByCode(request.getProblema().getAccion());

		if (accion == null) {
			LOGGER.info("La acción no es válida");
			throw new BackEndException("La acción no es válida");
		}

		Problema problema = null;

		if (request.getProblema().getAccion().equals("CREAR")) {

			// Recuperar conductor
			Usuario conductor = usuarioDAO.recuperarUsuarioPorDNI(request.getProblema().getConductor());

			if (conductor == null) {
				LOGGER.info("El conductor no existe");
				throw new BackEndException("El conductor no existe");
			}

			idConductor = conductor.getId();
			estado = "CREADO";
			problema = crearProblema(request, idConductor, estado);
		} else if (request.getProblema().getAccion().equals("ASIGNAR")) {
			estado = "EN PROCESO";
			problema = asignarProblema(request, estado);
		} else if (request.getProblema().getAccion().equals("ATENDER")) {
			estado = "ATENDIDO";
			problema = atenderProblema(request, estado);
		} else if (request.getProblema().getAccion().equals("RECHAZAR")) {
			estado = "CREADO";
			problema = rechazarProblema(request, estado);
		} else if (request.getProblema().getAccion().equals("CANCELAR")) {
			estado = "CANCELADO";
			problema = cancelarProblema(request, estado);
		}

		// Problema problema = gestionProblema(request, idConductor, estado);

		if (problema != null) {

			response.setStatus(new ResponseStatusBase());
			response.getStatus().setSuccess(Boolean.TRUE);
			response.getStatus().setMessage("Registro OK");

			response.setProblema(new ProblemaRest());
			response.getProblema().setCodigoProblema(problema.getIdProblema());
			response.getProblema().setConductor(request.getProblema().getConductor());
			response.getProblema().setMecanico(request.getProblema().getMecanico());
			response.getProblema().setDetalles(problema.getDetalles());
			response.getProblema().setFoto(problema.getFoto());
			response.getProblema().setAccion(request.getProblema().getAccion());
			response.getProblema().setLatitud(problema.getLatitud());
			response.getProblema().setLongitud(problema.getLongitud());

		} else {
			throw new BackEndException("No se pudo registrar el problema");
		}

		LOGGER.traceExit(methodName);
	}

	@Override
	public void listar() {
		// TODO Auto-generated method stub
		final String methodName = "listar";
		LOGGER.traceEntry(methodName);

		// validationService.validateRegistroParams();

		UserContext context = ContextHolder.get(UserContext.class);
		ListarProblemasResponse listarProblemasResponse = context.getListarProblemasResponse();

		List<Problema> listProblem = problemaDAO.listarProblemas();

		if (listProblem.size() == 0) {
			LOGGER.info("No hay problemas que necesiten atención");
			throw new BackEndException("No hay problemas que necesiten atención");
		}

		List<ListProblemaRest> listaProblema = new ArrayList<>();
		ListProblemaRest listProblemaRest = new ListProblemaRest();

		for (int i = 0; i < listProblem.size(); i++) {
			listProblemaRest = new ListProblemaRest();
			if (listProblem.get(i).getEstado().equals("CREADO")) {
				Usuario searchUsuario = usuarioDAO.recuperarUsuarioPorID(listProblem.get(i).getIdUsuarioEmi());

				listProblemaRest.setCodigoProblema(listProblem.get(i).getIdProblema());
				listProblemaRest.setDetalleProblema(listProblem.get(i).getDetalles());
				listProblemaRest.setNombreConductor(searchUsuario.getNombres() + " " + searchUsuario.getApellidos());
				listProblemaRest.setFoto(listProblem.get(i).getFoto());
				listProblemaRest.setFechaRegistro(listProblem.get(i).getFechaRegistro());

				listaProblema.add(listProblemaRest);
			}

		}

		Collections.sort(listaProblema, (s1, s2) -> {
			return Integer.valueOf(s2.getCodigoProblema()).compareTo(Integer.valueOf(s1.getCodigoProblema()));
		});

		listarProblemasResponse.setListaProblema(listaProblema);
		LOGGER.traceExit(methodName);
	}

	// private Problema gestionProblema(GestionarProblemaRequest request, int
	// idConductor, String estado) {
	//
	// Problema problemaRegistro = new Problema();
	//
	// if (!request.getProblema().getAccion().equals("CREAR")) {
	// Problema searchProblema =
	// problemaDAO.buscarProblema(request.getProblema().getCodigoProblema());
	//
	// if (searchProblema == null) {
	// LOGGER.info("El problema no se encuentra registrado");
	// throw new BackEndException("El problema no se encuentra registrado");
	// }
	//
	// Usuario mecanico =
	// usuarioDAO.recuperarUsuarioPorDNI(request.getProblema().getMecanico());
	//
	// if (mecanico == null) {
	// LOGGER.info("El mecanico no existe");
	// throw new BackEndException("El mecanico no existe");
	// }
	//
	// problemaRegistro.setIdProblema(searchProblema.getIdProblema());
	// problemaRegistro.setIdUsuarioEmi(searchProblema.getIdUsuarioEmi());
	// problemaRegistro.setIdUsuarioRecep(mecanico.getId());
	// problemaRegistro.setDetalles(searchProblema.getDetalles());
	// problemaRegistro.setFoto(searchProblema.getFoto());
	// problemaRegistro.setFechaRegistro(searchProblema.getFechaRegistro());
	// problemaRegistro.setFechaFin(new Date());
	// problemaRegistro.setLatitud(searchProblema.getLatitud());
	// problemaRegistro.setLongitud(searchProblema.getLongitud());
	// problemaRegistro.setEstado(estado);
	//
	// } else {
	// problemaRegistro.setIdUsuarioEmi(idConductor);
	// problemaRegistro.setDetalles(request.getProblema().getDetalles());
	// problemaRegistro.setFoto(request.getProblema().getFoto());
	// problemaRegistro.setEstado(estado);
	// problemaRegistro.setFechaRegistro(new Date());
	// problemaRegistro.setLatitud(request.getProblema().getLatitud());
	// problemaRegistro.setLongitud(request.getProblema().getLongitud());
	//
	// }
	//
	// problemaRegistro = problemaDAO.registrarProblema(problemaRegistro);
	//
	// final Problema problemaParaPush = problemaRegistro;
	// Thread newThread = new Thread(() -> {
	// List<String> tiposUsuarioParaPush = Arrays.asList(new String[] {
	// TipoUsuarioEnum.MECANICO.getCode() });
	// List<Integer> estadosValidosParaPush = Arrays.asList(
	// new Integer[] { EstadoUsuarioEnum.ACTIVO.getCode(),
	// EstadoUsuarioEnum.EN_PRUEBA.getCode() });
	// List<Usuario> mecanicos = usuarioDAO.recuperarUsuarios(tiposUsuarioParaPush,
	// estadosValidosParaPush);
	//
	// if (mecanicos != null && !mecanicos.isEmpty()) {
	//
	// List<String> registrationIdsParaPush = new ArrayList<>();
	// for (Usuario mecanico : mecanicos) {
	// if (registrationIdsParaPush.size() == 1000) {
	// break;
	// } else {
	// if (mecanico.getFirebaseToken() != null &&
	// !mecanico.getFirebaseToken().trim().isEmpty()) {
	// registrationIdsParaPush.add(mecanico.getFirebaseToken());
	// }
	// }
	// }
	//
	// if (!registrationIdsParaPush.isEmpty()) {
	// LOGGER.info("Preparando notificacion");
	// FirebaseNotification firebaseNotification = new FirebaseNotification();
	// firebaseNotification.setTitle("Hola, alguien ha notificado un problema");
	// firebaseNotification
	// .setBody("Alguien ha registrado un problema, entra aqui para ver mas
	// detalles.");
	// FirebasePushNotificationG<Problema> firebasePushNotification = new
	// FirebasePushNotificationG<Problema>();
	//
	// //
	// firebasePushNotification.setTo("dPqkDmLaZVY:APA91bGBHbm6xrB0UFdCQdX6aoferELdkDCmqQUw-U_VwEYTppM7oZGD6ezQ0BLk8zmVs04YYvMC6t7nYxYVcVL0xc7gTSyY6x5V6tZD1xpVOeAkor0jOa6erSEXMkAVstfYIsVeWbVH
	// // ");
	// firebasePushNotification.setRegistration_ids(registrationIdsParaPush);
	// firebasePushNotification.setNotification(firebaseNotification);
	// firebasePushNotification.setData(problemaParaPush);
	//
	// FirebasePushClient firebasePushClient = new FirebasePushClient();
	// try {
	// LOGGER.info("Enviando notificacion");
	// if (!firebasePushClient.sendPushNotification(firebasePushNotification)) {
	// LOGGER.warn("No se pudo enviar el push");
	// }
	// } catch (Exception e) {
	// LOGGER.error("Hubo un error al enviar el push", e);
	// }
	// }
	// }
	//
	// });
	// newThread.start();
	//
	// return problemaRegistro;
	// }

	@Override
	public void consultar() {
		final String methodName = "consultar";
		LOGGER.traceEntry(methodName);

		ProblemaContext context = ContextHolder.get(ProblemaContext.class);
		String problemaId = context.getProblemaId();
		GestionarProblemaResponse response = context.getRegistrarProblemaResponse();

		Problema problema = problemaDAO.buscarProblema(Integer.parseInt(problemaId));

		if (problema == null) {
			LOGGER.info("El problema consultado no existe");
			throw new BackEndException("El problema consultado no existe");
		}

		Usuario usuarioEmisorDeProblema = usuarioDAO.recuperarUsuarioPorID(problema.getIdUsuarioEmi());

		if (usuarioEmisorDeProblema == null || problema.getIdUsuarioEmi() == null) {
			LOGGER.info("La data del problema esta corrupta");
			throw new BackEndException("La data del problema esta corrupta");
		}

		Usuario usuarioReceptorDeProblema = null;
		if (problema.getIdUsuarioRecep() != null) {
			usuarioReceptorDeProblema = usuarioDAO.recuperarUsuarioPorID(problema.getIdUsuarioRecep());
		}

		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Consulta OK");

		response.setProblema(new ProblemaRest());
		response.getProblema().setCodigoProblema(problema.getIdProblema());
		response.getProblema().setConductor(usuarioEmisorDeProblema.getDni());
		response.getProblema().setDetalles(problema.getDetalles());
		response.getProblema().setFoto(problema.getFoto());
		response.getProblema().setAccion(problema.getEstado());
		response.getProblema().setLatitud(problema.getLatitud());
		response.getProblema().setLongitud(problema.getLongitud());

		response.getProblema().setDatosConductor(new UsuarioRest());
		response.getProblema().getDatosConductor().setDni(usuarioEmisorDeProblema.getDni());
		response.getProblema().getDatosConductor().setCelular(usuarioEmisorDeProblema.getCelular());
		response.getProblema().getDatosConductor().setApellidos(usuarioEmisorDeProblema.getApellidos());
		response.getProblema().getDatosConductor().setNombres(usuarioEmisorDeProblema.getNombres());
		response.getProblema().getDatosConductor().setCorreo(usuarioEmisorDeProblema.getCorreo());

		if (usuarioReceptorDeProblema != null) {
			response.getProblema().setDatosMecanico(new UsuarioRest());
			response.getProblema().getDatosMecanico().setDni(usuarioReceptorDeProblema.getDni());
			response.getProblema().getDatosMecanico().setCelular(usuarioReceptorDeProblema.getCelular());
			response.getProblema().getDatosMecanico().setApellidos(usuarioReceptorDeProblema.getApellidos());
			response.getProblema().getDatosMecanico().setNombres(usuarioReceptorDeProblema.getNombres());
			response.getProblema().getDatosMecanico().setCorreo(usuarioReceptorDeProblema.getCorreo());
			response.getProblema().getDatosMecanico().setFoto(usuarioReceptorDeProblema.getFoto());
			response.getProblema().setMecanico(usuarioReceptorDeProblema.getDni());
		}

	}

	@Override
	public void consultarXConductor() {
		// TODO Auto-generated method stub
		final String methodName = "consultarXConductor";
		LOGGER.traceEntry(methodName);

		ProblemaContext context = ContextHolder.get(ProblemaContext.class);
		String dniConductor = context.getDniConductor();
		GestionarProblemaResponse response = context.getRegistrarProblemaResponse();

		Usuario conductor = usuarioDAO.recuperarUsuarioPorDNI(dniConductor);

		if (conductor == null) {
			LOGGER.info("El conductor ingresado no existe");
			throw new BackEndException("El conductor ingresado no existe");
		}

		Problema problema = problemaDAO.buscarUltimoProblemaDeConductor(conductor.getId());

		if (problema == null) {
			LOGGER.info("El conductor no cuenta con problemas registrados");
			throw new BackEndException("El conductor no cuenta con problemas registrados");
		}

		if (!problema.getEstado().equals("CREADO") && !problema.getEstado().equals("EN PROCESO")) {
			LOGGER.info("El conductor no cuenta con problemas pendientes");
			throw new BackEndException("El conductor no cuenta con problemas pendientes");
		}

		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Consulta OK");

		response.setProblema(new ProblemaRest());
		response.getProblema().setCodigoProblema(problema.getIdProblema());
		response.getProblema().setConductor(dniConductor);
		response.getProblema().setDetalles(problema.getDetalles());
		response.getProblema().setFoto(problema.getFoto());
		response.getProblema().setAccion(problema.getEstado());
		response.getProblema().setLatitud(problema.getLatitud());
		response.getProblema().setLongitud(problema.getLongitud());

		response.getProblema().setDatosConductor(new UsuarioRest());
		response.getProblema().getDatosConductor().setDni(conductor.getDni());
		response.getProblema().getDatosConductor().setCelular(conductor.getCelular());
		response.getProblema().getDatosConductor().setApellidos(conductor.getApellidos());
		response.getProblema().getDatosConductor().setNombres(conductor.getNombres());
		response.getProblema().getDatosConductor().setCorreo(conductor.getCorreo());

		if (problema.getIdUsuarioRecep() != null) {

			Usuario mecanico = usuarioDAO.recuperarUsuarioPorID(problema.getIdUsuarioRecep());

			response.getProblema().setDatosMecanico(new UsuarioRest());
			response.getProblema().getDatosMecanico().setDni(mecanico.getDni());
			response.getProblema().getDatosMecanico().setCelular(mecanico.getCelular());
			response.getProblema().getDatosMecanico().setApellidos(mecanico.getApellidos());
			response.getProblema().getDatosMecanico().setNombres(mecanico.getNombres());
			response.getProblema().getDatosMecanico().setCorreo(mecanico.getCorreo());
			response.getProblema().getDatosMecanico().setFoto(mecanico.getFoto());

			response.getProblema().setMecanico(mecanico.getDni());

		}
		// validaProblemaNoCerrado = false;
		// break;

		/*
		 * if (validaProblemaNoCerrado) {
		 * 
		 * LOGGER.info("El conductor no cuenta con problemas pendientes"); throw new
		 * BackEndException("El conductor no cuenta con problemas pendientes"); }
		 */
		LOGGER.traceExit(methodName);
	}

	@Override
	public void consultarXMecanico() {
		// TODO Auto-generated method stub
		final String methodName = "consultarXMecanico";
		LOGGER.traceEntry(methodName);

		ProblemaContext context = ContextHolder.get(ProblemaContext.class);
		String dniMecanico = context.getDniMecanico();
		GestionarProblemaResponse response = context.getRegistrarProblemaResponse();

		Usuario mecanico = usuarioDAO.recuperarUsuarioPorDNI(dniMecanico);

		if (mecanico == null) {
			LOGGER.info("El mecanico ingresado no existe");
			throw new BackEndException("El mecanico ingresado no existe");
		}

		Problema problema = problemaDAO.buscarUltimoProblemaDeMecanico(mecanico.getId());

		if (problema == null) {
			LOGGER.info("El mecanico no cuenta con problemas registrados");
			throw new BackEndException("El mecanico no cuenta con problemas registrados");
		}

		// boolean validaProblemaNoCerrado = true;

		if (!problema.getEstado().equals("CREADO") && !problema.getEstado().equals("EN PROCESO")) {
			LOGGER.info("El mecanico no cuenta con problemas pendientes");
			throw new BackEndException("El mecanico no cuenta con problemas pendientes");
		}

		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Consulta OK");

		response.setProblema(new ProblemaRest());
		response.getProblema().setCodigoProblema(problema.getIdProblema());

		response.getProblema().setDetalles(problema.getDetalles());
		response.getProblema().setFoto(problema.getFoto());
		response.getProblema().setAccion(problema.getEstado());
		response.getProblema().setLatitud(problema.getLatitud());
		response.getProblema().setLongitud(problema.getLongitud());

		response.getProblema().setDatosMecanico(new UsuarioRest());
		response.getProblema().getDatosMecanico().setDni(mecanico.getDni());
		response.getProblema().getDatosMecanico().setCelular(mecanico.getCelular());
		response.getProblema().getDatosMecanico().setApellidos(mecanico.getApellidos());
		response.getProblema().getDatosMecanico().setNombres(mecanico.getNombres());
		response.getProblema().getDatosMecanico().setCorreo(mecanico.getCorreo());
		response.getProblema().getDatosMecanico().setFoto(mecanico.getFoto());

		response.getProblema().setMecanico(mecanico.getDni());

		if (problema.getIdUsuarioRecep() != null) {

			Usuario conductor = usuarioDAO.recuperarUsuarioPorID(problema.getIdUsuarioEmi());

			response.getProblema().setDatosConductor(new UsuarioRest());
			response.getProblema().getDatosConductor().setDni(conductor.getDni());
			response.getProblema().getDatosConductor().setCelular(conductor.getCelular());
			response.getProblema().getDatosConductor().setApellidos(conductor.getApellidos());
			response.getProblema().getDatosConductor().setNombres(conductor.getNombres());
			response.getProblema().getDatosConductor().setCorreo(conductor.getCorreo());

			response.getProblema().setConductor(conductor.getDni());

		}

		/*
		 * validaProblemaNoCerrado = false; break; }
		 * 
		 * 
		 * 
		 * if (validaProblemaNoCerrado) {
		 * 
		 * LOGGER.info("El mecanico no cuenta con problemas pendientes"); throw new
		 * BackEndException("El mecanico no cuenta con problemas pendientes"); }
		 */

		LOGGER.traceExit(methodName);
	}

	public Problema crearProblema(GestionarProblemaRequest request, int idConductor, String estado) {

		// Validar la penalidad del conductor
		UsuarioPenalidad usuarioPenalidad = usuarioPenalidadDAO
				.recuperarPenalidadActivaPorDni(request.getProblema().getConductor(), EstadoGeneral.ACTIVO.getCode());

		if (usuarioPenalidad != null) {

			Penalidad penalidad = penalidadDAO.recuperarPenalidadActivaPorCodigo(usuarioPenalidad.getCodigoPenalidad(),
					EstadoGeneral.ACTIVO.getCode());

			LOGGER.info("Usted cuenta con una penalidad de: " + penalidad.getMonto()
					+ "debe cancelar para poder crear un problema");
			throw new BackEndException("Usted cuenta con una penalidad de: " + penalidad.getMonto()
					+ "debe cancelar para poder crear un problema");
		}

		Problema problemaRegistro = new Problema();

		problemaRegistro.setIdUsuarioEmi(idConductor);
		problemaRegistro.setDetalles(request.getProblema().getDetalles());
		problemaRegistro.setFoto(request.getProblema().getFoto());
		problemaRegistro.setEstado(estado);
		problemaRegistro.setFechaRegistro(new Date());
		problemaRegistro.setLatitud(request.getProblema().getLatitud());
		problemaRegistro.setLongitud(request.getProblema().getLongitud());

		problemaRegistro = problemaDAO.registrarProblema(problemaRegistro);

		final Problema problemaParaPush = problemaRegistro;
		Thread newThread = new Thread(() -> {
			List<String> tiposUsuarioParaPush = Arrays.asList(new String[] { TipoUsuarioEnum.MECANICO.getCode() });
			List<Integer> estadosValidosParaPush = Arrays.asList(
					new Integer[] { EstadoUsuarioEnum.ACTIVO.getCode(), EstadoUsuarioEnum.EN_PRUEBA.getCode() });
			List<Usuario> mecanicos = usuarioDAO.recuperarUsuarios(tiposUsuarioParaPush, estadosValidosParaPush);

			if (mecanicos != null && !mecanicos.isEmpty()) {

				List<String> registrationIdsParaPush = new ArrayList<>();
				for (Usuario mecanico : mecanicos) {
					if (registrationIdsParaPush.size() == 1000) {
						break;
					} else {
						if (mecanico.getFirebaseToken() != null && !mecanico.getFirebaseToken().trim().isEmpty()) {
							registrationIdsParaPush.add(mecanico.getFirebaseToken());
						}
					}
				}

				if (!registrationIdsParaPush.isEmpty()) {
					LOGGER.info("Preparando notificacion");
					FirebaseNotification firebaseNotification = new FirebaseNotification();
					firebaseNotification.setTitle("Hola, alguien ha notificado un problema");
					firebaseNotification
							.setBody("Alguien ha registrado un problema, entra aqui para ver mas detalles.");
					FirebasePushNotificationG<Problema> firebasePushNotification = new FirebasePushNotificationG<Problema>();

					// firebasePushNotification.setTo("dPqkDmLaZVY:APA91bGBHbm6xrB0UFdCQdX6aoferELdkDCmqQUw-U_VwEYTppM7oZGD6ezQ0BLk8zmVs04YYvMC6t7nYxYVcVL0xc7gTSyY6x5V6tZD1xpVOeAkor0jOa6erSEXMkAVstfYIsVeWbVH
					// ");
					firebasePushNotification.setRegistration_ids(registrationIdsParaPush);
					firebasePushNotification.setNotification(firebaseNotification);
					firebasePushNotification.setData(problemaParaPush);

					FirebasePushClient firebasePushClient = new FirebasePushClient();
					try {
						LOGGER.info("Enviando notificacion");
						if (!firebasePushClient.sendPushNotification(firebasePushNotification)) {
							LOGGER.warn("No se pudo enviar el push");
						}
					} catch (Exception e) {
						LOGGER.error("Hubo un error al enviar el push", e);
					}
				}
			}

		});
		newThread.start();

		return problemaRegistro;
	}

	public Problema asignarProblema(GestionarProblemaRequest request, String estado) {

		// Buscar el problema para asignar
		Problema searchProblema = problemaDAO.buscarProblema(request.getProblema().getCodigoProblema());

		if (searchProblema == null) {
			LOGGER.info("El problema no se encuentra registrado");
			throw new BackEndException("El problema no se encuentra registrado");
		}

		if (searchProblema.getEstado().equals("EN PROCESO") || searchProblema.getEstado().equals("ATENDIDO")
				|| searchProblema.getEstado().equals("CANCELADO") || searchProblema.getEstado().equals("RECHAZADO")) {
			LOGGER.info("El problema no se puede ASIGNAR, debido a que se encuentra " + searchProblema.getEstado());
			throw new BackEndException(
					"El problema no se puede ASIGNAR, debido a que se encuentra " + searchProblema.getEstado());
		}

		// Buscar el mecanico para asignar
		Usuario mecanico = usuarioDAO.recuperarUsuarioPorDNI(request.getProblema().getMecanico());

		if (mecanico == null) {
			LOGGER.info("El mecanico no existe");
			throw new BackEndException("El mecanico no existe");
		}

		// Validar la penalidad del mecanico
		UsuarioPenalidad usuarioPenalidad = usuarioPenalidadDAO.recuperarPenalidadActivaPorDni(mecanico.getDni(),
				EstadoGeneral.ACTIVO.getCode());

		if (usuarioPenalidad != null) {

			Penalidad penalidad = penalidadDAO.recuperarPenalidadActivaPorCodigo(usuarioPenalidad.getCodigoPenalidad(),
					EstadoGeneral.ACTIVO.getCode());

			LOGGER.info("Usted cuenta con una penalidad de: " + penalidad.getMonto()
					+ "debe cancelar para poder aceptar un problema");
			throw new BackEndException("Usted cuenta con una penalidad de: " + penalidad.getMonto()
					+ "debe cancelar para poder aceptar un problema");
		}

		// Buscar problemas atendidos del mecánico

		List<Problema> searchProblemaXMecanico = problemaDAO.buscarProblemaPorMecanico2(mecanico.getId(), "ATENDIDO");

		if (searchProblemaXMecanico != null) {
			if (searchProblemaXMecanico.size() > 5) {
				LOGGER.info(
						"No puede aceptar problema debido a que pasó el límite de atenciones y debe pagar una suscripción");
				throw new BackEndException(
						"No puede aceptar problema debido a que pasó el límite de atenciones y debe pagar una suscripción");
			}
		}

		Problema problemaRegistro = new Problema();

		problemaRegistro.setIdProblema(searchProblema.getIdProblema());
		problemaRegistro.setIdUsuarioEmi(searchProblema.getIdUsuarioEmi());
		problemaRegistro.setIdUsuarioRecep(mecanico.getId());
		problemaRegistro.setDetalles(searchProblema.getDetalles());
		problemaRegistro.setFoto(searchProblema.getFoto());
		problemaRegistro.setFechaRegistro(searchProblema.getFechaRegistro());
		problemaRegistro.setLatitud(searchProblema.getLatitud());
		problemaRegistro.setLongitud(searchProblema.getLongitud());
		problemaRegistro.setEstado(estado);

		problemaRegistro = problemaDAO.registrarProblema(problemaRegistro);

		return problemaRegistro;
	}

	public Problema atenderProblema(GestionarProblemaRequest request, String estado) {

		// Buscar el problema para atender
		Problema searchProblema = problemaDAO.buscarProblema(request.getProblema().getCodigoProblema());

		if (searchProblema == null) {
			LOGGER.info("El problema no se encuentra registrado");
			throw new BackEndException("El problema no se encuentra registrado");
		}

		if (searchProblema.getEstado().equals("ATENDIDO") || searchProblema.getEstado().equals("CANCELADO")) {
			LOGGER.info("El problema no se puede ATENDER, debido a que se encuentra " + searchProblema.getEstado());
			throw new BackEndException(
					"El problema no se puede ATENDER, debido a que se encuentra " + searchProblema.getEstado());
		}

		Problema problemaRegistro = new Problema();

		problemaRegistro.setIdProblema(searchProblema.getIdProblema());
		problemaRegistro.setIdUsuarioEmi(searchProblema.getIdUsuarioEmi());
		problemaRegistro.setIdUsuarioRecep(searchProblema.getIdUsuarioRecep());
		problemaRegistro.setDetalles(searchProblema.getDetalles());
		problemaRegistro.setFoto(searchProblema.getFoto());
		problemaRegistro.setFechaRegistro(searchProblema.getFechaRegistro());
		problemaRegistro.setFechaFin(new Date());
		problemaRegistro.setLatitud(searchProblema.getLatitud());
		problemaRegistro.setLongitud(searchProblema.getLongitud());
		problemaRegistro.setEstado(estado);

		problemaRegistro = problemaDAO.registrarProblema(problemaRegistro);

		return problemaRegistro;

	}

	public Problema rechazarProblema(GestionarProblemaRequest request, String estado) {

		// Buscar el problema para rechazar
		Problema searchProblema = problemaDAO.buscarProblema(request.getProblema().getCodigoProblema());

		if (searchProblema == null) {
			LOGGER.info("El problema no se encuentra registrado");
			throw new BackEndException("El problema no se encuentra registrado");
		}

		if (searchProblema.getEstado().equals("RECHAZADO") || searchProblema.getEstado().equals("ATENDIDO")) {
			LOGGER.info("El problema no se puede CANCELAR, debido a que se encuentra " + searchProblema.getEstado());
			throw new BackEndException(
					"El problema no se puede CANCELAR, debido a que se encuentra " + searchProblema.getEstado());

		}

		Problema problemaRegistro = new Problema();

		problemaRegistro.setIdProblema(searchProblema.getIdProblema());
		problemaRegistro.setIdUsuarioEmi(searchProblema.getIdUsuarioEmi());
		problemaRegistro.setIdUsuarioRecep(searchProblema.getIdUsuarioRecep());
		problemaRegistro.setDetalles(searchProblema.getDetalles());
		problemaRegistro.setFoto(searchProblema.getFoto());
		problemaRegistro.setFechaRegistro(searchProblema.getFechaRegistro());
		problemaRegistro.setFechaFin(new Date());
		problemaRegistro.setLatitud(searchProblema.getLatitud());
		problemaRegistro.setLongitud(searchProblema.getLongitud());
		problemaRegistro.setEstado(estado);

		problemaRegistro = problemaDAO.registrarProblema(problemaRegistro);

		return problemaRegistro;

	}

	public Problema cancelarProblema(GestionarProblemaRequest request, String estado) {

		// Buscar el problema para cancelar
		Problema searchProblema = problemaDAO.buscarProblema(request.getProblema().getCodigoProblema());

		if (searchProblema == null) {
			LOGGER.info("El problema no se encuentra registrado");
			throw new BackEndException("El problema no se encuentra registrado");
		}

		if (searchProblema.getEstado().equals("RECHAZADO") || searchProblema.getEstado().equals("ATENDIDO")
				/*|| searchProblema.getEstado().equals("EN PROCESO")*/) {

			LOGGER.info("El problema no se puede CANCELAR, debido a que se encuentra " + searchProblema.getEstado());
			throw new BackEndException(
					"El problema no se puede CANCELAR, debido a que se encuentra " + searchProblema.getEstado());

		}

		Problema problemaRegistro = new Problema();

		problemaRegistro.setIdProblema(searchProblema.getIdProblema());
		problemaRegistro.setIdUsuarioEmi(searchProblema.getIdUsuarioEmi());
		problemaRegistro.setIdUsuarioRecep(searchProblema.getIdUsuarioRecep());
		problemaRegistro.setDetalles(searchProblema.getDetalles());
		problemaRegistro.setFoto(searchProblema.getFoto());
		problemaRegistro.setFechaRegistro(searchProblema.getFechaRegistro());
		problemaRegistro.setFechaFin(new Date());
		problemaRegistro.setLatitud(searchProblema.getLatitud());
		problemaRegistro.setLongitud(searchProblema.getLongitud());
		problemaRegistro.setEstado(estado);

		problemaRegistro = problemaDAO.registrarProblema(problemaRegistro);

		return problemaRegistro;
	}

}
