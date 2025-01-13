package com.alineumsoft.zenwk.security.user.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.user.common.hist.enums.HistoricalOperationEnum;
import com.alineumsoft.zenwk.security.user.entity.Person;
import com.alineumsoft.zenwk.security.user.entity.PersonHist;
import com.alineumsoft.zenwk.security.user.repository.PersonHistRepository;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class PersonHistService
 */
@Service
public class PersonHistService {
	private final PersonHistRepository personHistRepository;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param personHistRepository
	 */
	public PersonHistService(PersonHistRepository personHistRepository) {
		this.personHistRepository = personHistRepository;
	}

	/**
	 * <p>
	 * <b> Persistencia historico </b>
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param person
	 * @param operationType
	 */
	public void saveHistorical(Person person, HistoricalOperationEnum operationType) {
		PersonHist personHist = new PersonHist();
		personHist.setIdPerson(person.getId());
		personHist.setName(person.getName());
		personHist.setFirstUsurname(person.getFirstUsurname());
		personHist.setEmail(person.getEmail());
		personHist.setCreationDate(person.getCreationDate());
		personHist.setModificationDate(person.getModificationDate());
		personHist.setUserCreation(person.getUserCreation());
		personHist.setUserModification(person.getUserModification());
		personHist.setHistCreationDate(LocalDateTime.now());
		personHist.setOperation(operationType);
		personHistRepository.save(personHist);
	}
}
