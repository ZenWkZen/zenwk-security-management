package com.alineumsoft.zenwk.security.person.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.exception.FunctionalException;
import com.alineumsoft.zenwk.security.common.exception.TechnicalException;
import com.alineumsoft.zenwk.security.common.hist.enums.HistoricalOperationEnum;
import com.alineumsoft.zenwk.security.common.util.HistoricalUtil;
import com.alineumsoft.zenwk.security.common.util.ObjectUpdaterUtil;
import com.alineumsoft.zenwk.security.constants.SecurityConstants;
import com.alineumsoft.zenwk.security.entity.LogSecurity;
import com.alineumsoft.zenwk.security.enums.SecurityExceptionEnum;
import com.alineumsoft.zenwk.security.enums.SecurityServiceNameEnum;
import com.alineumsoft.zenwk.security.enums.UserPersonEnum;
import com.alineumsoft.zenwk.security.helper.ApiRestSecurityHelper;
import com.alineumsoft.zenwk.security.person.dto.PagePersonDTO;
import com.alineumsoft.zenwk.security.person.dto.PersonDTO;
import com.alineumsoft.zenwk.security.person.entity.Person;
import com.alineumsoft.zenwk.security.person.repository.PersonRepository;
import com.alineumsoft.zenwk.security.repository.LogSecurityRespository;
import com.alineumsoft.zenwk.security.user.dto.UserDTO;
import com.alineumsoft.zenwk.security.user.entity.User;
import com.alineumsoft.zenwk.security.user.event.DeleteUserEvent;
import com.alineumsoft.zenwk.security.user.event.FindUserByIdPersonEvent;
import com.alineumsoft.zenwk.security.user.event.UpdateUserEvent;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonService extends ApiRestSecurityHelper {
	/**
	 * Repositorio de la persona
	 */
	private final PersonRepository personRepo;
	/**
	 * Repositorio para log persistible de modulo
	 */
	private final LogSecurityRespository logSecUserRespo;
	/**
	 * Template para transaccion
	 */
	private final TransactionTemplate templateTx;
	/**
	 * Interfaz para publicacion de evento
	 */
	private final ApplicationEventPublisher eventPublisher;

	/**
	 * <p>
	 * <b>Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param personRepo
	 * @param logSecurityUser
	 * @param templateTx
	 * @param eventPublisher
	 */
	public PersonService(PersonRepository personRepo, LogSecurityRespository logSecurityUser,
			TransactionTemplate templateTx, ApplicationEventPublisher eventPublisher) {
		this.personRepo = personRepo;
		this.logSecUserRespo = logSecurityUser;
		this.templateTx = templateTx;
		this.eventPublisher = eventPublisher;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Metodo de servicio que crea una
	 * persona si cumple con las validaciones
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param inDTOE
	 * @param request
	 * @param principal
	 * @param starTime
	 * @return
	 */
	public Long createPerson(PersonDTO dto, HttpServletRequest request, Principal principal, Long starTime) {
		LogSecurity logSecurity = initializeLog(request, principal.getName(), getJson(dto),
				CommonMessageConstants.NOT_APPLICABLE_BODY, SecurityServiceNameEnum.PERSON_CREATE.getCode());
		try {
			Person person = createPersonTx(dto, logSecurity, starTime, principal);
			return person.getId();
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Metodo se servicio que actualiza
	 * una persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @param dto
	 * @param request
	 * @param principal
	 * @param starTime
	 * @return
	 */
	public boolean updatePerson(Long idPerson, PersonDTO dto, HttpServletRequest request, Principal principal,
			Long starTime) {
		LogSecurity logSecurity = initializeLog(request, principal.getName(), getJson(dto),
				CommonMessageConstants.NOT_APPLICABLE_BODY, SecurityServiceNameEnum.PERSON_UPDATE.getCode());
		try {
			return updatePersonTx(idPerson, dto, principal.getName(), logSecurity, starTime);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Metodo de servicio que elmina una
	 * persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @param request
	 * @param principal
	 * @param starTime
	 * @return
	 */
	public boolean deletePerson(Long idPerson, HttpServletRequest request, Principal principal, Long starTime) {
		LogSecurity logSecurity = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityServiceNameEnum.PERSON_DELETE.getCode());
		try {
			return deletePersonTx(idPerson, logSecurity, starTime, principal, request != null);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Realiza la transaccion para la
	 * creacion de la persona en caso de error hace rollback y actualiza el log del
	 * modulo de seguridad
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param dto
	 * @param logSecurity
	 * @param starTime
	 * @param principal
	 * @return
	 */
	private Person createPersonTx(PersonDTO dto, LogSecurity logSecurity, Long starTime, Principal principal) {
		return templateTx.execute(transaction -> {
			try {
				Person person = createPersonRecord(dto, logSecurity, starTime);
				// Se asocia la persona la usuario
				updateUserEvent(dto.getIdUser(), person, principal);
				return person;
			} catch (ConstraintViolationException e) {
				throw new RuntimeException(e.getMessage());
			}
		});
	}

	/**
	 * 
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Realiza la transaccion para la
	 * actualizacion de la persona en caso de error hace rollback y actualiza el log
	 * del modulo
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @param dto
	 * @param name
	 * @param logSecurity
	 * @param starTime
	 * @return
	 */
	private boolean updatePersonTx(Long idPerson, PersonDTO dto, String name, LogSecurity logSecurity, Long starTime) {
		Person target = personRepo.findById(idPerson).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(idPerson.toString())));
		return templateTx.execute(transaction -> {
			try {
				ObjectUpdaterUtil.updateDataEqualObject(getPerson(dto), target);
				updatePersonRecord(target, name, logSecurity, starTime);
				return true;
			} catch (RuntimeException e) {
				transaction.setRollbackOnly();
				throw new IllegalArgumentException(getMessageSQLException(e));
			}
		});
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> SRealiza la transaccion para la
	 * eliminacion de la persona en caso de error hace rollback y actualiza el log
	 * del modulo
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @param logSecurity
	 * @param starTime
	 * @param principal
	 * @param isDeleteUser
	 * @return
	 */
	private boolean deletePersonTx(Long idPerson, LogSecurity logSecurity, Long starTime, Principal principal,
			boolean isDeleteUser) {
		return templateTx.execute(transaction -> {
			try {
				// ELminacion del usuario a travez del evento
				if (isDeleteUser) {
					deleteUserEvent(idPerson, principal);
				}

				return deletePersonRecord(idPerson, logSecurity, starTime);
			} catch (RuntimeException e) {
				transaction.setRollbackOnly();
				throw new IllegalArgumentException(getMessageSQLException(e));
			}
		});
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Realiza la creacioo de la persona
	 * en la BD
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param dto
	 * @param logSecurity
	 * @param starTime
	 * @return
	 */
	private Person createPersonRecord(PersonDTO dto, LogSecurity logSecurity, Long starTime) {
		Person person = getPerson(dto);
		person.setUserCreation(logSecurity.getUserCreation());
		person.setCreationDate(LocalDateTime.now());
		// Si la person no existe se persiste
		if (!isExistPerson(person)) {
			person = personRepo.save(person);
			// Persistencia de logs
			HistoricalUtil.registerHistorical(person, HistoricalOperationEnum.INSERT, PersonHistService.class);
			setLogSecuritySuccesfull(HttpStatus.CREATED.value(), logSecurity, starTime);
			logSecUserRespo.save(logSecurity);
		}
		return person;

	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> ealiza la actualizacion de la
	 * persona en la BD
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param person
	 * @param userModification
	 * @param logSecurity
	 * @param starTime
	 * @return
	 */
	private void updatePersonRecord(Person person, String userModification, LogSecurity logSecurity, Long starTime) {
		person.setUserModification(userModification);
		person.setModificationDate(LocalDateTime.now());
		personRepo.save(person);
		HistoricalUtil.registerHistorical(person, HistoricalOperationEnum.UPDATE, PersonHistService.class);
		setLogSecuritySuccesfull(HttpStatus.NO_CONTENT.value(), logSecurity, starTime);
		logSecUserRespo.save(logSecurity);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Realiza la eliminacion de la
	 * persona en la BD
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @param logSecurity
	 * @param starTime
	 * @return
	 */
	private Boolean deletePersonRecord(Long idPerson, LogSecurity logSecurity, Long starTime) {
		Person person = personRepo.findById(idPerson).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(idPerson.toString())));
		personRepo.deleteById(idPerson);
		HistoricalUtil.registerHistorical(person, HistoricalOperationEnum.DELETE, PersonHistService.class);
		setLogSecuritySuccesfull(HttpStatus.NO_CONTENT.value(), logSecurity, starTime);
		logSecUserRespo.save(logSecurity);
		return true;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Gener un entity persona a partir de
	 * un inDTO
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param dto
	 * @return
	 */
	private Person getPerson(PersonDTO dto) {
		Person person = new Person();
		person.setFirstName(dto.getFirstName());
		person.setMiddleName(dto.getMiddleName());
		person.setLastName(dto.getLastName());
		person.setMiddleLastName(dto.getMiddleLastName());
		person.setDateOfBirth(getDateIso8601(dto.getDateOfBirth()));
		person.setAddress(dto.getAddress());
		return person;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Recupera el mensaje predefinido
	 * para para el error datos a nivel de BD
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param e
	 * @return
	 */
	private String getMessageSQLException(Exception e) {
		if (e.getLocalizedMessage().contains(SecurityConstants.SQL_MESSAGE_EMAIL_EXISTS)) {
			return SecurityExceptionEnum.FUNC_USER_MAIL_EXISTS.getCodeMessage();
		}

		return e.getMessage();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Busqueda de persona por id en caso
	 * de error se persiste en log de modulo
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @param request
	 * @param principal
	 * @param starTime
	 * @return
	 */
	public PersonDTO findByIdPerson(Long idPerson, HttpServletRequest request, Principal principal, Long starTime) {
		LogSecurity logSecurity = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityServiceNameEnum.PERSON_FIND_BY_ID.getCode());
		try {
			Person person = personRepo.findById(idPerson).orElseThrow(() -> new EntityNotFoundException(
					SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(idPerson.toString())));
			setLogSecuritySuccesfull(HttpStatus.OK.value(), logSecurity, starTime);
			logSecUserRespo.save(logSecurity);
			return new PersonDTO(person);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
		}

	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Recupera la entidad person a partir
	 * de su id en caso contrario glanza excepcion
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @return
	 */
	public Person findEntityByIdPerson(Long id) {
		return personRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(id.toString())));
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Recuperación de todos las personas
	 * almacenas en el sistema paginados, esto solo debe ser accedido por rol admin
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pageable
	 * @param request
	 * @param principal
	 * @param starTime
	 * @return
	 */
	public PagePersonDTO findAll(Pageable pageable, HttpServletRequest request, Principal principal, Long starTime) {
		LogSecurity logSecurity = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityServiceNameEnum.PERSON_FIND_ALL.getCode());
		try {
			int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
			Page<Person> pagePerson = personRepo.findAll(PageRequest.of(pageNumber, pageable.getPageSize(),
					pageable.getSortOr(Sort.by(Sort.Direction.ASC, UserPersonEnum.PERSON_LAST_NAME.getMessageKey())
							.and(Sort.by(Sort.Direction.ASC, UserPersonEnum.PERSON_FIRST_NAME.getMessageKey())))));

			return getFindAll(pagePerson, logSecurity, starTime);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecUserRespo, logSecurity);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Obtiene la lista de personas en
	 * caso de ser vacia excepciona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pagePerson
	 * @param logSecurity
	 * @param starTime
	 * @return
	 */
	private PagePersonDTO getFindAll(Page<Person> pagePerson, LogSecurity logSecurity, Long starTime) {
		List<PersonDTO> listPeople = pagePerson.stream().map(person -> new PersonDTO(person))
				.collect(Collectors.toList());
		setLogSecuritySuccesfull(HttpStatus.OK.value(), logSecurity, starTime);
		logSecUserRespo.save(logSecurity);
		return new PagePersonDTO(listPeople, pagePerson.getTotalElements(), pagePerson.getTotalPages(),
				pagePerson.getNumber() + 1);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Publicacion de los eventos buscar y
	 * eliminar un usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @param principal
	 */
	private void deleteUserEvent(Long idPerson, Principal principal) {
		FindUserByIdPersonEvent eventFindUser = new FindUserByIdPersonEvent(this, idPerson);
		eventPublisher.publishEvent(eventFindUser);
		User user = eventFindUser.getUser();
		if (user != null) {
			// Evento para la eliminacion del usuario
			DeleteUserEvent eventDelete = new DeleteUserEvent(this, user.getId(), principal);
			eventPublisher.publishEvent(eventDelete);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Evento para la actualizacion del
	 * usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param person
	 * @param principal
	 */
	private void updateUserEvent(Long idUser, Person person, Principal principal) {
		UserDTO dto = new UserDTO();
		dto.setPerson(person);
		UpdateUserEvent event = new UpdateUserEvent(this, idUser, dto, principal, person);
		eventPublisher.publishEvent(event);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Valida si el request usado para la
	 * creacion de la persona ya existe
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param person
	 * @return
	 */
	private boolean isExistPerson(Person person) {
		if (personRepo.existsByFirstNameAndMiddleNameAndLastNameAndMiddleLastNameAndDateOfBirth(person.getFirstName(),
				person.getMiddleName(), person.getLastName(), person.getMiddleLastName(), person.getDateOfBirth())) {
			throw new IllegalArgumentException(SecurityExceptionEnum.FUNC_PERSON_EXIST.getCodeMessage());
		}
		return false;
	}
}
