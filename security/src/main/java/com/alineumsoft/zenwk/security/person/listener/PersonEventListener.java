package com.alineumsoft.zenwk.security.person.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.alineumsoft.zenwk.security.person.entity.Person;
import com.alineumsoft.zenwk.security.person.event.FindPersonByIdEvent;
import com.alineumsoft.zenwk.security.person.event.PersonCreatedEvent;
import com.alineumsoft.zenwk.security.person.service.PersonService;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class PersonEventListener
 */
@Component
public class PersonEventListener {
	private final PersonService personService;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param personService
	 */
	public PersonEventListener(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Evento para la creacion de la
	 * persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param event
	 */
	@EventListener
	public void handlePersonCreatedEvent(PersonCreatedEvent event) {
		Long idPerson = personService.createPerson(event.getPersonDTO(), null, event.getPrincipal());
		event.setIdPerson(idPerson);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Evento para la busqueda de la
	 * persona creada
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param event
	 */
	@EventListener
	public void handleFindPersonByIdEvent(FindPersonByIdEvent event) {
		Person person = personService.findEntityByIdPerson(event.getIdPerson());
		event.setPerson(person);
	}

}
