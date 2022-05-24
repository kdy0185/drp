package com.jsplan.drp.domain.pl.report.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.jsplan.drp.domain.pl.report.service.PlanReportService;
import java.util.Locale;
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
 * @Class : PlanReportControllerTest
 * @Author : KDW
 * @Date : 2022-05-14
 * @Description : 데일리 리포트 Controller Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class PlanReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    PlanReportService planReportService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .defaultRequest(MockMvcRequestBuilders.head("/**/*.do").locale(Locale.KOREA))
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("데일리 리포트 테스트")
    public void planReportList() throws Exception {
        mockMvc.perform(post("/pl/report/planReportList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("pl/report/planReportList"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("분류 목록 조회 테스트")
    public void planReportRtneCtg() throws Exception {
        String userId = "kdy0185";

        mockMvc.perform(get("/pl/report/planReportRtneCtg.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", userId)
            ).andExpect(jsonPath("$.length()").value(40))
            .andExpect(jsonPath("$.[0].rtneCtgCd").value("A01"))
            .andExpect(jsonPath("$.[39].rtneCtgCd").value("E08"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("일과 목록 조회 테스트")
    public void planReportSearch() throws Exception {
        String userId = "kdy0185";
        String rtneStartDate = "2022-01-01";
        String rtneEndDate = "2022-04-30";
        String rtneCtgCd = "C07";
        String rtneNm = "운동";

        mockMvc.perform(get("/pl/report/planReportSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "0")
                .param("pageSize", "200")
                .param("userId", userId)
                .param("rtneStartDate", rtneStartDate)
                .param("rtneEndDate", rtneEndDate)
                .param("rtneCtgCd", rtneCtgCd)
                .param("rtneNm", rtneNm)
            ).andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].rtneId").value(2763))
            .andExpect(jsonPath("$.content[0].rtneDate").value("2022-02-02"))
            .andExpect(jsonPath("$.totalElements").value(30))
            .andExpect(status().isOk());
    }
}
