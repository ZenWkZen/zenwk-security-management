package com.alineumsoft.zenwk.security.auth.dto;

import com.alineumsoft.zenwk.security.entity.Role;
import com.alineumsoft.zenwk.security.enums.RoleEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * DTO usado para la gestion de los roles en el sistema
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class RoleDTO
 */
@Data
@NoArgsConstructor
public class RoleDTO {
	/**
	 * idRol
	 */
	private Long idRol;
	/**
	 * rolName
	 */
	private RoleEnum rolName;
	/**
	 * rolDescription
	 */
	private String rolDescription;

	/**
	 * 
	 * <p>
	 * <b> CU002_Seguridad_Asignación_Roles </b> Constructor a partir del entity
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param role
	 */
	public RoleDTO(Role role) {
		this.idRol = role.getId();
		this.rolName = role.getName();
		this.rolDescription = role.getDescription();
	}

}
