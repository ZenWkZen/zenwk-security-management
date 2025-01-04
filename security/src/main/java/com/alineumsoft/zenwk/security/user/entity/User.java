package com.alineumsoft.zenwk.security.user.entity;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.user.common.component.AppContextHolderComponent;
import com.alineumsoft.zenwk.security.user.common.hist.enums.HistoricalEnum;
import com.alineumsoft.zenwk.security.user.dto.PersonDTO;
import com.alineumsoft.zenwk.security.user.dto.UserOutDTO;
import com.alineumsoft.zenwk.security.user.service.UserHistService;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 */

@Entity
@Table(name = "seg_user")
@Data
@NoArgsConstructor
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

	@Column(name = "seguseusecreation")
	private String userCreation;

	@Column(name = "seguseusemodification")
	private String userModification;

	@ManyToOne
	@JoinColumn(name = "segusestateid")
	private UserState userState;

	@ManyToOne
	@JoinColumn(name = "seguseidperson")
	private Person person;

	/**
	 * <p>
	 * <b> CU001_Seguridad_Creación_Usuario </b> DTO de salida
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
		userOutDTO.setPerson(personDTO);
		return userOutDTO;
	}

	@PrePersist
	private void create() {
		this.creationDate = LocalDateTime.now();
	}
	
	@PostPersist
	private void PosCreate() {
		registerHistorical(HistoricalEnum.INSERT);
	}

	@PostUpdate
	private void update() {
		this.modificationDate = LocalDateTime.now();
		registerHistorical(HistoricalEnum.UPDATE);

	}

	@PreRemove
	private void delete() {
		registerHistorical(HistoricalEnum.DELETE);
	}

	/**
	 * <p>
	 * <b> Util </b> proceso registro historico
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param operation
	 */
	private void registerHistorical(HistoricalEnum operation) {
		// Usa un contexto estático de Spring para obtener el servicio
		UserHistService userHistService = AppContextHolderComponent.getBean(UserHistService.class);
		if (userHistService != null) {
			userHistService.saveUserHist(this, operation);
		}
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