package com.example.backend.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.repository.UsuarioRepository;
import com.example.backend.repository.bean.Usuario;

@Service
public class UsuarioDAO {

	@Autowired
	UsuarioRepository  usuarioRepository;
	
	/*crear un usuario*/
	public Usuario registrarUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	/*buscar un usuario por el dni*/
	public Usuario recuperarUsuarioPorDNI(String dni, List<Integer> estados) {
		return usuarioRepository.findFirstByDniAndEstadoNotIn(dni, estados);
	}
	
	
	/*buscar un usuario por el dni*/
	public Usuario recuperarUsuarioPorDNIyConPenalidad(String dni, Integer estado) {
		return usuarioRepository.findFirstByDniAndEstado(dni, estado);
	}
	
	/*buscar un usuario por el dni*/
	public Usuario recuperarUsuarioPorDNI(String dni) {
		return usuarioRepository.findFirstByDni(dni);
	}
	
	/*actualizar un usuario*/
	public Usuario actualizarUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	
	/*buscar usuario */
	public Usuario recuperarUsuarioPorID(int id) {
		return usuarioRepository.getOne(id);
	}
	
	public List<Usuario> recuperarUsuarios(List<String> tipos, List<Integer> estados){
		return usuarioRepository.findByCodigoTipoInAndEstadoIn(tipos, estados);
	}
}
