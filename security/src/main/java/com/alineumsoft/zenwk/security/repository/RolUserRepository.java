package com.alineumsoft.zenwk.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alineumsoft.zenwk.security.entity.RoleUser;
import com.alineumsoft.zenwk.security.entity.id.RolUserId;

/**
 * <p>
 * Repositorio para la creación de la relación del rol relacioado al usuario
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class RolUserRepository
 */
@Repository
public interface RolUserRepository extends JpaRepository<RoleUser, RolUserId> {
	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Eliminacion de los registros
	 * asociados al usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @return
	 */
	public void deleteByIdUser(Long idUser);

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Se recupera el registro RoleUser a
	 * partir de el id del usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @return
	 */
	public Optional<RoleUser> findByIdUser(Long idUser);
}
