package com.jsplan.drp.domain.sys.usermng.repository;

import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : UserMngRepository
 * @Author : KDW
 * @Date : 2022-04-08
 * @Description : 사용자 관리 Repository
 */
public interface UserMngRepository extends JpaRepository<UserMng, String>, UserMngCustomRepository {

}
