package com.alineumsoft.zenwk.security.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class LogSecurityUser
 */
@Entity(name = "log_seg_user")
@Data
public class LogSecurityUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "logseguseid")
	private Long idLogUser;

	@Column(name = "logsegusestatuscode")
	private Integer statusCode;

	@Column(name = "logseguseurl")
	private String url;

	@Column(name = "logseguserequest")
	private String request;

	@Column(name = "logseguseresponse")
	private String reponse;

	@Column(name = "logseguseerrormessage")
	private String erroMessage;

	@Column(name = "logsegusecreationdate")
	private LocalDateTime creationDate;

	@Column(name = "logseguseusercreation")
	private String userCreation;
}
