package com.alineumsoft.zenwk.security.verification.dto;

import java.io.Serializable;

import com.alineumsoft.zenwk.security.common.constants.RegexConstants;
import com.alineumsoft.zenwk.security.constants.DtoValidationKeys;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * DTO para la gestión de la solicitud de token de verificación.
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class TokenDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Email (max 254).
	 */
	@Pattern(regexp = RegexConstants.EMAIL, message = DtoValidationKeys.USER_EMAIL_INVALID)
	@NotNull(message = DtoValidationKeys.USER_EMAIL_NOT_NULL)
	@Size(max = 254, message = DtoValidationKeys.USER_EMAIL_MAX_LENGTH)
	private String email;
	/**
	 * Nombre de usuario
	 */
	private String username;
}
