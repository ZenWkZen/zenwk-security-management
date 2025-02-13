package com.alineumsoft.zenwk.security.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alineumsoft.zenwk.security.auth.Service.AuthService;
import com.alineumsoft.zenwk.security.auth.definitions.AuthEnums;
import com.alineumsoft.zenwk.security.auth.dto.AuthRequestDTO;
import com.alineumsoft.zenwk.security.auth.dto.AuthResponseDTO;
import com.alineumsoft.zenwk.security.auth.dto.LogoutOutDTO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * Controlador para la autenticación del usuario
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class AuthController
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	/**
	 * Servicio para auth
	 */
	private final AuthService authService;

	/**
	 * 
	 * <p>
	 * <b>Constructor</b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param authService
	 */
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	/**
	 * 
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Controller para la autenticación
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param servRequest
	 * @param principal
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody @Validated AuthRequestDTO request,
			HttpServletRequest servRequest, Principal principal) {
		return ResponseEntity.ok(authService.authenticate(request, servRequest, principal));
	}

	/**
	 * 
	 * <p>
	 * <b> CU002_Seguridad_Cierre_Sesion </b> Controller para cerrar sesión
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param userDetails
	 * @return
	 */
	@DeleteMapping("/logout")
	public ResponseEntity<LogoutOutDTO> logout(HttpServletRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {
		authService.logout(request, userDetails);
		return ResponseEntity.ok(new LogoutOutDTO(AuthEnums.AUTH_LOGOUT_SUCCES.getMessage()));
	}

}
