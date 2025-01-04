package com.alineumsoft.zenwk.security.user.common;

import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
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
		ObjectMapper objectMapper = new ObjectMapper();
		writeLogRequest(request);
		log.info(objectMapper.writeValueAsString(modUserInDTO));
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
		log.info(CommonMessageConstants.LOG_NO_BODY);
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

}
