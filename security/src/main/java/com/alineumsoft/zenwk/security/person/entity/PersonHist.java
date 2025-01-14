package com.alineumsoft.zenwk.security.person.entity;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.common.hist.enums.HistoricalOperationEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "per_person_hist")
@NoArgsConstructor
public class PersonHist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hperperid")
	private Long id;
	
	@Column(name = "hperperidperson")
	private Long idPerson;

	@Column(name = "hperpername")
	private String name;

	@Column(name = "hperperfirstusurname")
	private String firstUsurname;

	@Column(name = "hperperemail")
	private String email;

	@Column(name = "hperpercreationdate")
	private LocalDateTime creationDate;

	@Column(name = "hperpermodificationdate")
	private LocalDateTime modificationDate;

	@Column(name = "hperperusercreation")
	private String userCreation;

	@Column(name = "hperperusermodification")
	private String userModification;

	// Campo de auditoria propio de la entidad historica
	@Column(name = "hperperhistcreationdate")
	private LocalDateTime histCreationDate;

	// Campo de auditoria propio de la entidad historica
	@Column(name = "hperperoperation")
	@Enumerated(EnumType.STRING)
	private HistoricalOperationEnum operation;

}
