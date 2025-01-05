package com.alineumsoft.zenwk.security.user.common.exception;

import org.springframework.data.jpa.repository.JpaRepository;

/*
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class BaseException
 */
public abstract class CoreException extends RuntimeException {
	static final long serialVersionUID = 1L;

	private final String code;

	/**
	 * <p>
	 * <b> General </b> CoreException
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param <T>
	 * @param message
	 * @param code
	 * @param cause
	 * @param repository
	 * @param entity
	 */
	public <T> CoreException(String message, String code, Throwable cause, JpaRepository<T, ?> repository, T entity) {
		super(message, cause);
		this.code = code;
		saveLog(repository, entity);
	}

	/**
	 * <p>
	 * <b> General </b> Periste la excepcion
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param <T>
	 * @param repository
	 * @param entity
	 */
	private <T> void saveLog(JpaRepository<T, ?> repository, T entity) {
		if (repository != null && entity != null) {
			repository.save(entity);

		}
	}

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public String getCode() {
		return this.code;
	}

}
