package com.alineumsoft.zenwk.security.helper;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.helper.ApiRestHelper;
import com.alineumsoft.zenwk.security.entity.LogSecurityUser;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class ApiRestSecurityHelper
 */
@Slf4j
public class ApiRestSecurityHelper  extends ApiRestHelper{
	/**
	 * <p>
	 * <b> General </b> Fija el valor para los atributos: creationDate, userCreation
	 * y url
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param response
	 * @param request
	 * @param principal
	 * @throws JsonProcessingException
	 */
	public LogSecurityUser initializeLog(HttpServletRequest httRequest, String userName, String request,
			String response) {
		Optional<String> urlOptional = Optional.ofNullable(httRequest).map(req -> req.getRequestURL().toString());
		Optional<String> methodOptional = Optional.ofNullable(httRequest).map(HttpServletRequest::getMethod);
		LogSecurityUser regLog = new LogSecurityUser();

		regLog.setCreationDate(LocalDateTime.now());
		regLog.setUserCreation(null);
		regLog.setUrl(urlOptional.orElse(CommonMessageConstants.NOT_APPLICABLE_URL));
		regLog.setUserCreation(userName);
		regLog.setMethod(methodOptional.orElse(CommonMessageConstants.NOT_APPLICABLE_METHOD));
		regLog.setRequest(request);
		regLog.setResponse(response);
		return regLog;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Actualizacion generica en caso de
	 * error
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param e
	 * @param logSecUser
	 */
	public void setLogSecurityError(RuntimeException e, LogSecurityUser logSecUser) {
		log.error(CommonMessageConstants.LOG_MSG_EXCEPTION, e.getMessage());
		logSecUser.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		logSecUser.setErrorMessage(e.getMessage());

	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Actualizacion generica en caso de
	 * exito
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param e
	 * @param logSecUser
	 */
	public void setLogSecuritySuccesful(int httpStatusCode, LogSecurityUser logSecUser) {
		log.info(CommonMessageConstants.REQUEST_SUCCESSFUL);
		logSecUser.setStatusCode(httpStatusCode);
		logSecUser.setErrorMessage(CommonMessageConstants.REQUEST_SUCCESSFUL);
	}
}
