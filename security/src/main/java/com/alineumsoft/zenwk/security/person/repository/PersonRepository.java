package com.alineumsoft.zenwk.security.person.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.alineumsoft.zenwk.security.person.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long>, PagingAndSortingRepository<Person, Long> {
	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param id
	 * @return
	 * @see org.springframework.data.repository.CrudRepository#findById(java.lang.Object)
	 */
	public Optional<Person> findById(Long id);

}
