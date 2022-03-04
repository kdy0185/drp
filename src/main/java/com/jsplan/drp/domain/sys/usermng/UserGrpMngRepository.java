package com.jsplan.drp.domain.sys.usermng;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGrpMngRepository extends JpaRepository<UserGrpMng, String>, UserGrpMngCustomRepository {

    UserGrpMng findByGrpCd(String grpCd);
}
