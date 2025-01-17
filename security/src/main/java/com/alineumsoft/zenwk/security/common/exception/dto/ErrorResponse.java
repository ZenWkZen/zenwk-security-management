package com.alineumsoft.zenwk.security.common.exception.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

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
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String field;

	private String error;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String code;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime timestamp;
}
