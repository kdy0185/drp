package com.jsplan.drp.domain.sys.usermng.repository;

import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : UserGrpMngRepository
 * @Author : KDW
 * @Date : 2022-03-07
 * @Description : 그룹 관리 Repository
 */
public interface UserGrpMngRepository extends JpaRepository<UserGrpMng, String>,
    UserGrpMngCustomRepository {

}
