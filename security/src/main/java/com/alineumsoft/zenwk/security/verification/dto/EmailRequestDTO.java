package com.alineumsoft.zenwk.security.verification.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * DTO que contiene los datos del correo
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class EmailRequestDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Para
	 */
	private String to;
	/**
	 * Asunto del correo
	 */
	private String subject;
	/**
	 * Cuerpo del correo
	 */
	private Map<String, Object> variables;
	/**
	 * Nombre de la plantilla del correo
	 */
	private String templateName;

}
