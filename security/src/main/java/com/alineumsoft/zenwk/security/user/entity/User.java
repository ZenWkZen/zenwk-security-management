package com.alineumsoft.zenwk.security.user.entity;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.person.entity.Person;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "seguseidperson")
	private Person person;

}