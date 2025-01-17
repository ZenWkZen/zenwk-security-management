package com.alineumsoft.zenwk.security.entity;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.enums.PermissionEnum;

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
@Table(name = "sec_permission")
@Data
public class Permission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "secperidpermission")
	private Long id;

	@Column(name = "secpername")
	@Enumerated(EnumType.STRING)
	private PermissionEnum name;

	@Column(name = "secperdescription")
	private String description;

	@Column(name = "secpercreationdate")
	private LocalDateTime creationDate;

	@Column(name = "secpermodificationdate")
	private LocalDateTime modificationDate;

	@Column(name = "secpercreationuser")
	private String creationUser;

	@Column(name = "secpermodificationuser")
	private String modificationUser;

}
