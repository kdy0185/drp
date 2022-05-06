package com.jsplan.drp.domain.sys.codemng.repository;

import com.jsplan.drp.domain.sys.codemng.entity.CodeMng;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMngId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : CodeMngRepository
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 공통 코드 관리 Repository
 */
public interface CodeMngRepository extends JpaRepository<CodeMng, CodeMngId>,
    CodeMngCustomRepository {

}
