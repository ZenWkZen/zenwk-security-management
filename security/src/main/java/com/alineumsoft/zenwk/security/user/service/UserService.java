package com.alineumsoft.zenwk.security.user.service;

import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.user.common.ApiRestHelper;
import com.alineumsoft.zenwk.security.user.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.user.common.exception.TechnicalException;
import com.alineumsoft.zenwk.security.user.common.util.ObjectUpdaterUtil;
import com.alineumsoft.zenwk.security.user.dto.CreateUserInDTO;
import com.alineumsoft.zenwk.security.user.dto.ModUserInDTO;
import com.alineumsoft.zenwk.security.user.dto.PageUserDTO;
import com.alineumsoft.zenwk.security.user.dto.PersonDTO;
import com.alineumsoft.zenwk.security.user.dto.UserOutDTO;
import com.alineumsoft.zenwk.security.user.entity.LogSecurityUser;
import com.alineumsoft.zenwk.security.user.entity.Person;
import com.alineumsoft.zenwk.security.user.entity.User;
import com.alineumsoft.zenwk.security.user.entity.UserState;
import com.alineumsoft.zenwk.security.user.enums.UserCoreExceptionEnum;
import com.alineumsoft.zenwk.security.user.enums.UserEnum;
import com.alineumsoft.zenwk.security.user.enums.UserStateEnum;
import com.alineumsoft.zenwk.security.user.repository.LogSecurityUserRespository;
import com.alineumsoft.zenwk.security.user.repository.PersonRepository;
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
public class UserService extends ApiRestHelper {
	private final UserRepository userRepository;

	private final PersonRepository personRepository;

	private final UserStateRepository UserStateRepository;

	private final LogSecurityUserRespository logRepository;

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Constructor
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userRepository
	 * @param personRepository
	 * @param UserStateRepository
	 * @param logRepository
	 */
	public UserService(UserRepository userRepository, PersonRepository personRepository,
			UserStateRepository UserStateRepository, LogSecurityUserRespository logRepository) {
		this.userRepository = userRepository;
		this.personRepository = personRepository;
		this.UserStateRepository = UserStateRepository;
		this.logRepository = logRepository;
	}

