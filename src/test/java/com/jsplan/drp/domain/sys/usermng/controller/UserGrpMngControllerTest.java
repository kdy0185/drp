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

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDetailDTO;
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
 * @Description : ?????? ?????? Controller Test
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
    UserGrpMngDetailDTO detailDTO;
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
        grpNm = "????????? ??????";
        grpDesc = "??????";

        request = UserGrpMngRequestBuilder.build(grpCd, grpNm, grpDesc);
        userGrpMngService.insertUserGrpMngData(request);
        detailDTO = userGrpMngService.selectUserGrpMngDetail(request);
    }

    @AfterEach
    public void tearDown() {
        userGrpMngService.deleteUserGrpMngData(request);
    }

    // ????????? ??????
    // 1. ?????? ?????? ??? Security ?????????
    // 2. DTO ?????? ????????? ??????
    // 3. @RequestBody, @ResponseBody ??????

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("?????? ?????? ?????????")
    public void userGrpMngList() throws Exception {
        mockMvc.perform(post("/sys/usermng/userGrpMngList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("sys/usermng/userGrpMngList"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("?????? ?????? ?????? ?????????")
    public void userGrpMngSearch() throws Exception {
        mockMvc.perform(get("/sys/usermng/userGrpMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "0")
                .param("pageSize", "20")
                .param("grpNm", "?????????")
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
    @DisplayName("?????? ?????? ?????? ?????????")
    public void userGrpMngDetail() throws Exception {
        mockMvc.perform(post("/sys/usermng/userGrpMngDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("detailStatus", "UPDATE")
            ).andExpect(view().name("sys/usermng/userGrpMngDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("?????? ?????? ?????????")
    public void userGrpMngInsert() throws Exception {
        String grpCd = "GRP_TEST2";

        mockMvc.perform(post("/sys/usermng/userGrpMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
            ).andExpect(jsonPath("$.grpCd").value(grpCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("?????? ?????? ?????? ????????? : ??? ???")
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
    @DisplayName("?????? ?????? ?????? ????????? : ?????? ??????")
    public void userGrpMngInsert_dupCode() throws Exception {
        mockMvc.perform(post("/sys/usermng/userGrpMngInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
            ).andExpect(jsonPath("$.dataStatus").value("DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("?????? ?????? ?????????")
    public void userGrpMngUpdate() throws Exception {
        String grpNm = "????????? ?????? ??????";
        String grpDesc = "?????? ??????";

        mockMvc.perform(put("/sys/usermng/userGrpMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
            ).andExpect(jsonPath("$.grpCd").value(grpCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("?????? ?????? ?????????")
    public void userGrpMngDelete() throws Exception {
        String grpCd = "GRP_TEST2";

        mockMvc.perform(delete("/sys/usermng/userGrpMngDelete.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("grpNm", grpNm)
                .param("grpDesc", grpDesc)
            ).andExpect(jsonPath("$.grpCd").value(grpCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }
}
