package com.alineumsoft.zenwk.security.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alineumsoft.zenwk.security.user.entity.UserState;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserStateRepository
 */
public interface UserStateRepository extends JpaRepository<UserState, Long> {
	/**
	 * @author <a href="alineumsoft@gmail.com">C. Alegria</a>
	 * @param pageable
	 * @return
	 * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Pageable)
	 */
	public Page<UserState> findAll(Pageable pageable);
}
