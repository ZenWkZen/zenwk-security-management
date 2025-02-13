package com.alineumsoft.zenwk.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * DTO de salidad para logout
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class AuthRequestDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutOutDTO {
	/**
	 * message
	 */
	private String message;
}
