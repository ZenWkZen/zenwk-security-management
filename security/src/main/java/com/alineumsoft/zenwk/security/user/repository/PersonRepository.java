package com.alineumsoft.zenwk.security.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alineumsoft.zenwk.security.user.model.Person;
import com.alineumsoft.zenwk.security.user.model.User;

public interface PersonRepository  extends JpaRepository<Person, Long>{
	/**
	 * <p> <b> CU001_XX </b> XXX  </p> 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a> 
	 * @param idPerson
	 * @return
	 */
	public Person findByIdPerson(Long idPerson);
	
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
