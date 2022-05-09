package com.jsplan.drp.domain.sys.menumng.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.jsplan.drp.domain.sys.menumng.dto.MenuMngDetailDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngRequest;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngRequestBuilder;
import com.jsplan.drp.domain.sys.menumng.service.MenuMngService;
import com.jsplan.drp.global.obj.entity.UseStatus;
import java.util.Locale;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @Class : MenuMngControllerTest
 * @Author : KDW
 * @Date : 2022-04-26
 * @Description : 메뉴 관리 Controller Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class MenuMngControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    MenuMngService menuMngService;

    MenuMngRequest request;
    MenuMngDetailDTO detailDTO;
    String menuCd, upperMenuCd, menuNm, menuUrl, authCd;
    Integer menuLv, menuOrd;
    UseStatus useYn;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .defaultRequest(MockMvcRequestBuilders.head("/**/*.do").locale(Locale.KOREA))
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();

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
        menuMngService.insertMenuMngData(request);
        detailDTO = menuMngService.selectMenuMngDetail(request);
    }

    @AfterEach
    public void tearDown() {
        menuMngService.deleteMenuMngData(request);
    }

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 관리 테스트")
    public void menuMngList() throws Exception {
        mockMvc.perform(post("/sys/menumng/menuMngList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("sys/menumng/menuMngList"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 목록 조회 테스트")
    public void menuMngSearch() throws Exception {
        mockMvc.perform(get("/sys/menumng/menuMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("menuCd", "P0000")
                .param("searchCd", "menuCd")
                .param("searchWord", "")
            ).andExpect(jsonPath("$.menuMngList").isArray())
            .andExpect(jsonPath("$.menuMngList[0].menuCd").value("P0100"))
            .andExpect(jsonPath("$.menuMngList[0].menuUrl").value("/pl/report/planReportList.do"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 상세 조회 테스트")
    public void menuMngDetail() throws Exception {
        mockMvc.perform(post("/sys/menumng/menuMngDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("menuCd", menuCd)
                .param("detailStatus", "UPDATE")
            ).andExpect(view().name("sys/menumng/menuMngDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 등록 테스트")
    public void menuMngInsert() throws Exception {
        String menuCd = "P0500";
        String menuNm = "TEST5";
        Integer menuOrd = 5;

        mockMvc.perform(post("/sys/menumng/menuMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("menuCd", menuCd)
                .param("upperMenuCd", upperMenuCd)
                .param("menuNm", menuNm)
                .param("menuUrl", menuUrl)
                .param("menuLv", String.valueOf(menuLv))
                .param("menuOrd", String.valueOf(menuOrd))
                .param("useYn", useYn.getCode())
                .param("authCd", authCd)
            ).andExpect(jsonPath("$.menuCd").value(menuCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 등록 에러 테스트 : 빈 값")
    public void menuMngInsert_emptyValue() throws Exception {
        mockMvc.perform(post("/sys/menumng/menuMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("menuCd", menuCd)
                .param("upperMenuCd", upperMenuCd)
                .param("menuNm", menuNm)
                .param("menuUrl", "")
                .param("menuLv", "")
                .param("menuOrd", String.valueOf(menuOrd))
                .param("useYn", useYn.getCode())
                .param("authCd", authCd)
        ).andExpect(
            result -> assertTrue(
                Objects.requireNonNull(result.getResolvedException()).getClass()
                    .isAssignableFrom(BindException.class)
            )
        ).andReturn();
    }

    @Test
    @Order(6)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 등록 에러 테스트 : 중복 코드")
    public void menuMngInsert_dupCode() throws Exception {
        mockMvc.perform(post("/sys/menumng/menuMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("menuCd", menuCd)
                .param("upperMenuCd", upperMenuCd)
                .param("menuNm", menuNm)
                .param("menuUrl", menuUrl)
                .param("menuLv", String.valueOf(menuLv))
                .param("menuOrd", String.valueOf(menuOrd))
                .param("useYn", useYn.getCode())
                .param("authCd", authCd)
            ).andExpect(jsonPath("$.dataStatus").value("DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 수정 테스트")
    public void menuMngUpdate() throws Exception {
        String menuNm = "메뉴 수정";
        String menuUrl = "/pl/test/planModifyList.do";

        mockMvc.perform(put("/sys/menumng/menuMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("menuCd", menuCd)
                .param("upperMenuCd", upperMenuCd)
                .param("menuNm", menuNm)
                .param("menuUrl", menuUrl)
                .param("menuLv", String.valueOf(menuLv))
                .param("menuOrd", String.valueOf(menuOrd))
                .param("useYn", useYn.getCode())
                .param("authCd", authCd)
            ).andExpect(jsonPath("$.menuCd").value(menuCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 삭제 테스트")
    public void menuMngDelete() throws Exception {
        String menuCd = "P0500";

        mockMvc.perform(delete("/sys/menumng/menuMngDelete.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("menuCd", menuCd)
            ).andExpect(jsonPath("$.menuCd").value(menuCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴별 권한 목록 조회 테스트")
    public void menuAuthMngSearch() throws Exception {
        mockMvc.perform(get("/sys/menumng/menuAuthMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("menuCd", "P0201,P0202,P0203")
                .param("authCd", "AUTH_NORMAL")
            ).andExpect(jsonPath("$.menuAuthMngList").isArray())
            .andExpect(jsonPath("$.menuAuthMngList[0].id").value("AUTH_SNS"))
            .andExpect(jsonPath("$.menuAuthMngList[0].leaf").value(true))
            .andExpect(jsonPath("$.menuAuthMngList[0].checked").value(true))
            .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한 설정 적용 테스트")
    public void menuAuthMngUpdate() throws Exception {
        String menuCdList = "P0400";
        String authCdList = "AUTH_NORMAL";

        mockMvc.perform(put("/sys/menumng/menuAuthMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("menuCd", menuCdList)
                .param("authCd", authCdList)
            ).andExpect(jsonPath("$.menuCd").value(menuCdList))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }
}
