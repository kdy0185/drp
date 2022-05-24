package com.jsplan.drp.domain.pl.report.service;

import com.jsplan.drp.domain.pl.report.dto.PlanReportListDTO;
import com.jsplan.drp.domain.pl.report.dto.PlanReportSearchDTO;
import com.jsplan.drp.domain.pl.report.repository.PlanReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @Class : PlanReportService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 데일리 리포트 Service
 */
@Service
@RequiredArgsConstructor
public class PlanReportService {

    private final PlanReportRepository planReportRepository;

    /**
     * <p>분류 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (분류 목록)
     */
    public List<PlanReportListDTO> selectPlanReportRtneCtgList(PlanReportSearchDTO searchDTO) {
        return planReportRepository.searchPlanReportRtneCtgList(searchDTO.getUserId());
    }

    /**
     * <p>일과 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (일과 목록)
     */
    public Page<PlanReportListDTO> selectPlanReportList(PlanReportSearchDTO searchDTO) {
        PageRequest pageRequest = PageRequest.of(searchDTO.getPageNo(), searchDTO.getPageSize());
        return planReportRepository.searchPlanReportList(searchDTO.getUserId(),
            searchDTO.getRtneStartDate(), searchDTO.getRtneEndDate(), searchDTO.getRtneCtgCd(),
            searchDTO.getRtneNm(), pageRequest);
    }

    /**
     * <p>일과 엑셀 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (일과 목록)
     */
    public List<PlanReportListDTO> selectPlanReportExcelList(PlanReportSearchDTO searchDTO) {
        return planReportRepository.searchPlanReportExcelList(searchDTO.getUserId(),
            searchDTO.getRtneStartDate(), searchDTO.getRtneEndDate(), searchDTO.getRtneCtgCd(),
            searchDTO.getRtneNm());
    }
}
