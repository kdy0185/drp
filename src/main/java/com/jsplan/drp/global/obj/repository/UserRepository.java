package com.jsplan.drp.global.obj.repository;

import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : UserRepository
 * @Author : KDW
 * @Date : 2022-05-19
 * @Description : 사용자 계정 Repository
 */
public interface UserRepository extends JpaRepository<UserMng, String>, UserCustomRepository {

}
