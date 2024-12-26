package com.alineumsoft.zenwk.security.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.alineumsoft.zenwk.security.user.model.User;

public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
	/**
	 * <p>
	 * <b> CU0001_Seguridad_Creación_Usuario </b> Busqueda total paginada
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pageable
	 * @return
	 */
	public Page<User> findAll(PageRequest pageRequest);

	/**
	 * 
	 * <p>
	 * <b> CU0001_Seguridad_Creación_Usuario </b> Busqueda por email
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param email
	 * @return
	 */
	public User findByEmail(String email);

}
