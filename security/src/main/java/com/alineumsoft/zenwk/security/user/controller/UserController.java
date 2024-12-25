package com.alineumsoft.zenwk.security.user.controller;

import java.net.URI;
import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alineumsoft.zenwk.security.user.entity.User;
import com.alineumsoft.zenwk.security.user.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	private final UserRepository userRepository;

	/**
	 * <p>
	 * <b> Constructor parametrizado </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userRepository
	 */
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * <p>
	 * <b> Seguridad_Creación_Usuario </b> Creacion usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userRequest
	 * @param uriCB
	 * @param principal
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody User userRequest, UriComponentsBuilder uriCB,
			Principal principal) {
		// Crear metodo validacion
		User savedUser = userRepository.save(userRequest);
		URI location = uriCB.path("user/{idUser}").buildAndExpand(savedUser.getIdUsuario()).toUri();
		return ResponseEntity.created(location).build();
	}

}
