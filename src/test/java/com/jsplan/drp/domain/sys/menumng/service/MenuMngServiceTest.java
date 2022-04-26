package com.jsplan.drp.domain.sys.menumng.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jsplan.drp.domain.sys.menumng.dto.MenuAuthMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngDetailDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngRequest;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngRequestBuilder;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngSearchDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngSearchDTOBuilder;
import com.jsplan.drp.domain.sys.menumng.entity.MenuMng;
import com.jsplan.drp.domain.sys.menumng.repository.MenuMngRepository;
import com.jsplan.drp.global.obj.entity.UseStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.sf.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @Class : MenuMngServiceTest
 * @Author : KDW
 * @Date : 2022-04-26
 * @Description : 메뉴 관리 Service Test
 */
@ExtendWith(MockitoExtension.class)
class MenuMngServiceTest {

    @InjectMocks
    MenuMngService menuMngService;

    @Mock
    MenuMngRepository menuMngRepository;

    MenuMngRequest request;
    MenuMng menuMng;
    MenuMngSearchDTO searchDTO;
    MenuMngListDTO listDTO;
    MenuMngDetailDTO detailDTO;

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

        request = MenuMngRequestBuilder.build(menuCd, upperMenuCd, menuNm, null, menuUrl, null,
            menuLv, menuOrd, useYn, authCd);
        menuMng = MenuMng.builder().menuCd(menuCd)
            .upperMenuMng(MenuMng.builder().menuCd(upperMenuCd).build()).menuNm(menuNm)
            .menuUrl(menuUrl).menuLv(menuLv).menuOrd(menuOrd).useYn(useYn).menuAuthMng(authCd)
            .build();

        searchDTO = MenuMngSearchDTOBuilder.build(menuCd, "menuCd", "", useYn);

        listDTO = new MenuMngListDTO(menuCd, menuNm, menuUrl, null, menuLv, menuOrd, useYn, "Y");
        detailDTO = new MenuMngDetailDTO(menuCd, upperMenuCd, menuNm, null, menuUrl, null, menuLv,
            menuOrd, useYn);
    }

    @Test
    @DisplayName("메뉴 목록 조회 테스트")
    public void selectMenuMngList() throws Exception {
        // given
        List<MenuMngListDTO> menuMngList = new ArrayList<>();
        menuMngList.add(listDTO);

        // mocking
        given(menuMngRepository.searchMenuMngList(anyString(), anyString(), anyString(),
            any())).willReturn(menuMngList);

        // when
        JSONArray menuMngArray = menuMngService.selectMenuMngList(searchDTO);

        // then
        assertThat(menuMngArray.getJSONObject(0).get("menuCd")).isEqualTo(menuCd);
        assertThat(menuMngArray.getJSONObject(0).get("menuNm")).isEqualTo(menuNm);
        assertThat(menuMngArray.getJSONObject(0).get("leaf")).isEqualTo(true);
    }

    @Test
    @DisplayName("메뉴 상세 조회 테스트")
    public void selectMenuMngDetail() throws Exception {
        // mocking
        given(menuMngRepository.findMenuMngByMenuCd(anyString())).willReturn(detailDTO);

        // when
        MenuMngDetailDTO findDetail = menuMngService.selectMenuMngDetail(request);

        // then
        assertThat(findDetail.getMenuCd()).isEqualTo(menuCd);
        assertThat(findDetail.getMenuUrl()).isEqualTo(menuUrl);
    }

    @Test
    @DisplayName("메뉴별 권한 목록 조회 테스트")
    public void selectMenuAuthMngList() throws Exception {
        // given
        request.setMenuCd("P0201,P0202,P0203");
        request.setAuthCd("AUTH_NORMAL");

        List<MenuAuthMngListDTO> menuAuthMngList = new ArrayList<>();
        menuAuthMngList.add(new MenuAuthMngListDTO("AUTH_SNS", "SNS 회원 권한", "Y", "Y"));

        // mocking
        given(menuMngRepository.searchMenuAuthMngList(anyList(), anyString())).willReturn(
            menuAuthMngList);

        // when
        JSONArray authArray = menuMngService.selectMenuAuthMngList(request);

        // then
        assertThat(authArray.getJSONObject(0).get("id")).isEqualTo("AUTH_SNS");
        assertThat(authArray.getJSONObject(0).get("leaf")).isEqualTo(true);
        assertThat(authArray.getJSONObject(0).get("checked")).isEqualTo(true);
    }

    @Test
    @DisplayName("메뉴 등록 테스트")
    public void insertMenuMngData() throws Exception {
        // given
        given(menuMngRepository.save(any())).willReturn(menuMng);
        given(menuMngRepository.findMenuMngByMenuCd(anyString())).willReturn(detailDTO);

        // when
        menuMngService.insertMenuMngData(request);
        String findMenuCd = menuMngService.selectMenuMngDetail(request).getMenuCd();

        // then
        assertThat(findMenuCd).isEqualTo(menuCd);
    }

    @Test
    @DisplayName("메뉴 수정 테스트")
    public void updateMenuMngData() throws Exception {
        // given
        request = MenuMngRequestBuilder.build(menuCd, upperMenuCd, "메뉴 수정", null,
            "/pl/test/planModifyList.do", null, menuLv, menuOrd, useYn, authCd);

        // mocking
        when(menuMngRepository.findById(any())).thenReturn(Optional.of(menuMng));

        // when
        menuMngService.updateMenuMngData(request);

        // then
        assertThat(menuMng.getMenuNm()).isEqualTo(request.getMenuNm());
        assertThat(menuMng.getMenuUrl()).isEqualTo(request.getMenuUrl());
    }

    @Test
    @DisplayName("메뉴 삭제 테스트")
    public void deleteMenuMngData() throws Exception {
        // when
        menuMngService.deleteMenuMngData(request);

        // then
        verify(menuMngRepository, times(1)).deleteById(request.getMenuCd());
    }

    @Test
    @DisplayName("권한 설정 적용 테스트")
    public void updateMenuAuthMngData() throws Exception {
        // given
        String menuCdList = "P0400";
        String authCdList = "AUTH_NORMAL";

        // mocking
        when(menuMngRepository.findById(any())).thenReturn(Optional.of(menuMng));

        // when
        menuMngService.updateMenuAuthMngData(menuCdList, authCdList);

        // then
        assertThat(menuMng.getMenuCd()).isEqualTo(menuCdList);
        assertThat(menuMng.getMenuAuthMng().get(0).getAuthMng().getAuthCd()).isEqualTo(authCdList);
    }
}
