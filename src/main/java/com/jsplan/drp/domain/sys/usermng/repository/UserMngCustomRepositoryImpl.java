package com.jsplan.drp.domain.sys.usermng.repository;

import static com.jsplan.drp.domain.sys.authmng.entity.QAuthMng.authMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserAuthMng.userAuthMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserGrpMng.userGrpMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserMng.userMng;

import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import com.jsplan.drp.domain.sys.usermng.dto.QUserMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.QUserMngListDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserAuthMngListDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngListDTO;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.global.obj.vo.UseStatus;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.jsplan.drp.global.util.StringUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @return Page (사용자 목록)
     */
    @Override
    public Page<UserMngListDTO> searchUserMngList(String grpCd, String searchCd,
        String searchWord, UseStatus useYn, Pageable pageable) {
        return applyPagination(pageable,
            contentQuery -> getUserMngListQuery(grpCd, searchCd, searchWord, useYn, contentQuery),
            countQuery -> getUserMngCountQuery(grpCd, searchCd, searchWord, useYn, countQuery)
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
    private JPAQuery<UserMngListDTO> getUserMngListQuery(String grpCd, String searchCd,
        String searchWord, UseStatus useYn, JPAQueryFactory contentQuery) {
        return contentQuery.select(new QUserMngListDTO(
                userMng.userGrpMng.grpCd,
                userMng.userGrpMng.grpNm,
                userMng.userId,
                userMng.userNm,
                userMng.mobileNum,
                userMng.email,
                Expressions.simpleTemplate(String.class, "getCodeNm({0}, {1})", "USER_TYPE",
                    userMng.userType),
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
    private JPAQuery<Long> getUserMngCountQuery(String grpCd, String searchCd,
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
        return !StringUtil.isEmpty(grpCd) ? userMng.userGrpMng.grpCd.eq(grpCd) : null;
    }

    /**
     * <p>사용자 목록 조회 조건 : 사용자</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression userLike(String searchCd, String searchWord) {
        if (!StringUtil.isEmpty(searchWord)) {
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
     * @return UserMngDetailDTO (사용자 DTO)
     */
    public UserMngDetailDTO findUserMngByUserId(String userId) {
        return select(new QUserMngDetailDTO(
            userMng.userGrpMng.grpCd,
            userMng.userGrpMng.grpNm,
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
     * <p>사용자별 권한 목록</p>
     *
     * @param userIdList (사용자 아이디 목록)
     * @param authCd     (권한 코드)
     * @return List (권한 목록)
     */
    @Override
    public List<UserAuthMngListDTO> searchUserAuthMngList(List<String> userIdList, String authCd) {
        List<AuthMng> authMngList = selectFrom(authMng)
            .where(authMng.useYn.eq(UseStatus.Y), upperAuthCdEq(authCd))
            .orderBy(authMng.authOrd.asc())
            .fetch();

        return authMngList.stream()
            .map(v -> new UserAuthMngListDTO(v.getAuthCd(), v.getAuthNm(),
                getAuthYn(userIdList, v.getAuthCd()), v.getLastYn()))
            .collect(Collectors.toList());
    }

    /**
     * <p>사용자별 권한 목록 조회 조건 : 상위 권한 코드</p>
     *
     * @param authCd (권한 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression upperAuthCdEq(String authCd) {
        return !StringUtil.isEmpty(authCd) ? authMng.upperAuthMng.authCd.eq(authCd)
            : authMng.upperAuthMng.authCd.isNull();
    }

    /**
     * <p>사용자별 권한 여부 조회</p>
     *
     * @param userIdList (사용자 아이디 목록)
     * @param authCd     (권한 코드)
     * @return String (권한 여부)
     */
    private String getAuthYn(List<String> userIdList, String authCd) {
        Long userCnt = select(userAuthMng.authMng.authCd.count())
            .from(userAuthMng)
            .where(userAuthMng.userMng.userId.in(userIdList),
                userAuthMng.authMng.authCd.eq(authCd))
            .groupBy(userAuthMng.authMng.authCd)
            .fetchOne();
        return userCnt != null && userCnt > 0 ? "Y" : "N";
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
    public List<UserMngListDTO> searchUserMngExcelList(String grpCd, String searchCd,
        String searchWord, UseStatus useYn) {
        List<UserMngListDTO> excelList = getUserMngListQuery(grpCd, searchCd, searchWord, useYn,
            getQueryFactory()).fetch();
        return addRowNum(excelList, 1, excelList.size());
    }
}
