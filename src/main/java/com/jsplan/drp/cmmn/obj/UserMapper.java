package com.jsplan.drp.cmmn.obj;

import java.util.List;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

/**
 * @Class : UserMapper
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 사용자 계정 Mapper
 */

@Repository("UserMapper")
public class UserMapper extends AbstractDAO {

    String namespace = "User.";

    /**
     * <p>사용자 정보</p>
     *
     * @param userId
     * @return UserVO
     * @throws UsernameNotFoundException throws UsernameNotFoundException
     */
    public UserVO selectUserDetail(String userId)
        throws UsernameNotFoundException {
        return (UserVO) selectOne(namespace + "selectUserDetail", userId);
    }

    /**
     * <p>사용자 권한 정보</p>
     *
     * @param userId
     * @return List<UserVO>
     */
    @SuppressWarnings("unchecked")
    public List<UserVO> selectUserAuthList(String userId) {
        return (List<UserVO>) selectList(namespace + "selectUserAuthList", userId);
    }
}
