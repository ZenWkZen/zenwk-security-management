package com.alineumsoft.zenwk.security.auth.Service;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.auth.dto.AuthRequestDTO;
import com.alineumsoft.zenwk.security.auth.dto.AuthResponseDTO;
import com.alineumsoft.zenwk.security.auth.jwt.JwtProvider;
import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.exception.FunctionalException;
import com.alineumsoft.zenwk.security.common.exception.TechnicalException;
import com.alineumsoft.zenwk.security.entity.LogSecurity;
import com.alineumsoft.zenwk.security.enums.SecurityActionEnum;
import com.alineumsoft.zenwk.security.helper.ApiRestSecurityHelper;
import com.alineumsoft.zenwk.security.repository.LogSecurityRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Servicio usado para la autenticacion en el sistema
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class AuthService
 */
@Service
@Slf4j
public class AuthService extends ApiRestSecurityHelper {
	/**
	 * Repositorio para log persistible de modulo
	 */
	private final LogSecurityRepository logSecRepo;

	/**
	 * Manejador de autenticacion para spring security
	 */
	private final AuthenticationManager authManager;

	/**
	 * Manejador de jwt
	 */
	private final JwtProvider jwtProvider;

	/**
	 * Servicio para la carga de los permisos
	 */
	private final PermissionService permissionService;

	/**
	 * 
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param logSecRepo
	 * @param authManager
	 * @param jwtProvider
	 */
	public AuthService(LogSecurityRepository logSecRepo, AuthenticationManager authManager, JwtProvider jwtProvider,
			PermissionService permissionService) {
		this.logSecRepo = logSecRepo;
		this.authManager = authManager;
		this.jwtProvider = jwtProvider;
		this.permissionService = permissionService;
	}

	/**
	 * 
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Servicio usado para la
	 * autenticación en el sistema
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param inDTO
	 * @param request
	 * @param principal
	 * @param starTime
	 * @return
	 */
	public AuthResponseDTO authenticate(AuthRequestDTO inDTO, HttpServletRequest request, Principal principal,
			Long starTime) {
		AuthResponseDTO outDTO = new AuthResponseDTO();
		LogSecurity logSec = initializeLog(request, inDTO.getUsername(), getJson(inDTO),
				CommonMessageConstants.NOT_APPLICABLE_BODY, SecurityActionEnum.AUTH_LOGIN.getCode());

		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(inDTO.getUsername(), inDTO.getPassword()));
			// La constrasea se borra por seguridad cuando la autentiacion es exitosa
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			// Se genera el token con los permisos
			outDTO.setToken(jwtProvider.generateToken(userDetails,
					permissionService.listAllowedUrlsForUserRole(userDetails.getUsername())));
			setLogSecuritySuccesfull(HttpStatus.OK.value(), logSec, starTime);
			logSecRepo.save(logSec);
			return outDTO;
		} catch (RuntimeException e) {
			log.error(CommonMessageConstants.LOG_MSG_EXCEPTION, e);
			setLogSecurityError(e, logSec, starTime);
			throw new FunctionalException(e.getMessage(), e.getCause(), logSecRepo, logSec);
		}
	}

	/***
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Inhabilita token
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param starTime
	 */
	public void logout(HttpServletRequest request, UserDetails userDetails, Long starTime) {
		LogSecurity logSec = initializeLog(request, userDetails.getUsername(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityActionEnum.AUTH_LOGOUT.getCode());
		try {
			String token = jwtProvider.extractToken(request).orElseThrow();
			jwtProvider.invalidateToken(token);
			setLogSecuritySuccesfull(HttpStatus.OK.value(), logSec, starTime);
			logSecRepo.save(logSec);
		} catch (RuntimeException e) {
			log.error(CommonMessageConstants.LOG_MSG_EXCEPTION, e);
			setLogSecurityError(e, logSec, starTime);
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecRepo, logSec);
		}
	}

}
