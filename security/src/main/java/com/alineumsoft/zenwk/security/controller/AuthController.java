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
	 * Contador de tiempo de la solicitud
	 */
	private static final ThreadLocal<Long> starTime = new ThreadLocal<>();
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
		starTime.set(System.currentTimeMillis());
		return ResponseEntity.ok(authService.authenticate(request, servRequest, principal, starTime.get()));
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
	public ResponseEntity<String> logout(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
		starTime.set(System.currentTimeMillis());
		authService.logout(request, userDetails, starTime.get());
		return ResponseEntity.ok(AuthEnums.AUTH_LOGOUT_SUCCES.getMessage());
	}

}
