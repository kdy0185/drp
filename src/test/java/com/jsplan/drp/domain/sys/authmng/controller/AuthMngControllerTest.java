package com.jsplan.drp.domain.sys.authmng.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.jsplan.drp.domain.sys.authmng.dto.AuthMngDetailDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngRequest;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngRequestBuilder;
import com.jsplan.drp.domain.sys.authmng.service.AuthMngService;
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
 * @Class : AuthMngControllerTest
 * @Author : KDW
 * @Date : 2022-04-28
 * @Description : 권한 관리 Controller Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class AuthMngControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext ctx;

    @Autowired
    AuthMngService authMngService;

    AuthMngRequest request;
    AuthMngDetailDTO detailDTO;
    String authCd, upperAuthCd, authNm;
    Integer authLv, authOrd;
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
        authCd = "AUTH_TEST";
        upperAuthCd = "AUTH_ADMIN";
        authNm = "테스트 권한";
        authLv = 2;
        authOrd = 2;
        useYn = UseStatus.Y;

        request = AuthMngRequestBuilder.build(authCd, upperAuthCd, authNm, null, authLv, authOrd);
        authMngService.insertAuthMngData(request);
        detailDTO = authMngService.selectAuthMngDetail(request);
    }

    @AfterEach
    public void tearDown() {
        authMngService.deleteAuthMngData(request);
    }

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한 관리 테스트")
    public void authMngList() throws Exception {
        mockMvc.perform(post("/sys/authmng/authMngList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("sys/authmng/authMngList"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한 목록 조회 테스트")
    public void authMngSearch() throws Exception {
        mockMvc.perform(get("/sys/authmng/authMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", "AUTH_ADMIN")
                .param("searchCd", "authCd")
                .param("searchWord", "")
            ).andExpect(jsonPath("$.authMngList").isArray())
            .andExpect(jsonPath("$.authMngList[1].authCd").value(authCd))
            .andExpect(jsonPath("$.authMngList[1].authNm").value(authNm))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한 상세 조회 테스트")
    public void authMngDetail() throws Exception {
        mockMvc.perform(post("/sys/authmng/authMngDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", authCd)
                .param("detailStatus", "UPDATE")
            ).andExpect(view().name("sys/authmng/authMngDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한 등록 테스트")
    public void authMngInsert() throws Exception {
        String authCd = "AUTH_MOBILE";
        String authNm = "TEST_MOBILE_AUTH";
        Integer authLv = 3;
        Integer authOrd = 2;

        mockMvc.perform(post("/sys/authmng/authMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", authCd)
                .param("upperAuthCd", "AUTH_NORMAL")
                .param("authNm", authNm)
                .param("authLv", String.valueOf(authLv))
                .param("authOrd", String.valueOf(authOrd))
                .param("useYn", useYn.getCode())
            ).andExpect(jsonPath("$.authCd").value(authCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한 등록 에러 테스트 : 빈 값")
    public void authMngInsert_emptyValue() throws Exception {
        mockMvc.perform(post("/sys/authmng/authMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", authCd)
                .param("upperAuthCd", "")
                .param("authNm", authNm)
                .param("authLv", "")
                .param("authOrd", String.valueOf(authOrd))
                .param("useYn", useYn.getCode())
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
    @DisplayName("권한 등록 에러 테스트 : 중복 코드")
    public void authMngInsert_dupCode() throws Exception {
        mockMvc.perform(post("/sys/authmng/authMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", authCd)
                .param("upperAuthCd", upperAuthCd)
                .param("authNm", authNm)
                .param("authLv", String.valueOf(authLv))
                .param("authOrd", String.valueOf(authOrd))
                .param("useYn", useYn.getCode())
            ).andExpect(jsonPath("$.dataStatus").value("DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한 수정 테스트")
    public void authMngUpdate() throws Exception {
        String authNm = "권한 수정";

        mockMvc.perform(put("/sys/authmng/authMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", authCd)
                .param("upperAuthCd", upperAuthCd)
                .param("authNm", authNm)
                .param("authLv", String.valueOf(authLv))
                .param("authOrd", String.valueOf(authOrd))
                .param("useYn", useYn.getCode())
            ).andExpect(jsonPath("$.authCd").value(authCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한 삭제 테스트")
    public void authMngDelete() throws Exception {
        String authCd = "AUTH_MOBILE";

        mockMvc.perform(delete("/sys/authmng/authMngDelete.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", authCd)
            ).andExpect(jsonPath("$.authCd").value(authCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한별 사용자 목록 조회 테스트")
    public void authUserMngSearch() throws Exception {
        mockMvc.perform(get("/sys/authmng/authUserMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "0")
                .param("pageSize", "20")
                .param("authCd", "AUTH_ADMIN")
                .param("grpCd", "GRP_MNG")
                .param("searchCd", "authCd")
                .param("searchWord", "")
            ).andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[1].rn").value(2))
            .andExpect(jsonPath("$.content[1].userId").value("sys_app"))
            .andExpect(jsonPath("$.content[1].authYn").value("허용"))
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 설정 적용 테스트")
    public void authUserMngUpdate() throws Exception {
        String authCdList = "AUTH_NORMAL";
        String userIdList = "sys_app,kdy0185";
        String authYn = "N";

        mockMvc.perform(put("/sys/authmng/authUserMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", authCdList)
                .param("userId", userIdList)
                .param("authYn", authYn)
            ).andExpect(jsonPath("$.authCd").value(authCdList))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(11)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한별 메뉴 목록 조회 테스트")
    public void authMenuMngSearch() throws Exception {
        mockMvc.perform(get("/sys/authmng/authMenuMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", "AUTH_NORMAL")
                .param("menuCd", "S0000")
            ).andExpect(jsonPath("$.authMenuMngList").isArray())
            .andExpect(jsonPath("$.authMenuMngList[0].id").value("S0100"))
            .andExpect(jsonPath("$.authMenuMngList[0].leaf").value(true))
            .andExpect(jsonPath("$.authMenuMngList[0].checked").value(true))
            .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메뉴 설정 적용 테스트")
    public void authMenuMngUpdate() throws Exception {
        String authCdList = "AUTH_NORMAL";
        String menuCdList = "S0000,S0100,S0200";

        mockMvc.perform(put("/sys/authmng/authMenuMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authCd", authCdList)
                .param("menuCd", menuCdList)
            ).andExpect(jsonPath("$.authCd").value(authCdList))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }
}
