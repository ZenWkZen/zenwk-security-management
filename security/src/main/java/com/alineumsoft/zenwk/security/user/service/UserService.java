package com.alineumsoft.zenwk.security.user.service;

import java.security.Principal;
import java.sql.SQLException;
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
import com.alineumsoft.zenwk.security.common.util.CryptoUtil;
import com.alineumsoft.zenwk.security.common.util.HistoricalUtil;
import com.alineumsoft.zenwk.security.common.util.ObjectUpdaterUtil;
import com.alineumsoft.zenwk.security.constants.SecurityUserConstants;
import com.alineumsoft.zenwk.security.entity.LogSecurityUser;
import com.alineumsoft.zenwk.security.enums.SecurityExceptionEnum;
import com.alineumsoft.zenwk.security.helper.ApiRestSecurityHelper;
import com.alineumsoft.zenwk.security.person.dto.PersonDTO;
import com.alineumsoft.zenwk.security.person.entity.Person;
import com.alineumsoft.zenwk.security.person.event.FindPersonByIdEvent;
import com.alineumsoft.zenwk.security.person.event.PersonCreatedEvent;
import com.alineumsoft.zenwk.security.repository.LogSecurityUserRespository;
import com.alineumsoft.zenwk.security.user.dto.CreateUserInDTO;
import com.alineumsoft.zenwk.security.user.dto.ModUserInDTO;
import com.alineumsoft.zenwk.security.user.dto.PageUserDTO;
import com.alineumsoft.zenwk.security.user.dto.UserOutDTO;
import com.alineumsoft.zenwk.security.user.entity.User;
import com.alineumsoft.zenwk.security.user.entity.UserState;
import com.alineumsoft.zenwk.security.user.enums.UserEnum;
import com.alineumsoft.zenwk.security.user.enums.UserStateEnum;
import com.alineumsoft.zenwk.security.user.repository.UserRepository;
import com.alineumsoft.zenwk.security.user.repository.UserStateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserService
 */
@Service
@Slf4j
public class UserService extends ApiRestSecurityHelper {
	private final UserRepository userRepository;
	private final UserStateRepository userStateRepository;
	private final LogSecurityUserRespository logSecurityUserRespository;
	private final TransactionTemplate transactionTemplate;
	private final ApplicationEventPublisher eventPublisher;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userRepository
	 * @param UserStateRepository
	 * @param logRepository
	 * @param transationTemplate
	 * @param eventPublisher
	 */
	public UserService(UserRepository userRepository, UserStateRepository UserStateRepository,
			LogSecurityUserRespository logRepository, TransactionTemplate transationTemplate,
			ApplicationEventPublisher eventPublisher) {
		this.userRepository = userRepository;
		this.userStateRepository = UserStateRepository;
		this.logSecurityUserRespository = logRepository;
		this.transactionTemplate = transationTemplate;
		this.eventPublisher = eventPublisher;
	}

