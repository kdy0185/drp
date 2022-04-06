package com.jsplan.drp.domain.sys.usermng.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequestBuilder;
import com.jsplan.drp.domain.sys.usermng.service.UserGrpMngService;
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
 * @Class : UserGrpMngControllerTest
 * @Author : KDW
 * @Date : 2022-03-24
 * @Description : 그룹 관리 Controller Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class UserGrpMngControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private UserGrpMngService userGrpMngService;

    UserGrpMngRequest request;
    UserGrpMngDetailDto detailDto;
    String grpCd, grpNm, grpDesc;

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
        grpCd = "GRP_TEST";
        grpNm = "테스트 그룹";
        grpDesc = "설명";

        request = UserGrpMngRequestBuilder.build(grpCd, grpNm, grpDesc);
        userGrpMngService.insertUserGrpMngData(request);
        detailDto = userGrpMngService.selectUserGrpMngDetail(request);
    }

    @AfterEach
    public void tearDown() {
        userGrpMngService.deleteUserGrpMngData(request);
    }

    // 테스트 조건
    // 1. 계정 연동 및 Security 활성화
    // 2. DTO 기본 생성자 필수
    // 3. @RequestBody, @ResponseBody 필수

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 관리 테스트")
    public void userGrpMngList() throws Exception {
        mockMvc.perform(post("/sys/usermng/userGrpMngList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("sys/usermng/userGrpMngList"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 목록 조회 테스트")
    public void userGrpMngSearch() throws Exception {
        mockMvc.perform(get("/sys/usermng/userGrpMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "0")
                .param("pageSize", "20")
                .param("grpNm", "테스트")
            ).andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].rn").value(1))
            .andExpect(jsonPath("$.content[0].grpCd").value(grpCd))
            .andExpect(jsonPath("$.content[0].grpNm").value(grpNm))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 상세 조회 테스트")
    public void userGrpMngDetail() throws Exception {
        mockMvc.perform(post("/sys/usermng/userGrpMngDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
                .param("state", "U")
            ).andExpect(view().name("sys/usermng/userGrpMngDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 등록 테스트")
    public void userGrpMngInsert() throws Exception {
        String grpCd = "GRP_TEST2";

        mockMvc.perform(post("/sys/usermng/userGrpMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
            ).andExpect(jsonPath("$.grpCd").value(grpCd))
            .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 등록 에러 테스트 : 빈 값")
    public void userGrpMngInsert_emptyValue() throws Exception {
        mockMvc.perform(post("/sys/usermng/userGrpMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", "")
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
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
    @DisplayName("그룹 등록 에러 테스트 : 중복 코드")
    public void userGrpMngInsert_dupCode() throws Exception {
        mockMvc.perform(post("/sys/usermng/userGrpMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
            ).andExpect(jsonPath("$.code").value("D"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 수정 테스트")
    public void userGrpMngUpdate() throws Exception {
        String grpNm = "테스트 그룹 수정";
        String grpDesc = "설명 수정";

        mockMvc.perform(put("/sys/usermng/userGrpMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
            ).andExpect(jsonPath("$.grpCd").value(grpCd))
            .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 삭제 테스트")
    public void userGrpMngDelete() throws Exception {
        String grpCd = "GRP_TEST2";

        mockMvc.perform(delete("/sys/usermng/userGrpMngDelete.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
            ).andExpect(jsonPath("$.grpCd").value(grpCd))
            .andExpect(status().isOk());
    }
}
