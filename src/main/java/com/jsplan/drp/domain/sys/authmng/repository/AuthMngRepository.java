package com.jsplan.drp.domain.sys.authmng.repository;

import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import com.jsplan.drp.global.obj.vo.UseStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : AuthMngRepository
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 관리 Repository
 */
public interface AuthMngRepository extends JpaRepository<AuthMng, String>, AuthMngCustomRepository {

    /**
     * <p>상위 권한 목록</p>
     *
     * @param useYn (사용 여부)
     * @return List (권한 목록)
     */
    List<AuthMng> findUpperAuthMngByUseYn(UseStatus useYn);
}
