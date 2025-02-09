package com.alineumsoft.zenwk.security.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alineumsoft.zenwk.security.entity.Permission;

/**
 * <p>
 * Representa el repositorio de la clase sed_permission
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class PermissionRepository
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
