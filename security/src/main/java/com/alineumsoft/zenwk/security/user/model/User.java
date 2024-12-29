package com.alineumsoft.zenwk.security.user.model;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.user.dto.PersonDTO;
import com.alineumsoft.zenwk.security.user.dto.UserOutDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 */
@Entity
@Table(name = "seg_user")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seguseiduser")
	private Long idUser;

	@Column(name = "seguseusername")
	private String username;

	@Column(name = "segusepassword")
	private String password;

	@Column(name = "segusecreationdate")
	private LocalDateTime creationDate;

	@Column(name = "segusemodificationdate")
	private LocalDateTime modificationDate;

	@Column(name = "seguseusercreation")
	private String UserCreation;

	@Column(name = "seguseusermodification")
	private String UserModification;

	@ManyToOne
	@JoinColumn(name = "perperstateid")
	private UserState userState;

	@ManyToOne
	@JoinColumn(name = "perperidperson")
	private Person person;

	/**
	 * <p>
	 * <b> CU001_XX </b> Genera el DTO de salida
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @return
	 */
	public UserOutDTO getUserOutDTO() {
		UserOutDTO userOutDTO = new UserOutDTO();
		PersonDTO personDTO = new PersonDTO();
		personDTO.setEmail(person.getEmail());
		personDTO.setFirstUsurname(person.getFirstUsurname());
		personDTO.setName(person.getName());
		userOutDTO.setUsername(username);
		userOutDTO.setState(userState.getNameState().name());
		userOutDTO.setPersonDTO(personDTO);
		return userOutDTO;
	}

	/**
	 * <p>
	 * <b> Util </b> Asigna la fecha actual
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 */
	@PrePersist
	protected void onCreate() {
		if (this.creationDate == null) {
			this.creationDate = LocalDateTime.now();
		}
	}

	/**
	 * <p>
	 * <b>Actualiza la fecha de modificación</b> cada vez que la entidad se
	 * modifica.
	 * </p>
	 */
	@PreUpdate
	protected void onUpdate() {
		this.modificationDate = LocalDateTime.now();
	}

	/**
	 * <p>
	 * <b> CU0001_Seguridad_Creación_Usuario </b> Encriptacion de la constraeña a
	 * ser persistida
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param rawPassword
	 * @return
	 */
//	public String encryptPassword(String rawPassword) {
//		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		return passwordEncoder.encode(rawPassword);
//	}
}