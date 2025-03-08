package com.alineumsoft.zenwk.security.verification.service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.alineumsoft.zenwk.security.common.exception.FunctionalException;
import com.alineumsoft.zenwk.security.entity.LogSecurity;
import com.alineumsoft.zenwk.security.enums.SecurityActionEnum;
import com.alineumsoft.zenwk.security.enums.SecurityExceptionEnum;
import com.alineumsoft.zenwk.security.helper.ApiRestSecurityHelper;
import com.alineumsoft.zenwk.security.repository.LogSecurityRepository;
import com.alineumsoft.zenwk.security.verification.dto.EmailRequestDTO;
import com.alineumsoft.zenwk.security.verification.dto.TokenDTO;
import com.alineumsoft.zenwk.security.verification.entity.Token;
import com.alineumsoft.zenwk.security.verification.repository.TokenRepository;
import com.alineumsoft.zenwk.security.verification.util.CodeGenerator;
import com.alineumsoft.zenwk.security.verification.util.Constants;
import com.alineumsoft.zenwk.security.verification.util.MessagesVerification;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Servicio para el api encargado de la gestión del token de verificación de
 * usuario.
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class VerificationService
 */
@Service
@RequiredArgsConstructor
public class VerificationService extends ApiRestSecurityHelper {
	/**
	 * Repositorio para log persistible de modulo
	 */
	private final LogSecurityRepository logSecurityUserRespository;
	/**
	 * tokenRepository
	 */
	private final TokenRepository tokenRepository;
	/**
	 * rabbitTemplate
	 */
	private final AmqpTemplate rabbitTemplate;

	/**
	 * <p>
	 * <b> CU003_Gestionar token de verificación. </b> Envía un token de
	 * verificación a la cuenta de correo del usuario.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param dto
	 * @param userDetails
	 * @return
	 */
	public void sendToken(@Validated TokenDTO dto, HttpServletRequest request, UserDetails userDetails) {
		String username = userDetails == null ? dto.getEmail() : userDetails.getUsername();
		LogSecurity logSecurity = initializeLog(request, username, getJson(dto), notBody,
				SecurityActionEnum.VERIFICATION_SEND_TOKEN.getCode());
		try {

			// Veridicar si el usuario ya tiene tokens si es el caso elimnarlos
			String code = CodeGenerator.generateCode(Constants.TOKEN_CODE_ZISE);
			Token token = new Token(code, dto.getEmail(), username);
			token.setExpirationDate(LocalDateTime.now().plusMinutes(Constants.TOKEN_CODE_MINUTES));
			tokenRepository.save(token);
			EmailRequestDTO emailDTO = generateEmailToToken(dto.getEmail(), code, username);
			rabbitTemplate.convertAndSend(Constants.RABBITH_EMAIL_QUEUE, emailDTO);
			saveSuccessLog(HttpStatus.OK.value(), logSecurity, logSecurityUserRespository);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity);
			throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
		}

	}

	/**
	 * <p>
	 * <b> CU003_Gestionar token de verificación </b> Genera lo datos para el email
	 * con el código de verificación.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param email
	 * @param token
	 * @param username
	 * @return
	 */
	private EmailRequestDTO generateEmailToToken(String email, String token, String username) {
		EmailRequestDTO dto = new EmailRequestDTO();
		Map<String, Object> metadata = new HashMap<>();
		// Metadatos para la plantilla.
		metadata.put(Constants.TOKEN_TEMPLATE_EMAIL_NAME_CODE, token);
		metadata.put(Constants.TOKEN_TEMPLATE_EMAIL_NAME_USERNAME, username != null ? username : email);
		metadata.put(Constants.TOKEN_EMPLATE_EMAIL_NAME_CORPORATION, Constants.ZENWK);
		// Dto para enviar a cola de rabbithMq.
		dto.setTo(email);
		dto.setSubject(MessagesVerification.TOKEN_EMAIL_SUBJECT.getDescription());
		dto.setVariables(metadata);
		dto.setTemplateName(Constants.TOKEN_EMAIL_TEMPLATE);
		return dto;
	}

	/**
	 * <p>
	 * <b> CU003_Gestionar token de verificación. </b> lida un código de
	 * verificación generado previamente.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param code
	 * @param dto
	 * @param userDetails
	 * @return
	 */
	public boolean verifyToken(String code, TokenDTO dto, HttpServletRequest request, UserDetails userDetails) {
		String username = userDetails == null ? dto.getEmail() : userDetails.getUsername();
		LogSecurity logSecurity = initializeLog(request, username, getJson(dto), notBody,
				SecurityActionEnum.VERIFICATION_VALIDATE_TOKEN.getCode());
		try {
			// Consulta del token.
			Token token = tokenRepository.findByEmailAndCode(dto.getEmail(), code)
					.orElseThrow(() -> new EntityNotFoundException(
							SecurityExceptionEnum.FUNC_VERIFICATION_TOKEN_NOT_FOUND.getCodeMessage()));
			// Validacion expiracion del token.
			if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
				throw new DateTimeException(SecurityExceptionEnum.FUNC_VERIFICATION_TOKEN_EXPIRATION.getCodeMessage());
			}

			saveSuccessLog(HttpStatus.OK.value(), logSecurity, logSecurityUserRespository);
			return true;
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity);
			throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
		}

	}

}
