package com.jsplan.drp.domain.pl.settle.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.jsplan.drp.domain.pl.settle.service.PlanSettleService;
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
 * @Class : PlanSettleControllerTest
 * @Author : KDW
 * @Date : 2022-05-18
 * @Description : 일일 결산 Controller Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class PlanSettleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    PlanSettleService planSettleService;

    String planUser, rtneDate;

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
        planUser = "kdy0185";
        rtneDate = "2022-04-30";
    }

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("일일 결산 테스트")
    public void planSettleDayList() throws Exception {
        mockMvc.perform(post("/pl/settle/planSettleDayList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("pl/settle/planSettleDayList"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("일일 결산 목록 조회 테스트")
    public void planSettleSearch() throws Exception {
        String rtneStartDate = "2022-01-01";

        mockMvc.perform(get("/pl/settle/planSettleSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "0")
                .param("pageSize", "200")
                .param("userId", planUser)
                .param("rtneStartDate", rtneStartDate)
                .param("rtneEndDate", rtneDate)
            ).andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[119].dailyId").value(334))
            .andExpect(jsonPath("$.content[119].dailyScore").value(76.6))
            .andExpect(jsonPath("$.totalElements").value(120))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("일일 결산 상세 조회 테스트")
    public void planSettleDayDetail() throws Exception {
        mockMvc.perform(post("/pl/settle/planSettleDayDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneDate", rtneDate)
                .param("planUser", planUser)
            ).andExpect(view().name("pl/settle/planSettleDayDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("분류별 할당 시간 조회 테스트")
    public void planSettleDayTimeSearch() throws Exception {
        mockMvc.perform(get("/pl/settle/planSettleDayTimeSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneDate", rtneDate)
                .param("planUser", planUser)
            ).andExpect(jsonPath("$.length()").value(4))
            .andExpect(jsonPath("$.[0].rtneAssignPer").value("41.67%"))
            .andExpect(jsonPath("$.[0].rtneCnt").value(2))
            .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("일과별 달성률 조회 테스트")
    public void planSettleDayAchvRateSearch() throws Exception {
        mockMvc.perform(get("/pl/settle/planSettleDayAchvRateSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneDate", rtneDate)
                .param("planUser", planUser)
            ).andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$.[0].achvRate").value("100%"))
            .andExpect(jsonPath("$.[0].rtneCnt").value(6))
            .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("일과별 몰입도 조회 테스트")
    public void planSettleDayConcRateSearch() throws Exception {
        mockMvc.perform(get("/pl/settle/planSettleDayConcRateSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneDate", rtneDate)
                .param("planUser", planUser)
            ).andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.[0].concRate").value("★★★★★"))
            .andExpect(jsonPath("$.[0].rtneCnt").value(8))
            .andExpect(status().isOk());
    }
}
