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
import com.alineumsoft.zenwk.security.constants.SecurityConstants;
import com.alineumsoft.zenwk.security.entity.LogSecurity;
import com.alineumsoft.zenwk.security.enums.SecurityExceptionEnum;
import com.alineumsoft.zenwk.security.enums.SecurityServiceNameEnum;
import com.alineumsoft.zenwk.security.enums.UserPersonEnum;
import com.alineumsoft.zenwk.security.enums.UserStateEnum;
import com.alineumsoft.zenwk.security.helper.ApiRestSecurityHelper;
import com.alineumsoft.zenwk.security.person.entity.Person;
import com.alineumsoft.zenwk.security.person.event.PersonDeleteEvent;
import com.alineumsoft.zenwk.security.repository.LogSecurityRespository;
import com.alineumsoft.zenwk.security.user.dto.PageUserDTO;
import com.alineumsoft.zenwk.security.user.dto.UserDTO;
import com.alineumsoft.zenwk.security.user.entity.User;
import com.alineumsoft.zenwk.security.user.repository.UserRepository;
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
	/**
	 * Repositorio para user
	 */
	private final UserRepository userRepository;
	/**
	 * Repositorio para log persistible de modulo
	 */
	private final LogSecurityRespository logSecurityUserRespository;
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
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userRepository
	 * @param logRepository
	 * @param transationTemplate
	 * @param eventPublisher
	 */
	public UserService(UserRepository userRepository, LogSecurityRespository logRepository,
			TransactionTemplate transationTemplate, ApplicationEventPublisher eventPublisher) {
		this.userRepository = userRepository;
		this.logSecurityUserRespository = logRepository;
		this.templateTx = transationTemplate;
		this.eventPublisher = eventPublisher;
	}

	/*
	 * 
	 * <p> <b> CU001_Seguridad_Creacion_Usuario </b> Metodo de servicio que crea un
	 * nuevo usuario si cumple con las validaciones </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * 
	 * @param dto
	 * 
	 * @param principal
	 * 
	 * @param startTime
	 * 
	 * @return
	 * 
	 * @throws JsonProcessingException
	 */
	public Long createUser(UserDTO dto, HttpServletRequest request, Principal principal, Long startTime) {
		// inicializacion log transaccional
		LogSecurity logSecurity = initializeLog(request, principal.getName(), getJson(dto),
				CommonMessageConstants.NOT_APPLICABLE_BODY, SecurityServiceNameEnum.USER_CREATE.getCode());
		try {
			return createUserTx(dto, logSecurity, startTime);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, startTime);
			if (isFunctionalException(e)) {
				throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
			}
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Actualizacion general de usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param idUser
	 * @param dto
	 * @param principal
	 * @param starTime
	 * @oaram person
	 */
	public boolean updateUser(HttpServletRequest request, Long idUser, UserDTO dto, Principal principal, Person person,
			Long starTime) {
		// Inicializacion log transaccional
		LogSecurity logSecurity = initializeLog(request, principal.getName(), getJson(dto),
				CommonMessageConstants.NOT_APPLICABLE_BODY, SecurityServiceNameEnum.USER_UPDATE.getCode());
		try {
			return updateUserTx(idUser, dto, person, logSecurity, starTime);
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
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Se realiza un borrado fisico del
	 * usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param principal
	 * @param request
	 * @param starTime
	 * @return
	 * @throws JsonProcessingException
	 */
	public boolean deleteUser(Long idUser, HttpServletRequest request, Principal principal, Long starTime) {
		LogSecurity logSecurity = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityServiceNameEnum.USER_DELETE.getCode());

		try {
			return deleteUserTx(idUser, logSecurity, request != null, principal, starTime);
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
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Realiza la transaccion para la
	 * eliminacion de un usuario en caso de error hace rollback y actualiza el log
	 * del modulo de seguridad
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @param logSecurity
	 * @param principal
	 * @param starTime
	 * @return
	 */
	private boolean deleteUserTx(Long idUser, LogSecurity logSecurity, boolean isDeletePerson, Principal principal,
			Long starTime) {
		User user = userRepository.findById(idUser).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_USER_NOT_FOUND.getCodeMessage(idUser.toString())));

		if (isDeletePerson && user.getPerson() != null) {
			deletePersonEvent(user.getPerson().getId(), principal);
		}

		return templateTx.execute(transaction -> {
			try {
				return deleteUserRecord(idUser, logSecurity, user, starTime);
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
	 * @param logSecurity
	 * @param user
	 * @param starTime
	 * @return
	 */
	private Boolean deleteUserRecord(Long idUser, LogSecurity logSecurity, User user, Long starTime) {
		userRepository.deleteById(idUser);
		// Registro de logs
		setLogSecuritySuccesfull(HttpStatus.NO_CONTENT.value(), logSecurity, starTime);
		HistoricalUtil.registerHistorical(user, HistoricalOperationEnum.DELETE, UserHistService.class);
		logSecurityUserRespository.save(logSecurity);
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
	 * @param dto
	 * @param logSecurity
	 * @param startTime
	 * @param person
	 * @return
	 */
	private Long createUserTx(UserDTO dto, LogSecurity logSecurity, Long startTime) {
		return templateTx.execute(transaction -> {
			try {
				return createUserRecord(dto, logSecurity, startTime).getId();
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
	 * @param dto
	 * @param person
	 * @param logSecurity
	 * @param starTime
	 * @return
	 * @throws SQLException
	 */
	private boolean updateUserTx(Long idUser, UserDTO dto, Person person, LogSecurity logSecurity, Long starTime) {
		User user = userRepository.findById(idUser).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_USER_NOT_FOUND.getCodeMessage(idUser.toString())));
		// Se da inicio a la transccion de actualizacion
		return templateTx.execute(transaction -> {
			try {
				updateUserRecord(user, idUser, dto, person, logSecurity, starTime);
				return true;
			} catch (RuntimeException e) {
				transaction.setRollbackOnly();
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Realiza la actualizacion de la
	 * persona y el usuario en la BD
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param user
	 * @param idUser
	 * @param dto
	 * @param person
	 * @param logSecurity
	 * @param starTime
	 */
	private void updateUserRecord(User user, Long idUser, UserDTO dto, Person person, LogSecurity logSecurity,
			Long starTime) {
		// actualizacion del usuario desde api creacion de persona
		if (person != null) {
			user.setPerson(person);
			user.setState(UserStateEnum.ACTIVE);
		}

		ObjectUpdaterUtil.updateDataEqualObject(getUser(dto), user);
		user.setUserModification(logSecurity.getUserCreation());
		user.setModificationDate(LocalDateTime.now());
		userRepository.save(user);
		// Registro en log
		HistoricalUtil.registerHistorical(user, HistoricalOperationEnum.UPDATE, UserHistService.class);
		setLogSecuritySuccesfull(HttpStatus.NO_CONTENT.value(), logSecurity, starTime);
		logSecurityUserRespository.save(logSecurity);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Obtiene un User desde su dto
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param dto
	 * @return
	 */
	private User getUser(UserDTO dto) {
		User user = new User();
		user.setUsername(dto.getUsername());
		user.setPassword(CryptoUtil.encryptPassword(dto.getPassword()));
		user.setEmail(dto.getEmail());
		user.setState(dto.getState());
		return user;
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
	 * @param long1
	 * @return
	 */
	public UserDTO findByIdUser(Long idUser, HttpServletRequest request, Principal principal, Long starTime) {
		LogSecurity logSecurity = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityServiceNameEnum.USER_FIND_BY_ID.getCode());
		try {
			User user = userRepository.findById(idUser).orElseThrow(() -> new EntityNotFoundException(
					SecurityExceptionEnum.FUNC_USER_NOT_FOUND.getCodeMessage(idUser.toString())));
			setLogSecuritySuccesfull(HttpStatus.OK.value(), logSecurity, starTime);
			logSecurityUserRespository.save(logSecurity);
			return new UserDTO(user);
		} catch (EntityNotFoundException e) {
			setLogSecurityError(e, logSecurity, starTime);
			throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
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
	 * @param starTime
	 * @return
	 */
	public PageUserDTO findAll(Pageable pageable, HttpServletRequest request, Principal principal, Long starTime) {
		LogSecurity logSecurity = initializeLog(request, principal.getName(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityServiceNameEnum.USER_FIND_ALL.getCode());
		try {
			// Conteo paginacion en 1
			int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
			// Consulta paginada y ordenamiento
			Page<User> pageUser = userRepository.findAll(PageRequest.of(pageNumber, pageable.getPageSize(),
					pageable.getSortOr(Sort.by(Sort.Direction.ASC, UserPersonEnum.USER_EMAIL.getMessageKey())
							.and(Sort.by(Sort.Direction.ASC, UserPersonEnum.USER_USERNAME.getMessageKey())))));
			return getFindAll(pageUser, logSecurity, starTime);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity, starTime);
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Obtiene la lista de usuarios en
	 * caso de ser vacia excepciona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param starTime
	 * @param pagePerson
	 * @return
	 */
	private PageUserDTO getFindAll(Page<User> pageUser, LogSecurity logSecurity, Long starTime) {
		List<UserDTO> listUser = pageUser.stream().map(user -> new UserDTO(user)).collect(Collectors.toList());
		setLogSecuritySuccesfull(HttpStatus.OK.value(), logSecurity, starTime);
		logSecurityUserRespository.save(logSecurity);
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
		if (e.getMessage().contains(SecurityConstants.SQL_MESSAGE_EMAIL_EXISTS)) {
			return SecurityExceptionEnum.FUNC_USER_MAIL_EXISTS.getCodeMessage();
		}
		return e.getMessage();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Persistencia del registro user
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param dto
	 * @param logSecurity
	 * @param startTime
	 * @return
	 */
	private User createUserRecord(UserDTO dto, LogSecurity logSecurity, Long startTime) {
		User user = new User();
		user.setUsername(dto.getUsername());
		user.setPassword(CryptoUtil.encryptPassword(dto.getPassword()));
		user.setEmail(dto.getEmail());
		user.setUserCreation(logSecurity.getUserCreation());
		user.setState(UserStateEnum.INCOMPLETE_PERFIL);
		user.setCreationDate(LocalDateTime.now());
		// validacion si el usuario ya existe
		if (!isExistuser(user)) {
			user = userRepository.save(user);
			// Persistencia en log
			HistoricalUtil.registerHistorical(user, HistoricalOperationEnum.INSERT, UserHistService.class);
			setLogSecuritySuccesfull(HttpStatus.CREATED.value(), logSecurity, startTime);
			logSecurityUserRespository.save(logSecurity);
		}

		return user;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Evento que invoca la operacion
	 * elminar persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param id
	 * @param principal
	 */
	private void deletePersonEvent(Long id, Principal principal) {
		PersonDeleteEvent event = new PersonDeleteEvent(this, id, principal);
		eventPublisher.publishEvent(event);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Valida si existe el usuario por id
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @return
	 */
	public boolean existsById(Long idUser) {
		User user = userRepository.findById(idUser).orElse(null);
		return user != null && user.getId() > 0;
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
	private boolean isExistuser(User user) {
		if (userRepository.existsByUsernameAndEmail(user.getUsername(), user.getEmail())) {
			throw new IllegalArgumentException(SecurityExceptionEnum.FUNC_USER_EXIST.getCodeMessage());
		}
		return false;
	}

}
