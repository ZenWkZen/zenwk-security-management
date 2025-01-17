package com.alineumsoft.zenwk.security.helper;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.exception.FunctionalException;
import com.alineumsoft.zenwk.security.common.helper.ApiRestHelper;
import com.alineumsoft.zenwk.security.entity.LogSecurity;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class ApiRestSecurityHelper
 */
@Slf4j
public class ApiRestSecurityHelper extends ApiRestHelper {
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
	 * @param serviceName
	 * @return
	 */
	public LogSecurity initializeLog(HttpServletRequest httpRequest, String userName, String request, String response,
			String serviceName) {
		Optional<String> urlOptional = Optional.ofNullable(httpRequest).map(req -> req.getRequestURL().toString());
		Optional<String> methodOptional = Optional.ofNullable(httpRequest).map(HttpServletRequest::getMethod);
		LogSecurity regLog = new LogSecurity();
		regLog.setCreationDate(LocalDateTime.now());
		regLog.setUserCreation(null);
		regLog.setUrl(urlOptional.orElse(CommonMessageConstants.AUTO_GENERATED_EVENT));
		regLog.setUserCreation(userName);
		regLog.setMethod(methodOptional.orElse(CommonMessageConstants.AUTO_GENERATED_EVENT));
		regLog.setRequest(request);
		regLog.setResponse(response);
		regLog.setIpAddress(getClientIp(httpRequest));
		regLog.setUserAgent(getUserAgent(httpRequest));
		regLog.setServiceName(serviceName);
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
	 * @param starTime
	 */
	public void setLogSecurityError(RuntimeException e, LogSecurity logSecUser, Long startTime) {
		log.error(CommonMessageConstants.LOG_MSG_EXCEPTION, e.getMessage());
		logSecUser.setErrorMessage(e.getMessage());
		logSecUser.setExecutionTime(getExecutionTime(startTime));
		if (e instanceof FunctionalException || e instanceof EntityNotFoundException) {
			logSecUser.setStatusCode(HttpStatus.NOT_FOUND.value());
		} else {
			logSecUser.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
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
	 * @param starTime
	 */
	public void setLogSecuritySuccesfull(int httpStatusCode, LogSecurity logSecUser, Long startTime) {
		log.info(CommonMessageConstants.REQUEST_SUCCESSFUL);
		logSecUser.setStatusCode(httpStatusCode);
		logSecUser.setErrorMessage(CommonMessageConstants.REQUEST_SUCCESSFUL);
		logSecUser.setExecutionTime(getExecutionTime(startTime));

	}
}
