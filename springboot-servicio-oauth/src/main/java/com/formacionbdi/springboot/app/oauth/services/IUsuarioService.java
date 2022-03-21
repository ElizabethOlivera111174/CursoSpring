package com.formacionbdi.springboot.app.oauth.services;



import com.springboot.app.usuarios.commons.models.Entity.Usuario;

public interface IUsuarioService {
	
	public Usuario findByUsername(String username);
	
	public Usuario update(Usuario usuario, Long id);

}
