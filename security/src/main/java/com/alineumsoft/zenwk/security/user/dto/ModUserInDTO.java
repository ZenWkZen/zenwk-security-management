package com.alineumsoft.zenwk.security.user.dto;

import java.io.Serializable;

import com.alineumsoft.zenwk.security.person.dto.PersonDTO;

import lombok.Data;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class userOutDTO
 */
@Data
public class ModUserInDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	private PersonDTO person;
}
