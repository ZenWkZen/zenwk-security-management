package com.alineumsoft.zenwk.security.entity;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "seg_user_rol")
@Data
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "segrolid")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "segrolname")
	private RoleEnum name;

	@Column(name = "segroldescription")
	private String description;

	@Column(name = "segrolcreationdate")
	private LocalDateTime creationDate;

	@Column(name = "segrolmodificationdate")
	private LocalDateTime modificationDate;

	@Column(name = "segrolcreationuser")
	private String creationUser;

	@Column(name = "segrolmodificationuser")
	private LocalDateTime modificationUser;

}
