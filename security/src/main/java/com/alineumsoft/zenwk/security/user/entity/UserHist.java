package com.alineumsoft.zenwk.security.user.entity;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.user.hist.enums.HistoricalEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 */
@Entity
@Table(name = "seg_user_hist")
@Data
public class UserHist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hseguseidhist")
	private Long idHist;
	
	@Column(name="hseguseiduser")
	private Long idUser;

	@Column(name = "hseguseusername")
	private String username;

	@Column(name = "hsegusepassword")
	private String password;

	@Column(name = "hsegusucreationdate")
	private LocalDateTime creationDate;

	@Column(name = "hsegusumodificationdate")
	private LocalDateTime modificationDate;

	@Column(name = "hseguseusercreation")
	private String userCreation;

	@Column(name = "hseguseusermodification")
	private String userModification;

	@Column(name = "hsegusidestate")
	private Long userState;

	@Column(name = "hseguseidperson")
	private Long person;

	@Column(name = "hsegusehistcreationdate")
	private LocalDateTime histCreationDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "hseguseoperation")
	private HistoricalEnum operation;

}