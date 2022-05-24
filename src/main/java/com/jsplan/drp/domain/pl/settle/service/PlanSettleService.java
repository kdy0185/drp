package com.jsplan.drp.domain.pl.settle.service;

import com.jsplan.drp.domain.pl.settle.dto.PlanSettleDetailDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleListDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleSearchDTO;
import com.jsplan.drp.domain.pl.settle.repository.PlanSettleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @Class : PlanSettleService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 일일 결산 Service
 */
@Service
@RequiredArgsConstructor
public class PlanSettleService {

    private final PlanSettleRepository planSettleRepository;

    /**
     * <p>일일 결산 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (일일 결산 목록)
     */
    public Page<PlanSettleListDTO> selectPlanSettleDayList(PlanSettleSearchDTO searchDTO) {
        PageRequest pageRequest = PageRequest.of(searchDTO.getPageNo(), searchDTO.getPageSize());
        return planSettleRepository.searchPlanSettleDayList(searchDTO.getUserId(),
            searchDTO.getRtneStartDate(), searchDTO.getRtneEndDate(), pageRequest);
    }

    /**
     * <p>분류별 할당 시간 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (분류별 할당 시간 목록)
     */
    public List<PlanSettleDetailDTO> selectPlanSettleDayTime(PlanSettleSearchDTO searchDTO) {
        return planSettleRepository.searchPlanSettleDayTime(searchDTO.getRtneDate(),
            searchDTO.getPlanUser());
    }

    /**
     * <p>일과별 달성률 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (분류별 할당 시간 목록)
     */
    public List<PlanSettleDetailDTO> selectPlanSettleDayAchvRate(PlanSettleSearchDTO searchDTO) {
        return planSettleRepository.searchPlanSettleDayAchvRate(searchDTO.getRtneDate(),
            searchDTO.getPlanUser());
    }

    /**
     * <p>일과별 몰입도 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (분류별 할당 시간 목록)
     */
    public List<PlanSettleDetailDTO> selectPlanSettleDayConcRate(PlanSettleSearchDTO searchDTO) {
        return planSettleRepository.searchPlanSettleDayConcRate(searchDTO.getRtneDate(),
            searchDTO.getPlanUser());
    }

    /**
     * <p>일일 결산 엑셀 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (일일 결산 목록)
     */
    public List<PlanSettleListDTO> selectPlanSettleDayExcelList(PlanSettleSearchDTO searchDTO) {
        return planSettleRepository.searchPlanSettleDayExcelList(searchDTO.getUserId(),
            searchDTO.getRtneStartDate(), searchDTO.getRtneEndDate());
    }
}
