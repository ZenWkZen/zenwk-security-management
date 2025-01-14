package com.alineumsoft.zenwk.security.common.exception.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class ErrorResponse
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
	private String message;
	private String code;
	private LocalDateTime timestamp;
}
