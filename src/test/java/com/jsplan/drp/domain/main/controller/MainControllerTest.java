package com.jsplan.drp.domain.main.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.jsplan.drp.domain.main.service.MainService;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequestBuilder;
import com.jsplan.drp.domain.sys.usermng.service.UserMngService;
import com.jsplan.drp.global.obj.vo.UseStatus;
import java.util.Locale;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @Class : MainControllerTest
 * @Author : KDW
 * @Date : 2022-05-19
 * @Description : 메인 화면 Controller Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class MainControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    MainService mainService;

    @Autowired
    UserMngService userMngService;

    UserMngRequest request;
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
    }

    @AfterEach
    public void tearDown() {
        userMngService.deleteUserMngData(request);
    }

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("메인 화면 테스트")
    public void main() throws Exception {
        mockMvc.perform(post("/main/main/main.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("main/main/main"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 상세 조회 테스트")
    public void myInfoDetail() throws Exception {
        mockMvc.perform(post("/main/myinfo/myInfoDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", userId)
            ).andExpect(view().name("main/myinfo/myInfoDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 수정 테스트")
    public void myInfoUpdate() throws Exception {
        String userNm = "이름 수정";
        String email = "modify@mail.com";

        mockMvc.perform(put("/main/myinfo/myInfoUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("userId", userId)
                .param("userNm", userNm)
                .param("userPw", userPw)
                .param("email", email)
                .param("userType", userType)
                .param("useYn", useYn.getCode())
                .param("authCd", authCd)
            ).andExpect(jsonPath("$.userId").value(userId))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }
}
