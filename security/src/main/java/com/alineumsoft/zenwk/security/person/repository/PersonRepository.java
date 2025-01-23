package com.alineumsoft.zenwk.security.person.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.alineumsoft.zenwk.security.person.entity.Person;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class PersonRepository
 */
public interface PersonRepository extends JpaRepository<Person, Long>, PagingAndSortingRepository<Person, Long> {
	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param id
	 * @return
	 * @see org.springframework.data.repository.CrudRepository#findById(java.lang.Object)
	 */
	public Optional<Person> findById(Long id);

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creacion_Usuario </b> valida si la persona ya existen en
	 * la base de datos con las siguientes campos:
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param middleLastName
	 * @param dateOfBirth
	 * @return
	 */
	public boolean existsByFirstNameAndMiddleNameAndLastNameAndMiddleLastNameAndDateOfBirth(String firstName,
			String middleName, String lastName, String middleLastName, LocalDateTime dateOfBirth);

}
