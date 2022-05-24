package com.jsplan.drp.domain.pl.report.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.jsplan.drp.domain.pl.report.dto.PlanReportListDTO;
import com.jsplan.drp.domain.pl.report.dto.PlanReportSearchDTO;
import com.jsplan.drp.domain.pl.report.dto.PlanReportSearchDTOBuilder;
import com.jsplan.drp.domain.pl.report.repository.PlanReportRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * @Class : PlanReportServiceTest
 * @Author : KDW
 * @Date : 2022-05-14
 * @Description : 데일리 리포트 Service Test
 */
@ExtendWith(MockitoExtension.class)
class PlanReportServiceTest {

    @InjectMocks
    PlanReportService planReportService;

    @Mock
    PlanReportRepository planReportRepository;

    PlanReportSearchDTO searchDTO;

    String userId, rtneDate, rtneCtgCd, rtneNm;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        userId = "kdy0185";
        rtneDate = "2022-04-29";
        rtneCtgCd = "E07";
        rtneNm = "휴식";

        searchDTO = PlanReportSearchDTOBuilder.build(0, 200, userId, null, rtneDate, null,
            rtneCtgCd, rtneNm);
    }

    @Test
    @DisplayName("분류 목록 조회 테스트")
    public void selectPlanReportRtneCtgList() {
        // given
        String rtneCtgCd = "E07";
        String rtneCtgNm = "휴식7";

        PlanReportListDTO listDTO = new PlanReportListDTO(rtneCtgCd, rtneCtgNm);

        List<PlanReportListDTO> list = new ArrayList<>();
        list.add(listDTO);

        // mocking
        given(planReportRepository.searchPlanReportRtneCtgList(anyString())).willReturn(list);

        // when
        List<PlanReportListDTO> rtneCtgList = planReportService.selectPlanReportRtneCtgList(
            searchDTO);

        // then
        assertThat(rtneCtgList.get(0).getRtneCtgCd()).isEqualTo(rtneCtgCd);
        assertThat(rtneCtgList.get(0).getRtneCtgNm()).isEqualTo(rtneCtgNm);
    }

    @Test
    @DisplayName("일과 목록 조회 테스트")
    public void selectPlanReportList() {
        // given
        Long rtneId = 3659L;

        PlanReportListDTO listDTO = new PlanReportListDTO(rtneId, LocalDate.parse(rtneDate), null,
            LocalDateTime.now(), LocalDateTime.now(), "휴식7", rtneNm, 100, "★★★★★", userId);

        List<PlanReportListDTO> list = new ArrayList<>();
        list.add(listDTO);
        Page<PlanReportListDTO> pageList = new PageImpl<>(list);

        // mocking
        given(planReportRepository.searchPlanReportList(anyString(), any(), any(), anyString(),
            anyString(), any())).willReturn(pageList);

        // when
        Page<PlanReportListDTO> planReportList = planReportService.selectPlanReportList(searchDTO);

        // then
        assertThat(planReportList.getNumberOfElements()).isEqualTo(1);
        assertThat(planReportList.stream().findFirst().orElseThrow(NoSuchElementException::new)
            .getRtneNm()).contains(rtneNm);
    }
}
