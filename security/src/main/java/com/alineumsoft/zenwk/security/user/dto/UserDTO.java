package com.alineumsoft.zenwk.security.user.dto;

import java.io.Serializable;
import java.util.Optional;

import com.alineumsoft.zenwk.security.enums.UserStateEnum;
import com.alineumsoft.zenwk.security.person.entity.Person;
import com.alineumsoft.zenwk.security.user.entity.User;
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
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long id;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long idPerson;

	private String username;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;

	private String email;

	private UserStateEnum state;

	@JsonIgnore
	private Person person;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param user
	 */
	public UserDTO(User user) {
		Long idpersonTemp = Optional.ofNullable(user.getPerson().getId()).get();
		this.id = user.getId();
		this.idPerson = idpersonTemp;
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.state = user.getState();

	}
}
