package com.alineumsoft.zenwk.security.user.dto;

import java.io.Serializable;

import com.alineumsoft.zenwk.security.user.entity.Person;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class PersonDTO
 */
@Data
@NoArgsConstructor
public class PersonDTO implements Serializable {
	static final long serialVersionUID = 1L;

	private String name;

	private String firstUsurname;

	private String email;

	/**
	 * <p>
	 * <b>Constructor </b> Genera una instancia a partir de la entidad JPA Person
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param person
	 */
	public PersonDTO(Person person) {
		this.name = person.getName();
		this.firstUsurname = person.getFirstUsurname();
		this.email = person.getEmail();
	}

}
