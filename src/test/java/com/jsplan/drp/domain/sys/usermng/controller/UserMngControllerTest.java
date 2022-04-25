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

import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequestBuilder;
import com.jsplan.drp.domain.sys.usermng.service.UserMngService;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @Class : UserMngControllerTest
 * @Author : KDW
 * @Date : 2022-04-19
 * @Description : 사용자 관리 Controller Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class UserMngControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    UserMngService userMngService;

    UserMngRequest request;
    UserMngDetailDto detailDto;
    String grpCd, userId, userNm, userPw, mobileNum, email, userType, authCd;
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
        grpCd = "GRP_USER";
        userId = "123456";
        userNm = "테스트";
        userPw = "1a2b3c4d5e6f";
        mobileNum = "010-0000-0000";
        email = "test@mail.com";
        userType = "T";
        useYn = UseStatus.Y;
        authCd = "AUTH_NORMAL";

        request = UserMngRequestBuilder.build(grpCd, userId, userNm, userPw, mobileNum, email,
            userType, useYn, authCd);
        userMngService.insertUserMngData(request);
        detailDto = userMngService.selectUserMngDetail(request);
    }

    @AfterEach
    public void tearDown() {
        userMngService.deleteUserMngData(request);
    }

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 관리 테스트")
    public void userMngList() throws Exception {
        mockMvc.perform(post("/sys/usermng/userMngList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("sys/usermng/userMngList"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 목록 조회 테스트")
    public void userMngSearch() throws Exception {
        mockMvc.perform(get("/sys/usermng/userMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "0")
                .param("pageSize", "20")
                .param("grpCd", "GRP_USER")
                .param("searchCd", "userId")
                .param("searchWord", "34")
            ).andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].rn").value(1))
            .andExpect(jsonPath("$.content[0].userId").value(userId))
            .andExpect(jsonPath("$.content[0].userNm").value(userNm))
            .andExpect(jsonPath("$.totalElements").value(6))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 상세 조회 테스트")
    public void userMngDetail() throws Exception {
        mockMvc.perform(post("/sys/usermng/userMngDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", userId)
                .param("state", "U")
            ).andExpect(view().name("sys/usermng/userMngDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 아이디 중복 체크 테스트")
    public void userMngIdDupCheck() throws Exception {
        mockMvc.perform(post("/sys/usermng/userMngIdDupCheck.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", userId)
            ).andExpect(jsonPath("$.dataStatus").value("DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 등록 테스트")
    public void userMngInsert() throws Exception {
        String userId = "654321";
        String userPw = "6f5e4d3c2b1a";

        mockMvc.perform(post("/sys/usermng/userMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("userId", userId)
                .param("userNm", userNm)
                .param("userPw", userPw)
                .param("mobileNum", mobileNum)
                .param("email", email)
                .param("userType", userType)
                .param("useYn", useYn.getCode())
                .param("authCd", authCd)
            ).andExpect(jsonPath("$.userId").value(userId))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 등록 에러 테스트 : 빈 값")
    public void userMngInsert_emptyValue() throws Exception {
        mockMvc.perform(post("/sys/usermng/userMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("userId", userId)
                .param("userNm", "")
                .param("userPw", userPw)
                .param("mobileNum", mobileNum)
                .param("email", "")
                .param("userType", userType)
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
    @Order(7)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 등록 에러 테스트 : 중복 코드")
    public void userMngInsert_dupCode() throws Exception {
        mockMvc.perform(post("/sys/usermng/userMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("userId", userId)
                .param("userNm", userNm)
                .param("userPw", userPw)
                .param("mobileNum", mobileNum)
                .param("email", email)
                .param("userType", userType)
                .param("useYn", useYn.getCode())
                .param("authCd", authCd)
            ).andExpect(jsonPath("$.dataStatus").value("DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 수정 테스트")
    public void userMngUpdate() throws Exception {
        String userNm = "이름 수정";
        String email = "modify@mail.com";

        mockMvc.perform(put("/sys/usermng/userMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("userId", userId)
                .param("userNm", userNm)
                .param("userPw", "")
                .param("email", email)
                .param("userType", userType)
                .param("useYn", useYn.getCode())
                .param("authCd", authCd)
            ).andExpect(jsonPath("$.userId").value(userId))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 삭제 테스트")
    public void userMngDelete() throws Exception {
        String userId = "654321";

        mockMvc.perform(delete("/sys/usermng/userMngDelete.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", userId)
            ).andExpect(jsonPath("$.userId").value(userId))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 권한 목록 조회 테스트")
    public void selectUserAuthMngList() throws Exception {
        mockMvc.perform(get("/sys/usermng/userAuthMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "075082,424981,784252,885235")
                .param("authCd", "AUTH_ADMIN")
            ).andExpect(jsonPath("$.userAuthMngList").isArray())
            .andExpect(jsonPath("$.userAuthMngList[0].id").value("AUTH_NORMAL"))
            .andExpect(jsonPath("$.userAuthMngList[0].leaf").value(false))
            .andExpect(jsonPath("$.userAuthMngList[0].checked").value(true))
            .andExpect(status().isOk());
    }

    @Test
    @Order(11)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("권한 설정 적용 테스트")
    public void userAuthMngUpdate() throws Exception {
        String userIdList = "078869,204520";
        String authCdList = "AUTH_SNS";

        mockMvc.perform(put("/sys/usermng/userAuthMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", userIdList)
                .param("authCd", authCdList)
            ).andExpect(jsonPath("$.userId").value(userIdList))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }
}
