package com.jsplan.drp.domain.sys.codemng.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngRequestBuilder;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngRequestBuilder;
import com.jsplan.drp.domain.sys.codemng.service.CodeMngService;
import com.jsplan.drp.global.obj.entity.UseStatus;
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
 * @Class : CodeMngControllerTest
 * @Author : KDW
 * @Date : 2022-05-06
 * @Description : 코드 관리 Controller Test
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class CodeMngControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    CodeMngService codeMngService;

    CodeGrpMngRequest grpRequest;
    CodeMngRequest codeRequest;
    CodeGrpMngDetailDTO grpDetailDTO;
    CodeMngDetailDTO codeDetailDTO;

    String grpCd, grpNm, comCd, comNm, jsonData;
    Integer ord;
    UseStatus useYn;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .defaultRequest(MockMvcRequestBuilders.head("/**/*.do").locale(Locale.KOREA))
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();

        setGrpTestModel();
        setCodeTestModel();
        setDetailModel();
    }

    private void setGrpTestModel() {
        grpCd = "WEEKLY";
        grpNm = "요일";
        useYn = UseStatus.Y;
        jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"grpNm\" : \"요일\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    }\n"
            + "]";

        grpRequest = CodeGrpMngRequestBuilder.jsonBuild(jsonData);
        codeMngService.updateCodeGrpMngData(grpRequest);
    }

    private void setCodeTestModel() {
        comCd = "Mon";
        comNm = "월";
        ord = 1;
        jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"comCd\" : \"Mon\",\n"
            + "        \"comNm\" : \"월\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"ord\" : \"1\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    }\n"
            + "]";

        codeRequest = CodeMngRequestBuilder.jsonBuild(jsonData);
        codeMngService.updateCodeMngData(codeRequest);
    }

    private void setDetailModel() {
        grpRequest = CodeGrpMngRequestBuilder.build(grpCd, grpNm, useYn, null);
        codeRequest = CodeMngRequestBuilder.build(grpCd, comCd, comNm, useYn, null, ord);

        grpDetailDTO = codeMngService.selectCodeGrpMngDetail(grpRequest);
        codeDetailDTO = codeMngService.selectCodeMngDetail(codeRequest);
    }

    @AfterEach
    public void tearDown() {
        codeMngService.deleteCodeMngData(codeRequest);
        codeMngService.deleteCodeGrpMngData(grpRequest);
    }

    @Test
    @Order(1)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("코드 관리 테스트")
    public void codeMngList() throws Exception {
        mockMvc.perform(post("/sys/codemng/codeMngList.do")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(view().name("sys/codemng/codeMngList"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 목록 조회 테스트")
    public void codeGrpMngSearch() throws Exception {
        mockMvc.perform(get("/sys/codemng/codeGrpMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "0")
                .param("pageSize", "20")
                .param("searchCd", "grpCd")
                .param("searchWord", "")
            ).andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[2].rn").value(3))
            .andExpect(jsonPath("$.content[2].grpCd").value(grpCd))
            .andExpect(jsonPath("$.content[2].grpNm").value(grpNm))
            .andExpect(jsonPath("$.totalElements").value(4))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 목록 조회 테스트")
    public void codeMngSearch() throws Exception {
        mockMvc.perform(get("/sys/codemng/codeMngSearch.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "0")
                .param("pageSize", "20")
                .param("grpCd", "WEEKLY")
                .param("searchCd", "grpCd")
                .param("searchWord", "")
            ).andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].rn").value(1))
            .andExpect(jsonPath("$.content[0].comCd").value(comCd))
            .andExpect(jsonPath("$.content[0].comNm").value(comNm))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 상세 조회 테스트")
    public void codeGrpMngDetail() throws Exception {
        mockMvc.perform(post("/sys/codemng/codeGrpMngDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
            ).andExpect(view().name("sys/codemng/codeGrpMngDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 상세 조회 테스트")
    public void codeMngDetail() throws Exception {
        mockMvc.perform(post("/sys/codemng/codeMngDetail.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("comCd", comCd)
            ).andExpect(view().name("sys/codemng/codeMngDetail"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 수정 테스트")
    public void codeGrpMngUpdate() throws Exception {
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"grpNm\" : \"요일 수정\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"UPDATE\"\n"
            + "    },\n"
            + "    {\n"
            + "        \"grpCd\" : \"MONTH\",\n"
            + "        \"grpNm\" : \"월\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    }\n"
            + "]";

        mockMvc.perform(put("/sys/codemng/codeGrpMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("jsonData", jsonData)
            ).andExpect(jsonPath("$.dataCnt").value(2))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS_UPDATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 수정 테스트 : 공백")
    public void codeGrpMngUpdate_blank() throws Exception {
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"grpNm\" : \"\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"UPDATE\"\n"
            + "    }\n"
            + "]";

        mockMvc.perform(put("/sys/codemng/codeGrpMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("jsonData", jsonData)
            ).andExpect(jsonPath("$.dataStatus").value("BLANK"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 수정 테스트 : 중복")
    public void codeGrpMngUpdate_dup() throws Exception {
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"YEAR\",\n"
            + "        \"grpNm\" : \"년\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    },\n"
            + "    {\n"
            + "        \"grpCd\" : \"YEAR\",\n"
            + "        \"grpNm\" : \"년\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    }\n"
            + "]";

        mockMvc.perform(put("/sys/codemng/codeGrpMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("jsonData", jsonData)
            ).andExpect(jsonPath("$.dataStatus").value("DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 수정 테스트 : 등록 시 중복")
    public void codeGrpMngUpdate_insertDup() throws Exception {
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"grpNm\" : \"요일\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    }\n"
            + "]";

        mockMvc.perform(put("/sys/codemng/codeGrpMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("jsonData", jsonData)
            ).andExpect(jsonPath("$.dataStatus").value("INSERT_DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 수정 테스트")
    public void codeMngUpdate() throws Exception {
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"comCd\" : \"Mon\",\n"
            + "        \"comNm\" : \"월요일\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"ord\" : \"1\",\n"
            + "        \"detailStatus\" : \"UPDATE\"\n"
            + "    },\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"comCd\" : \"Tue\",\n"
            + "        \"comNm\" : \"화요일\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"ord\" : \"2\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    }\n"
            + "]";

        mockMvc.perform(put("/sys/codemng/codeMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("jsonData", jsonData)
            ).andExpect(jsonPath("$.dataCnt").value(2))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS_UPDATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(11)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 수정 테스트 : 공백")
    public void codeMngUpdate_blank() throws Exception {
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"comCd\" : \"Mon\",\n"
            + "        \"grpNm\" : \"\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"UPDATE\"\n"
            + "    }\n"
            + "]";

        mockMvc.perform(put("/sys/codemng/codeMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("jsonData", jsonData)
            ).andExpect(jsonPath("$.dataStatus").value("BLANK"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 수정 테스트 : 중복")
    public void codeMngUpdate_dup() throws Exception {
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"comCd\" : \"Wed\",\n"
            + "        \"comNm\" : \"수요일\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"ord\" : \"3\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    },\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"comCd\" : \"Wed\",\n"
            + "        \"comNm\" : \"수요일\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"ord\" : \"3\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    }\n"
            + "]";

        mockMvc.perform(put("/sys/codemng/codeMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("jsonData", jsonData)
            ).andExpect(jsonPath("$.dataStatus").value("DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(13)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 수정 테스트 : 등록 시 중복")
    public void codeMngUpdate_insertDup() throws Exception {
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"comCd\" : \"Mon\",\n"
            + "        \"comNm\" : \"월\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    }\n"
            + "]";

        mockMvc.perform(put("/sys/codemng/codeMngUpdate.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("jsonData", jsonData)
            ).andExpect(jsonPath("$.dataStatus").value("INSERT_DUPLICATE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 삭제 테스트")
    public void codeMngDelete() throws Exception {
        String comCd = "Tue";

        mockMvc.perform(delete("/sys/codemng/codeMngDelete.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
                .param("comCd", comCd)
            ).andExpect(jsonPath("$.dataCnt").value(1))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS_DELETE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(15)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 삭제 테스트")
    public void codeGrpMngDelete() throws Exception {
        String grpCd = "MONTH";

        mockMvc.perform(delete("/sys/codemng/codeGrpMngDelete.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
            ).andExpect(jsonPath("$.dataCnt").value(1))
            .andExpect(jsonPath("$.dataStatus").value("SUCCESS_DELETE"))
            .andExpect(status().isOk());
    }

    @Test
    @Order(16)
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 삭제 테스트 : 공통 코드 존재")
    public void codeGrpMngDelete_constraint() throws Exception {
        mockMvc.perform(delete("/sys/codemng/codeGrpMngDelete.do")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grpCd", grpCd)
            ).andExpect(jsonPath("$.dataStatus").value("CONSTRAINT"))
            .andExpect(status().isOk());
    }
}
