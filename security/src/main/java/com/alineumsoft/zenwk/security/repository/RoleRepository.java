package com.alineumsoft.zenwk.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alineumsoft.zenwk.security.entity.Role;
import com.alineumsoft.zenwk.security.enums.RoleEnum;

/**
 * <p>
 * Repositorio para entidad sec_role
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class RoleRepository
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Busca un role con su nombre
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param name
	 * @return
	 */
	public Optional<Role> findByName(RoleEnum name);

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Busca un role por su id
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param name
	 * @return
	 */
	public Optional<Role> findById(Long id);

}
