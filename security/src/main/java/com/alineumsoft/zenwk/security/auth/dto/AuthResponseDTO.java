package com.alineumsoft.zenwk.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * DTO con la respuesta para la autenticación en el sistema
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class AuthResponseDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
	/**
	 * token
	 */
	private String token;

}
