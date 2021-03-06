package com.springbootappusuarios.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.springboot.app.usuarios.commons.models.Entity.Usuario;

@RepositoryRestResource(path="usuarios" )
public interface UsuarioDao extends PagingAndSortingRepository<Usuario, Long>{
	@RestResource(path="buscar-username")
	public Usuario findByUsername(@Param("username")String username);

}
