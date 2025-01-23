package com.alineumsoft.zenwk.security.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.alineumsoft.zenwk.security.user.entity.User;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class UserRepository
 */
public interface UserRepository
		extends JpaRepository<User, Long>, CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
	public final static String JPQL_FIND_USER_BY_PERSON_ID = "SELECT u FROM User u WHERE u.person.id = :idPerson";

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

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> JPQL para la busqueda de un usuario
	 * a partir del id de la persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param idPerson
	 * @return
	 */
	@Query(JPQL_FIND_USER_BY_PERSON_ID)
	public User finByIdPerson(Long idPerson);

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> Valida si un usuario ya existe para
	 * los campos:
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param username
	 * @param email
	 * @return
	 */
	public boolean existsByUsernameAndEmail(String username, String email);
}
