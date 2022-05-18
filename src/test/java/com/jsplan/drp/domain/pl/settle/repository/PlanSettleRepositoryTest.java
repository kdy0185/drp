package com.jsplan.drp.domain.pl.settle.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.domain.pl.settle.dto.PlanSettleDetailDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleListDTO;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : PlanSettleRepositoryTest
 * @Author : KDW
 * @Date : 2022-05-17
 * @Description : 일일 결산 Repository Test
 */
@SpringBootTest
@Transactional
class PlanSettleRepositoryTest {

    @Autowired
    PlanSettleRepository planSettleRepository;

    String planUser, rtneDate;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        planUser = "kdy0185";
        rtneDate = "2022-04-30";
    }

    @Test
    @DisplayName("일일 결산 목록 조회 테스트")
    public void searchPlanSettleDayList() {
        // given
        String rtneStartDate = "2022-01-01";
        PageRequest pageRequest = PageRequest.of(0, 200);

        // when
        Page<PlanSettleListDTO> planSettleList = planSettleRepository.searchPlanSettleDayList(
            planUser, rtneStartDate, rtneDate, pageRequest);

        // then
        assertThat(planSettleList.getNumberOfElements()).isEqualTo(120);

        // print
        for (PlanSettleListDTO listDTO : planSettleList) {
            System.out.println("dailyId : " + listDTO.getDailyId());
            System.out.println("rtneDate : " + listDTO.getRtneDate());
            System.out.println("achvRate : " + listDTO.getAchvRate());
            System.out.println("concRate : " + listDTO.getConcRate());
            System.out.println("dailyScore : " + listDTO.getDailyScore());
            System.out.println("\n================================================\n");
        }
    }

    @Test
    @DisplayName("분류별 할당 시간 목록 조회 테스트")
    public void searchPlanSettleDayTime() {
        // when
        List<PlanSettleDetailDTO> planSettleDayTimeList = planSettleRepository.searchPlanSettleDayTime(
            rtneDate, planUser);

        // then
        assertThat(planSettleDayTimeList.size()).isEqualTo(4);
        assertThat(planSettleDayTimeList.stream().findFirst()
            .orElseThrow(NoSuchElementException::new)
            .getRtneAssignPer()).isEqualTo("41.67%");

        // print
        for (PlanSettleDetailDTO detailDTO : planSettleDayTimeList) {
            System.out.println("rtneCtgNm : " + detailDTO.getRtneCtgNm());
            System.out.println("rtneAssignCnt : " + detailDTO.getRtneAssignCnt());
            System.out.println("rtneAssignTime : " + detailDTO.getRtneAssignTime());
            System.out.println("rtneAssignPer : " + detailDTO.getRtneAssignPer());
            System.out.println("rtneCnt : " + detailDTO.getRtneCnt());
            System.out.println("\n================================================\n");
        }
    }

    @Test
    @DisplayName("일과별 달성률 목록 조회 테스트")
    public void searchPlanSettleDayAchvRate() {
        // when
        List<PlanSettleDetailDTO> planSettleDayAchvRateList = planSettleRepository.searchPlanSettleDayAchvRate(
            rtneDate, planUser);

        // then
        assertThat(planSettleDayAchvRateList.size()).isEqualTo(3);
        assertThat(planSettleDayAchvRateList.stream().findFirst()
            .orElseThrow(NoSuchElementException::new)
            .getRtneCnt()).isEqualTo(6);

        // print
        for (PlanSettleDetailDTO detailDTO : planSettleDayAchvRateList) {
            System.out.println("rn : " + detailDTO.getRn());
            System.out.println("achvRate : " + detailDTO.getAchvRate());
            System.out.println("rtneCnt : " + detailDTO.getRtneCnt());
            System.out.println("\n================================================\n");
        }
    }
    
    @Test
    @DisplayName("일과별 몰입도 목록 조회 테스트")
    public void searchPlanSettleDayConcRate() {
        // when
        List<PlanSettleDetailDTO> planSettleDayConcRateList = planSettleRepository.searchPlanSettleDayConcRate(
            rtneDate, planUser);

        // then
        assertThat(planSettleDayConcRateList.size()).isEqualTo(2);
        assertThat(planSettleDayConcRateList.stream().findFirst()
            .orElseThrow(NoSuchElementException::new)
            .getRtneCnt()).isEqualTo(8);

        // print
        for (PlanSettleDetailDTO detailDTO : planSettleDayConcRateList) {
            System.out.println("rn : " + detailDTO.getRn());
            System.out.println("concRate : " + detailDTO.getConcRate());
            System.out.println("rtneCnt : " + detailDTO.getRtneCnt());
            System.out.println("\n================================================\n");
        }
    }
}
