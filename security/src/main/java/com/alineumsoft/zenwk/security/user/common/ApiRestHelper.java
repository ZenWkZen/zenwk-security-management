package com.alineumsoft.zenwk.security.user.common;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.user.common.constants.UtilConstants;
import com.alineumsoft.zenwk.security.user.common.exception.handler.GlobalHandlerException;
import com.alineumsoft.zenwk.security.user.constants.GeneralUserConstants;
import com.alineumsoft.zenwk.security.user.entity.LogSecurityUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ServiceCommons
 */
@Slf4j
public class ApiRestHelper {
	/**
	 * <p>
	 * <b> Util </b> Escribe el log de la peticion cuando existe un request body
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param modUserInDTO
	 * @param request
	 * @throws JsonProcessingException
	 */
	public void logRequest(Object modUserInDTO, HttpServletRequest request) throws JsonProcessingException {

		writeLogRequest(request);
		log.info(getJson(modUserInDTO));
	}

	/**
	 * <p>
	 * <b>General</b> Obtener Json, oculta datos sensibles con el password
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param inDTO
	 * @param objectMapper
	 * @return
	 * @throws JsonProcessingException
	 */
	public String getJson(Object inDTO) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(inDTO);

			if (json != null) {
				Map<String, Object> jsonMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
				});
				if (jsonMap.containsKey(GeneralUserConstants.FIELD_PASSWORD)) {
					jsonMap.put(GeneralUserConstants.FIELD_PASSWORD, UtilConstants.VALUE_SENSITY_MASK);

				}
				json = objectMapper.writeValueAsString(jsonMap);
			}
			return json;

		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * <p>
	 * <b> Util </b> Escribe el log de la peticion cuando NO existe un request body
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param modUserInDTO
	 * @param request
	 * @throws JsonProcessingException
	 */
	public static void logRequest(HttpServletRequest request) throws JsonProcessingException {
		writeLogRequest(request);
		log.info(CommonMessageConstants.NOT_APPLICABLE_BODY);
	}

	/**
	 * <p>
	 * <b> Performance </b> this.logRequest()
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 */
	private static void writeLogRequest(HttpServletRequest request) {
		log.info(request.getMethod());
		log.info(request.getRequestURL().toString());
	}

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

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> maneja de acuerdo a una
	 * runtimeException que que excpecion custom corresponde: tecnica o funcional
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param <T>
	 * @param e
	 * @param repository
	 * @param logSecUser
	 */
	public boolean isFunctionalException(RuntimeException e) {
		String code = GlobalHandlerException.extractCode(e.getMessage());
		if (code.contains(CommonMessageConstants.FUNCTIONAL_EXCEPTION_PREFIX)) {
			return true;
		}
		return false;
	}

}
