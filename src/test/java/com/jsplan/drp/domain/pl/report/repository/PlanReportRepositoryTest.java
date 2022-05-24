package com.jsplan.drp.domain.pl.report.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.domain.pl.report.dto.PlanReportListDTO;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : PlanReportRepositoryTest
 * @Author : KDW
 * @Date : 2022-05-13
 * @Description : 데일리 리포트 Repository Test
 */
@SpringBootTest
@Transactional
class PlanReportRepositoryTest {

    @Autowired
    PlanReportRepository planReportRepository;

    @Test
    @DisplayName("분류 목록 조회 테스트")
    public void searchPlanReportRtneCtgList() {
        // given
        String userId = "kdy0185";

        // when
        List<PlanReportListDTO> planReportList = planReportRepository.searchPlanReportRtneCtgList(
            userId);

        // then
        assertThat(planReportList.size()).isEqualTo(40);
        assertThat(planReportList.stream().findFirst().orElseThrow(NoSuchElementException::new)
            .getRtneCtgCd()).isEqualTo("A01");
    }

    @Test
    @DisplayName("일과 목록 조회 테스트")
    public void searchPlanReportList() {
        // given
        String userId = "kdy0185";
        String rtneStartDate = "2022-01-01";
        String rtneEndDate = "2022-04-30";
        String rtneCtgCd = "C07";
        String rtneNm = "운동";
        PageRequest pageRequest = PageRequest.of(0, 200);

        // when
        Page<PlanReportListDTO> planReportList = planReportRepository.searchPlanReportList(userId,
            rtneStartDate, rtneEndDate, rtneCtgCd, rtneNm, pageRequest);

        // then
        assertThat(planReportList.getNumberOfElements()).isEqualTo(30);
    }
}
