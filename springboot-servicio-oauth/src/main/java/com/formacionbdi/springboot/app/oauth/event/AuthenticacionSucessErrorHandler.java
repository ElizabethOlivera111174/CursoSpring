package com.formacionbdi.springboot.app.oauth.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.formacionbdi.springboot.app.oauth.services.IUsuarioService;
import com.springboot.app.usuarios.commons.models.Entity.Usuario;

import brave.Tracer;
import feign.FeignException;

@Component
public class AuthenticacionSucessErrorHandler implements AuthenticationEventPublisher {
	
//Configuracion de succes y Login, mensajes de usuarios y logs
	
	private Logger log = LoggerFactory.getLogger(AuthenticacionSucessErrorHandler.class);

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private Tracer tracer;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		
		if(authentication.getDetails() instanceof WebAuthenticationDetails) {
			return;
		}
		UserDetails user = (UserDetails) authentication.getPrincipal();

		String mensaje = "Sucess Login: " + user.getUsername();
		System.out.println(mensaje);
		log.info(mensaje);
		
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		if (usuario.getIntentos() != null && usuario.getIntentos()>0) {
			usuario.setIntentos(0);
			usuarioService.update(usuario, usuario.getId());
		}
		//System.out.println(usuario);
		
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		
		String mensaje = "Error Login: " + exception.getMessage();
		log.error(mensaje);
		System.out.println(mensaje);
		
		try {
			StringBuilder errors = new StringBuilder();
			errors.append(mensaje);
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			
			if (usuario.getIntentos() == null) {
				usuario.setIntentos(0);
			}
			log.info("Intentos actual es de: " + usuario.getIntentos());
			usuario.setIntentos(usuario.getIntentos()+1);
			
			log.info("Intentos despues de: " + usuario.getIntentos());		
			if (usuario.getIntentos() >=3) {
				log.error(String.format("El usuario %s des-habilitadi por maximos intentos", usuario.getNombre()));
				usuario.setEnabled(false);
			}
	
			usuarioService.update(usuario, usuario.getId());
			
		} catch (FeignException e) {
			
			String error="El usuario %s no existe en el sistema"+ authentication.getName();
			log.error(error);
			tracer.currentSpan().tag("error.mensaje", error +": " + e.getMessage());
		}

	}

}
