package com.alineumsoft.zenwk.security.user.controller;

import java.net.URI;
import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alineumsoft.zenwk.security.user.dto.UserInDTO;
import com.alineumsoft.zenwk.security.user.model.User;
import com.alineumsoft.zenwk.security.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	/**
	 * <p>
	 * <b> Constructor parametrizado </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userRepository
	 */
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * <p>
	 * <b> Seguridad_Creación_Usuario </b> Creacion usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @param uriCB
	 * @param principal
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody UserInDTO userInDTO, UriComponentsBuilder uriCB,
			Principal principal) {
		Long idUser = userService.createNewUser(userInDTO);
		URI location = uriCB.path("user/{idUser}").buildAndExpand(idUser).toUri();
		return ResponseEntity.created(location).build();
	}

}
