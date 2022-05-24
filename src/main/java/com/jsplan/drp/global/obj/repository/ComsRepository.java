package com.jsplan.drp.global.obj.repository;

import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : ComsRepository
 * @Author : KDW
 * @Date : 2022-05-20
 * @Description : 공통 Repository
 */
public interface ComsRepository extends JpaRepository<UserMng, String>, ComsCustomRepository {

}
