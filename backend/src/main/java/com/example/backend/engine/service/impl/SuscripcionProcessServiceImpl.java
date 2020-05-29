package com.example.backend.engine.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.dao.PlanesSuscripcionDAO;
import com.example.backend.dao.SuscripcionDAO;
import com.example.backend.dao.UsuarioDAO;
import com.example.backend.engine.context.ContextHolder;
import com.example.backend.engine.context.SuscripcionContext;
import com.example.backend.engine.service.SuscripcionProcessService;
import com.example.backend.exceptions.BackEndException;
import com.example.backend.repository.bean.PlanesSuscripcion;
import com.example.backend.repository.bean.Suscripcion;
import com.example.backend.repository.bean.Usuario;
import com.example.backend.repository.enums.EstadoGeneral;
import com.example.backend.repository.enums.EstadoUsuarioEnum;
import com.example.backend.repository.enums.TipoUsuarioEnum;
import com.example.backend.rest.type.base.ResponseStatusBase;
import com.example.backend.rest.type.business.PlanesSuscripcionRest;
import com.example.backend.rest.type.business.SuscripcionRest;
import com.example.backend.rest.type.request.RegistrarSuscripcionRequest;
import com.example.backend.rest.type.response.ConsultaPlanesSuscripcionResponse;
import com.example.backend.rest.type.response.RegistrarSuscripcionResponse;

@Service
public class SuscripcionProcessServiceImpl implements SuscripcionProcessService {

	private static final Logger LOGGER = LogManager.getLogger(SuscripcionProcessServiceImpl.class);

	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private SuscripcionDAO suscripcionDAO;
	
	@Autowired
	private PlanesSuscripcionDAO planesSuscripcionDAO;
	
	@Override
	public void consultaPlanes() {
		final String methodName = "consultaPlanes";
		LOGGER.traceEntry(methodName);
		
		SuscripcionContext context = ContextHolder.get(SuscripcionContext.class);
		ConsultaPlanesSuscripcionResponse response = context.getConsultaPlanesResponse();
		
		List<PlanesSuscripcion> planesSuscripcion = suscripcionDAO.consultaPlanesSuscripcion(0);
		
		if (planesSuscripcion == null || planesSuscripcion.isEmpty()) {
			LOGGER.info("No se encontraron planes de suscripcion");
			throw new BackEndException("No se encontraron planes de suscripcion");
		}
		
		List<PlanesSuscripcionRest> planes = new ArrayList<>();
		for(PlanesSuscripcion plan : planesSuscripcion) {
			PlanesSuscripcionRest planRest = new PlanesSuscripcionRest();
			planRest.setCodigo(plan.getCodigo());
			planRest.setDescripcion(plan.getDescripcion());
			planRest.setMonto(plan.getMonto());
			planes.add(planRest);
		}

		Collections.sort(planes, (s1, s2) -> { 
			return s1.getMonto().compareTo(s2.getMonto());
		});
		
		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Consulta planes de suscripcion OK");
		
		response.setPlanes(planes);
		LOGGER.traceExit();
	}

	@Override
	public void registrar() {
		final String methodName = "registrar";
		LOGGER.traceEntry(methodName);
		SuscripcionContext context = ContextHolder.get(SuscripcionContext.class);
		RegistrarSuscripcionRequest request = context.getRegistrarRequest();
		RegistrarSuscripcionResponse response = context.getRegistrarResponse();
		
		
		//Solo los usuario en pruebas, o que hayan terminado el trial o su suscripcion actual pueden registrar una suscripcion
		List<Integer> estadoInvalidos = Arrays.asList(new Integer[] {
				EstadoUsuarioEnum.ACTIVO.getCode(),
				EstadoUsuarioEnum.ELIMINADO.getCode(),
				EstadoUsuarioEnum.BLOQUEADO_X_PENALIDAD.getCode()
			});
		Usuario usuario = usuarioDAO.recuperarUsuarioPorDNI(request.getUsuario().getDni(), estadoInvalidos);
		if (usuario == null) {
			LOGGER.info("No existe el usuario para el dni enviado");
			throw new BackEndException("No existe el usuario para el dni enviado");
		}
		
		//Solo los usuario de tipo mecanico pueden pagar suscripcion
		if (!usuario.getCodigoTipo().equals(TipoUsuarioEnum.MECANICO.getCode())){
			LOGGER.info("El usuario no es mecanico, solo los mecanicos pueden pagar suscripcion");
			throw new BackEndException("El usuario no es mecánico, solo los mecánicos pueden pagar suscripción");
		}
		
		
		PlanesSuscripcion plan = planesSuscripcionDAO.recuperarPlanActivoPorCodigo(request.getPlan().getCodigo(), EstadoGeneral.ACTIVO.getCode());
		if(plan == null) {
			LOGGER.info("El codigo de plan de suscripcion es invalido");
			throw new BackEndException("El codigo de plan de suscripcion es invalido");
		}
		
		
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, plan.getPeriodo());
		
		Suscripcion suscripcion = new Suscripcion();
		suscripcion.setCodigoPlan(request.getPlan().getCodigo());
		suscripcion.setDniUsuario(request.getUsuario().getDni());
		suscripcion.setFechaInicio(new Date());
		suscripcion.setFechaFin(calendar.getTime());
		suscripcion.setEstado(EstadoGeneral.ACTIVO.getCode());
		
		suscripcion = suscripcionDAO.registrarSuscripcion(suscripcion);
		
		if(suscripcion == null) {
			LOGGER.info("No se pudo registrar la suscripcion");
			throw new BackEndException("No se pudo registrar la suscripcion");
		}
		
		//Activando al usuario
		usuario.setEstado(EstadoUsuarioEnum.ACTIVO.getCode());
		usuario = usuarioDAO.actualizarUsuario(usuario);
		if (usuario == null) {
			LOGGER.info("No se pudo actualizar el estado del usuario");
			throw new BackEndException("No se pudo actualizar el estado del usuario");
		} 
		
		
		
		response.setStatus(new ResponseStatusBase());
		response.getStatus().setSuccess(Boolean.TRUE);
		response.getStatus().setMessage("Consulta planes de suscripcion OK");
		
		response.setSuscripcion(new SuscripcionRest());
		response.getSuscripcion().setFechaInicio(suscripcion.getFechaInicio());
		response.getSuscripcion().setFechaFin(suscripcion.getFechaFin());
		
		LOGGER.traceExit();
	}

}
