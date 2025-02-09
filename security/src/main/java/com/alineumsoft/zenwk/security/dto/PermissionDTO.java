package com.alineumsoft.zenwk.security.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * DTO para el manejo de los permisos en la autorizacion de las api´s del modulo
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class PermissionDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
	/**
	 * Nombre del rol
	 */
	private String rolName;

	/**
	 * Metodo http
	 */
	private String method;

	/**
	 * Recurso
	 */
	private String resource;
	
}
