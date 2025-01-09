package com.alineumsoft.zenwk.security.user.entity;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.user.common.hist.enums.HistoricalOperationEnum;
import com.alineumsoft.zenwk.security.user.common.util.CryptoUtil;
import com.alineumsoft.zenwk.security.user.common.util.HistoricalUtil;
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
	private Long id;

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
		this.password = CryptoUtil.encryptPassword(this.password);
	}

	@PostPersist
	private void PosCreate() {
		HistoricalUtil.registerHistorical(this, HistoricalOperationEnum.INSERT, UserHistService.class);
	}

	@PostUpdate
	private void update() {
		this.modificationDate = LocalDateTime.now();
		HistoricalUtil.registerHistorical(this, HistoricalOperationEnum.UPDATE, UserHistService.class);

	}

	@PreRemove
	private void delete() {
		HistoricalUtil.registerHistorical(this, HistoricalOperationEnum.DELETE, UserHistService.class);

	}

}