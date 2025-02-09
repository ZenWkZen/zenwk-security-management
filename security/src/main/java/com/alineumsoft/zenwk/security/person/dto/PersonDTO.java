package com.alineumsoft.zenwk.security.person.dto;

import java.io.Serializable;

import com.alineumsoft.zenwk.security.common.constants.RegexConstants;
import com.alineumsoft.zenwk.security.common.validation.EntityExists;
import com.alineumsoft.zenwk.security.constants.DtoValidationKeys;
import com.alineumsoft.zenwk.security.person.entity.Person;
import com.alineumsoft.zenwk.security.user.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
	/**
	 * Id de person.
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long id;
	/**
	 * Id del usuario asociado.
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@NotNull(message = DtoValidationKeys.PERSON_ID_USER_NOT_NULL)
	@EntityExists(service = UserService.class, message = DtoValidationKeys.PERSON_ID_USER_NOT_FOUND)
	private Long idUser;
	/**
	 * Primer nombre.
	 */
	@Pattern(regexp = RegexConstants.NAME, message = DtoValidationKeys.PERSON_NAME_INVALID)
	@NotNull(message = DtoValidationKeys.PERSON_FIRST_NAME_NOT_NULL)
	@Size(max = 30, message = DtoValidationKeys.PERSON_NAME_INVALID)
	private String firstName;
	/**
	 * Segundo nombre.
	 */
	@Pattern(regexp = RegexConstants.NAME, message = DtoValidationKeys.PERSON_NAME_INVALID)
	@Size(max = 30, message = DtoValidationKeys.PERSON_NAME_INVALID)
	private String middleName;
	/**
	 * Primer apellido.
	 */
	@NotNull(message = DtoValidationKeys.PERSON_LAST_NAME_NOT_NULL)
	@Pattern(regexp = RegexConstants.NAME, message = DtoValidationKeys.PERSON_LAST_NAME_INVALID)
	@Size(max = 30, message = DtoValidationKeys.PERSON_LAST_NAME_INVALID)
	private String lastName;
	/**
	 * Segundo apellido.
	 */
	@Pattern(regexp = RegexConstants.NAME, message = DtoValidationKeys.PERSON_LAST_NAME_INVALID)
	@Size(max = 30, message = DtoValidationKeys.PERSON_LAST_NAME_INVALID)
	private String middleLastName;
	/**
	 * Fecha de cumpleanios
	 */
	@Pattern(regexp = RegexConstants.DATE_ISO_8601, message = DtoValidationKeys.PERSON_DATE_INVALID)
	private String dateOfBirth;
	/**
	 * Direccion.
	 */
	@Pattern(regexp = RegexConstants.COLOMBIA_ADDRESS, message = DtoValidationKeys.PERSON_COLOMBIA_ADDRESS)
	private String address;

	/**
	 * <b>Constructor </b> Genera una instancia a partir de la entidad JPA Person
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param person
	 */
	public PersonDTO(Person person) {
		this.id = person.getId();
		this.firstName = person.getFirstName();
		this.middleName = person.getMiddleName();
		this.lastName = person.getLastName();
		this.middleLastName = person.getMiddleLastName();
		this.dateOfBirth = person.getDateOfBirth().toString();
		this.address = person.getAddress();
	}

}
