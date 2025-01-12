package com.alineumsoft.zenwk.security.user.common.exception.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.user.common.constants.UtilConstants;
import com.alineumsoft.zenwk.security.user.common.exception.FunctionalException;
import com.alineumsoft.zenwk.security.user.common.exception.TechnicalException;
import com.alineumsoft.zenwk.security.user.common.exception.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class GlobalHandlerException
 */
@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {
	/**
	 * <p>
	 * <b> CommonException: </b> Manejador general
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param e
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> HandleGeneralException(RuntimeException e) {
		log.error(CommonMessageConstants.LOG_MSG_EXCEPTION, e.getMessage());
		String code = extractCode(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createNewError(e, code));
	}

	/**
	 * <p>
	 * <b> CommonException: </b> Manejador para exceptiones tecnicas
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param e
	 * @return
	 */
	@ExceptionHandler(TechnicalException.class)
	public ResponseEntity<ErrorResponse> HandleTechnicalException(TechnicalException e) {
		log.error(CommonMessageConstants.LOG_MSG_EXCEPTION_TECHNICAL, e.getMessage());
		String code = extractCode(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createNewError(e, code));
	}

	/**
	 * <p>
	 * <b> CommonException: </b> Manejador para exceptiones funcionales
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param e
	 * @return
	 */
	@ExceptionHandler(FunctionalException.class)
	public ResponseEntity<ErrorResponse> HandleFunctionalException(FunctionalException e) {
		log.error(CommonMessageConstants.LOG_MSG_EXCEPTION_FUNCTIONAL, e.getMessage());
		String code = extractCode(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createNewError(e, code));
	}

	/**
	 * <p>
	 * <b> Util: </b> Crea una nueva instancia para ErrorResponse
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param e
	 * @return
	 */
	private static ErrorResponse createNewError(RuntimeException e, String code) {
		// Se realiza una ultima comprobacion del codigo de error
		if (code == null) {
			code = extractCode(e.getMessage());
		}

		return new ErrorResponse(CommonMessageConstants.MSG_EXEPTION_GENERAL,
				code == null ? CommonMessageConstants.CODE_MSG_GENERAL : code, LocalDateTime.now());
	}

	/**
	 * <p>
	 * <b> Util: </b> Obtiene codigo de la excepcion si existe
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param formattedString
	 * @return
	 */
	private static String extractCode(String formattedString) {
		int start = formattedString.indexOf(UtilConstants.LEFT_BRACKET);
		int end = formattedString.indexOf(UtilConstants.RIGHT_BRACKET);

		if (start < 0 || end < 0 || start >= end) {
			return null;
		}

		String code = formattedString.substring(start + 1, end);

		return (code.startsWith(CommonMessageConstants.FUNCTIONAL_EXCEPTION_PREFIX)
				|| code.startsWith(CommonMessageConstants.TECHNICAL_EXCEPTION_PREFIX)) ? code : null;
	}

}
