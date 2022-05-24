package com.jsplan.drp.domain.pl.ctgopt.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptDetailDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptRequest;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptRequestBuilder;
import com.jsplan.drp.domain.pl.ctgopt.service.PlanCtgOptService;
import com.jsplan.drp.global.obj.vo.UseStatus;
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
 * @Class : PlanCtgOptControllerTest
 * @Author : KDW
 * @Date : 2022-05-12
 * @Description : 분류 옵션 설정 Controller Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class PlanCtgOptControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext ctx;

    @Autowired
    PlanCtgOptService planCtgOptService;

    PlanCtgOptRequest request;
    PlanCtgOptDetailDTO detailDTO;

    String rtneCtgCd, upperRtneCtgCd, rtneCtgNm, rtneDate, userId, userNm;
    Float wtVal;
    Integer recgMinTime, recgMaxTime;
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
        rtneCtgCd = "B0701";
        upperRtneCtgCd = "B07";
        rtneCtgNm = "개발";
        wtVal = 3.0f;
        recgMinTime = 7;
        recgMaxTime = 12;
        rtneDate = "2022-01-01 ~ 2022-06-30";
        useYn = UseStatus.Y;
        userId = "kdy0185";
        userNm = "테스트";

        request = PlanCtgOptRequestBuilder.build(rtneCtgCd, upperRtneCtgCd, rtneCtgNm, wtVal,
            recgMinTime, recgMaxTime, useYn, userId);
        planCtgOptService.insertPlanCtgOptData(request);
        detailDTO = planCtgOptService.selectPlanCtgOptDetail(request);
    }

    @AfterEach
    public void tearDown() {
        planCtgOptService.deletePlanCtgOptData(request);
    }

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("분류 옵션 설정 테스트")
    public void planCtgOptList() throws Exception {
        mockMvc.perform(post("/pl/ctgopt/planCtgOptList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("pl/ctgopt/planCtgOptList"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("분류 옵션 목록 조회 테스트")
    public void planCtgOptSearch() throws Exception {
        mockMvc.perform(get("/pl/ctgopt/planCtgOptSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneCtgCd", upperRtneCtgCd)
            ).andExpect(jsonPath("$.planCtgOptList").isArray())
            .andExpect(jsonPath("$.planCtgOptList[0].rtneCtgCd").value(rtneCtgCd))
            .andExpect(jsonPath("$.planCtgOptList[0].rtneCtgNm").value(rtneCtgNm))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("분류 옵션 상세 조회 테스트")
    public void planCtgOptDetail() throws Exception {
        mockMvc.perform(post("/pl/ctgopt/planCtgOptDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneCtgCd", rtneCtgCd)
                .param("detailStatus", "UPDATE")
            ).andExpect(view().name("pl/ctgopt/planCtgOptDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("분류 옵션 등록 테스트")
    public void planCtgOptInsert() throws Exception {
        String rtneCtgCd = "B0702";
        String rtneCtgNm = "경제";

        mockMvc.perform(post("/pl/ctgopt/planCtgOptInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneCtgCd", rtneCtgCd)
                .param("upperRtneCtgCd", upperRtneCtgCd)
                .param("rtneCtgNm", rtneCtgNm)
                .param("wtVal", String.valueOf(wtVal))
                .param("recgMinTime", String.valueOf(recgMinTime))
                .param("recgMaxTime", String.valueOf(recgMaxTime))
                .param("useYn", useYn.getCode())
                .param("userId", userId)
            ).andExpect(jsonPath("$.rtneCtgCd").value(rtneCtgCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("분류 옵션 등록 테스트 : 빈 값")
    public void planCtgOptInsert_emptyValue() throws Exception {
        mockMvc.perform(post("/pl/ctgopt/planCtgOptInsert.do")
            .contentType(MediaType.APPLICATION_JSON)
            .param("rtneCtgCd", rtneCtgCd)
            .param("upperRtneCtgCd", upperRtneCtgCd)
            .param("rtneCtgNm", rtneCtgNm)
            .param("wtVal", "")
            .param("recgMinTime", "")
            .param("recgMaxTime", "")
            .param("useYn", useYn.getCode())
            .param("userId", userId)
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
    @DisplayName("분류 옵션 등록 테스트 : 중복 코드")
    public void planCtgOptInsert_dupCode() throws Exception {
        mockMvc.perform(post("/pl/ctgopt/planCtgOptInsert.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneCtgCd", rtneCtgCd)
                .param("upperRtneCtgCd", upperRtneCtgCd)
                .param("rtneCtgNm", rtneCtgNm)
                .param("wtVal", String.valueOf(wtVal))
                .param("recgMinTime", String.valueOf(recgMinTime))
                .param("recgMaxTime", String.valueOf(recgMaxTime))
                .param("useYn", useYn.getCode())
                .param("userId", userId)
            ).andExpect(jsonPath("$.dataStatus").value("DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("분류 옵션 수정 테스트")
    public void planCtgOptUpdate() throws Exception {
        String rtneCtgNm = "개발 수정";

        mockMvc.perform(put("/pl/ctgopt/planCtgOptUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneCtgCd", rtneCtgCd)
                .param("upperRtneCtgCd", upperRtneCtgCd)
                .param("rtneCtgNm", rtneCtgNm)
                .param("wtVal", String.valueOf(wtVal))
                .param("recgMinTime", String.valueOf(recgMinTime))
                .param("recgMaxTime", String.valueOf(recgMaxTime))
                .param("useYn", useYn.getCode())
                .param("userId", userId)
            ).andExpect(jsonPath("$.rtneCtgCd").value(rtneCtgCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("분류 옵션 삭제 테스트")
    public void planCtgOptDelete() throws Exception {
        String rtneCtgCd = "B0702";

        mockMvc.perform(delete("/pl/ctgopt/planCtgOptDelete.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("rtneCtgCd", rtneCtgCd)
                .param("userId", userId)
            ).andExpect(jsonPath("$.rtneCtgCd").value(rtneCtgCd))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS"))
            .andExpect(status().isOk());
    }
}
