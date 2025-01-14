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
@Table(name = "seg_permission")
@Data
public class Permission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "segperidpermission")
	private Long id;

	@Column(name = "segpername")
	@Enumerated(EnumType.STRING)
	private PermissionEnum name;

	@Column(name = "segperdescription")
	private String description;

	@Column(name = "segpercreationdate")
	private LocalDateTime creationDate;

	@Column(name = "segpermodificationdate")
	private LocalDateTime modificationDate;

	@Column(name = "segpercreationuser")
	private String creationUser;

	@Column(name = "segpermodificationuser")
	private String modificationUser;

}
