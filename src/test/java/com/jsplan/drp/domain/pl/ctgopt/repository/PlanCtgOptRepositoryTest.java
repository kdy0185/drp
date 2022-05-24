package com.jsplan.drp.domain.pl.ctgopt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptDetailDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptListDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptRequest;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptRequestBuilder;
import com.jsplan.drp.domain.pl.ctgopt.entity.PlanCtgOpt;
import com.jsplan.drp.domain.pl.ctgopt.entity.RtneCtgId;
import com.jsplan.drp.global.obj.vo.UseStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : PlanCtgOptRepositoryTest
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 분류 옵션 설정 Repository Test
 */
@SpringBootTest
@Transactional
class PlanCtgOptRepositoryTest {

    @Autowired
    PlanCtgOptRepository planCtgOptRepository;

    PlanCtgOptRequest request;
    String rtneCtgCd, upperRtneCtgCd, rtneCtgNm, userId;
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
        useYn = UseStatus.Y;
        userId = "kdy0185";

        request = PlanCtgOptRequestBuilder.build(rtneCtgCd, upperRtneCtgCd, rtneCtgNm, wtVal,
            recgMinTime, recgMaxTime, useYn, userId);
    }

    @Test
    @DisplayName("분류 옵션 목록 조회 테스트")
    public void searchPlanCtgOptList() {
        // given
        String userId = "kdy0185";
        UseStatus useYn = UseStatus.Y;

        // when
        List<PlanCtgOptListDTO> planCtgOptList = planCtgOptRepository.searchPlanCtgOptList(null,
            userId, useYn);

        // then
        assertThat(planCtgOptList.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("분류 옵션 상세 조회 테스트")
    public void findPlanCtgOptByRtneCtgId() {
        // given
        String rtneCtgCd = "B07";

        // when
        PlanCtgOptDetailDTO detailDTO = planCtgOptRepository.findPlanCtgOptByRtneCtgId(rtneCtgCd,
            userId);

        // then
        assertThat(detailDTO.getRtneCtgNm()).isEqualTo("공부7");
    }

    @Test
    @DisplayName("분류 옵션 등록 테스트")
    public void insertPlanCtgOptData() {
        // when
        PlanCtgOpt savedPlanCtgOpt = planCtgOptRepository.save(request.toEntity());
        String findRtneCtgCd = planCtgOptRepository.findPlanCtgOptByRtneCtgId(
            savedPlanCtgOpt.getRtneCtgId().getRtneCtgCd(),
            savedPlanCtgOpt.getRtneCtgId().getPlanUser()).getRtneCtgCd();

        // then
        assertThat(findRtneCtgCd).isEqualTo(rtneCtgCd);
    }

    @Test
    @DisplayName("분류 옵션 수정 테스트")
    public void updatePlanCtgOptData() {
        // given
        String rtneCtgCd = "B07";
        String rtneCtgNm = "공부7 수정";
        LocalDateTime beforeDate = LocalDateTime.now();

        // when
        PlanCtgOptRequest request = PlanCtgOptRequestBuilder.build(rtneCtgCd, null, rtneCtgNm,
            wtVal, recgMinTime, recgMaxTime, useYn, userId);
        PlanCtgOpt planCtgOpt = planCtgOptRepository.findById(
            RtneCtgId.createRtneCtgId(rtneCtgCd, userId)).orElseThrow(NoSuchElementException::new);
        planCtgOpt.updatePlanCtgOpt(request);
        planCtgOptRepository.flush();

        // then
        assertThat(planCtgOpt.getRtneCtgNm()).isEqualTo(rtneCtgNm);
        assertThat(planCtgOpt.getModDate()).isAfter(beforeDate);
    }

    @Test
    @DisplayName("분류 옵션 삭제 테스트")
    public void deletePlanCtgOptData() {
        // when
        planCtgOptRepository.saveAndFlush(request.toEntity());

        planCtgOptRepository.deleteById(RtneCtgId.createRtneCtgId(rtneCtgCd, userId));
        PlanCtgOptDetailDTO detailDTO = planCtgOptRepository.findPlanCtgOptByRtneCtgId(rtneCtgCd,
            userId);

        // then
        assertThat(detailDTO).isNull();
    }
}
