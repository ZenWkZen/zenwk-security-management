package com.alineumsoft.zenwk.security.person.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alineumsoft.zenwk.security.entity.RolePermission;
import com.alineumsoft.zenwk.security.enums.RoleEnum;

/**
 * <p>
 * Repositorio para la entidad sec_role_permission
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class RolePermissionRepository
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
	public static final String JPQL_ROLE_PERMISSIONS = "SELECT p.operation, r.name, p.method, p.resource "
			+ "FROM RolePermission rp " + "LEFT JOIN rp.role r " + "LEFT JOIN rp.permission p " + "ORDER BY r.name";

	public static final String JPQL_RESOURCES_FILTER_ROL_NAME = "SELECT DISTINCT p.resource "
			+ "FROM RolePermission rp " + "LEFT JOIN rp.role r " + "LEFT JOIN rp.permission p "
			+ "WHERE r.name IN (:rolName) ";

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Consulta todos lo roles
	 * configurados para el modulo security
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	@Query(JPQL_ROLE_PERMISSIONS)
	public List<Object[]> findAllRolePermissions();

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Consulta todos lo roles
	 * configurados para el modulo security filtrando por el nombre del rol
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param rolName
	 * @return
	 */
	@Query(JPQL_RESOURCES_FILTER_ROL_NAME)
	public List<String> findResourcesByRolName(List<RoleEnum> rolName);

}
