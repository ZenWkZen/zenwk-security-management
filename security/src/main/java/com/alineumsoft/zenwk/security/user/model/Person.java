package com.alineumsoft.zenwk.security.user.model;

import java.time.LocalDateTime;

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
 * @class UserState
 */
@Entity
@Table(name = "per_person")
@Data
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "perperidperson")
	private Long idPerson;

	@Column(name = "perpername")
	private String name;

	@Column(name = "perperfirstusurname")
	private String firstUsurname;

	@Column(name = "perperemail")
	private String email;

	@Column(name = "perpercreationdate")
	private LocalDateTime creationDate;

	@Column(name = "perpermodificationdate")
	private LocalDateTime modificationDate;

	@Column(name = "perperusercreation")
	private String UserCreation;

	@Column(name = "perperusermodification")
	private String UserModification;

	/**
	 * <p>
	 * <b> Util </b> Asigna la fecha actual
	 * </p>
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

}
