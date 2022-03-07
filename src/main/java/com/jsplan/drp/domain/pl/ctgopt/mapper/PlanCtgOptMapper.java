package com.jsplan.drp.domain.pl.ctgopt.mapper;

import com.jsplan.drp.domain.pl.ctgopt.entity.PlanCtgOptVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Class : PlanCtgOptMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 분류 옵션 설정 Mapper
 */
@Repository
@Mapper
public interface PlanCtgOptMapper {

    /**
     * <p>분류 옵션 목록</p>
     *
     * @param planCtgOptVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanCtgOptVO> selectPlanCtgOptList(PlanCtgOptVO planCtgOptVO) throws Exception;

    /**
     * <p>분류 옵션 상세</p>
     *
     * @param planCtgOptVO
     * @return PlanCtgOptVO
     * @throws Exception throws Exception
     */
    PlanCtgOptVO selectPlanCtgOptDetail(PlanCtgOptVO planCtgOptVO) throws Exception;

    /**
     * <p>분류 옵션 등록</p>
     *
     * @param planCtgOptVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertPlanCtgOptData(PlanCtgOptVO planCtgOptVO) throws Exception;

    /**
     * <p>분류 옵션 수정</p>
     *
     * @param planCtgOptVO
     * @return int
     * @throws Exception throws Exception
     */
    int updatePlanCtgOptData(PlanCtgOptVO planCtgOptVO) throws Exception;

    /**
     * <p>적용 일과 수</p>
     *
     * @param planCtgOptVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectPlanReportListCnt(PlanCtgOptVO planCtgOptVO) throws Exception;

    /**
     * <p>분류 옵션 삭제</p>
     *
     * @param planCtgOptVO
     * @return int
     * @throws Exception throws Exception
     */
    int deletePlanCtgOptData(PlanCtgOptVO planCtgOptVO) throws Exception;

    /**
     * <p>분류 옵션 엑셀 목록</p>
     *
     * @param planCtgOptVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanCtgOptVO> selectPlanCtgOptExcelList(PlanCtgOptVO planCtgOptVO) throws Exception;
}
