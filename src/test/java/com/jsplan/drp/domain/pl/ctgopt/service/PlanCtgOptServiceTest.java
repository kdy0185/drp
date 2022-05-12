package com.jsplan.drp.domain.pl.ctgopt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptDetailDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptListDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptRequest;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptRequestBuilder;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptSearchDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptSearchDTOBuilder;
import com.jsplan.drp.domain.pl.ctgopt.entity.PlanCtgOpt;
import com.jsplan.drp.domain.pl.ctgopt.entity.RtneCtgId;
import com.jsplan.drp.domain.pl.ctgopt.repository.PlanCtgOptRepository;
import com.jsplan.drp.global.obj.vo.UseStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.sf.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @Class : PlanCtgOptServiceTest
 * @Author : KDW
 * @Date : 2022-05-12
 * @Description : 분류 옵션 설정 Service Test
 */
@ExtendWith(MockitoExtension.class)
class PlanCtgOptServiceTest {

    @InjectMocks
    PlanCtgOptService planCtgOptService;

    @Mock
    PlanCtgOptRepository planCtgOptRepository;

    PlanCtgOptRequest request;
    PlanCtgOpt planCtgOpt;
    PlanCtgOptSearchDTO searchDTO;
    PlanCtgOptListDTO listDTO;
    PlanCtgOptDetailDTO detailDTO;

    String rtneCtgCd, upperRtneCtgCd, rtneCtgNm, rtneDate, userId, userNm;
    Float wtVal;
    Integer recgMinTime, recgMaxTime;
    UseStatus useYn;

    @BeforeEach
    public void setUp() {
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
        planCtgOpt = PlanCtgOpt.builder().rtneCtgId(RtneCtgId.createRtneCtgId(rtneCtgCd, userId))
            .upperRtneCtgCd(upperRtneCtgCd).rtneCtgNm(rtneCtgNm).wtVal(wtVal)
            .recgMinTime(recgMinTime).recgMaxTime(recgMaxTime).useYn(useYn).build();

        listDTO = new PlanCtgOptListDTO(rtneCtgCd, rtneCtgNm, wtVal, recgMinTime, recgMaxTime,
            rtneDate, useYn, userId, "Y");
        detailDTO = new PlanCtgOptDetailDTO(rtneCtgCd, upperRtneCtgCd, rtneCtgNm, wtVal,
            recgMinTime, recgMaxTime, useYn, userId, userNm);
    }

    @Test
    @DisplayName("분류 옵션 목록 조회 테스트")
    public void selectPlanCtgOptList() {
        // given
        List<PlanCtgOptListDTO> planCtgOptList = new ArrayList<>();
        planCtgOptList.add(listDTO);
        searchDTO = PlanCtgOptSearchDTOBuilder.build(rtneCtgCd, "", "", useYn);

        // mocking
        given(planCtgOptRepository.searchPlanCtgOptList(anyString(), anyString(),
            any())).willReturn(planCtgOptList);

        // when
        JSONArray planCtgOptArray = planCtgOptService.selectPlanCtgOptList(searchDTO);

        // then
        assertThat(planCtgOptArray.getJSONObject(0).get("rtneCtgCd")).isEqualTo(rtneCtgCd);
        assertThat(planCtgOptArray.getJSONObject(0).get("rtneCtgNm")).isEqualTo(rtneCtgNm);
        assertThat(planCtgOptArray.getJSONObject(0).get("leaf")).isEqualTo(true);
    }

    @Test
    @DisplayName("분류 옵션 상세 조회 테스트")
    public void selectPlanCtgOptDetail() {
        // mocking
        given(planCtgOptRepository.findPlanCtgOptByRtneCtgId(anyString(), anyString())).willReturn(
            detailDTO);

        // when
        PlanCtgOptDetailDTO findDetail = planCtgOptService.selectPlanCtgOptDetail(request);

        // then
        assertThat(findDetail.getRtneCtgCd()).isEqualTo(rtneCtgCd);
        assertThat(findDetail.getRtneCtgNm()).isEqualTo(rtneCtgNm);
    }

    @Test
    @DisplayName("분류 옵션 등록 테스트")
    public void insertPlanCtgOptData() {
        // mocking
        given(planCtgOptRepository.save(any())).willReturn(planCtgOpt);
        given(planCtgOptRepository.findPlanCtgOptByRtneCtgId(anyString(), anyString())).willReturn(
            detailDTO);

        // when
        planCtgOptService.insertPlanCtgOptData(request);
        String findRtneCtgCd = planCtgOptService.selectPlanCtgOptDetail(request).getRtneCtgCd();

        // then
        assertThat(findRtneCtgCd).isEqualTo(rtneCtgCd);
    }

    @Test
    @DisplayName("분류 옵션 수정 테스트")
    public void updatePlanCtgOptData() {
        // given
        request = PlanCtgOptRequestBuilder.build(rtneCtgCd, upperRtneCtgCd, "개발 수정", wtVal,
            recgMinTime, recgMaxTime, useYn, userId);

        // mocking
        when(planCtgOptRepository.findById(any())).thenReturn(Optional.of(planCtgOpt));

        // when
        planCtgOptService.updatePlanCtgOptData(request);

        // then
        assertThat(planCtgOpt.getRtneCtgNm()).isEqualTo(request.getRtneCtgNm());
    }

    @Test
    @DisplayName("분류 옵션 삭제 테스트")
    public void deletePlanCtgOptData() {
        // when
        planCtgOptService.deletePlanCtgOptData(request);

        // then
        verify(planCtgOptRepository, times(1)).deleteById(request.getRtneCtgId());
    }
}
