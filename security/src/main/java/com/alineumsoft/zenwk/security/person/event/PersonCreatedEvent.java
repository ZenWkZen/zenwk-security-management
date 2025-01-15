package com.alineumsoft.zenwk.security.person.event;

import java.security.Principal;

import org.springframework.context.ApplicationEvent;

import com.alineumsoft.zenwk.security.person.dto.PersonDTO;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class PersonCreatedEvent
 */
public class PersonCreatedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private final PersonDTO personDTO;
	private final Principal principal;
	private Long idPerson;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param source
	 * @param personDTO
	 * @param principal
	 */
	public PersonCreatedEvent(Object source, PersonDTO personDTO, Principal principal) {
		super(source);
		this.personDTO = personDTO;
		this.principal = principal;
	}

	/**
	 * Gets the value of idPerson.
	 * 
	 * @return the value of idPerson.
	 */
	public Long getIdPerson() {
		return idPerson;
	}

	/**
	 * Sets the value of idPerson.
	 * 
	 * @param idPerson the new value of idPerson.
	 */
	public void setIdPerson(Long idPerson) {
		this.idPerson = idPerson;
	}

	/**
	 * Gets the value of personDTO.
	 * 
	 * @return the value of personDTO.
	 */
	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	/**
	 * Gets the value of principal.
	 * 
	 * @return the value of principal.
	 */
	public Principal getPrincipal() {
		return principal;
	}

}
