package com.jsplan.drp.domain.pl.ctgopt.repository;

import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptDetailDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptListDTO;
import com.jsplan.drp.global.obj.vo.UseStatus;
import java.util.List;

/**
 * @Class : PlanCtgOptCustomRepository
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 분류 옵션 설정 Custom Repository
 */
public interface PlanCtgOptCustomRepository {

    /**
     * <p>분류 옵션 목록</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param userId    (사용자 아이디)
     * @param useYn     (사용 여부)
     * @return List (분류 옵션 목록)
     */
    List<PlanCtgOptListDTO> searchPlanCtgOptList(String rtneCtgCd, String userId, UseStatus useYn);

    /**
     * <p>분류 옵션 상세</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param planUser  (담당자)
     * @return PlanCtgOptDetailDTO (분류 옵션 DTO)
     */
    PlanCtgOptDetailDTO findPlanCtgOptByRtneCtgId(String rtneCtgCd, String planUser);

    /**
     * <p>일과 확인</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param userId    (사용자 아이디)
     * @return boolean (일과 존재 여부)
     */
    boolean existsPlanReportByRtneCtgId(String rtneCtgCd, String userId);

    /**
     * <p>분류 옵션 엑셀 목록</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param userId    (사용자 아이디)
     * @param useYn     (사용 여부)
     * @return List (분류 옵션 목록)
     */
    List<PlanCtgOptListDTO> searchPlanCtgOptExcelList(String rtneCtgCd, String userId,
        UseStatus useYn);
}
