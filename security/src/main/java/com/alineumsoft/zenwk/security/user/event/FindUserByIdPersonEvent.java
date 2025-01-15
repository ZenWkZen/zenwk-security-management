package com.alineumsoft.zenwk.security.user.event;

import org.springframework.context.ApplicationEvent;

import com.alineumsoft.zenwk.security.user.entity.User;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project security-zenwk
 * @class FindUserByIdPersonEvent
 */
public class FindUserByIdPersonEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private final Long idPerson;
	private User user;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param source
	 * @param idPerson
	 */
	public FindUserByIdPersonEvent(Object source, Long idPerson) {
		super(source);
		this.idPerson = idPerson;
	}

	/**
	 * Gets the value of user.
	 * 
	 * @return the value of user.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the value of user.
	 * 
	 * @param user the new value of user.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the value of idPerson.
	 * 
	 * @return the value of idPerson.
	 */
	public Long getIdPerson() {
		return idPerson;
	}

}
