package com.alineumsoft.zenwk.security.controller;

import java.net.URI;
import java.security.Principal;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alineumsoft.zenwk.security.constants.SecurityConstants;
import com.alineumsoft.zenwk.security.person.dto.PagePersonDTO;
import com.alineumsoft.zenwk.security.person.dto.PersonDTO;
import com.alineumsoft.zenwk.security.person.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class PersonController
 */
@RestController
@RequestMapping("/person")
public class PersonController {
	private final PersonService personService;
	private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Servicio REST para la creacion de
	 * una persona en el sistema
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param inDTO
	 * @param request
	 * @param uri
	 * @param principal
	 * @return
	 * @throws JsonProcessingException
	 */
	@PostMapping
	public ResponseEntity<Void> createPerson(@RequestBody PersonDTO inDTO, HttpServletRequest request,
			UriComponentsBuilder uriCB, Principal principal) {
		startTime.set(System.currentTimeMillis());
		Long idPerson = personService.createPerson(inDTO, request, principal, startTime.get());
		URI location = uriCB.path(SecurityConstants.HEADER_PERSON_LOCATION).buildAndExpand(idPerson).toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Servcio REST para la actualizacion
	 * de una persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param inDTO
	 * @param request
	 * @param princiapl
	 * @return
	 */
	@PutMapping("/{idUser}")
	public ResponseEntity<Void> updatePerson(@PathVariable Long idPerson, @RequestBody PersonDTO inDTO,
			HttpServletRequest request, Principal princiapl) {
		startTime.set(System.currentTimeMillis());
		personService.updatePerson(idPerson, inDTO, request, princiapl, startTime.get());
		return ResponseEntity.noContent().build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Servicio REST para la eliminación
	 * de una persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param request
	 * @param principal
	 * @return
	 */
	@DeleteMapping("/{idUser}")
	public ResponseEntity<Void> deletePerson(@PathVariable Long idUser, HttpServletRequest request,
			Principal principal) {
		startTime.set(System.currentTimeMillis());
		personService.deletePerson(idUser, request, principal, startTime.get());
		return ResponseEntity.noContent().build();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Servicio REST retorna la busqueda
	 * de una persona por id
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @param request
	 * @param principal
	 * @return
	 */
	@GetMapping("/{idPerson}")
	public ResponseEntity<PersonDTO> findById(@PathVariable Long idPerson, HttpServletRequest request,
			Principal principal) {
		startTime.set(System.currentTimeMillis());
		return ResponseEntity.ok(personService.findByIdPerson(idPerson, request, principal, startTime.get()));
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Servicio REST retorna la busqueda
	 * de todas las personas guardadas en el sistema
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param request
	 * @param principal
	 * @return
	 */
	@GetMapping
	public ResponseEntity<PagePersonDTO> findAll(HttpServletRequest request, Principal principal, Pageable pegeable) {
		startTime.set(System.currentTimeMillis());
		return ResponseEntity.ok(personService.findAll(pegeable, request, principal, startTime.get()));
	}

}
