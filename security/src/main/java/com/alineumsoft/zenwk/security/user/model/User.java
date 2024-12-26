package com.alineumsoft.zenwk.security.user.model;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "seg_usuario")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long idUsuario;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apellido")
	private String apellido;

	@Column(name = "email")
	private String email;

	@Column(name = "username")
	private String username;

	@Column(name = "estado")
	private String estado;

	@JsonIgnore
	@Column(name = "password")
	private String password;

	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;

	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;

	@Column(name = "usuario_creacion")
	private String usuarioCreacion;

	@Column(name = "usuario_modificacion")
	private String usuarioModificacion;

	
	/**
	 * <p>
	 * <b> CU0001_Seguridad_Creación_Usuario </b> Encriptacion de la constraeña a
	 * ser persistida
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param rawPassword
	 * @return
	 */
	public String encryptPassword(String rawPassword) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(rawPassword);
	}
}