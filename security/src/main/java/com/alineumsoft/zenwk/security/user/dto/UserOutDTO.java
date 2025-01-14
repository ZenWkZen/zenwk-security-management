package com.alineumsoft.zenwk.security.user.dto;

import java.io.Serializable;

import com.alineumsoft.zenwk.security.person.dto.PersonDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class userOutDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String username;
	
	private String state;
	
	private PersonDTO person;
}
