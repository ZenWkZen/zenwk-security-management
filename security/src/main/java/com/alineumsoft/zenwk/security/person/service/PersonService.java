package com.alineumsoft.zenwk.security.person.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.alineumsoft.zenwk.security.common.ApiRestHelper;
import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.exception.TechnicalException;
import com.alineumsoft.zenwk.security.common.hist.enums.HistoricalOperationEnum;
import com.alineumsoft.zenwk.security.common.util.HistoricalUtil;
import com.alineumsoft.zenwk.security.common.util.ObjectUpdaterUtil;
import com.alineumsoft.zenwk.security.constants.SecurityUserConstants;
import com.alineumsoft.zenwk.security.entity.LogSecurityUser;
import com.alineumsoft.zenwk.security.enums.SecurityExceptionEnum;
import com.alineumsoft.zenwk.security.person.dto.PagePersonDTO;
import com.alineumsoft.zenwk.security.person.dto.PersonDTO;
import com.alineumsoft.zenwk.security.person.entity.Person;
import com.alineumsoft.zenwk.security.person.repository.PersonRepository;
import com.alineumsoft.zenwk.security.repository.LogSecurityUserRespository;
import com.alineumsoft.zenwk.security.user.enums.UserEnum;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonService extends ApiRestHelper {
	private final PersonRepository personRepository;
	private final LogSecurityUserRespository logSecurityUserRespository;
	private final TransactionTemplate transactionTemplate;

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
	public PersonService(PersonRepository personRepository, LogSecurityUserRespository logSecurityUser,
			TransactionTemplate transactionTemplate) {
		this.personRepository = personRepository;
		this.logSecurityUserRespository = logSecurityUser;
		this.transactionTemplate = transactionTemplate;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Metodo de servicio que crea una
	 * persona si cumple con las validaciones
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param inDTOE
	 * @param request
	 * @param principal
	 * @return
	 */
	public Long createPerson(PersonDTO inDTO, HttpServletRequest request, Principal principal) {
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(), getJson(inDTO),
				CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			return transactionCreatePerson(inDTO, logSecUser);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecUser);
			if (isFunctionalException(e)) {
				throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);

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
	 * @param inDTO
	 * @param request
	 * @param principal
	 * @return
	 */
	public boolean updatePerson(Long idPerson, PersonDTO inDTO, HttpServletRequest request, Principal principal) {
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(), getJson(inDTO),
				CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			return transactionUpdatePerson(idPerson, inDTO, principal.getName(), logSecUser);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecUser);
			if (isFunctionalException(e)) {
				throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
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
	 * @return
	 */
	public boolean deletePerson(Long idPerson, HttpServletRequest request, Principal principal) {
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			return transactionDeletePerson(idPerson, logSecUser);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecUser);
			if (isFunctionalException(e)) {
				throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
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
	 * @param inDTO
	 * @param logSecUser
	 * @return
	 */
	private Long transactionCreatePerson(PersonDTO inDTO, LogSecurityUser logSecUser) {
		return transactionTemplate.execute(transaction -> {
			try {
				return createPersonRecord(inDTO, logSecUser).getId();
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
	 * @param inDTO
	 * @param name
	 * @param logSecUser
	 * @return
	 */
	private boolean transactionUpdatePerson(Long idPerson, PersonDTO inDTO, String name, LogSecurityUser logSecUser) {
		Person target = personRepository.findById(idPerson).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(idPerson.toString())));
		return transactionTemplate.execute(transaction -> {
			try {
				ObjectUpdaterUtil.updateDataEqualObject(getPerson(inDTO), target);
				updatePersonRecord(target, name, logSecUser);
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
	 * @param logSecUser
	 * @return
	 */
	private boolean transactionDeletePerson(Long idPerson, LogSecurityUser logSecUser) {
		return transactionTemplate.execute(transaction -> {
			try {
				return deletePersonRecord(idPerson, logSecUser);
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
	 * @param inDTO
	 * @param userCreation
	 * @param creationDate
	 * @return
	 */
	private Person createPersonRecord(PersonDTO inDTO, LogSecurityUser logSecUser) {
		Person person = getPerson(inDTO);
		person.setUserCreation(logSecUser.getUserCreation());
		person.setCreationDate(LocalDateTime.now());
		person = personRepository.save(person);
		HistoricalUtil.registerHistorical(person, HistoricalOperationEnum.INSERT, PersonHistService.class);
		setLogSecuritySuccesful(HttpStatus.CREATED.value(), logSecUser);
		logSecurityUserRespository.save(logSecUser);
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
	 * @param logSecUser
	 * @return
	 */
	private void updatePersonRecord(Person person, String userModification, LogSecurityUser logSecUser) {
		person.setUserModification(userModification);
		person.setModificationDate(LocalDateTime.now());
		personRepository.save(person);
		HistoricalUtil.registerHistorical(person, HistoricalOperationEnum.UPDATE, PersonHistService.class);
		setLogSecuritySuccesful(HttpStatus.NO_CONTENT.value(), logSecUser);
		logSecurityUserRespository.save(logSecUser);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Realiza la eliminacion de la
	 * persona en la BD
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @param logSecUser
	 * @return
	 */
	private Boolean deletePersonRecord(Long idPerson, LogSecurityUser logSecUser) {
		Person person = personRepository.findById(idPerson).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(idPerson.toString())));
		personRepository.deleteById(idPerson);
		HistoricalUtil.registerHistorical(person, HistoricalOperationEnum.DELETE, PersonHistService.class);
		setLogSecuritySuccesful(HttpStatus.NO_CONTENT.value(), logSecUser);
		logSecurityUserRespository.save(logSecUser);
		return true;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Gener un entity persona a partir de
	 * un inDTO
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param inDTO
	 * @return
	 */
	private Person getPerson(PersonDTO inDTO) {
		Person person = new Person();
		person.setFirstUsurname(inDTO.getFirstUsurname());
		person.setEmail(inDTO.getEmail());
		person.setName(inDTO.getName());
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
		if (e.getMessage().contains(SecurityUserConstants.SQL_MESSAGE_EMAIL_EXISTS)) {
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
	 * @param idUser
	 * @return
	 */
	public PersonDTO findByIdPerson(Long idPerson, HttpServletRequest request, Principal principal) {
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			Person person = personRepository.findById(idPerson).orElseThrow(() -> new EntityNotFoundException(
					SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage(idPerson.toString())));
			setLogSecuritySuccesful(HttpStatus.OK.value(), logSecUser);
			logSecurityUserRespository.save(logSecUser);
			return new PersonDTO(person);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecUser);
			if (isFunctionalException(e)) {
				throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
		}

	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Recuperación de todos las personas
	 * almacenas en el sistema paginados, esto solo debe ser accedido por rol admin
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pageable
	 * @return
	 */
	public PagePersonDTO findAll(Pageable pageable, HttpServletRequest request, Principal principal) {
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
			Page<Person> pagePerson = personRepository.findAll(PageRequest.of(pageNumber, pageable.getPageSize(),
					pageable.getSortOr(Sort.by(Sort.Direction.ASC, UserEnum.PERSON_FIRST_NAME.getMessageKey())
							.and(Sort.by(Sort.Direction.ASC, UserEnum.PERSON_NAME.getMessageKey())))));

			return getFindAll(pagePerson, logSecUser);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecUser);
			if (isFunctionalException(e)) {
				throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
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
	 * @return
	 */
	private PagePersonDTO getFindAll(Page<Person> pagePerson, LogSecurityUser logSecUser) {
		List<PersonDTO> listPeople = pagePerson.stream().map(person -> new PersonDTO(person))
				.collect(Collectors.toList());
		if (listPeople.isEmpty()) {
			throw new EntityNotFoundException(SecurityExceptionEnum.FUNC_PERSON_NOT_FOUND.getCodeMessage());
		}
		setLogSecuritySuccesful(HttpStatus.OK.value(), logSecUser);
		logSecurityUserRespository.save(logSecUser);
		return new PagePersonDTO(listPeople, pagePerson.getTotalElements(), pagePerson.getTotalPages(),
				pagePerson.getNumber() + 1);
	}
}
