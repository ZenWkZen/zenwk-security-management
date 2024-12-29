package com.alineumsoft.zenwk.security.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.alineumsoft.zenwk.security.user.model.User;

public interface UserRepository
		extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
	/**
	 * <p>
	 * <b> CU0001_Seguridad_Creación_Usuario </b> Busqueda total paginada
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pageable
	 * @return
	 */
	public Page<User> findAll(Pageable pageable);

	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param id
	 * @return
	 * @see org.springframework.data.repository.CrudRepository#findById(java.lang.Object)
	 */
	public Optional<User> findById(Long idUser);
}
