package com.alineumsoft.zenwk.security.user.dto;

import java.io.Serializable;

import com.alineumsoft.zenwk.security.enums.UserStateEnum;
import com.alineumsoft.zenwk.security.person.entity.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserInDTO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;

	private String email;

	private UserStateEnum state;
	
	@JsonIgnore
	private Person person;
}
