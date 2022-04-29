package com.jsplan.drp.domain.sys.authmng.repository;

import static com.jsplan.drp.domain.sys.authmng.entity.QAuthMng.authMng;
import static com.jsplan.drp.domain.sys.menumng.entity.QMenuAuthMng.menuAuthMng;
import static com.jsplan.drp.domain.sys.menumng.entity.QMenuMng.menuMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserAuthMng.userAuthMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserMng.userMng;

import com.jsplan.drp.domain.sys.authmng.dto.AuthMenuMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngDetailDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthUserMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.QAuthMngDetailDTO;
import com.jsplan.drp.domain.sys.authmng.dto.QAuthUserMngListDTO;
import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import com.jsplan.drp.domain.sys.menumng.entity.MenuMng;
import com.jsplan.drp.global.obj.entity.UseStatus;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.jsplan.drp.global.util.StringUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : AuthMngCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 관리 Custom Repository Impl
 */
public class AuthMngCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    AuthMngCustomRepository {

    /**
     * <p>권한 관리 Custom Repository Impl 생성자</p>
     */
    public AuthMngCustomRepositoryImpl() {
        super(AuthMng.class);
    }

    /**
     * <p>권한 목록</p>
     *
     * @param authCd     (권한 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (권한 목록)
     */
    @Override
    public List<AuthMngListDTO> searchAuthMngList(String authCd, String searchCd, String searchWord,
        UseStatus useYn) {
        List<AuthMng> authMngList = Objects.requireNonNull(
            getAuthMngListQuery(authCd, searchCd, searchWord, useYn, "list")).fetch();

        return authMngList.stream()
            .map(v -> new AuthMngListDTO(v.getAuthCd(), v.getAuthNm(), v.getAuthDesc(),
                v.getAuthLv(), v.getAuthOrd(), v.getUseYn(), v.getLastYn()))
            .collect(Collectors.toList());
    }

    /**
     * <p>권한 목록 쿼리</p>
     *
     * @param authCd     (권한 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @param queryType  (쿼리 유형)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<AuthMng> getAuthMngListQuery(String authCd, String searchCd, String searchWord,
        UseStatus useYn, String queryType) {
        JPAQuery<AuthMng> contentQuery = selectFrom(authMng)
            .where(upperAuthCdEq(authCd, queryType), authLike(searchCd, searchWord), useYnEq(useYn));

        if ("list".equals(queryType)) {
            return contentQuery.orderBy(authMng.authOrd.asc());
        }
        if ("excel".equals(queryType)) {
            return contentQuery.orderBy(authMng.authCd.asc());
        }
        return null;
    }

    /**
     * <p>권한 목록 조회 조건 : 상위 권한 코드</p>
     *
     * @param authCd    (권한 코드)
     * @param queryType (쿼리 유형)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression upperAuthCdEq(String authCd, String queryType) {
        if ("list".equals(queryType)) {
            return !StringUtil.isEmpty(authCd) ? authMng.upperAuthMng.authCd.eq(authCd)
                : authMng.upperAuthMng.authCd.isNull();
        }
        return null;
    }

    /**
     * <p>권한 목록 조회 조건 : 권한</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression authLike(String searchCd, String searchWord) {
        if (!StringUtil.isEmpty(searchWord)) {
            if ("authCd".equals(searchCd)) {
                return authMng.authCd.contains(searchWord);
            }
            if ("authNm".equals(searchCd)) {
                return authMng.authNm.contains(searchWord);
            }
        }
        return null;
    }

    /**
     * <p>권한 목록 조회 조건 : 사용 여부</p>
     *
     * @param useYn (사용 여부)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression useYnEq(UseStatus useYn) {
        return useYn != null ? authMng.useYn.eq(useYn) : null;
    }

    /**
     * <p>권한 상세</p>
     *
     * @param authCd (권한 코드)
     * @return AuthMngDetailDTO (권한 DTO)
     */
    @Override
    public AuthMngDetailDTO findAuthMngByAuthCd(String authCd) {
        return select(new QAuthMngDetailDTO(
            authMng.authCd,
            authMng.upperAuthMng.authCd,
            authMng.authNm,
            authMng.authDesc,
            authMng.authLv,
            authMng.authOrd,
            authMng.useYn))
            .from(authMng)
            .where(authMng.authCd.eq(authCd))
            .fetchOne();
    }

    /**
     * <p>권한별 사용자 확인</p>
     *
     * @param authCd (권한 코드)
     * @return boolean (사용자 존재 여부)
     */
    @Override
    public boolean existsAuthUserMngByAuthCd(String authCd) {
        Long userAuthMngCnt = select(userAuthMng.count())
            .from(userAuthMng)
            .where(userAuthMng.authMng.authCd.eq(authCd))
            .fetchOne();
        return userAuthMngCnt != null && userAuthMngCnt > 0;
    }

    /**
     * <p>권한별 메뉴 확인</p>
     *
     * @param authCd (권한 코드)
     * @return boolean (메뉴 존재 여부)
     */
    @Override
    public boolean existsAuthMenuMngByAuthCd(String authCd) {
        Long userMenuMngCnt = select(menuAuthMng.count())
            .from(menuAuthMng)
            .where(menuAuthMng.authMng.authCd.eq(authCd))
            .fetchOne();
        return userMenuMngCnt != null && userMenuMngCnt > 0;
    }

