package com.alineumsoft.zenwk.security.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.alineumsoft.zenwk.security.user.entity.User;

public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
	public final static String DELETE_PERSON = "DELETE FROM Person "
			+ "p WHERE id = (SELECT u.person.id FROM User u WHERE u.id = :idUser)";

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

	@Modifying
	@Transactional
	@Query(DELETE_PERSON)
	void deletePersonByUserId(@Param("idUser") Long idUser);
}
