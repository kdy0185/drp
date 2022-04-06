package com.jsplan.drp.domain.sys.usermng.repository;

import static com.jsplan.drp.domain.sys.usermng.entity.QUserGrpMng.userGrpMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserMng.userMng;

import com.jsplan.drp.domain.sys.usermng.dto.QUserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.QUserGrpMngListDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngListDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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
    @Override
    public Page<UserGrpMngListDto> searchPageList(String grpNm, Pageable pageable) {
        return applyPagination(pageable,
            contentQuery -> getContentQuery(grpNm, contentQuery),
            countQuery -> getCountQuery(grpNm, countQuery)
        );
    }

    /**
     * <p>그룹 목록 쿼리</p>
     *
     * @param grpNm        (그룹명)
     * @param contentQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<UserGrpMngListDto> getContentQuery(String grpNm,
        JPAQueryFactory contentQuery) {
        return contentQuery.select(new QUserGrpMngListDto(
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
            .orderBy(userGrpMng.grpCd.asc());
    }

    /**
     * <p>그룹 목록 카운트 쿼리</p>
     *
     * @param grpNm      (그룹명)
     * @param countQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<Long> getCountQuery(String grpNm, JPAQueryFactory countQuery) {
        return countQuery.select(userGrpMng.count())
            .from(userGrpMng)
            .where(grpNmLike(grpNm));
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
     * @return UserGrpMngDto (그룹 DTO)
     */
    @Override
    public UserGrpMngDetailDto findByGrpCd(String grpCd) {
        return select(new QUserGrpMngDetailDto(
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

    /**
     * <p>사용자 확인</p>
     *
     * @param grpCd (그룹 코드)
     * @return boolean (사용자 존재 여부)
     */
    @Override
    public boolean existsUserMngByGrpCd(String grpCd) {
        Long userMngCnt = select(userMng.count())
            .from(userMng)
            .where(userMng.userGrpMng.grpCd.eq(grpCd))
            .fetchOne();
        return userMngCnt != null && userMngCnt > 0;
    }

    /**
     * <p>그룹 엑셀 목록</p>
     *
     * @param grpNm (그룹명)
     * @return List (그룹 목록)
     */
    @Override
    public List<UserGrpMngListDto> searchExcelList(String grpNm) {
        List<UserGrpMngListDto> excelList = getContentQuery(grpNm, getQueryFactory()).fetch();
        return addRowNum(excelList, 1, excelList.size());
    }
}
