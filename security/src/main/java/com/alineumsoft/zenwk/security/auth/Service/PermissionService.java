package com.alineumsoft.zenwk.security.auth.Service;

import static com.alineumsoft.zenwk.security.auth.definitions.AuthConfig.ID;
import static com.alineumsoft.zenwk.security.auth.definitions.AuthConfig.URL_PERSON;
import static com.alineumsoft.zenwk.security.auth.definitions.AuthConfig.URL_USER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.common.constants.CommonMessageConstants;
import com.alineumsoft.zenwk.security.common.enums.PermissionOperationEnum;
import com.alineumsoft.zenwk.security.dto.PermissionDTO;
import com.alineumsoft.zenwk.security.entity.RoleUser;
import com.alineumsoft.zenwk.security.enums.RoleEnum;
import com.alineumsoft.zenwk.security.person.repository.RolePermissionRepository;
import com.alineumsoft.zenwk.security.user.entity.User;
import com.alineumsoft.zenwk.security.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Consulta los roles y permisos del sistema
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class PermissionService
 */
@Service
@Slf4j
public class PermissionService {
	/**
	 * Repositorio para la consulta de permisos y roles.
	 */
	private final RolePermissionRepository rolePermRepo;

	/**
	 * Servicio para la gestion de user
	 */
	private final UserService userService;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param rolePermRepo
	 */
	public PermissionService(RolePermissionRepository rolePermRepo, UserService userService) {
		this.rolePermRepo = rolePermRepo;
		this.userService = userService;
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> RecRecupera y organiza los permisos
	 * de los roles en el sistema. Agrupa los permisos por operación (CREATE,
	 * UPDATE, DELETE, FIND_ALL, FIND_BY_ID).
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public Map<PermissionOperationEnum, List<PermissionDTO>> getOperationPermission() {
		try {
			// Consulta de roles y permisos
			List<Object[]> listRolPermssions = rolePermRepo.findAllRolePermissions();
			return getMapPermissions(listRolPermssions);

		} catch (Exception e) {
			log.error(CommonMessageConstants.LOG_MSG_EXCEPTION, e.getMessage(), e);
			return Collections.emptyMap();
		}
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Retorna los recursos rest
	 * permitidos para el usuario para el rol user
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public List<String> listAllowedUrlsForUserRole(String username) {
		User user = userService.findByUsername(username);
		List<RoleUser> rolesUser = userService.findRolesUserByUser(user);
		List<RoleEnum> namesRole = rolesUser.stream().map(role -> role.getRole().getName())
				.collect(Collectors.toList());
		List<String> permissionResources = rolePermRepo.findResourcesByRolName(namesRole);

		permissionResources = permissionResources.stream().map(url -> {
			if (url.contains(ID)) {
				url = generatedUrlFromId(user, url);
			}
			return url;
		}).collect(Collectors.toList());

		return permissionResources.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Actualiza las rutas de los recursos
	 * que tengan como parametro el valor {id}
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param user
	 * @param url
	 * @return
	 */
	private String generatedUrlFromId(User user, String url) {
		String updateUrl = null;
		if (url.contains(URL_USER) && user.getId() != null) {
			updateUrl = url.replace(ID, user.getId().toString());
		}
		if (url.contains(URL_PERSON) && url.contains(ID) && user.getPerson() != null
				&& user.getPerson().getId() != null) {
			updateUrl = url.replace(ID, user.getPerson().getId().toString());
		}
		return updateUrl;
	}

	/**
	 * 
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Util. Mapeo del resultado desde bd
	 * a un mapa
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param listRolPermssions
	 * @return
	 */
	private Map<PermissionOperationEnum, List<PermissionDTO>> getMapPermissions(List<Object[]> listRolPermssions) {
		Map<PermissionOperationEnum, List<PermissionDTO>> mapRolPermissions = new HashMap<>();
		// Mapa con cache de roles
		listRolPermssions.forEach(rolPerm -> {
			PermissionOperationEnum keyMap = PermissionOperationEnum.valueOf(rolPerm[0].toString());
			// Agregar o actualizar la lista de permisos en el mapa
			mapRolPermissions.computeIfAbsent(keyMap, k -> new ArrayList<>()).add(new PermissionDTO(
					String.valueOf(rolPerm[1]), String.valueOf(rolPerm[2]), String.valueOf(rolPerm[3])));
		});
		return mapRolPermissions;
	}

}
