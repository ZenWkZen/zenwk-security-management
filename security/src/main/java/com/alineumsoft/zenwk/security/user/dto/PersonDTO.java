package com.alineumsoft.zenwk.security.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class PersonDTO
 */
@Data
public class PersonDTO implements Serializable {
	static final long serialVersionUID = 1L;

	private String name;

	private String firstSurname;

	private String email;
}
