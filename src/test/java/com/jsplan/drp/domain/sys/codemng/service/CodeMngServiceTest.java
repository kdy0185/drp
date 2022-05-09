package com.jsplan.drp.domain.sys.codemng.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngRequestBuilder;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngRequestBuilder;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngSearchDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngSearchDTOBuilder;
import com.jsplan.drp.domain.sys.codemng.entity.CodeGrpMng;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMng;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMngId;
import com.jsplan.drp.domain.sys.codemng.repository.CodeGrpMngRepository;
import com.jsplan.drp.domain.sys.codemng.repository.CodeMngRepository;
import com.jsplan.drp.global.obj.entity.BaseEntity;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import com.jsplan.drp.global.obj.entity.UseStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @Class : CodeMngServiceTest
 * @Author : KDW
 * @Date : 2022-05-03
 * @Description : 코드 관리 Service Test
 */
@ExtendWith(MockitoExtension.class)
class CodeMngServiceTest {

    @InjectMocks
    CodeMngService codeMngService;

    @Mock
    CodeGrpMngRepository codeGrpMngRepository;

    @Mock
    CodeMngRepository codeMngRepository;

    CodeGrpMngRequest grpRequest;
    CodeMngRequest codeRequest;
    CodeGrpMng codeGrpMng;
    CodeMng codeMng;
    CodeMngSearchDTO searchDTO;
    CodeGrpMngDetailDTO grpDetailDTO;
    CodeMngDetailDTO codeDetailDTO;

    String grpCd, grpNm, comCd, comNm, regUser, modUser;
    Integer ord;
    UseStatus useYn;
    LocalDateTime regDate, modDate;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        grpCd = "WEEKLY";
        grpNm = "요일";
        comCd = "Mon";
        comNm = "월";
        useYn = UseStatus.Y;
        ord = 1;
        regUser = "sys_app";
        regDate = LocalDateTime.now();
        modUser = "sys_app";
        modDate = LocalDateTime.now();

        grpRequest = CodeGrpMngRequestBuilder.build(grpCd, grpNm, useYn, null);
        codeRequest = CodeMngRequestBuilder.build(grpCd, comCd, comNm, useYn, null, ord);
        codeGrpMng = CodeGrpMng.builder().grpCd(grpCd).grpNm(grpNm).useYn(useYn).build();
        codeMng = CodeMng.builder().codeMngId(CodeMngId.createCodeMngId(grpCd, comCd)).comNm(comNm)
            .useYn(useYn).ord(ord).build();
        ReflectionTestUtils.setField(codeGrpMng, BaseEntity.class, "regUser", regUser,
            String.class);
        ReflectionTestUtils.setField(codeGrpMng, BaseTimeEntity.class, "regDate", regDate,
            LocalDateTime.class);
        ReflectionTestUtils.setField(codeGrpMng, BaseEntity.class, "modUser", modUser,
            String.class);
        ReflectionTestUtils.setField(codeGrpMng, BaseTimeEntity.class, "modDate", modDate,
            LocalDateTime.class);
        ReflectionTestUtils.setField(codeMng, BaseEntity.class, "regUser", regUser, String.class);
        ReflectionTestUtils.setField(codeMng, BaseTimeEntity.class, "regDate", regDate,
            LocalDateTime.class);
        ReflectionTestUtils.setField(codeMng, BaseEntity.class, "modUser", modUser, String.class);
        ReflectionTestUtils.setField(codeMng, BaseTimeEntity.class, "modDate", modDate,
            LocalDateTime.class);

        searchDTO = CodeMngSearchDTOBuilder.build(0, 20, grpCd, null, "grpCd", "");

