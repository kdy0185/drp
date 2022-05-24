package com.jsplan.drp.domain.pl.ctgopt.service;

import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptDetailDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptListDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptRequest;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptResponse;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptSearchDTO;
import com.jsplan.drp.domain.pl.ctgopt.entity.PlanCtgOpt;
import com.jsplan.drp.domain.pl.ctgopt.repository.PlanCtgOptRepository;
import com.jsplan.drp.global.obj.vo.DataStatus;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : PlanCtgOptService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 분류 옵션 설정 Service
 */
@Service
@RequiredArgsConstructor
public class PlanCtgOptService {

    private final PlanCtgOptRepository planCtgOptRepository;

    /**
     * <p>분류 옵션 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return JSONArray (분류 옵션 목록)
     */
    public JSONArray selectPlanCtgOptList(PlanCtgOptSearchDTO searchDTO) {
        JSONArray planCtgOptArray = new JSONArray();
        JSONObject planCtgOptObject;

        // 하위 분류 옵션 조회
        List<PlanCtgOptListDTO> planCtgOptList = planCtgOptRepository.searchPlanCtgOptList(
            searchDTO.getRtneCtgCd(), searchDTO.getUserId(), searchDTO.getUseYn());
        for (PlanCtgOptListDTO listDTO : planCtgOptList) {
            planCtgOptObject = new JSONObject();
            planCtgOptObject.put("rtneCtgNm", listDTO.getRtneCtgNm());
            planCtgOptObject.put("rtneCtgCd", listDTO.getRtneCtgCd());
            planCtgOptObject.put("wtVal", listDTO.getWtVal());
            planCtgOptObject.put("recgTime", listDTO.getRecgTime());
            planCtgOptObject.put("rtneDate", listDTO.getRtneDate());
            planCtgOptObject.put("useYn", listDTO.getUseYn());
            planCtgOptObject.put("planUser", listDTO.getPlanUser());
            planCtgOptObject.put("leaf", "Y".equals(listDTO.getLastYn()));
            planCtgOptObject.put("expanded", !"Y".equals(listDTO.getLastYn()));

            planCtgOptArray.add(planCtgOptObject);
        }

        return planCtgOptArray;
    }

    /**
     * <p>분류 옵션 상세</p>
     *
     * @param request (분류 옵션 정보)
     * @return PlanCtgOptDetailDTO (분류 옵션 DTO)
     */
    public PlanCtgOptDetailDTO selectPlanCtgOptDetail(PlanCtgOptRequest request) {
        return planCtgOptRepository.findPlanCtgOptByRtneCtgId(request.getRtneCtgCd(),
            request.getUserId());
    }

    /**
     * <p>분류 옵션 등록</p>
     *
     * @param request (분류 옵션 정보)
     * @return PlanCtgOptResponse (응답 정보)
     */
    @Transactional
    public PlanCtgOptResponse insertPlanCtgOptData(PlanCtgOptRequest request) {
        if (validatePlanCtgOptDupData(request)) {
            return new PlanCtgOptResponse(null, DataStatus.DUPLICATE);
        } else {
            PlanCtgOpt planCtgOpt = planCtgOptRepository.save(request.toEntity());
            return new PlanCtgOptResponse(planCtgOpt.getRtneCtgId().getRtneCtgCd(),
                DataStatus.SUCCESS);
        }
    }

    /**
     * <p>중복 분류 옵션 체크</p>
     *
     * @param request (분류 옵션 정보)
     * @return boolean (중복 여부)
     */
    private boolean validatePlanCtgOptDupData(PlanCtgOptRequest request) {
        Optional<PlanCtgOpt> optionalPlanCtgOpt = planCtgOptRepository.findById(
            request.getRtneCtgId());
        return optionalPlanCtgOpt.isPresent();
    }

    /**
     * <p>분류 옵션 수정</p>
     *
     * @param request (분류 옵션 정보)
     * @return PlanCtgOptResponse (응답 정보)
     */
    @Transactional
    public PlanCtgOptResponse updatePlanCtgOptData(PlanCtgOptRequest request) {
        PlanCtgOpt planCtgOpt = planCtgOptRepository.findById(request.getRtneCtgId())
            .orElseThrow(NoSuchElementException::new);
        planCtgOpt.updatePlanCtgOpt(request);
        return new PlanCtgOptResponse(planCtgOpt.getRtneCtgId().getRtneCtgCd(), DataStatus.SUCCESS);
    }

    /**
     * <p>분류 옵션 삭제</p>
     *
     * @param request (분류 옵션 정보)
     * @return PlanCtgOptResponse (응답 정보)
     */
    @Transactional
    public PlanCtgOptResponse deletePlanCtgOptData(PlanCtgOptRequest request) {
        if (existsPlanReportData(request)) { // 분류 옵션에 해당하는 일과가 존재할 경우
            return new PlanCtgOptResponse(request.getRtneCtgCd(), DataStatus.CONSTRAINT);
        } else {
            planCtgOptRepository.deleteById(request.getRtneCtgId());
            return new PlanCtgOptResponse(request.getRtneCtgCd(), DataStatus.SUCCESS);
        }
    }

    /**
     * <p>일과 확인</p>
     *
     * @param request (분류 옵션 정보)
     * @return boolean (일과 존재 여부)
     */
    private boolean existsPlanReportData(PlanCtgOptRequest request) {
        return planCtgOptRepository.existsPlanReportByRtneCtgId(request.getRtneCtgCd(),
            request.getUserId());
    }

    /**
     * <p>분류 옵션 엑셀 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (분류 옵션 목록)
     */
    public List<PlanCtgOptListDTO> selectPlanCtgOptExcelList(PlanCtgOptSearchDTO searchDTO) {
        return planCtgOptRepository.searchPlanCtgOptExcelList(searchDTO.getRtneCtgCd(),
            searchDTO.getUserId(), searchDTO.getUseYn());
    }
}
