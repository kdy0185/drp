package com.jsplan.drp.global.obj.repository;

import static com.jsplan.drp.domain.sys.usermng.entity.QUserAuthMng.userAuthMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserMng.userMng;

import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.global.obj.entity.UserVO;
import com.querydsl.core.types.Projections;
import java.util.List;

/**
 * @Class : UserRepositoryImpl
 * @Author : KDW
 * @Date : 2022-05-19
 * @Description : 사용자 계정 Custom Repository Impl
 */
public class UserCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    UserCustomRepository {

    /**
     * <p>사용자 계정 Repository Impl 생성자</p>
     */
    public UserCustomRepositoryImpl() {
        super(UserMng.class);
    }

    /**
     * <p>사용자 정보</p>
     *
     * @param userId (사용자 아이디)
     * @return UserVO (사용자 정보)
     */
    @Override
    public UserVO searchUserDetail(String userId) {
        return select(Projections.bean(UserVO.class,
            userMng.userId,
            userMng.userNm,
            userMng.userPw,
            userMng.useYn))
            .from(userMng)
            .where(userMng.userId.eq(userId))
            .fetchOne();
    }

    /**
     * <p>사용자 권한 정보</p>
     *
     * @param userId (사용자 아이디)
     * @return UserVO (사용자 권한 정보)
     */
    @Override
    public List<String> searchUserAuthList(String userId) {
        return select(userAuthMng.authMng.authCd)
            .from(userAuthMng)
            .where(userAuthMng.userMng.userId.eq(userId))
            .fetch();
    }
}
