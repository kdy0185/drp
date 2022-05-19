package com.jsplan.drp.global.obj.repository;

import com.jsplan.drp.global.obj.entity.UserVO;
import java.util.List;

/**
 * @Class : UserRepository
 * @Author : KDW
 * @Date : 2022-05-19
 * @Description : 사용자 계정 Custom Repository
 */
public interface UserCustomRepository {

    /**
     * <p>사용자 정보</p>
     *
     * @param userId (사용자 아이디)
     * @return UserVO (사용자 정보)
     */
    UserVO searchUserDetail(String userId);

    /**
     * <p>사용자 권한 정보</p>
     *
     * @param userId (사용자 아이디)
     * @return List (사용자 권한 목록)
     */
    List<String> searchUserAuthList(String userId);
}