	/**
	 * 
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Metodo de servicio que crea un
	 * nuevo usuario si cumple con las validaciones
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @param principal
	 * @return
	 * @throws JsonProcessingException
	 */
	public Long createUser(CreateUserInDTO userInDTO, HttpServletRequest request, Principal principal) {
		// inicializacion log transaccional
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(), getJson(userInDTO),
				CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			Person person = createPersonEvent(userInDTO, principal);
			return transactionCreateUser(userInDTO, logSecUser, person);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecUser);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Actualizacion general de usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param principal
	 * @param userInDTO
	 * @param userOutDTO
	 * @throws JsonProcessingException
	 */
	public boolean updateUser(HttpServletRequest request, Long idUser, ModUserInDTO modUserInDTO, Principal principal) {
		// Inicializacion log transaccional
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(), getJson(modUserInDTO),
				CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			return transactionUpdateUser(idUser, modUserInDTO, logSecUser);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecUser);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Se realiza un borrado fisico del
	 * usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param principal
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	public boolean deleteUser(Long idUser, HttpServletRequest request, Principal principal) {
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			return transactionDeleteUser(idUser, logSecUser);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecUser);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Realiza la transaccion para la
	 * eliminacion de un usuario en caso de error hace rollback y actualiza el log
	 * del modulo de seguridad
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param logSecUser
	 * @return
	 */
	private boolean transactionDeleteUser(Long idUser, LogSecurityUser logSecUser) {
		return transactionTemplate.execute(transaction -> {
			try {
				return deleteUserRecord(idUser, logSecUser);
			} catch (RuntimeException e) {
				transaction.setRollbackOnly();
				throw new RuntimeException(getMessageSQLException(e));
			}
		});
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Elimina el registro del usuario en
	 * cascada en la BD
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param logSecUser
	 * @return
	 */
	private Boolean deleteUserRecord(Long idUser, LogSecurityUser logSecUser) {
		User user = userRepository.findById(idUser).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_USER_NOT_FOUND.getCodeMessage(idUser.toString())));
		userRepository.deleteById(idUser);
		setLogSecuritySuccesful(HttpStatus.NOT_FOUND.value(), logSecUser);
		HistoricalUtil.registerHistorical(user, HistoricalOperationEnum.DELETE, UserHistService.class);
		logSecurityUserRespository.save(logSecUser);
		return true;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Realiza la transaccion para la
	 * creacion de un usuario en caso de error hace rollback y actualiza el log del
	 * modulo de seguridad
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @param logSecUser
	 * @param person
	 * @return
	 */
	private Long transactionCreateUser(CreateUserInDTO userInDTO, LogSecurityUser logSecUser, Person person) {
		return transactionTemplate.execute(transaction -> {
			try {
				return createUserRecord(userInDTO, person, logSecUser).getId();
			} catch (RuntimeException e) {
				transaction.setRollbackOnly();
				throw new IllegalArgumentException(getMessageSQLException(e));
			}
		});
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Realiza la transaccion para la
	 * actualizacion de un usuario en caso de error hace rollback y actualiza el log
	 * del modulo de seguridad
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param modUserInDTO
	 * @param logSecUser
	 * @throws SQLException
	 */
	private boolean transactionUpdateUser(Long idUser, ModUserInDTO modUserInDTO, LogSecurityUser logSecUser) {
		User user = userRepository.findById(idUser).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_USER_NOT_FOUND.getCodeMessage(idUser.toString())));
		// Se da inicio a la transccion de actualizacion
		return transactionTemplate.execute(transaction -> {
			try {
				updateUserRecord(user, idUser, modUserInDTO, logSecUser);
				return true;
			} catch (RuntimeException e) {
				transaction.setRollbackOnly();
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * <p>
	 * <b> CU001_XX </b> CU001_Seguridad_Creación_Usuario </b> Realiza la
	 * actualizacion de la persona y el usuario en la BD
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param user
	 * @param idUser
	 * @param modUserInDTO
	 * @param logSecUser
	 */
	private void updateUserRecord(User user, Long idUser, ModUserInDTO modUserInDTO, LogSecurityUser logSecUser) {
		Person person = user.getPerson();
		updateLoguin(user, modUserInDTO.getUsername(), modUserInDTO.getPassword());
		user.setPerson(person);
		ObjectUpdaterUtil.updateDataEqualObject(getPerson(modUserInDTO.getPerson()), person);
		updateLoguin(user, modUserInDTO.getUsername(), modUserInDTO.getPassword());
		user.setPerson(person);
		user.setUserModification(logSecUser.getUserCreation());
		user.setModificationDate(LocalDateTime.now());
		userRepository.save(user);
		HistoricalUtil.registerHistorical(user, HistoricalOperationEnum.UPDATE, UserHistService.class);
		setLogSecuritySuccesful(HttpStatus.NO_CONTENT.value(), logSecUser);
		logSecurityUserRespository.save(logSecUser);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Actualizacion de credenciales de
	 * acceso el usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param user
	 * @param username
	 * @param password
	 */
	private void updateLoguin(User user, String username, String password) {
		if (username != null && !username.equals(user.getUsername())) {
			user.setUsername(username);
		}
		if (password != null && !password.equals(user.getPassword())) {
			user.setPassword(password);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Recuperacion del usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param request
	 * @param principal
	 * @return
	 */
	public UserOutDTO findByIdUser(Long idUser, HttpServletRequest request, Principal principal) {
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			User user = userRepository.findById(idUser).orElseThrow(
					() -> new EntityNotFoundException(SecurityExceptionEnum.FUNC_USER_NOT_FOUND.getCodeMessage()));
			setLogSecuritySuccesful(HttpStatus.OK.value(), logSecUser);
			logSecurityUserRespository.save(logSecUser);
			return getUserOutDTO(user);
		} catch (EntityNotFoundException e) {
			setLogSecurityError(e, logSecUser);
			throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
		}

	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Recupera la entidad user con su id
	 * en caso de que no exista retorna null
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @return
	 */
	public User findUserByIdPerson(Long idPerson) {
		return userRepository.finByIdPerson(idPerson);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Recuperación de todos los usuarios
	 * paginados, esto solo debe ser accedido por rol admin
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pageable
	 * @return
	 */
	public PageUserDTO findAll(Pageable pageable, HttpServletRequest request, Principal principal) {
		LogSecurityUser logSecUser = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY);
		try {
			// Conteo paginacion en 1
			int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
			// Consulta paginada y ordenamiento
			Page<User> pageUser = userRepository.findAll(PageRequest.of(pageNumber, pageable.getPageSize(),
					pageable.getSortOr(Sort.by(Sort.Direction.ASC, UserEnum.USER_PERSON_FIRST_NAME.getMessageKey())
							.and(Sort.by(Sort.Direction.ASC, UserEnum.USER_PERSON_NAME.getMessageKey())))));
			return getFindAll(pageUser, logSecUser);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecUser);
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecUser);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Obtiene la lista de usuarios en
	 * caso de ser vacia excepciona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pagePerson
	 * @return
	 */
	private PageUserDTO getFindAll(Page<User> pageUser, LogSecurityUser logSecUser) {
		List<UserOutDTO> listUser = pageUser.stream().map(user -> getUserOutDTO(user)).collect(Collectors.toList());

		if (listUser.isEmpty()) {
			throw new EntityNotFoundException(SecurityExceptionEnum.FUNC_USER_NOT_FOUND.getCodeMessage());
		}
		setLogSecuritySuccesful(HttpStatus.OK.value(), logSecUser);
		logSecurityUserRespository.save(logSecUser);
		return new PageUserDTO(listUser, pageUser.getTotalElements(), pageUser.getTotalPages(),
				pageUser.getNumber() + 1);
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
	 * <b> Util </b> genera una nueva instancia de persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @return
	 */
	private Person getPerson(PersonDTO personDTO) {
		Person person = new Person();
		person.setName(personDTO.getName());
		person.setFirstUsurname(personDTO.getFirstUsurname());
		person.setEmail(personDTO.getEmail());
		return person;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> XXX
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @param person
	 * @param logSecUser
	 * @return
	 */
	private User createUserRecord(CreateUserInDTO userInDTO, Person person, LogSecurityUser logSecUser) {
		User user = new User();
		// Crear enun con los estados
		UserState userState = userStateRepository.findAll().stream()
				.filter(state -> UserStateEnum.ENABLE.equals(state.getNameState())).collect(Collectors.toList()).get(0);
		user.setUsername(userInDTO.getUsername());
		user.setPassword(CryptoUtil.encryptPassword(userInDTO.getPassword()));
		user.setUserCreation(logSecUser.getUserCreation());
		user.setUserState(userState);
		user.setPerson(person);
		user.setCreationDate(LocalDateTime.now());
		user = userRepository.save(user);
		HistoricalUtil.registerHistorical(user, HistoricalOperationEnum.INSERT, UserHistService.class);
		setLogSecuritySuccesful(HttpStatus.CREATED.value(), logSecUser);
		logSecurityUserRespository.save(logSecUser);
		return user;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> DTO de salida para las solicitudes
	 * de busqueda
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private UserOutDTO getUserOutDTO(User user) {
		UserOutDTO userOutDTO = new UserOutDTO();
		PersonDTO personDTO = new PersonDTO();
		personDTO.setEmail(user.getPerson().getEmail());
		personDTO.setFirstUsurname(user.getPerson().getFirstUsurname());
		personDTO.setName(user.getPerson().getName());
		userOutDTO.setUsername(user.getUsername());
		userOutDTO.setState(user.getUserState().getNameState().name());
		userOutDTO.setPerson(personDTO);
		return userOutDTO;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Publicacion de los eventos crear y
	 * buscar persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @param principal
	 * @return
	 */
	private Person createPersonEvent(CreateUserInDTO userInDTO, Principal principal) {
		// Se invoca el servicio de creación de la persona
		PersonCreatedEvent createEvent = new PersonCreatedEvent(this, userInDTO.getPerson(), principal);
		eventPublisher.publishEvent(createEvent);
		Long idPerson = createEvent.getIdPerson();
		// Publicar el evento para buscar la persona creada
		FindPersonByIdEvent findEvent = new FindPersonByIdEvent(this, idPerson);
		eventPublisher.publishEvent(findEvent);
		return findEvent.getPerson();
	}

}
