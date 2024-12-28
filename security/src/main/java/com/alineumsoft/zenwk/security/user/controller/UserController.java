package com.alineumsoft.zenwk.security.user.controller;

import java.net.URI;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alineumsoft.zenwk.security.user.dto.UserInDTO;
import com.alineumsoft.zenwk.security.user.enums.UserStateEnum;
import com.alineumsoft.zenwk.security.user.messages.enums.MessageKeyEnum;
import com.alineumsoft.zenwk.security.user.messages.service.MessageService;
import com.alineumsoft.zenwk.security.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;

	private MessageService messageService;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userService
	 * @param messageService
	 */
	public UserController(UserService userService, MessageService messageService) {
		this.userService = userService;
		this.messageService = messageService;
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

		String messageCreate = messageService.getMessage(MessageKeyEnum.USER_CREATION_EXIT, "1", "éxito");
		String messageCreate2 = messageService.getMessage(MessageKeyEnum.USER_CREATION_EXIT);

		System.out.print("=======messageCreate========" + messageCreate);
		System.out.print("=======messageCreate2========" + messageCreate2);
		Long idUser = userService.createNewUser(userInDTO);
		URI location = uriCB.path("user/{idUser}").buildAndExpand(idUser).toUri();
		return ResponseEntity.created(location).build();
	}

}
