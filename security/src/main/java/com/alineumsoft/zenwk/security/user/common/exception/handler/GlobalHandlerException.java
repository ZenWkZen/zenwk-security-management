package com.alineumsoft.zenwk.security.user.common.exception.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;
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
		log.error(CommonMessageConstants.MSG_EXCEPTION, e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createNewError(e, null));
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
		log.warn(CommonMessageConstants.MSG_EXCEPTION_TECHNICAL, e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createNewError(e, e.getCode()));
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
		log.warn(CommonMessageConstants.MSG_EXCEPTION_FUNCTIONAL, e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createNewError(e, e.getCode()));
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
	private ErrorResponse createNewError(RuntimeException e, String code) {
		return new ErrorResponse(CommonMessageConstants.MSG_EXEPTION_GENERAL,
				code == null ? CommonMessageConstants.CODE_MSG_GENERAL : code, LocalDateTime.now());
	}

}
