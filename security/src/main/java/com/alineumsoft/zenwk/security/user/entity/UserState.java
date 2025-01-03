package com.alineumsoft.zenwk.security.user.entity;

import java.time.LocalDateTime;

import com.alineumsoft.zenwk.security.user.enums.UserStateEnum;

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
 * @class UserState
 */
@Entity
@Table(name = "seg_user_state")
@Data
public class UserState {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "segusustateid")
	private Long stateId;

	@Enumerated(EnumType.STRING)
	@Column(name = "segusunamestate")
	private UserStateEnum nameState;

	@Column(name = "segusudescriptionstate")
	private String descriptionState;

	@Column(name = "segusucreationDate")
	private LocalDateTime creationDate;

	@Column(name = "segusumodificationdate")
	private LocalDateTime modificationDate;

	@Column(name = "segusuusercreation")
	private String UserCreation;

	@Column(name = "segusuusermodification")
	private String UserModification;

}
