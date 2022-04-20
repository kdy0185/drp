package com.jsplan.drp.domain.sys.usermng.repository;

import static com.jsplan.drp.domain.sys.usermng.entity.QUserGrpMng.userGrpMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserMng.userMng;

import com.jsplan.drp.domain.sys.usermng.dto.QUserMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.QUserMngListDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngListDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.global.obj.entity.UseStatus;
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
 * @Class : UserMngCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-04-08
 * @Description : 사용자 관리 Custom Repository Impl
 */
public class UserMngCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    UserMngCustomRepository {

    /**
     * <p>사용자 관리 Custom Repository Impl 생성자</p>
     */
    public UserMngCustomRepositoryImpl() {
        super(UserMng.class);
    }

    /**
     * <p>사용자 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @param pageable   (페이징 정보)
     * @return Page (페이징 목록)
     */
    @Override
    public Page<UserMngListDto> searchPageList(String grpCd, String searchCd,
        String searchWord, UseStatus useYn, Pageable pageable) {
        return applyPagination(pageable,
            contentQuery -> getContentQuery(grpCd, searchCd, searchWord, useYn, contentQuery),
            countQuery -> getCountQuery(grpCd, searchCd, searchWord, useYn, countQuery)
        );
    }

    /**
     * <p>사용자 목록 쿼리</p>
     *
     * @param grpCd        (그룹 코드)
     * @param searchCd     (검색 조건)
     * @param searchWord   (검색어)
     * @param useYn        (사용 여부)
     * @param contentQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<UserMngListDto> getContentQuery(String grpCd, String searchCd,
        String searchWord, UseStatus useYn, JPAQueryFactory contentQuery) {
        return contentQuery.select(new QUserMngListDto(
                userMng.userGrpMng.grpCd,
                userMng.userGrpMng.grpNm,
                userMng.userId,
                userMng.userNm,
                userMng.mobileNum,
                userMng.email,
                Expressions.simpleTemplate(String.class, "getCodeNm({0}, {1})",
                    "USER_TYPE", userMng.userType),
                userMng.useYn,
                userMng.regDate,
                userMng.modDate))
            .from(userMng)
            .join(userMng.userGrpMng, userGrpMng)
            .where(grpCdEq(grpCd), userLike(searchCd, searchWord), useYnEq(useYn))
            .orderBy(userMng.regDate.desc());
    }

    /**
     * <p>사용자 목록 카운트 쿼리</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @param countQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<Long> getCountQuery(String grpCd, String searchCd,
        String searchWord, UseStatus useYn, JPAQueryFactory countQuery) {
        return countQuery.select(userMng.count())
            .from(userMng)
            .where(grpCdEq(grpCd), userLike(searchCd, searchWord), useYnEq(useYn));
    }

    /**
     * <p>사용자 목록 조회 조건 : 그룹</p>
     *
     * @param grpCd (그룹 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression grpCdEq(String grpCd) {
        return StringUtils.hasText(grpCd) ? userMng.userGrpMng.grpCd.eq(grpCd) : null;
    }

    /**
     * <p>사용자 목록 조회 조건 : 사용자</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression userLike(String searchCd, String searchWord) {
        if (StringUtils.hasText(searchWord)) {
            if ("userId".equals(searchCd)) {
                return userMng.userId.contains(searchWord);
            }
            if ("userNm".equals(searchCd)) {
                return userMng.userNm.contains(searchWord);
            }
        }
        return null;
    }

    /**
     * <p>사용자 목록 조회 조건 : 사용 여부</p>
     *
     * @param useYn (사용 여부)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression useYnEq(UseStatus useYn) {
        return useYn != null ? userMng.useYn.eq(useYn) : null;
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param userId (사용자 아이디)
     * @return UserMngDetailDto (사용자 DTO)
     */
    public UserMngDetailDto findByUserId(String userId) {
        return select(new QUserMngDetailDto(
            userMng.userGrpMng.grpCd,
            userMng.userId,
            userMng.userNm,
            userMng.mobileNum,
            userMng.email,
            userMng.userType,
            userMng.useYn,
            userMng.regDate,
            userMng.modDate))
            .from(userMng)
            .where(userMng.userId.eq(userId))
            .fetchOne();
    }

    /**
     * <p>사용자 엑셀 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (사용자 목록)
     */
    @Override
    public List<UserMngListDto> searchExcelList(String grpCd, String searchCd,
        String searchWord, UseStatus useYn) {
        List<UserMngListDto> excelList = getContentQuery(grpCd, searchCd, searchWord, useYn,
            getQueryFactory()).fetch();
        return addRowNum(excelList, 1, excelList.size());
    }
}
