package com.alineumsoft.zenwk.security.entity;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.entity.id.RolUserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Entidad que realciona las tablas sec_role y sec_user
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class RolUser
 */
@Entity
@Table(name = "sec_role_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(RolUserId.class)
public class RoleUser {
	/**
	 * Referencia la pk con sec_user
	 */
	@Id
	@Column(name = "relroluseiduser")
	private Long idUser;
	/**
	 * Referencia la pk con role
	 */
	@Id
	@Column(name = "relroluseidrole")
	private Long idRole;
	/**
	 * Usuario de creacion
	 */
	@Column(name = "relrolusecreationuser")
	private String creationUser;
	/**
	 * Fecha de creacion del registro
	 */
	@Column(name = "relrolusecreationdate")
	private LocalDateTime creationDate;
}