	/**
	 * 
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Cracion/modificacion nuevo usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @return
	 * @throws JsonProcessingException
	 */
	public Long createNewUser(CreateUserInDTO userInDTO, HttpServletRequest request) throws JsonProcessingException {
		LogSecurityUser logSecUser = getLogSecurityUser(request, null);
		logSecUser.setStatusCode(HttpStatus.CREATED.value());
		logSecUser.setResponse(CommonMessageConstants.NOT_APPLICABLE_BODY);
		logSecUser.setRequest(getJson(userInDTO));
		try {
			Person person = createPerson(userInDTO);
			User user = createUser(userInDTO, person);
			return user.getId();
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			throw new TechnicalException(e.getMessage(), null, e.getCause(), logRepository, logSecUser);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Actualizacion general de usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @param userOutDTO
	 * @throws JsonProcessingException
	 */
	public boolean updateUser(HttpServletRequest request, Long idUser, ModUserInDTO modUserInDTO)
			throws JsonProcessingException {
		LogSecurityUser logSecUser = getLogSecurityUser(request, null);
		// Respuesta por defecto
		logSecUser.setResponse(CommonMessageConstants.NOT_APPLICABLE_BODY);
		logSecUser.setStatusCode(HttpStatus.NOT_FOUND.value());
		logSecUser.setRequest(getJson(modUserInDTO));
		try {
			updateUserEntity(idUser, modUserInDTO);
			logSecUser.setStatusCode(HttpStatus.NO_CONTENT.value());
			logSecUser.setErroMessage(CommonMessageConstants.REQUEST_SUCCESSFUL);
			logRepository.save(logSecUser);
			return true;
		} catch (IllegalArgumentException | SQLException e) {
			logSecUser.setErroMessage(e.getMessage());
			// En TechnicalException se persiste en el log de errores
			throw new TechnicalException(e.getMessage(), null, e.getCause(), logRepository, logSecUser);
		} catch (EntityNotFoundException ex) {
			UserCoreExceptionEnum errorEnum = UserCoreExceptionEnum.FUNC_USER_NOT_FOUND;
			logSecUser.setErroMessage(errorEnum.getCodeMessage());
			throw new TechnicalException(errorEnum.getMessage(), errorEnum.getCode(), ex.getCause(), logRepository,
					logSecUser);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Persiste la actualizacion del
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param modUserInDTO
	 * @throws SQLException
	 */
	private void updateUserEntity(Long idUser, ModUserInDTO modUserInDTO) throws SQLException {
		try {
			User user = userRepository.findById(idUser).orElseThrow(() -> new EntityNotFoundException());
			Person person = user.getPerson();
			ObjectUpdaterUtil.updateDataEqualObject(getPerson(modUserInDTO.getPerson()), person);
			updateLoguin(user, modUserInDTO.getUsername(), modUserInDTO.getPassword());
			user.setPerson(person);
			user.setUserModification(user.getUsername());
			userRepository.save(user);
		} catch (RuntimeException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Actualizacion de credenciales de
	 * acceso
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
	public boolean deleteUser(Long idUser, HttpServletRequest request, Principal principal)
			throws JsonProcessingException {
		LogSecurityUser logSecUser = getLogSecurityUser(request, principal);
		logSecUser.setResponse(CommonMessageConstants.NOT_APPLICABLE_BODY);
		logSecUser.setRequest(CommonMessageConstants.NOT_APPLICABLE_BODY);

		if (userRepository.existsById(idUser)) {
			userRepository.deleteById(idUser);
			logSecUser.setStatusCode(HttpStatus.NO_CONTENT.value());
			logSecUser.setErroMessage(CommonMessageConstants.REQUEST_SUCCESSFUL);
			logRepository.save(logSecUser);
			return true;
		}
		UserCoreExceptionEnum errorEnum = UserCoreExceptionEnum.FUNC_USER_NOT_FOUND;
		logSecUser.setStatusCode(HttpStatus.NOT_FOUND.value());
		logSecUser.setErroMessage(errorEnum.getCodeMessage());
		throw new TechnicalException(errorEnum.getMessage(), errorEnum.getCode(), null, logRepository, logSecUser);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Recuperacion de usuario sin
	 * seguridad
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @return
	 */
	public UserOutDTO findByIdUser(Long idUser) {
		User user = userRepository.findById(idUser).orElse(null);
		return user.getUserOutDTO();

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
	public PageUserDTO findAll(Pageable pageable) {
		// Conteo paginacion en 1
		int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
		// Consulta paginada
		Page<User> pageUser = userRepository.findAll(PageRequest.of(pageNumber, pageable.getPageSize(),
				pageable.getSortOr(Sort.by(Sort.Direction.ASC, UserEnum.PERSON_FIRST_NAME.getMessageKey())
						.and(Sort.by(Sort.Direction.ASC, UserEnum.PERSON_NAME.getMessageKey())))));
		// Lista de usuario
		List<UserOutDTO> listUser = pageUser.stream().map(User::getUserOutDTO).collect(Collectors.toList());

		return new PageUserDTO(listUser, pageUser.getTotalElements(), pageUser.getTotalPages(),
				pageUser.getNumber() + 1);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Creación persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @return
	 */
	private Person createPerson(CreateUserInDTO userInDTO) {
		Person person = getPerson(userInDTO.getPerson());
		person.setUserCreation(userInDTO.getUsername());
		personRepository.save(person);
		return person;
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
	 * <b> CU001_Seguridad_Creación_Usuario </b> Creación usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @param person
	 * @return
	 */
	private User createUser(CreateUserInDTO userInDTO, Person person) {
		User user = new User();
		// Crear enun con los estados
		UserState userState = UserStateRepository.findAll().stream()
				.filter(state -> UserStateEnum.ENABLE.equals(state.getNameState())).collect(Collectors.toList()).get(0);

		user.setUsername(userInDTO.getUsername());
		user.setPassword(userInDTO.getPassword());
		user.setUserCreation(userInDTO.getUsername());
		user.setUserState(userState);
		user.setPerson(person);
		return userRepository.save(user);
	}

	/**
	 * <p>
	 * <b> General </b> Fija el valor para los atributos: creationDate, userCreation
	 * y url
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param principal
	 * @throws JsonProcessingException
	 */
	private LogSecurityUser getLogSecurityUser(HttpServletRequest request, Principal principal) {
		LogSecurityUser regLog = new LogSecurityUser();
		regLog.setCreationDate(LocalDateTime.now());
		regLog.setUserCreation(null);
		regLog.setUrl(request.getRequestURL().toString());
		return regLog;
	}

}
