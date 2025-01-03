package com.alineumsoft.zenwk.security.user.controller;

import java.net.URI;
import java.security.Principal;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alineumsoft.zenwk.security.user.constants.ConfigUserConstants;
import com.alineumsoft.zenwk.security.user.dto.CreateUserInDTO;
import com.alineumsoft.zenwk.security.user.dto.ModUserInDTO;
import com.alineumsoft.zenwk.security.user.dto.PageUserDTO;
import com.alineumsoft.zenwk.security.user.dto.UserOutDTO;
import com.alineumsoft.zenwk.security.user.service.UserService;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserController
 */
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	/**
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userService
	 * @param messageService
	 */
	private UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * <p>
	 * <b> U001_Seguridad_Creacion_Usuario </b> Creacion usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @param uriCB
	 * @param principal
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody CreateUserInDTO userInDTO, UriComponentsBuilder uriCB,
			Principal principal) {
		Long idUser = userService.createNewUser(userInDTO);
		URI location = uriCB.path(ConfigUserConstants.HEADER_LOCATION).buildAndExpand(idUser).toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Busqueda por id
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @return
	 */
	@GetMapping("/{idUser}")
	public ResponseEntity<UserOutDTO> findById(@PathVariable Long idUser) {
		UserOutDTO userOutDTO = userService.findByIdUser(idUser);
		if (userOutDTO != null) {
			return ResponseEntity.ok(userOutDTO);
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Busqueda total
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pageable
	 * @return
	 */
	@GetMapping
	public ResponseEntity<PageUserDTO> findAll(Pageable pageable) {
		PageUserDTO response = userService.findAll(pageable);
		if (!response.getUsers().isEmpty()) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Actualizacion de usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param modUserInDTO
	 * @return
	 */
	@PutMapping("{idUser}")
	public ResponseEntity<Void> updateUser(@PathVariable Long idUser, @RequestBody ModUserInDTO modUserInDTO) {
		if (userService.updateUser(idUser, modUserInDTO)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Eliminacion de usuario de la bd
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @return
	 */
	@DeleteMapping("/{idUser}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long idUser) {
		if (userService.deleteUser(idUser)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
