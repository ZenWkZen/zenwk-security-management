package com.alineumsoft.zenwk.security.person.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.person.entity.Person;
import com.fasterxml.jackson.annotation.JsonInclude;

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
	private static final long serialVersionUID = 1L;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long idUser;
	
	private String firstName;

	private String middleName;

	private String lastName;

	private String middleLastName;

	private LocalDateTime dateOfBirth;

	private String address;

	/**
	 * <b>Constructor </b> Genera una instancia a partir de la entidad JPA Person
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param person
	 */
	public PersonDTO(Person person) {
		this.firstName = person.getFirstName();
		this.middleName = person.getMiddleName();
		this.lastName = person.getLastName();
		this.middleLastName = person.getMiddleLastName();
		this.dateOfBirth = person.getDateOfBirth();
		this.address = person.getAddress();
	}

}
