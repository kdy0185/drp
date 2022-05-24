package com.jsplan.drp.global.obj.repository;

import static com.jsplan.drp.domain.sys.authmng.entity.QAuthMng.authMng;
import static com.jsplan.drp.domain.sys.codemng.entity.QCodeMng.codeMng;
import static com.jsplan.drp.domain.sys.menumng.entity.QMenuAuthMng.menuAuthMng;
import static com.jsplan.drp.domain.sys.menumng.entity.QMenuMng.menuMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserGrpMng.userGrpMng;
import static com.jsplan.drp.domain.sys.usermng.entity.QUserMng.userMng;

import com.jsplan.drp.domain.sys.menumng.entity.MenuAuthMng;
import com.jsplan.drp.domain.sys.menumng.entity.MenuMng;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.global.obj.dto.ComsDTO;
import com.jsplan.drp.global.obj.dto.ComsMenuDTO;
import com.jsplan.drp.global.obj.vo.UseStatus;
import com.jsplan.drp.global.util.StringUtil;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Class : ComsCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-05-20
 * @Description : 공통 Custom Repository Impl
 */
public class ComsCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    ComsCustomRepository {

    /**
     * <p>공통 Custom Repository Impl 생성자</p>
     */
    public ComsCustomRepositoryImpl() {
        super(UserMng.class);
    }

    /**
     * <p>메뉴 목록</p>
     *
     * @return List (메뉴 목록)
     */
    @Override
    public List<ComsMenuDTO> selectComsMenuList() {
        // 대메뉴 정렬
        OrderSpecifier<Integer> menuOrdAsc = menuMng.menuCd.substring(0, 1)
            .when("A").then(1)
            .when("P").then(2)
            .when("M").then(3)
            .when("S").then(4)
            .when("Z").then(5)
            .otherwise(9)
            .asc();

        // 중메뉴, 소메뉴 정렬
        OrderSpecifier<String> menuSubOrdAsc = menuMng.menuCd.substring(1,
            menuMng.menuLv.multiply(2).subtract(1)).asc();

        List<MenuMng> menuMngList = selectFrom(menuMng)
            .where(menuMng.useYn.eq(UseStatus.Y))
            .orderBy(menuOrdAsc, menuSubOrdAsc)
            .fetch();

        return menuMngList.stream()
            .map(v -> new ComsMenuDTO(v.getMenuCd(), v.getMenuNm(), v.getUpperMenuCd(),
                v.getMenuUrl(), v.getMenuLv(), v.getLastYn(), getAuthCd(v.getMenuAuthMng())))
            .collect(Collectors.toList());
    }

    /**
     * <p>메뉴별 권한 목록</p>
     *
     * @param menuAuthMng (메뉴 권한 매핑 Entity)
     * @return String (권한 목록)
     */
    private String getAuthCd(List<MenuAuthMng> menuAuthMng) {
        return menuAuthMng.stream()
            .map(v -> StringUtil.quote(v.getAuthMng().getAuthCd()))
            .collect(Collectors.joining(", "));
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuCd (메뉴 코드)
     * @return ComsMenuDTO (메뉴 정보)
     */
    @Override
    public ComsMenuDTO selectComsMenuDetail(String menuCd) {
        return select(Projections.fields(ComsMenuDTO.class,
            menuMng.menuCd,
            menuMng.menuNm,
            menuMng.menuEngNm,
            menuMng.upperMenuMng.menuCd.as("upperMenuCd"),
            menuMng.upperMenuMng.menuNm.as("upperMenuNm"),
            menuMng.menuLv))
            .from(menuMng)
            .where(menuMng.menuCd.eq(menuCd))
            .fetchOne();
    }

    /**
     * <p>URL별 권한 목록</p>
     *
     * @return List (URL별 권한 목록)
     */
    @Override
    public List<ComsMenuDTO> selectComsUrlAuthList() {
        return select(Projections.fields(ComsMenuDTO.class,
            menuMng.menuUrl,
            menuAuthMng.authMng.authCd))
            .from(menuMng)
            .join(menuMng.menuAuthMng, menuAuthMng)
            .orderBy(menuMng.menuLv.desc())
            .fetch();
    }

    /**
     * <p>계층화 권한 목록</p>
     *
     * @return List (계층화 권한 목록)
     */
    @Override
    public List<ComsMenuDTO> selectHierarchicalAuthList() {
        return select(Projections.fields(ComsMenuDTO.class,
            authMng.authCd,
            authMng.upperAuthMng.authCd.as("upperAuthCd")))
            .from(authMng)
            .where(authMng.useYn.eq(UseStatus.Y), authMng.upperAuthMng.authCd.isNotNull())
            .fetch();
    }

    /**
     * <p>공통 코드 목록</p>
     *
     * @param grpCd (그룹 코드)
     * @return List (공통 코드 목록)
     */
    @Override
    public List<ComsDTO> selectComsCodeList(String grpCd) {
        return select(Projections.fields(ComsDTO.class,
            codeMng.codeMngId.comCd,
            codeMng.comNm))
            .from(codeMng)
            .where(codeMng.useYn.eq(UseStatus.Y), codeMng.codeMngId.grpCd.eq(grpCd))
            .orderBy(codeMng.ord.asc())
            .fetch();
    }

    /**
     * <p>그룹 목록</p>
     *
     * @return List (그룹 목록)
     */
    @Override
    public List<ComsDTO> selectComsGrpList() {
        return select(Projections.fields(ComsDTO.class,
            userGrpMng.grpCd,
            userGrpMng.grpNm))
            .from(userGrpMng)
            .orderBy(userGrpMng.grpNm.asc())
            .fetch();
    }

    /**
     * <p>담당자 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return List (담당자 목록)
     */
    @Override
    public List<ComsDTO> selectComsUserList(String grpCd, String searchCd, String searchWord) {
        return select(Projections.fields(ComsDTO.class,
            userMng.userGrpMng.grpCd,
            userMng.userGrpMng.grpNm,
            userMng.userId,
            userMng.userNm))
            .from(userMng)
            .where(grpCdEq(grpCd), userLike(searchCd, searchWord))
            .fetch();
    }

    /**
     * <p>담당자 목록 조회 조건 : 그룹</p>
     *
     * @param grpCd (그룹 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression grpCdEq(String grpCd) {
        return !StringUtil.isEmpty(grpCd) ? userMng.userGrpMng.grpCd.eq(grpCd) : null;
    }

    /**
     * <p>담당자 목록 조회 조건 : 담당자</p>
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
}
