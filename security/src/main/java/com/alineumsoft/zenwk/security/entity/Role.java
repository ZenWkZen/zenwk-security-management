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
@Table(name = "sec_role")
@Data
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "secrolidrole")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "secrolname")
	private RoleEnum name;

	@Column(name = "secroldescription")
	private String description;

	@Column(name = "secrolcreationdate")
	private LocalDateTime creationDate;

	@Column(name = "secrolmodificationdate")
	private LocalDateTime modificationDate;

	@Column(name = "secrolcreationuser")
	private String creationUser;

	@Column(name = "secrolmodificationuser")
	private LocalDateTime modificationUser;

}
