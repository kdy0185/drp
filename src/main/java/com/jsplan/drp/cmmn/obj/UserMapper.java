package com.jsplan.drp.cmmn.obj;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

/**
 * @Class : UserMapper
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 사용자 계정 Mapper
 */

@Repository
@Mapper
public interface UserMapper {

    /**
     * <p>사용자 정보</p>
     *
     * @param userId
     * @return UserVO
     * @throws UsernameNotFoundException throws UsernameNotFoundException
     */
    UserVO selectUserDetail(String userId) throws UsernameNotFoundException;

    /**
     * <p>사용자 권한 정보</p>
     *
     * @param userId
     * @return List<UserVO>
     */
    List<UserVO> selectUserAuthList(String userId);
}
