package com.alineumsoft.zenwk.security.user.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.alineumsoft.zenwk.security.user.entity.User;
import com.alineumsoft.zenwk.security.user.entity.UserHist;
import com.alineumsoft.zenwk.security.user.hist.enums.HistoricalEnum;
import com.alineumsoft.zenwk.security.user.repository.UserHistRepository;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserServiceHist
 */
@Service
public class UserHistService {
	private final UserHistRepository userHistRepository;

	/**
	 * <p>
	 * <b> Constructor </b>
	 * </p>
	 * 
	 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
	 * @param userHistRepository
	 */
	public UserHistService(UserHistRepository userHistRepository) {
		this.userHistRepository = userHistRepository;
	}

	/**
	 * <p>
	 * <b> Persistencia historico
	 * </p>
	 * 
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param user
	 * @param userHist
	 */
	public void saveUserHist(User user, HistoricalEnum OperationType) {
		UserHist userHist = new UserHist();
		userHist.setIdUser(user.getIdUser());
		userHist.setUsername(user.getUsername());
		userHist.setPassword(user.getPassword());
		userHist.setUserCreation(user.getUserCreation());
		userHist.setUserModification(user.getUserModification());
		userHist.setUserState(user.getUserState().getStateId());
		userHist.setPerson(user.getPerson().getIdPerson());
		userHist.setModificationDate(LocalDateTime.now());
		userHist.setOperation(OperationType);
		userHist.setCreationDate(user.getCreationDate());
		userHist.setModificationDate(user.getModificationDate());
		userHist.setHistCreationDate(LocalDateTime.now());
		userHistRepository.save(userHist);
	}

}
