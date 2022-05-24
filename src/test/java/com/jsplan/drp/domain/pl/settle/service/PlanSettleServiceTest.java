package com.jsplan.drp.domain.pl.settle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

import com.jsplan.drp.domain.pl.settle.dto.PlanSettleDetailDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleListDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleSearchDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleSearchDTOBuilder;
import com.jsplan.drp.domain.pl.settle.repository.PlanSettleRepository;
import java.time.LocalDate;
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
 * @Class : PlanSettleServiceTest
 * @Author : KDW
 * @Date : 2022-05-18
 * @Description : 일일 결산 Service Test
 */
@ExtendWith(MockitoExtension.class)
class PlanSettleServiceTest {

    @InjectMocks
    PlanSettleService planSettleService;

    @Mock
    PlanSettleRepository planSettleRepository;

    PlanSettleSearchDTO searchDTO;

    String rtneDate, planUser;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        rtneDate = "2022-04-30";
        planUser = "kdy0185";

        searchDTO = PlanSettleSearchDTOBuilder.detailBuild(rtneDate, planUser);
    }

    @Test
    @DisplayName("일일 결산 목록 조회 테스트")
    public void selectPlanSettleList() {
        // given
        Long dailyId = 334L;
        LocalDate rtneDate = LocalDate.parse("2022-04-30");
        Float dailyScore = 76.6f;

        List<PlanSettleListDTO> list = new ArrayList<>();
        list.add(new PlanSettleListDTO(dailyId, rtneDate, dailyScore, null, planUser));
        Page<PlanSettleListDTO> pageList = new PageImpl<>(list);

        PlanSettleSearchDTO searchDTO = PlanSettleSearchDTOBuilder.listBuild(0, 200, planUser, "",
            "", "");

        // mocking
        given(planSettleRepository.searchPlanSettleDayList(anyString(), anyString(), anyString(),
            any())).willReturn(pageList);

        // when
        Page<PlanSettleListDTO> planSettleDayList = planSettleService.selectPlanSettleDayList(
            searchDTO);

        // then
        assertThat(planSettleDayList.getNumberOfElements()).isEqualTo(1);
        assertThat(planSettleDayList.stream().findFirst().orElseThrow(NoSuchElementException::new)
            .getDailyScore()).isEqualTo(dailyScore);
    }

    @Test
    @DisplayName("분류별 할당 시간 목록 조회 테스트")
    public void selectPlanSettleDayTime() {
        // given
        String rtneCtgCd = "B07";
        Integer rtneAssignCnt = 600;
        String rtneAssignTime = "10시간";
        Long rtneCnt = 2L;

        List<PlanSettleDetailDTO> list = new ArrayList<>();
        list.add(new PlanSettleDetailDTO(rtneCtgCd, rtneAssignCnt, rtneAssignTime, rtneCnt));

        // mocking
        given(planSettleRepository.searchPlanSettleDayTime(anyString(), anyString())).willReturn(
            list);

        // when
        List<PlanSettleDetailDTO> timeList = planSettleService.selectPlanSettleDayTime(
            searchDTO);

        // then
        assertThat(timeList.get(0).getRtneAssignTime()).isEqualTo(rtneAssignTime);
        assertThat(timeList.get(0).getRtneCnt()).isEqualTo(rtneCnt);
    }

    @Test
    @DisplayName("일과별 달성률 목록 조회 테스트")
    public void selectPlanSettleDayAchvRate() {
        // given
        Integer achvRate = 100;
        Long rtneCnt = 6L;

        List<PlanSettleDetailDTO> list = new ArrayList<>();
        list.add(new PlanSettleDetailDTO(achvRate, rtneCnt));

        // mocking
        given(planSettleRepository.searchPlanSettleDayAchvRate(anyString(), anyString()))
            .willReturn(list);

        // when
        List<PlanSettleDetailDTO> achvRateList = planSettleService.selectPlanSettleDayAchvRate(
            searchDTO);

        // then
        assertThat(achvRateList.get(0).getAchvRate()).isEqualTo(achvRate + "%");
        assertThat(achvRateList.get(0).getRtneCnt()).isEqualTo(rtneCnt);
    }

    @Test
    @DisplayName("일과별 몰입도 목록 조회 테스트")
    public void selectPlanSettleDayConcRate() {
        // given
        String concRate = "★★★★★";
        Long rtneCnt = 8L;

        List<PlanSettleDetailDTO> list = new ArrayList<>();
        list.add(new PlanSettleDetailDTO(concRate, rtneCnt));

        // mocking
        given(planSettleRepository.searchPlanSettleDayConcRate(anyString(), anyString()))
            .willReturn(list);

        // when
        List<PlanSettleDetailDTO> concRateList = planSettleService.selectPlanSettleDayConcRate(
            searchDTO);

        // then
        assertThat(concRateList.get(0).getConcRate()).isEqualTo(concRate);
        assertThat(concRateList.get(0).getRtneCnt()).isEqualTo(rtneCnt);
    }
}