        grpDetailDTO = new CodeGrpMngDetailDTO(grpCd, grpNm, useYn, null, regUser, regDate, modUser,
            modDate);
        codeDetailDTO = new CodeMngDetailDTO(grpCd, grpNm, comCd, comNm, useYn, null, ord, regUser,
            regDate, modUser, modDate);
    }

    @Test
    @DisplayName("그룹 코드 목록 조회 테스트")
    public void selectCodeGrpMngList() throws Exception {
        // given
        List<CodeGrpMngListDTO> list = new ArrayList<>();
        list.add(
            new CodeGrpMngListDTO(grpCd, grpNm, useYn, null, regUser, regDate, modUser, modDate));
        Page<CodeGrpMngListDTO> pageList = new PageImpl<>(list);

        // mocking
        given(codeGrpMngRepository.searchCodeGrpMngList(anyString(), anyString(), any()))
            .willReturn(pageList);

        // when
        Page<CodeGrpMngListDTO> codeGrpMngList = codeMngService.selectCodeGrpMngList(searchDTO);

        // then
        assertThat(codeGrpMngList.getNumberOfElements()).isEqualTo(1);
        assertThat(codeGrpMngList.stream().findFirst().orElseThrow(NoSuchElementException::new)
            .getGrpNm()).contains(grpNm);
    }

    @Test
    @DisplayName("공통 코드 목록 조회 테스트")
    public void selectCodeMngList() throws Exception {
        // given
        List<CodeMngListDTO> list = new ArrayList<>();
        list.add(new CodeMngListDTO(grpCd, grpNm, comCd, comNm, useYn, null, ord, regUser, regDate,
            modUser, modDate));
        Page<CodeMngListDTO> pageList = new PageImpl<>(list);

        // mocking
        given(codeMngRepository.searchCodeMngList(anyString(), anyString(), anyString(), any()))
            .willReturn(pageList);

        // when
        Page<CodeMngListDTO> codeMngList = codeMngService.selectCodeMngList(searchDTO);

        // then
        assertThat(codeMngList.getNumberOfElements()).isEqualTo(1);
        assertThat(codeMngList.stream().findFirst().orElseThrow(NoSuchElementException::new)
            .getComNm()).contains(comNm);
    }

    @Test
    @DisplayName("그룹 코드 상세 조회 테스트")
    public void selectCodeGrpMngDetail() throws Exception {
        // mocking
        given(codeGrpMngRepository.findCodeGrpMngByGrpCd(anyString())).willReturn(grpDetailDTO);

        // when
        CodeGrpMngDetailDTO findDetail = codeMngService.selectCodeGrpMngDetail(grpRequest);

        // then
        assertThat(findDetail.getGrpCd()).isEqualTo(grpCd);
        assertThat(findDetail.getGrpNm()).isEqualTo(grpNm);
        assertThat(LocalDateTime.parse(findDetail.getRegDate(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("공통 코드 상세 조회 테스트")
    public void selectCodeMngDetail() throws Exception {
        // mocking
        given(codeMngRepository.findCodeMngByComCd(anyString(), anyString())).willReturn(
            codeDetailDTO);

        // when
        CodeMngDetailDTO findDetail = codeMngService.selectCodeMngDetail(codeRequest);

        // then
        assertThat(findDetail.getComCd()).isEqualTo(comCd);
        assertThat(findDetail.getComNm()).isEqualTo(comNm);
        assertThat(LocalDateTime.parse(findDetail.getRegDate(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("그룹 코드 등록 테스트")
    public void insertCodeGrpMngData() throws Exception {
        // given
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"grpNm\" : \"요일\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"INSERT\"\n"
            + "    }\n"
            + "]";
        CodeGrpMngRequest request = CodeGrpMngRequestBuilder.jsonBuild(jsonData);

        // mocking
        given(codeGrpMngRepository.save(any())).willReturn(codeGrpMng);
        given(codeGrpMngRepository.findCodeGrpMngByGrpCd(anyString())).willReturn(grpDetailDTO);

        // when
        codeMngService.updateCodeGrpMngData(request);
        String findGrpCd = codeMngService.selectCodeGrpMngDetail(grpRequest).getGrpCd();

        // then
        assertThat(findGrpCd).isEqualTo(grpCd);
    }

    @Test
    @DisplayName("그룹 코드 수정 테스트")
    public void updateCodeGrpMngData() throws Exception {
        // given
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"grpNm\" : \"요일 수정\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"UPDATE\"\n"
            + "    }\n"
            + "]";
        CodeGrpMngRequest request = CodeGrpMngRequestBuilder.jsonBuild(jsonData);

        // mocking
        when(codeGrpMngRepository.findById(any())).thenReturn(Optional.of(codeGrpMng));

        // when
        codeMngService.updateCodeGrpMngData(request);

        // then
        assertThat(codeGrpMng.getGrpNm()).isEqualTo("요일 수정");
    }

    @Test
    @DisplayName("공통 코드 등록 테스트")
    public void insertCodeMngData() throws Exception {
        // given
        String jsonData = ""
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
        CodeMngRequest request = CodeMngRequestBuilder.jsonBuild(jsonData);

        // mocking
        given(codeMngRepository.save(any())).willReturn(codeMng);
        given(codeMngRepository.findCodeMngByComCd(anyString(), anyString())).willReturn(
            codeDetailDTO);

        // when
        codeMngService.updateCodeMngData(request);
        String findComCd = codeMngService.selectCodeMngDetail(codeRequest).getComCd();

        // then
        assertThat(findComCd).isEqualTo(comCd);
    }

    @Test
    @DisplayName("공통 코드 수정 테스트")
    public void updateCodeMngData() throws Exception {
        String jsonData = ""
            + "[\n"
            + "    {\n"
            + "        \"grpCd\" : \"WEEKLY\",\n"
            + "        \"comCd\" : \"Mon\",\n"
            + "        \"comNm\" : \"월요일\",\n"
            + "        \"useYn\" : \"Y\",\n"
            + "        \"detailStatus\" : \"UPDATE\"\n"
            + "    }\n"
            + "]";
        CodeMngRequest request = CodeMngRequestBuilder.jsonBuild(jsonData);

        // mocking
        when(codeMngRepository.findById(any())).thenReturn(Optional.of(codeMng));

        // when
        codeMngService.updateCodeMngData(request);

        // then
        assertThat(codeMng.getComNm()).isEqualTo("월요일");
    }

    @Test
    @DisplayName("그룹 코드 삭제 테스트")
    public void deleteCodeGrpMngData() throws Exception {
        // when
        codeMngService.deleteCodeGrpMngData(grpRequest);

        // then
        verify(codeGrpMngRepository, times(1)).deleteById(grpRequest.getGrpCd());
    }

    @Test
    @DisplayName("공통 코드 삭제 테스트")
    public void deleteCodeMngData() throws Exception {
        // given
        CodeMngId codeMngId = CodeMngId.createCodeMngId(codeRequest.getGrpCd(),
            codeRequest.getComCd());

        // when
        codeMngService.deleteCodeMngData(codeRequest);

        // then
        verify(codeMngRepository, times(1)).deleteById(codeMngId);
    }
}
