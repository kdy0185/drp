package com.jsplan.drp.domain.sys.codemng.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngRequestBuilder;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngRequestBuilder;
import com.jsplan.drp.domain.sys.codemng.entity.CodeGrpMng;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMng;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMngId;
import com.jsplan.drp.global.obj.entity.UseStatus;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : CodeMngRepositoryTest
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 코드 관리 Repository Test
 */
@SpringBootTest
@Transactional
class CodeMngRepositoryTest {

    @Autowired
    CodeGrpMngRepository codeGrpMngRepository;

    @Autowired
    CodeMngRepository codeMngRepository;

    CodeGrpMngRequest grpRequest;
    CodeMngRequest codeRequest;
    String grpCd, grpNm, comCd, comNm;
    Integer ord;
    UseStatus useYn;

    @BeforeEach
    public void setUp() {
        setGrpTestModel();
        setCodeTestModel();
    }

    private void setGrpTestModel() {
        grpCd = "WEEKLY";
        grpNm = "요일";
        useYn = UseStatus.Y;

        grpRequest = CodeGrpMngRequestBuilder.build(grpCd, grpNm, useYn, null);
    }

    private void setCodeTestModel() {
        comCd = "Mon";
        comNm = "월";
        ord = 1;

        codeRequest = CodeMngRequestBuilder.build(grpCd, comCd, comNm, useYn, null, ord);
    }

    @Test
    @DisplayName("그룹 코드 목록 조회 테스트")
    public void searchCodeGrpMngList() throws Exception {
        // given
        String searchCd = "grpCd";
        String searchWord = "";
        PageRequest pageRequest = PageRequest.of(0, 20);

        // when
        Page<CodeGrpMngListDTO> codeGrpMngList = codeGrpMngRepository.searchCodeGrpMngList(
            searchCd, searchWord, pageRequest);

        // then
        assertThat(codeGrpMngList.getNumberOfElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("공통 코드 목록 조회 테스트")
    public void searchCodeMngList() throws Exception {
        // given
        String grpCd = "CONC_RATE";
        String searchCd = "comCd";
        String searchWord = "";
        PageRequest pageRequest = PageRequest.of(0, 20);

        // when
        Page<CodeMngListDTO> codeMngList = codeMngRepository.searchCodeMngList(grpCd, searchCd,
            searchWord, pageRequest);

        // then
        assertThat(codeMngList.getNumberOfElements()).isEqualTo(6);
    }

    @Test
    @DisplayName("그룹 코드 상세 조회 테스트")
    public void findCodeGrpMngByGrpCd() throws Exception {
        // given
        String grpCd = "CONC_RATE";

        // when
        CodeGrpMngDetailDTO detailDTO = codeGrpMngRepository.findCodeGrpMngByGrpCd(grpCd);

        // then
        assertThat(detailDTO.getGrpNm()).isEqualTo("몰입도");
    }

    @Test
    @DisplayName("공통 코드 상세 조회 테스트")
    public void findCodeMngByGrpCd() throws Exception {
        // given
        String grpCd = "CONC_RATE";
        String comCd = "5";

        // when
        CodeMngDetailDTO detailDTO = codeMngRepository.findCodeMngByComCd(grpCd, comCd);

        // then
        assertThat(detailDTO.getComNm()).isEqualTo("★★★★★");
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 등록 테스트")
    public void insertCodeGrpMngData() throws Exception {
        // when
        CodeGrpMng savedCodeGrpMng = codeGrpMngRepository.save(grpRequest.toEntity());
        String findGrpCd = codeGrpMngRepository.findCodeGrpMngByGrpCd(savedCodeGrpMng.getGrpCd())
            .getGrpCd();

        // then
        assertThat(findGrpCd).isEqualTo(grpCd);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 등록 테스트")
    public void insertCodeMngData() throws Exception {
        // when
        codeGrpMngRepository.save(grpRequest.toEntity());
        CodeMng savedCodeMng = codeMngRepository.save(codeRequest.toEntity());
        String findComCd = codeMngRepository.findCodeMngByComCd(savedCodeMng.getCodeMngId().getGrpCd(),
            savedCodeMng.getCodeMngId().getComCd()).getComCd();

        // then
        assertThat(findComCd).isEqualTo(comCd);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 수정 테스트")
    public void updateCodeGrpMngData() throws Exception {
        // given
        String grpCd = "CONC_RATE";
        String grpNm = "몰입도 수정";
        String detl = "설명 수정";
        LocalDateTime beforeDate = LocalDateTime.now();

        // when
        CodeGrpMngRequest grpRequest = CodeGrpMngRequestBuilder.build(grpCd, grpNm, useYn, detl);
        CodeGrpMng codeGrpMng = codeGrpMngRepository.findById(grpRequest.getGrpCd())
            .orElseThrow(NoSuchElementException::new);
        codeGrpMng.updateCodeGrpMng(grpRequest);
        codeGrpMngRepository.flush();

        // then
        assertThat(codeGrpMng.getGrpNm()).isEqualTo(grpNm);
        assertThat(codeGrpMng.getDetl()).isEqualTo(detl);
        assertThat(codeGrpMng.getModDate()).isAfter(beforeDate);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 수정 테스트")
    public void updateCodeMngData() throws Exception {
        // given
        String grpCd = "CONC_RATE";
        String comCd = "0";
        String comNm = "";
        String detl = "설명 수정";
        Integer ord = 7;
        LocalDateTime beforeDate = LocalDateTime.now();

        // when
        CodeMngRequest request = CodeMngRequestBuilder.build(grpCd, comCd, comNm, useYn, detl, ord);
        CodeMng codeMng = codeMngRepository.findById(CodeMngId.createCodeMngId(grpCd, comCd))
            .orElseThrow(NoSuchElementException::new);
        codeMng.updateCodeMng(request);
        codeMngRepository.flush();

        // then
        assertThat(codeMng.getComNm()).isEqualTo(comNm);
        assertThat(codeMng.getDetl()).isEqualTo(detl);
        assertThat(codeMng.getModDate()).isAfter(beforeDate);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 코드 삭제 테스트")
    public void deleteCodeGrpMngData() throws Exception {
        // when
        codeGrpMngRepository.saveAndFlush(grpRequest.toEntity());

        codeGrpMngRepository.deleteById(grpCd);
        CodeGrpMngDetailDTO detailDTO = codeGrpMngRepository.findCodeGrpMngByGrpCd(grpCd);

        // then
        assertThat(detailDTO).isNull();
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("공통 코드 삭제 테스트")
    public void deleteCodeMngData() throws Exception {
        // given
        codeGrpMngRepository.saveAndFlush(grpRequest.toEntity());
        codeMngRepository.saveAndFlush(codeRequest.toEntity());

        // when
        codeMngRepository.deleteById(CodeMngId.createCodeMngId(grpCd, comCd));
        CodeMngDetailDTO detailDTO = codeMngRepository.findCodeMngByComCd(grpCd, comCd);

        // then
        assertThat(detailDTO).isNull();
    }

}
