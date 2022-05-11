package com.jsplan.drp.domain.sys.menumng.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.domain.sys.menumng.dto.MenuAuthMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngDetailDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngRequest;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngRequestBuilder;
import com.jsplan.drp.domain.sys.menumng.entity.MenuMng;
import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : MenuMngRepositoryTest
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 관리 Repository Test
 */
@SpringBootTest
@Transactional
class MenuMngRepositoryTest {

    @Autowired
    MenuMngRepository menuMngRepository;

    MenuMngRequest request;
    String menuCd, upperMenuCd, menuNm, menuUrl, authCd;
    Integer menuLv, menuOrd;
    UseStatus useYn;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        menuCd = "P0400";
        upperMenuCd = "P0000";
        menuNm = "TEST";
        menuUrl = "/pl/test/planTestList.do";
        menuLv = 2;
        menuOrd = 4;
        useYn = UseStatus.Y;
        authCd = "AUTH_SNS";

        request = MenuMngRequestBuilder.build(menuCd, upperMenuCd, menuNm, null, menuUrl,
            null, menuLv, menuOrd, useYn, authCd);
    }

    @Test
    @DisplayName("메뉴 목록 조회 테스트")
    public void searchMenuMngList() {
        // given
        String menuCd = "P0000";
        String searchCd = "menuNm";
        String searchWord = "";

        // when
        List<MenuMngListDTO> menuMngList = menuMngRepository.searchMenuMngList(menuCd, searchCd,
            searchWord, useYn);

        // then
        assertThat(menuMngList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("메뉴 상세 조회 테스트")
    public void findMenuMngByMenuCd() {
        // given
        String menuCd = "P0100";

        // when
        MenuMngDetailDTO detailDTO = menuMngRepository.findMenuMngByMenuCd(menuCd);

        // then
        assertThat(detailDTO.getMenuNm()).isEqualTo("데일리 리포트");
        assertThat(detailDTO.getMenuUrl()).isEqualTo("/pl/report/planReportList.do");
    }

    @Test
    @DisplayName("메뉴별 권한 목록 조회 테스트")
    public void searchMenuAuthMngList() {
        // given
        String menuCd = "P0201,P0202,P0203";
        List<String> menuCdList = List.of(menuCd.split(","));

        // when
        List<MenuAuthMngListDTO> authMngList = menuMngRepository.searchMenuAuthMngList(menuCdList,
            "AUTH_NORMAL");

        // then
        for (MenuAuthMngListDTO listDTO : authMngList) {
            System.out.println("===================================");
            System.out.println(listDTO.toString());
            System.out.println("===================================");
        }
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 등록 테스트")
    public void insertMenuMngData() {
        // when
        MenuMng savedMenuMng = menuMngRepository.save(request.toEntity());
        String findMenuCd = menuMngRepository.findMenuMngByMenuCd(savedMenuMng.getMenuCd())
            .getMenuCd();

        // then
        assertThat(findMenuCd).isEqualTo(menuCd);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 수정 테스트")
    public void updateMenuMngData() {
        // given
        String menuCd = "P0100";
        String menuNm = "메뉴 수정";
        String menuUrl = "/pl/test/planModifyList.do";
        LocalDateTime beforeDate = LocalDateTime.now();

        // when
        MenuMngRequest request = MenuMngRequestBuilder.build(menuCd, upperMenuCd, menuNm, null,
            menuUrl, null, menuLv, menuOrd, useYn, authCd);
        MenuMng menuMng = menuMngRepository.findById(request.getMenuCd())
            .orElseThrow(NoSuchElementException::new);
        menuMng.updateMenuMng(request);
        menuMngRepository.flush();

        // then
        assertThat(menuMng.getMenuNm()).isEqualTo(menuNm);
        assertThat(menuMng.getMenuUrl()).isEqualTo(menuUrl);
        assertThat(menuMng.getModDate()).isAfter(beforeDate);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 삭제 테스트")
    public void deleteMenuMngData() {
        // when
        menuMngRepository.saveAndFlush(request.toEntity());

        menuMngRepository.deleteById(menuCd);
        MenuMngDetailDTO detailDTO = menuMngRepository.findMenuMngByMenuCd(menuCd);

        // then
        assertThat(detailDTO).isNull();
    }
}
