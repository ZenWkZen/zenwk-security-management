package com.alineumsoft.zenwk.security.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alineumsoft.zenwk.security.constants.SecurityConstants;
import com.alineumsoft.zenwk.security.user.dto.PageUserDTO;
import com.alineumsoft.zenwk.security.user.dto.UserDTO;
import com.alineumsoft.zenwk.security.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserController
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
	/**
	 * Servicio de controlador
	 */
	private final UserService userService;
	/**
	 * Constante para metrica de tiempo
	 */
	private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

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
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Creacion usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param dto
	 * @param uriCB
	 * @param principal
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@PostMapping
	public ResponseEntity<Void> createUser(@Validated @RequestBody UserDTO dto, UriComponentsBuilder uriCB,
			Principal principal, HttpServletRequest request) throws JsonProcessingException {
		startTime.set(System.currentTimeMillis());
		Long idUser = userService.createUser(dto, request, principal, startTime.get());
		URI location = uriCB.path(SecurityConstants.HEADER_USER_LOCATION).buildAndExpand(idUser).toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Actualizacion de usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param dto
	 * @return
	 * @throws IOException
	 */
	@PutMapping("{idUser}")
	public ResponseEntity<Void> updateUser(@PathVariable Long idUser, @RequestBody UserDTO dto,
			HttpServletRequest request, Principal principal) throws IOException {
		startTime.set(System.currentTimeMillis());
		userService.updateUser(request, idUser, dto, principal, null, startTime.get());
		return ResponseEntity.noContent().build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Eliminacion de usuario de la bd
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@DeleteMapping("/{idUser}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long idUser, HttpServletRequest request, Principal principal) {
		startTime.set(System.currentTimeMillis());
		userService.deleteUser(idUser, request, principal, startTime.get());
		return ResponseEntity.noContent().build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Busqueda por id
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param request
	 * @param principal
	 * @return
	 */
	@GetMapping("/{idUser}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long idUser, HttpServletRequest request,
			Principal principal) {
		startTime.set(System.currentTimeMillis());
		return ResponseEntity.ok(userService.findByIdUser(idUser, request, principal, startTime.get()));
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Busqueda de todos los usuarios en
	 * el sistema en caso de error excepciona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pageable
	 * @param request
	 * @return
	 */
	@GetMapping
	public ResponseEntity<PageUserDTO> findAll(Pageable pageable, HttpServletRequest request, Principal principal) {
		startTime.set(System.currentTimeMillis());
		return ResponseEntity.ok(userService.findAll(pageable, request, principal, startTime.get()));
	}
}
