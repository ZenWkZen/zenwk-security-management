package com.alineumsoft.zenwk.security.entity.id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Clase que referencia la pk compuest de la entidad sec_user_role
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class RolUserId
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUserId implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Id sec_user
	 */
	private Long idUser;
	/**
	 * Id sec_role
	 */
	private Long idRole;

}
