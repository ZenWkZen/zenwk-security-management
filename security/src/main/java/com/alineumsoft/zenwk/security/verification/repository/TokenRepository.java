package com.alineumsoft.zenwk.security.verification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alineumsoft.zenwk.security.verification.entity.Token;

/**
 * <p>
 * Repositorio de la entidad de token.
 * </p>
 * 
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class TokenRepository
 */
public interface TokenRepository extends JpaRepository<Token, Long> {
	/**
	 * <p>
	 * <b> CU003_Gestionar token de verificación. </b> Buscar un token por email y
	 * código.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param email
	 * @return
	 */
	public Optional<Token> findByEmailAndCode(String email, String code);

	/**
	 * 
	 * <p>
	 * <b> CU003_Gestionar token de verificación. </b> Buscar un token por email.
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param email
	 * @return
	 */
	public Optional<Token> findByEmail(String email);

}
