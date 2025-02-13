package com.alineumsoft.zenwk.security.auth.Service;

import static com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants.LOG_MSG_EXCEPTION;
import static com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants.NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.auth.dto.RoleDTO;
import com.alineumsoft.zenwk.security.auth.dto.RoleUserDTO;
import com.alineumsoft.zenwk.security.auth.jwt.JwtProvider;
import com.alineumsoft.zenwk.security.common.exception.TechnicalException;
import com.alineumsoft.zenwk.security.common.exception.enums.CoreExceptionEnum;
import com.alineumsoft.zenwk.security.entity.LogSecurity;
import com.alineumsoft.zenwk.security.entity.Role;
import com.alineumsoft.zenwk.security.entity.RoleUser;
import com.alineumsoft.zenwk.security.enums.RoleEnum;
import com.alineumsoft.zenwk.security.helper.ApiRestSecurityHelper;
import com.alineumsoft.zenwk.security.repository.LogSecurityRepository;
import com.alineumsoft.zenwk.security.repository.RolUserRepository;
import com.alineumsoft.zenwk.security.repository.RoleRepository;
import com.alineumsoft.zenwk.security.user.entity.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Servicio para las gestion de los roles
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class RoleService
 */
@Slf4j
@Service
public class RoleService extends ApiRestSecurityHelper {
	/**
	 * Repositorio para Role
	 */
	private final RoleRepository rolRepo;
	/**
	 * Repositorio para la entidad que relaciona Role con User
	 */
	private final RolUserRepository rolUserRepo;
	/**
	 * Repositorio utilizado para el log
	 */
	private final LogSecurityRepository logSecRepo;
	/**
	 * Manejador de JWT
	 */
	private final JwtProvider jwtProvider;

	/**
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles </b> Constructor
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param rolRepo
	 * @param rolUserRepo
	 * @param logSecRepo
	 * @param jwtProvider
	 */
	public RoleService(RoleRepository rolRepo, RolUserRepository rolUserRepo, LogSecurityRepository logSecRepo,
			JwtProvider jwtProvider) {
		this.rolRepo = rolRepo;
		this.rolUserRepo = rolUserRepo;
		this.logSecRepo = logSecRepo;
		this.jwtProvider = jwtProvider;
	}

	/***
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles</b> Crea un rol si no existe para el
	 * usuario.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param roleDTO
	 */
	public Role createRole(HttpServletRequest request, RoleDTO roleDTO) {
		try {
			Role rol = convertToEntityRole(roleDTO);
			rol.setCreationDate(LocalDateTime.now());
			rol.setCreationUser(null);
			return rolRepo.save(rol);
		} catch (RuntimeException e) {
			log.info(LOG_MSG_EXCEPTION, e);
			String token = jwtProvider.extractToken(request).orElse(null);
			String username = jwtProvider.extractAllClaims(token).getSubject();
			LogSecurity logSec = initializeLog(request, username, NOT_FOUND, NOT_FOUND, NOT_FOUND);
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecRepo, logSec);
		}
	}

	/**
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles </b> Creacion del registro que relaciona
	 * el rol con el usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param dto
	 * @return
	 */
	public RoleUser createRoleUser(HttpServletRequest request, RoleUserDTO dto) {
		try {
			// Pendiente. implmentar metodo generico programacion defensiva a nivel de DTO's
			Role role = rolRepo.findByName(RoleEnum.valueOf(dto.getNameRole()))
					.orElseThrow(() -> new EntityNotFoundException(
							CoreExceptionEnum.FUNC_COMMON_ROLE_NOT_EXIST.getCodeMessage(dto.getNameRole())));
			dto.setIdRole(role.getId());
			deleteRolNewUser(dto.getIdUser());
			return rolUserRepo.save(convertToEntityRoleUser(dto));
		} catch (RuntimeException e) {
			log.info(LOG_MSG_EXCEPTION, e);
			LogSecurity logSec = initializeLog(request, dto.getUsername(), NOT_FOUND, NOT_FOUND, NOT_FOUND);
			throw new TechnicalException(e.getMessage(), e.getCause(), logSecRepo, logSec);
		}

	}

	/**
	 * 
	 * <p>
	 * <b> CU001_Seguridad_Asignación_Roles </b> Eliminacion de registros por id de
	 * usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param request
	 * @param idUser
	 * @return
	 */
	public boolean deleteRoleUserByIdUser(Long idUser) {
		try {
			User user = new User();
			user.setId(idUser);
			rolUserRepo.deleteByUser(user);
			return true;
		} catch (RuntimeException e) {
			throw new EntityNotFoundException(e.getMessage());
		}
	}

	/**
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles </b> Se obtienen todos los registros
	 * asociados al usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @return
	 */
	public List<RoleUser> findRoleUserByIdUser(Long idUser) {
		User user = new User();
		user.setId(idUser);
		return rolUserRepo.findByUser(user);
	}
	
	/**
	 * 
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles </b> Se obtienen todos los roles
	 * asociados a los ids
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idsRoles
	 * @return
	 */
	public List<Role> findRoleByIds(List<Long> idsRoles) {
		return rolRepo.findByIds(idsRoles);
	}
	
	/**
	 * 
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles </b> Se obtiene el rol por nombre
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param rolName
	 * @return
	 */
	public Optional<Role> findRoleByName(RoleEnum rolName) {
		return rolRepo.findByName(rolName);
	}

	/**
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles </b> Si ya existe un rol y este es
	 * NEW_USER lo elimina.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 */
	private void deleteRolNewUser(Long idUser) {
		try {
			User user = new User();
			user.setId(idUser);
			List<RoleUser> listRoleUser = rolUserRepo.findByUser(user);
			listRoleUser = listRoleUser.stream()
					.filter(rolUser -> RoleEnum.NEW_USER.equals(rolUser.getRole().getName()))
					.collect(Collectors.toList());
			if (!listRoleUser.isEmpty() && listRoleUser.size() == 1) {
				rolUserRepo.delete(listRoleUser.get(0));
			}
		} catch (Exception e) {
			throw new IllegalAccessError();
		}
	}

	/**
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles </b> Obtiene una entidad Role a partir
	 * de su DTO
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param dto
	 * @return
	 */
	private Role convertToEntityRole(RoleDTO dto) {
		Role role = new Role();
		role.setName(dto.getRolName());
		role.setDescription(dto.getRolDescription());
		return role;
	}

	/**
	 * 
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles </b> Obtiene una entidad RoleUser a
	 * partir de su DTO
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param dto
	 * @return
	 */
	private RoleUser convertToEntityRoleUser(RoleUserDTO dto) {
		User user = new User();
		Role role = new Role();
		user.setId(dto.getIdUser());
		role.setId(dto.getIdRole());
		return new RoleUser(null, user, role, dto.getUsername(), LocalDateTime.now());
	}

}
