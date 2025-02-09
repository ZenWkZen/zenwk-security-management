package com.alineumsoft.zenwk.security.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alineumsoft.zenwk.security.constants.ServiceControllerConstants;
import com.alineumsoft.zenwk.security.enums.HttpMethodResourceEnum;
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
@RequestMapping("/api/person")
public class PersonController {
	/**
	 * Servicio de controlador
	 */
	private final PersonService personService;
	/**
	 * Constante para metrica de tiempo
	 */
	private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param personService
	 */
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
	 * @param dto
	 * @param request
	 * @param uri
	 * @param userDetails
	 * @return
	 * @throws JsonProcessingException
	 */
	@PostMapping
	public ResponseEntity<Void> createPerson(@Validated @RequestBody PersonDTO dto, HttpServletRequest request,
			UriComponentsBuilder uriCB, @AuthenticationPrincipal UserDetails userDetails) {
		startTime.set(System.currentTimeMillis());
		Long idPerson = personService.createPerson(dto, request, userDetails, startTime.get());
		URI location = uriCB.path(HttpMethodResourceEnum.PERSON_FIND_BY_ID.getResource()).buildAndExpand(idPerson)
				.toUri();
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
	 * @param userDetails
	 * @return
	 */
	@PutMapping("/{idUser}")
	public ResponseEntity<Void> updatePerson(@PathVariable Long idPerson, @Validated @RequestBody PersonDTO inDTO,
			HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
		startTime.set(System.currentTimeMillis());
		personService.updatePerson(idPerson, inDTO, request, userDetails, startTime.get());
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
	 * @param userDetails
	 * @return
	 */
	@DeleteMapping("/{idUser}")
	public ResponseEntity<Void> deletePerson(@PathVariable Long idUser, HttpServletRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {
		startTime.set(System.currentTimeMillis());
		personService.deletePerson(idUser, request, userDetails, startTime.get());
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
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/{idPerson}")
	public ResponseEntity<PersonDTO> findById(@PathVariable Long idPerson, HttpServletRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {
		startTime.set(System.currentTimeMillis());
		return ResponseEntity.ok(personService.findByIdPerson(idPerson, request, userDetails, startTime.get()));
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
	 * @param userDetails
	 * @return
	 */
	@GetMapping
	public ResponseEntity<PagePersonDTO> findAll(HttpServletRequest request,
			@AuthenticationPrincipal UserDetails userDetails, Pageable pegeable) {
		startTime.set(System.currentTimeMillis());
		return ResponseEntity.ok(personService.findAll(pegeable, request, userDetails, startTime.get()));
	}

}
