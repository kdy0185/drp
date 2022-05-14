package com.jsplan.drp.domain.pl.report.repository;

import com.jsplan.drp.domain.pl.report.dto.PlanReportListDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : PlanReportCustomRepository
 * @Author : KDW
 * @Date : 2022-05-13
 * @Description : 데일리 리포트 Custom Repository
 */
public interface PlanReportCustomRepository {

    /**
     * <p>분류 목록</p>
     *
     * @param userId (사용자 아이디)
     * @return List (분류 목록)
     */
    List<PlanReportListDTO> searchPlanReportRtneCtgList(String userId);

    /**
     * <p>일과 목록</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param rtneCtgCd     (루틴 분류 코드)
     * @param rtneNm        (루틴명)
     * @param pageable      (페이징 정보)
     * @return Page (일과 목록)
     */
    Page<PlanReportListDTO> searchPlanReportList(String userId, String rtneStartDate,
        String rtneEndDate, String rtneCtgCd, String rtneNm, Pageable pageable);

    /**
     * <p>일과 엑셀 목록</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param rtneCtgCd     (루틴 분류 코드)
     * @param rtneNm        (루틴명)
     * @return List (일과 목록)
     */
    List<PlanReportListDTO> searchPlanReportExcelList(String userId, String rtneStartDate,
        String rtneEndDate, String rtneCtgCd, String rtneNm);
}
