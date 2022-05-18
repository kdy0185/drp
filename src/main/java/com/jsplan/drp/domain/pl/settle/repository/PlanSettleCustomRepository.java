package com.jsplan.drp.domain.pl.settle.repository;

import com.jsplan.drp.domain.pl.settle.dto.PlanSettleDetailDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleListDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : PlanSettleCustomRepository
 * @Author : KDW
 * @Date : 2022-05-17
 * @Description : 일일 결산 Custom Repository
 */
public interface PlanSettleCustomRepository {

    /**
     * <p>일일 결산 목록</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param pageable      (페이징 정보)
     * @return Page (일일 결산 목록)
     */
    Page<PlanSettleListDTO> searchPlanSettleDayList(String userId, String rtneStartDate,
        String rtneEndDate, Pageable pageable);

    /**
     * <p>분류별 할당 시간 목록</p>
     *
     * @param rtneDate (루틴 일자)
     * @param planUser (담당자)
     * @return List (분류별 할당 시간 목록)
     */
    List<PlanSettleDetailDTO> searchPlanSettleDayTime(String rtneDate, String planUser);

    /**
     * <p>일과별 달성률 목록</p>
     *
     * @param rtneDate (루틴 일자)
     * @param planUser (담당자)
     * @return List (일과별 달성률 목록)
     */
    List<PlanSettleDetailDTO> searchPlanSettleDayAchvRate(String rtneDate, String planUser);

    /**
     * <p>일과별 몰입도 목록</p>
     *
     * @param rtneDate (루틴 일자)
     * @param planUser (담당자)
     * @return List (일과별 몰입도 목록)
     */
    List<PlanSettleDetailDTO> searchPlanSettleDayConcRate(String rtneDate, String planUser);

    /**
     * <p>일일 결산 엑셀 목록</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @return List (일일 결산 목록)
     */
    List<PlanSettleListDTO> searchPlanSettleDayExcelList(String userId, String rtneStartDate,
        String rtneEndDate);
}
