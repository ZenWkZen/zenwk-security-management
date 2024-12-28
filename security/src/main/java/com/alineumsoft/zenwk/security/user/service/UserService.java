package com.alineumsoft.zenwk.security.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.user.dto.UserInDTO;
import com.alineumsoft.zenwk.security.user.model.Person;
import com.alineumsoft.zenwk.security.user.model.User;
import com.alineumsoft.zenwk.security.user.model.UserState;
import com.alineumsoft.zenwk.security.user.repository.PersonRepository;
import com.alineumsoft.zenwk.security.user.repository.UserRepository;
import com.alineumsoft.zenwk.security.user.repository.UserStateRepository;

@Service
public class UserService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final UserRepository userRepository;

	private final PersonRepository personRepository;

	private final UserStateRepository UserStateRepository;

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> XXX
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userRepository
	 * @param personRepository
	 * @param UserStateRepository
	 */
	public UserService(UserRepository userRepository, PersonRepository personRepository,
			UserStateRepository UserStateRepository) {
		this.userRepository = userRepository;
		this.personRepository = personRepository;
		this.UserStateRepository = UserStateRepository;
	}

	/**
	 * 
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> XXX
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @return
	 */
	public Long createNewUser(UserInDTO userInDTO) {
		Person person = createPerson(userInDTO);
		User user = createUser(userInDTO, person);
		return user.getIdUsuario();
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Creación persona
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @return
	 */
	private Person createPerson(UserInDTO userInDTO) {
		Person person = new Person();
		person.setName(userInDTO.getPersonDTO().getName());
		person.setFirstSurname(userInDTO.getPersonDTO().getFirstSurname());
		person.setEmail(userInDTO.getPersonDTO().getEmail());
		person.setUserCreation(userInDTO.getUsername());
		return personRepository.save(person);
	}

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> Creación usuario
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param userInDTO
	 * @param person
	 * @return
	 */
	private User createUser(UserInDTO userInDTO, Person person) {
		User user = new User();
		
		// Crear enun con los estados
		UserState userState = UserStateRepository.findAll().stream()
				.filter(state -> state.getNameState().equals("ENABLE")).collect(Collectors.toList()).get(0);

		user.setUsername(userInDTO.getUsername());
		user.setPassword(userInDTO.getPassword());
		user.setUserCreation(userInDTO.getUsername());
		user.setUserEstate(userState);
		user.setPerson(person);
		return userRepository.save(user);
	}

	/**
	 * <p>
	 * <b> Util </b> imprime las tabals del esquema
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param jdbcTemplate
	 */
	public void printTableNames() {
		String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
		List<String> tableNames = jdbcTemplate.queryForList(sql, String.class);

		System.out.println("Tablas en la base de datos:");
		for (String tableName : tableNames) {
			System.out.println(tableName);
		}
	}

}
