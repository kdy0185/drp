package com.jsplan.drp.domain.sys.usermng.repository;

import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGrpMngRepository extends JpaRepository<UserGrpMng, String>, UserGrpMngCustomRepository {

    UserGrpMng findByGrpCd(String grpCd);
}
