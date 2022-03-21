package com.springbootappusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@EntityScan({"com.springboot.app.usuarios.commons.models.Entity"})
@SpringBootApplication
public class SpringbootServicioUsuriosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioUsuriosApplication.class, args);
	}

}
