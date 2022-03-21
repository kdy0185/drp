package com.jsplan.drp.domain.sys.usermng.repository;

import static com.jsplan.drp.domain.sys.usermng.entity.QUserGrpMng.userGrpMng;

import com.jsplan.drp.domain.sys.usermng.entity.QUserGrpMngDto_Detail;
import com.jsplan.drp.domain.sys.usermng.entity.QUserGrpMngDto_List;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

/**
 * @Class : UserGrpMngCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-03-07
 * @Description : 그룹 관리 Custom Repository Impl
 */
public class UserGrpMngCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    UserGrpMngCustomRepository {

    /**
     * <p>그룹 관리 Custom Repository Impl 생성자</p>
     */
    public UserGrpMngCustomRepositoryImpl() {
        super(UserGrpMng.class);
    }

    /**
     * <p>그룹 목록</p>
     *
     * @param grpNm    (그룹명)
     * @param pageable (페이징 정보)
     * @return Page (페이징 목록)
     */
    public Page<UserGrpMngDto.List> searchPageList(String grpNm, Pageable pageable) {
        return applyPagination(pageable, contentQuery ->
            contentQuery.select(new QUserGrpMngDto_List(
                    userGrpMng.grpCd,
                    userGrpMng.grpNm,
                    userGrpMng.grpDesc,
                    Expressions.simpleTemplate(String.class, "getUserNm({0})",
                        userGrpMng.regUser),
                    userGrpMng.regDate,
                    Expressions.simpleTemplate(String.class, "getUserNm({0})",
                        userGrpMng.modUser),
                    userGrpMng.modDate))
                .from(userGrpMng)
                .where(grpNmLike(grpNm))
                .orderBy(userGrpMng.grpCd.asc()
                ), countQuery ->
            countQuery.select(userGrpMng.count())
                .from(userGrpMng)
                .where(grpNmLike(grpNm))
        );
    }

    /**
     * <p>그룹 목록 조회 조건 : like</p>
     *
     * @param grpNm (그룹명)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression grpNmLike(String grpNm) {
        return StringUtils.hasText(grpNm) ? userGrpMng.grpNm.contains(grpNm) : null;
    }

    /**
     * <p>그룹 상세</p>
     *
     * @param grpCd (그룹 코드)
     * @return UserGrpMngDto (UserGrpMng DTO)
     */
    public UserGrpMngDto.Detail findByGrpCd(String grpCd) {
        return select(new QUserGrpMngDto_Detail(
            userGrpMng.grpCd,
            userGrpMng.grpNm,
            userGrpMng.grpDesc,
            Expressions.simpleTemplate(String.class, "getUserNm({0})",
                userGrpMng.regUser),
            userGrpMng.regDate,
            Expressions.simpleTemplate(String.class, "getUserNm({0})",
                userGrpMng.modUser),
            userGrpMng.modDate))
            .from(userGrpMng)
            .where(userGrpMng.grpCd.eq(grpCd))
            .fetchOne();
    }
}
