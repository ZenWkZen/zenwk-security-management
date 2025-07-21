package com.alineumsoft.zenwk.security.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alineumsoft.zenwk.security.user.entity.UserHist;

/**
 * @author <a href="mailto:alineumsoft@gmail.com">C. Alegria</a>
 * @project SecurityUser
 * @class UserHistRepository
 */
public interface UserHistRepository extends JpaRepository<UserHist, Long> {

}
