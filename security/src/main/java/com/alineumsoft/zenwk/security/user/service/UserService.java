package com.alineumsoft.zenwk.security.user.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.alineumsoft.zenwk.security.auth.Service.RoleService;
import com.alineumsoft.zenwk.security.auth.dto.RoleUserDTO;
import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.constants.GeneralConstants;
import com.alineumsoft.zenwk.security.common.exception.FunctionalException;
import com.alineumsoft.zenwk.security.common.exception.TechnicalException;
import com.alineumsoft.zenwk.security.common.exception.enums.CoreExceptionEnum;
import com.alineumsoft.zenwk.security.common.hist.enums.HistoricalOperationEnum;
import com.alineumsoft.zenwk.security.common.util.CryptoUtil;
import com.alineumsoft.zenwk.security.common.util.HistoricalUtil;
import com.alineumsoft.zenwk.security.common.util.ObjectUpdaterUtil;
import com.alineumsoft.zenwk.security.constants.ServiceControllerConstants;
import com.alineumsoft.zenwk.security.entity.LogSecurity;
import com.alineumsoft.zenwk.security.entity.Role;
import com.alineumsoft.zenwk.security.entity.RoleUser;
import com.alineumsoft.zenwk.security.enums.RoleEnum;
import com.alineumsoft.zenwk.security.enums.SecurityActionEnum;
import com.alineumsoft.zenwk.security.enums.SecurityExceptionEnum;
import com.alineumsoft.zenwk.security.enums.UserPersonEnum;
import com.alineumsoft.zenwk.security.enums.UserStateEnum;
import com.alineumsoft.zenwk.security.helper.ApiRestSecurityHelper;
import com.alineumsoft.zenwk.security.person.entity.Person;
import com.alineumsoft.zenwk.security.person.event.PersonDeleteEvent;
import com.alineumsoft.zenwk.security.repository.LogSecurityRepository;
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
	private final LogSecurityRepository logSecurityUserRespository;
	/**
	 * Template para transaccion
	 */
	private final TransactionTemplate templateTx;
	/**
	 * Interfaz para publicacion de evento
	 */
	private final ApplicationEventPublisher eventPublisher;
	/**
	 * Servicio para RolUser
	 */
	private final RoleService roleService;

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
	 * @param roleService
	 */
	public UserService(UserRepository userRepository, LogSecurityRepository logRepository,
			TransactionTemplate transationTemplate, ApplicationEventPublisher eventPublisher, RoleService roleService) {
		this.userRepository = userRepository;
		this.logSecurityUserRespository = logRepository;
		this.templateTx = transationTemplate;
		this.eventPublisher = eventPublisher;
		this.roleService = roleService;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Metodo de servicio que crea un
	 * nuevo usuario si cumple con las validaciones
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param dto
	 * @param userDetails
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	public Long createUser(UserDTO dto, HttpServletRequest request, UserDetails userDetails) {
		// inicializacion log transaccional
		LogSecurity logSecurity = initializeLog(request, dto.getUsername(), getJson(dto),
				CommonMessageConstants.NOT_APPLICABLE_BODY, SecurityActionEnum.USER_CREATE.getCode());
		try {
			return createUserTx(dto, logSecurity);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity);
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
	 * @param userDetails
	 * @oaram person
	 */
	public boolean updateUser(HttpServletRequest request, Long idUser, UserDTO dto, UserDetails userDetails,
			Person person) {
		// Inicializacion log transaccional
		LogSecurity logSecurity = initializeLog(request, userDetails.getUsername(), getJson(dto),
				CommonMessageConstants.NOT_APPLICABLE_BODY, SecurityActionEnum.USER_UPDATE.getCode());
		try {
			return updateUserTx(idUser, dto, person, logSecurity);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity);
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
	 * @param request
	 * @param userDetails
	 * @return
	 * @throws JsonProcessingException
	 */
	public boolean deleteUser(Long idUser, HttpServletRequest request, UserDetails userDetails) {
		LogSecurity logSecurity = initializeLog(request, userDetails.getUsername(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityActionEnum.USER_DELETE.getCode());
		try {
			// request = null, se carga en PersonEventListener.handlePersonDeleteEvent()
			return deleteUserTx(idUser, logSecurity, request != null, userDetails);
		} catch (RuntimeException e) {
			setLogSecurityError(e, logSecurity);
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
	 * @param userDetails
	 * @param invokedByListener
	 * @return
	 */
	private boolean deleteUserTx(Long idUser, LogSecurity logSecurity, boolean invokedByListener,
			UserDetails userDetails) {
		// Se valida si es necesario eliminar la relacion con Person
		if (invokedByListener) {
			deletePersonEvent(userDetails, idUser);
		}
		// Se recupera el usuario.
		User user = userRepository.findById(idUser).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_USER_NOT_FOUND_ID.getCodeMessage(idUser.toString())));
		// Se ejecuta la transaccion
		return templateTx.execute(transaction -> {
			try {
				return deleteUserRecord(idUser, logSecurity, user, userDetails);
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
	 * @param userDetails
	 * @return
	 */
	private Boolean deleteUserRecord(Long idUser, LogSecurity logSecurity, User user, UserDetails userDetails) {
		// Eliminacion de los permisos del usuario
		roleService.deleteRoleUserByIdUser(idUser);
		userRepository.deleteById(idUser);
		// Registro de logs
		setLogSecuritySuccesfull(HttpStatus.NO_CONTENT.value(), logSecurity);
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
	 * 
	 * @param person
	 * @return
	 */
	private Long createUserTx(UserDTO dto, LogSecurity logSecurity) {
		return templateTx.execute(transaction -> {
			try {
				return createUserRecord(dto, logSecurity).getId();
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
	 * @return
	 * @throws SQLException
	 */
	private boolean updateUserTx(Long idUser, UserDTO dto, Person person, LogSecurity logSecurity) {
		User user = userRepository.findById(idUser).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_USER_NOT_FOUND_ID.getCodeMessage(idUser.toString())));
		// Se da inicio a la transccion de actualizacion
		return templateTx.execute(transaction -> {
			try {
				updateUserRecord(user, idUser, dto, person, logSecurity);
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
	 */
	private void updateUserRecord(User user, Long idUser, UserDTO dto, Person person, LogSecurity logSecurity) {
		// actualizacion de la persona, el estado y el rol
		if (person != null) {
			RoleUserDTO roleUserDTO = new RoleUserDTO(null, RoleEnum.USER.name(), idUser, user.getUsername());
			user.setPerson(person);
			user.setState(UserStateEnum.ACTIVE);
			// Rol por defecto
			roleService.createRoleUser(null, roleUserDTO);
		}

		ObjectUpdaterUtil.updateDataEqualObject(getUser(dto), user);
		user.setUserModification(logSecurity.getUserCreation());
		user.setModificationDate(LocalDateTime.now());
		userRepository.save(user);
		// Registro en log
		HistoricalUtil.registerHistorical(user, HistoricalOperationEnum.UPDATE, UserHistService.class);
		setLogSecuritySuccesfull(HttpStatus.NO_CONTENT.value(), logSecurity);
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
	 * @param userDetails
	 * @return
	 */
	public UserDTO findByIdUser(Long idUser, HttpServletRequest request, UserDetails userDetails) {
		LogSecurity logSecurity = initializeLog(request, userDetails.getUsername(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityActionEnum.USER_FIND_BY_ID.getCode());
		try {
			User user = userRepository.findById(idUser).orElseThrow(() -> new EntityNotFoundException(
					SecurityExceptionEnum.FUNC_USER_NOT_FOUND_ID.getCodeMessage(idUser.toString())));
			setLogSecuritySuccesfull(HttpStatus.OK.value(), logSecurity);
			logSecurityUserRespository.save(logSecurity);
			// se oculta el password
			user.setPassword(GeneralConstants.VALUE_SENSITY_MASK);
			return new UserDTO(user);
		} catch (EntityNotFoundException e) {
			setLogSecurityError(e, logSecurity);
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
	 * @param request
	 * @param userDetails
	 * @return
	 */
	public PageUserDTO findAll(Pageable pageable, HttpServletRequest request, UserDetails userDetails) {
		LogSecurity logSecurity = initializeLog(request, userDetails.getUsername(),
				CommonMessageConstants.NOT_APPLICABLE_BODY, CommonMessageConstants.NOT_APPLICABLE_BODY,
				SecurityActionEnum.USER_FIND_ALL.getCode());
		try {
			// Conteo paginacion en 1
			int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
			// Consulta paginada y ordenamiento
			Page<User> pageUser = userRepository.findAll(PageRequest.of(pageNumber, pageable.getPageSize(),
					pageable.getSortOr(Sort.by(Sort.Direction.ASC, UserPersonEnum.USER_EMAIL.getMessageKey())
							.and(Sort.by(Sort.Direction.ASC, UserPersonEnum.USER_USERNAME.getMessageKey())))));
			return getFindAll(pageUser, logSecurity);
		} catch (RuntimeException e) {
			log.error(CommonMessageConstants.LOG_MSG_EXCEPTION, e);
			setLogSecurityError(e, logSecurity);
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
	 * @param pagePerson
	 * @return
	 */
	private PageUserDTO getFindAll(Page<User> pageUser, LogSecurity logSecurity) {
		List<UserDTO> listUser = pageUser.stream().map(user -> new UserDTO(user))
				.peek(user -> user.setPassword(GeneralConstants.VALUE_SENSITY_MASK)).collect(Collectors.toList());
		setLogSecuritySuccesfull(HttpStatus.OK.value(), logSecurity);
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
		if (e.getMessage().contains(ServiceControllerConstants.SQL_MESSAGE_EMAIL_EXISTS)) {
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
	 * @return
	 */
	private User createUserRecord(UserDTO dto, LogSecurity logSecurity) {
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
			setLogSecuritySuccesfull(HttpStatus.CREATED.value(), logSecurity);
			logSecurityUserRespository.save(logSecurity);
			// Se crea rol por defecto cuando apenas se crea el usuario
			RoleUserDTO roleUserDTO = new RoleUserDTO(null, RoleEnum.NEW_USER.name(), user.getId(), dto.getUsername());
			roleService.createRoleUser(null, roleUserDTO);
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
	 * @param userDetails
	 */
	private void deletePersonEvent(UserDetails userDetails, Long idUser) {
		Object idPerson = userRepository.findIdPersonByIdUser(idUser);
		if (idPerson != null) {
			PersonDeleteEvent event = new PersonDeleteEvent(this, Long.parseLong(idPerson.toString()), userDetails);
			eventPublisher.publishEvent(event);
		}
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

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Recupera la entidad rol del usuario
	 * si tiene un rol asignado asociada
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param username
	 * @param httpRequest
	 * @return
	 */
	public List<Role> findRolesByUsername(String username, HttpServletRequest httpRequest) {
		try {
			// se obtienen los roles del usuario
			return getRolesForUser(username);
		} catch (EntityNotFoundException e) {
			// inicializacion log transaccional
			LogSecurity logSecurity = initializeLog(null, username, CommonMessageConstants.NOT_APPLICABLE_BODY,
					CommonMessageConstants.NOT_APPLICABLE_BODY, SecurityActionEnum.USER_FIND_ROLE.getCode());
			logSecurity.setStatusCode(HttpStatus.FORBIDDEN.value());
			logSecurity.setExecutionTime(getExecutionTime());
			// Se lanza excepcion funcional
			throw new FunctionalException(e.getMessage(), e.getCause(), logSecurityUserRespository, logSecurity);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Obtien los roles del usuario si
	 * existe en caso contrario guarda un log con la excepcion generada
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param username
	 * @return
	 */
	private List<Role> getRolesForUser(String username) {
		// Consulta del usuario desde el campo username
		User user = findByUsername(username);
		// Se valida el estado del usuario.
		if (UserStateEnum.INCOMPLETE_PERFIL.equals(user.getState())) {
			// Se retorna el rol temporal new user
			return getRoleNewUser();
		} else {
			// Consulta que roles estan asignados al usuario
			List<RoleUser> rolesUser = findRolesUserByUser(user);
			// Consulta los roles del usuario
			return findRolesByRolesUser(rolesUser);
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Se genera una lista con el rol
	 * temporal new_user
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	private List<Role> getRoleNewUser() {
		Role rolTemp = roleService.findRoleByName(RoleEnum.NEW_USER).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_ROLE_USER_NOT_EXIST.getCodeMessage(RoleEnum.NEW_USER.name())));
		return Arrays.asList(rolTemp);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Consulta los roles del usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param rolesUser
	 * @return
	 */
	private List<Role> findRolesByRolesUser(List<RoleUser> rolesUser) {
		List<Long> idsRoles = rolesUser.stream().map(roleUser -> roleUser.getRole().getId())
				.collect(Collectors.toList());
		List<Role> roles = roleService.findRoleByIds(idsRoles);
		if (roles == null || roles.isEmpty()) {
			throw new EntityNotFoundException(CoreExceptionEnum.FUNC_COMMON_ROLE_NOT_EXIST.getCodeMessage());
		}
		return roles;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Consulta los registros de la tabla
	 * sec_role_user asociados al del usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param user
	 * @return
	 */
	public List<RoleUser> findRolesUserByUser(User user) {
		List<RoleUser> rolesUser = roleService.findRoleUserByIdUser(user.getId());
		if (rolesUser == null || rolesUser.isEmpty()) {
			throw new EntityNotFoundException(
					SecurityExceptionEnum.FUNC_ROLE_USER_NOT_EXIST.getCodeMessage(user.getUsername()));
		}
		return rolesUser;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Recupera la entidad user con el
	 * username si tiene un rol asignado asociada
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param username
	 * @return
	 */
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(
				SecurityExceptionEnum.FUNC_USER_NOT_FOUND_USERNAME.getCodeMessage(username)));
	}

}
