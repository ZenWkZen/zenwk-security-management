package com.alineumsoft.zenwk.security.common.helper;

import java.util.Map;

import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.constants.UtilConstants;
import com.alineumsoft.zenwk.security.common.exception.handler.GlobalHandlerException;
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
	 * @param inDTO
	 * @param request
	 * @throws JsonProcessingException
	 */
	public void logRequest(Object inDTO, HttpServletRequest request) throws JsonProcessingException {
		writeLogRequest(request);
		log.info(getJson(inDTO));
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
				if (jsonMap.containsKey(UtilConstants.FIELD_PASSWORD)) {
					jsonMap.put(UtilConstants.FIELD_PASSWORD, UtilConstants.VALUE_SENSITY_MASK);

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
