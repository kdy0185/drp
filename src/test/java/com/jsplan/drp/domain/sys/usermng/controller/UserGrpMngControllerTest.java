package com.jsplan.drp.domain.sys.usermng.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngSearchDto;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
class UserGrpMngControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 관리 테스트")
    public void userGrpMngList() throws Exception {
        mockMvc.perform(post("/sys/usermng/userGrpMngList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(model().attribute("userGrpMngList", new ArrayList<>()))
            .andExpect(view().name("sys/usermng/userGrpMngList"))
            .andExpect(status().isOk());
    }

    // 테스트 조건
    // 1. 계정 연동 및 Security 활성화
    // 2. DTO 기본 생성자 필수
    // 3. @RequestBody, @ResponseBody 필수
    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 목록 조회 테스트")
    public void userGrpMngSearch() throws Exception {
        int pageNo = 0;
        int pageSize = 20;
        String grpNm = "사용자";
        UserGrpMngSearchDto searchDto = new UserGrpMngSearchDto(pageNo, pageSize, grpNm);
        String jsonData = objectMapper.writeValueAsString(searchDto);

        mockMvc.perform(post("/sys/usermng/userGrpMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
            ).andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].grpCd").value("GRP_USER"))
            .andExpect(jsonPath("$.content[0].grpNm").value("사용자 그룹"))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(status().isOk());
    }
}
