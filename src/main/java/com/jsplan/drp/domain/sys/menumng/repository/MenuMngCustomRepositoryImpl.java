package com.jsplan.drp.domain.sys.menumng.repository;

import static com.jsplan.drp.domain.sys.authmng.entity.QAuthMng.authMng;
import static com.jsplan.drp.domain.sys.menumng.entity.QMenuAuthMng.menuAuthMng;
import static com.jsplan.drp.domain.sys.menumng.entity.QMenuMng.menuMng;

import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import com.jsplan.drp.domain.sys.menumng.dto.MenuAuthMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngDetailDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.QMenuMngDetailDTO;
import com.jsplan.drp.domain.sys.menumng.entity.MenuMng;
import com.jsplan.drp.global.obj.entity.UseStatus;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.jsplan.drp.global.util.StringUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Class : MenuMngCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 관리 Custom Repository Impl
 */
public class MenuMngCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    MenuMngCustomRepository {

    /**
     * <p>메뉴 관리 Custom Repository Impl 생성자</p>
     */
    public MenuMngCustomRepositoryImpl() {
        super(MenuMng.class);
    }

    /**
     * <p>메뉴 목록</p>
     *
     * @param menuCd     (메뉴 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (메뉴 목록)
     */
    @Override
    public List<MenuMngListDTO> searchMenuMngList(String menuCd, String searchCd, String searchWord,
        UseStatus useYn) {
        List<MenuMng> menuMngList = Objects.requireNonNull(
            getMenuMngListQuery(menuCd, searchCd, searchWord, useYn, "list")).fetch();

        return menuMngList.stream()
            .map(v -> new MenuMngListDTO(v.getMenuCd(), v.getMenuNm(), v.getMenuUrl(), null,
                v.getMenuLv(), v.getMenuOrd(), v.getUseYn(), v.getLastYn()))
            .collect(Collectors.toList());
    }

    /**
     * <p>메뉴 목록 쿼리</p>
     *
     * @param menuCd     (메뉴 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @param queryType  (쿼리 유형)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<MenuMng> getMenuMngListQuery(String menuCd, String searchCd, String searchWord,
        UseStatus useYn, String queryType) {
        JPAQuery<MenuMng> contentQuery = selectFrom(menuMng)
            .where(upperMenuCdEq(menuCd, queryType), menuLike(searchCd, searchWord), useYnEq(useYn));

        if ("list".equals(queryType)) {
            return contentQuery.orderBy(menuMng.menuOrd.asc());
        }
        if ("excel".equals(queryType)) {
            return contentQuery.orderBy(menuMng.menuCd.asc());
        }
        return null;
    }

    /**
     * <p>메뉴 목록 조회 조건 : 상위 메뉴 코드</p>
     *
     * @param menuCd    (메뉴 코드)
     * @param queryType (쿼리 유형)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression upperMenuCdEq(String menuCd, String queryType) {
        if ("list".equals(queryType)) {
            return !StringUtil.isEmpty(menuCd) ? menuMng.upperMenuMng.menuCd.eq(menuCd)
                : menuMng.upperMenuMng.menuCd.isNull();
        }
        return null;
    }

    /**
     * <p>메뉴 목록 조회 조건 : 메뉴</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression menuLike(String searchCd, String searchWord) {
        if (!StringUtil.isEmpty(searchWord)) {
            if ("menuCd".equals(searchCd)) {
                return menuMng.menuCd.contains(searchWord);
            }
            if ("menuNm".equals(searchCd)) {
                return menuMng.menuNm.contains(searchWord);
            }
        }
        return null;
    }

    /**
     * <p>메뉴 목록 조회 조건 : 사용 여부</p>
     *
     * @param useYn (사용 여부)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression useYnEq(UseStatus useYn) {
        return useYn != null ? menuMng.useYn.eq(useYn) : null;
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuCd (메뉴 코드)
     * @return MenuMngDetailDTO (메뉴 DTO)
     */
    @Override
    public MenuMngDetailDTO findMenuMngByMenuCd(String menuCd) {
        return select(new QMenuMngDetailDTO(
            menuMng.menuCd,
            menuMng.upperMenuMng.menuCd,
            menuMng.menuNm,
            menuMng.menuEngNm,
            menuMng.menuUrl,
            menuMng.menuDesc,
            menuMng.menuLv,
            menuMng.menuOrd,
            menuMng.useYn))
            .from(menuMng)
            .where(menuMng.menuCd.eq(menuCd))
            .fetchOne();
    }

    /**
     * <p>메뉴별 권한 목록</p>
     *
     * @param menuCdList (메뉴 코드 목록)
     * @param authCd     (권한 코드)
     * @return List (권한 목록)
     */
    @Override
    public List<MenuAuthMngListDTO> searchMenuAuthMngList(List<String> menuCdList, String authCd) {
        List<AuthMng> authMngList = selectFrom(authMng)
            .where(authMng.useYn.eq(UseStatus.Y), upperAuthCdEq(authCd))
            .orderBy(authMng.authOrd.asc())
            .fetch();

        return authMngList.stream()
            .map(v -> new MenuAuthMngListDTO(v.getAuthCd(), v.getAuthNm(),
                getAuthYn(menuCdList, v.getAuthCd()), v.getLastYn()))
            .collect(Collectors.toList());
    }

    /**
     * <p>메뉴별 권한 목록 조회 조건 : 상위 권한 코드</p>
     *
     * @param authCd (권한 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression upperAuthCdEq(String authCd) {
        return !StringUtil.isEmpty(authCd) ? authMng.upperAuthMng.authCd.eq(authCd)
            : authMng.upperAuthMng.authCd.isNull();
    }

    /**
     * <p>메뉴 권한 여부 조회</p>
     *
     * @param menuCdList (메뉴 코드 목록)
     * @param authCd     (권한 코드)
     * @return String (권한 여부)
     */
    private String getAuthYn(List<String> menuCdList, String authCd) {
        Long authCnt = select(menuAuthMng.authMng.authCd.count())
            .from(menuAuthMng)
            .where(menuAuthMng.menuMng.menuCd.in(menuCdList),
                menuAuthMng.authMng.authCd.eq(authCd))
            .groupBy(menuAuthMng.authMng.authCd)
            .fetchOne();
        return authCnt != null && authCnt > 0 ? "Y" : "N";
    }

    /**
     * <p>메뉴 엑셀 목록</p>
     *
     * @param menuCd     (메뉴 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (메뉴 목록)
     */
    @Override
    public List<MenuMngListDTO> searchMenuMngExcelList(String menuCd, String searchCd,
        String searchWord, UseStatus useYn) {
        List<MenuMng> menuMngList = Objects.requireNonNull(
            getMenuMngListQuery(menuCd, searchCd, searchWord, useYn, "excel")).fetch();

        List<MenuMngListDTO> excelList = menuMngList.stream()
            .map(v -> new MenuMngListDTO(v.getMenuCd(), v.getMenuNm(), v.getMenuUrl(),
                v.getMenuDesc(), v.getMenuLv(), v.getMenuOrd(), v.getUseYn(), null))
            .collect(Collectors.toList());

        return addRowNum(excelList, 1, excelList.size());
    }
}
