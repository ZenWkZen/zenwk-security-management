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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonService extends ApiRestSecurityHelper {
	private final PersonRepository personRepository;
	private final LogSecurityRespository logSecurityUserRespository;
	private final TransactionTemplate transactionTemplate;
	private final ApplicationEventPublisher eventPublisher;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param personRepository
	 * @param logSecurityUser
	 * @param transactionTemplate
	 */
	public PersonService(PersonRepository personRepository, LogSecurityRespository logSecurityUser,
			TransactionTemplate transactionTemplate, ApplicationEventPublisher eventPublisher) {
		this.personRepository = personRepository;
		this.logSecurityUserRespository = logSecurityUser;
		this.transactionTemplate = transactionTemplate;
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
			Person person = transactionCreatePerson(dto, logSecurity, starTime);
			// Se asocia la persona la usuario
			updateUserEvent(dto.getIdUser(), person, principal);
			return person.getId();
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
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
			return transactionUpdatePerson(idPerson, dto, principal.getName(), logSecurity, starTime);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
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
			deleteUserEvent(idPerson, principal);
			return transactionDeletePerson(idPerson, logSecurity, starTime);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
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
	 * @return
	 */
	private Person transactionCreatePerson(PersonDTO dto, LogSecurity logSecurity, Long starTime) {
		return transactionTemplate.execute(transaction -> {
			try {
				return createPersonRecord(dto, logSecurity, starTime);
			} catch (RuntimeException e) {
				transaction.setRollbackOnly();
				throw new IllegalArgumentException(getMessageSQLException(e));
			}
		});
	}

	/**
	 * 
	 * <p>
	 * <b> CU001_XSeguridad_Creación_UsuarioX </b> Realiza la transaccion para la
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
	private boolean transactionUpdatePerson(Long idPerson, PersonDTO dto, String name, LogSecurity logSecurity,
			Long starTime) {
		Person target = personRepository.findById(idPerson).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(idPerson.toString())));
		return transactionTemplate.execute(transaction -> {
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
	 * @return
	 */
	private boolean transactionDeletePerson(Long idPerson, LogSecurity logSecurity, Long starTime) {
		return transactionTemplate.execute(transaction -> {
			try {
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
		person = personRepository.save(person);
		// Persistencia de logs
		HistoricalUtil.registerHistorical(person, HistoricalOperationEnum.INSERT, PersonHistService.class);
		setLogSecuritySuccesfull(HttpStatus.CREATED.value(), logSecurity, starTime);
		logSecurityUserRespository.save(logSecurity);
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
		personRepository.save(person);
		HistoricalUtil.registerHistorical(person, HistoricalOperationEnum.UPDATE, PersonHistService.class);
		setLogSecuritySuccesfull(HttpStatus.NO_CONTENT.value(), logSecurity, starTime);
		logSecurityUserRespository.save(logSecurity);
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
		Person person = personRepository.findById(idPerson).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(idPerson.toString())));
		personRepository.deleteById(idPerson);
		HistoricalUtil.registerHistorical(person, HistoricalOperationEnum.DELETE, PersonHistService.class);
		setLogSecuritySuccesfull(HttpStatus.NO_CONTENT.value(), logSecurity, starTime);
		logSecurityUserRespository.save(logSecurity);
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
		person.setDateOfBirth(dto.getDateOfBirth());
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
		if (e.getMessage().contains(SecurityConstants.SQL_MESSAGE_EMAIL_EXISTS)) {
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
			Person person = personRepository.findById(idPerson).orElseThrow(() -> new EntityNotFoundException(
					SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(idPerson.toString())));
			setLogSecuritySuccesfull(HttpStatus.OK.value(), logSecurity, starTime);
			logSecurityUserRespository.save(logSecurity);
			return new PersonDTO(person);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
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
		return personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
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
			Page<Person> pagePerson = personRepository.findAll(PageRequest.of(pageNumber, pageable.getPageSize(),
					pageable.getSortOr(Sort.by(Sort.Direction.ASC, UserPersonEnum.PERSON_LAST_NAME.getMessageKey())
							.and(Sort.by(Sort.Direction.ASC, UserPersonEnum.PERSON_FIRST_NAME.getMessageKey())))));

			return getFindAll(pagePerson, logSecurity, starTime);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
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
		logSecurityUserRespository.save(logSecurity);
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
}