    /**
     * <p>권한별 사용자 목록</p>
     *
     * @param authCdList (권한 코드 목록)
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param pageable   (페이징 정보)
     * @return Page (사용자 목록)
     */
    @Override
    public Page<AuthUserMngListDTO> searchAuthUserMngList(List<String> authCdList, String grpCd,
        String searchCd, String searchWord, Pageable pageable) {
        Page<AuthUserMngListDTO> authUserMngList = applyPagination(pageable,
            contentQuery -> getAuthUserMngListQuery(grpCd, searchCd, searchWord, contentQuery),
            countQuery -> getAuthUserMngCountQuery(grpCd, searchCd, searchWord, countQuery));

        authUserMngList.forEach(v -> v.setAuthYn(getUserAuthYn(authCdList, v.getUserId())));
        return authUserMngList;
    }

    /**
     * <p>권한별 사용자 목록 쿼리</p>
     *
     * @param grpCd        (그룹 코드)
     * @param searchCd     (검색 조건)
     * @param searchWord   (검색어)
     * @param contentQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<AuthUserMngListDTO> getAuthUserMngListQuery(String grpCd, String searchCd,
        String searchWord, JPAQueryFactory contentQuery) {
        return contentQuery.select(new QAuthUserMngListDTO(
                userMng.userId,
                userMng.userNm,
                userMng.mobileNum
            ))
            .from(userMng)
            .where(grpCdEq(grpCd), userLike(searchCd, searchWord))
            .orderBy(userMng.userId.asc());
    }

    /**
     * <p>권한별 사용자 카운트 쿼리</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param countQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<Long> getAuthUserMngCountQuery(String grpCd, String searchCd,
        String searchWord, JPAQueryFactory countQuery) {
        return countQuery.select(userMng.count())
            .from(userMng)
            .where(grpCdEq(grpCd), userLike(searchCd, searchWord));
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
     * <p>사용자별 권한 여부 조회</p>
     *
     * @param authCdList (권한 목록)
     * @param userId     (사용자 아이디)
     * @return String (권한 여부)
     */
    private String getUserAuthYn(List<String> authCdList, String userId) {
        Long authCnt = select(userAuthMng.userMng.userId.count())
            .from(userAuthMng)
            .where(userAuthMng.authMng.authCd.in(authCdList),
                userAuthMng.userMng.userId.eq(userId))
            .groupBy(userAuthMng.userMng.userId)
            .fetchOne();
        return authCnt != null && authCnt > 0 ? "허용" : "허용하지 않음";
    }

    /**
     * <p>권한별 메뉴 목록</p>
     *
     * @param authCdList (권한 코드 목록)
     * @param menuCd     (메뉴 코드)
     * @return List (메뉴 목록)
     */
    @Override
    public List<AuthMenuMngListDTO> searchAuthMenuMngList(List<String> authCdList, String menuCd) {
        List<MenuMng> menuMngList = selectFrom(menuMng)
            .where(upperMenuCdEq(menuCd))
            .orderBy(menuMng.menuOrd.asc())
            .fetch();

        return menuMngList.stream()
            .map(v -> new AuthMenuMngListDTO(v.getMenuCd(), v.getMenuNm(),
                getMenuAuthYn(authCdList, v.getMenuCd()), v.getLastYn()))
            .collect(Collectors.toList());
    }

    /**
     * <p>메뉴별 권한 여부 조회</p>
     *
     * @param authCdList (권한 목록)
     * @param menuCd     (메뉴 코드)
     * @return String (권한 여부)
     */
    private String getMenuAuthYn(List<String> authCdList, String menuCd) {
        Long authCnt = select(menuAuthMng.menuMng.menuCd.count())
            .from(menuAuthMng)
            .where(menuAuthMng.authMng.authCd.in(authCdList),
                menuAuthMng.menuMng.menuCd.eq(menuCd))
            .groupBy(menuAuthMng.menuMng.menuCd)
            .fetchOne();
        return authCnt != null && authCnt > 0 ? "Y" : "N";
    }

    /**
     * <p>권한별 메뉴 목록 조회 조건 : 상위 메뉴 코드</p>
     *
     * @param menuCd (메뉴 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression upperMenuCdEq(String menuCd) {
        return !StringUtil.isEmpty(menuCd) ? menuMng.upperMenuMng.menuCd.eq(menuCd)
            : menuMng.upperMenuMng.menuCd.isNull();
    }

    /**
     * <p>권한 엑셀 목록</p>
     *
     * @param authCd     (권한 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (권한 목록)
     */
    @Override
    public List<AuthMngListDTO> searchAuthMngExcelList(String authCd, String searchCd,
        String searchWord, UseStatus useYn) {
        List<AuthMng> authMngList = Objects.requireNonNull(
            getAuthMngListQuery(authCd, searchCd, searchWord, useYn, "excel")).fetch();

        List<AuthMngListDTO> excelList = authMngList.stream()
            .map(v -> new AuthMngListDTO(v.getAuthCd(), v.getAuthNm(), v.getAuthDesc(),
                v.getAuthLv(), v.getAuthOrd(), v.getUseYn(), null))
            .collect(Collectors.toList());

        return addRowNum(excelList, 1, excelList.size());
    }
}
