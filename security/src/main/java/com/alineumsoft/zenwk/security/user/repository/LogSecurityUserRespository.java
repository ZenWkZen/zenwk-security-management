package com.alineumsoft.zenwk.security.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alineumsoft.zenwk.security.user.entity.LogSecurityUser;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 */
public interface LogSecurityUserRespository extends JpaRepository<LogSecurityUser, Long> {

}
