package com.alineumsoft.zenwk.security.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.alineumsoft.zenwk.security.user.model.User;

public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param id
	 * @return
	 * @see org.springframework.data.repository.CrudRepository#findById(java.lang.Object)
	 */
	public Optional<User> findById(Long idUser);
	
	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idUser
	 * @return 
	 * @see org.springframework.data.repository.CrudRepository#existsById(java.lang.Object)
	 */
	public boolean existsById(Long idUser);
}
