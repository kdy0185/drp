package com.jsplan.drp.domain.sys.codemng.repository;

import com.jsplan.drp.domain.sys.codemng.entity.CodeGrpMng;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : CodeGrpMngRepository
 * @Author : KDW
 * @Date : 2022-05-02
 * @Description : 그룹 코드 관리 Repository
 */
public interface CodeGrpMngRepository extends JpaRepository<CodeGrpMng, String>,
    CodeGrpMngCustomRepository {

}
