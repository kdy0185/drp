package com.jsplan.drp.domain.sys.codemng.service;

import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngResponse;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngSearchDTO;
import com.jsplan.drp.domain.sys.codemng.entity.CodeGrpMng;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMng;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMngId;
import com.jsplan.drp.domain.sys.codemng.repository.CodeGrpMngRepository;
import com.jsplan.drp.domain.sys.codemng.repository.CodeMngRepository;
import com.jsplan.drp.global.obj.entity.CodeMngDataStatus;
import com.jsplan.drp.global.obj.entity.DetailStatus;
import com.jsplan.drp.global.util.StringUtil;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : CodeMngService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 코드 관리 Service
 */
@Service
@RequiredArgsConstructor
public class CodeMngService {

    private final CodeGrpMngRepository codeGrpMngRepository;
    private final CodeMngRepository codeMngRepository;

    /**
     * <p>그룹 코드 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (그룹 코드 목록)
     */
    public Page<CodeGrpMngListDTO> selectCodeGrpMngList(CodeMngSearchDTO searchDTO) {
        PageRequest pageRequest = PageRequest.of(searchDTO.getPageNo(), searchDTO.getPageSize());
        return codeGrpMngRepository.searchCodeGrpMngList(searchDTO.getSearchCd(),
            searchDTO.getSearchWord(), pageRequest);
    }

    /**
     * <p>공통 코드 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (공통 코드 목록)
     */
    public Page<CodeMngListDTO> selectCodeMngList(CodeMngSearchDTO searchDTO) {
        PageRequest pageRequest = PageRequest.of(searchDTO.getPageNo(), searchDTO.getPageSize());
        return codeMngRepository.searchCodeMngList(searchDTO.getGrpCd(), searchDTO.getSearchCd(),
            searchDTO.getSearchWord(), pageRequest);
    }

    /**
     * <p>그룹 코드 상세</p>
     *
     * @param request (그룹 코드 정보)
     * @return CodeGrpMngDetailDTO (그룹 코드 DTO)
     */
    public CodeGrpMngDetailDTO selectCodeGrpMngDetail(CodeGrpMngRequest request) {
        return codeGrpMngRepository.findCodeGrpMngByGrpCd(request.getGrpCd());
    }

    /**
     * <p>공통 코드 상세</p>
     *
     * @param request (공통 코드 정보)
     * @return CodeMngDetailDTO (공통 코드 DTO)
     */
    public CodeMngDetailDTO selectCodeMngDetail(CodeMngRequest request) {
        return codeMngRepository.findCodeMngByComCd(request.getGrpCd(), request.getComCd());
    }

    /**
     * <p>그룹 코드 수정</p>
     *
     * @param request (그룹 코드 정보)
     * @return CodeMngResponse (응답 정보)
     */
    @Transactional
    public CodeMngResponse updateCodeGrpMngData(CodeGrpMngRequest request) {
        JSONArray codeGrpMngArray = request.changeJsonData();
        CodeMngDataStatus dataStatus;
        int dataCnt = 0;

        if (codeGrpMngArray.size() > 0) {
            dataStatus = validateCodeGrpMngData(codeGrpMngArray);
            if (CodeMngDataStatus.SUCCESS_UPDATE == dataStatus) {
                dataCnt = applyCodeGrpMngData(codeGrpMngArray);
            }
        } else {
            dataStatus = CodeMngDataStatus.NOT_UPDATE;
        }
        return new CodeMngResponse(dataCnt, dataStatus, dataStatus.getTitle());
    }

    /**
     * <p>그룹 코드 유효성 체크</p>
     *
     * @param codeGrpMngArray (그룹 코드 JSONArray 정보)
     * @return CodeMngDataStatus (유효성 체크 결과)
     */
    private CodeMngDataStatus validateCodeGrpMngData(JSONArray codeGrpMngArray) {
        String arrGrpCd = "";

        for (int i = 0; i < codeGrpMngArray.size(); i++) {
            JSONObject codeGrpMngObject = codeGrpMngArray.getJSONObject(i);
            CodeGrpMngRequest request = (CodeGrpMngRequest) JSONObject.toBean(codeGrpMngObject,
                CodeGrpMngRequest.class);
            String grpCd = request.getGrpCd();
            String grpNm = request.getGrpNm();
            String detailStatus = request.getDetailStatus();

            // 1. 공백 여부 체크
            if (!StringUtil.isBlank(detailStatus)) {
                if (StringUtil.isBlank(grpCd) || StringUtil.isBlank(grpNm)) {
                    return CodeMngDataStatus.BLANK;
                }
            }

            // 2. 코드 간 중복 체크
            arrGrpCd += grpCd + ",";
            if (i == codeGrpMngArray.size() - 1) {
                if (StringUtil.findDuplicateValue(arrGrpCd)) {
                    return CodeMngDataStatus.DUPLICATE;
                }
            }

            // 3. 중복 그룹 코드 체크
            if (DetailStatus.INSERT.getCode().equals(detailStatus)) {
                if (codeGrpMngRepository.existsCodeGrpMngByGrpCd(grpCd)) {
                    return CodeMngDataStatus.INSERT_DUPLICATE;
                }
            }
        }

        return CodeMngDataStatus.SUCCESS_UPDATE;
    }

    /**
     * <p>그룹 코드 변경 및 적용</p>
     *
     * @param codeGrpMngArray (그룹 코드 JSONArray 정보)
     * @return int (변경된 row 수)
     */
    private int applyCodeGrpMngData(JSONArray codeGrpMngArray) {
        CodeGrpMngRequest request;
        int dataCnt = 0;

        for (int i = 0; i < codeGrpMngArray.size(); i++) {
            JSONObject codeGrpMngObject = codeGrpMngArray.getJSONObject(i);
            request = (CodeGrpMngRequest) JSONObject.toBean(codeGrpMngObject,
                CodeGrpMngRequest.class);
            if (DetailStatus.INSERT.getCode().equals(request.getDetailStatus())) {
                codeGrpMngRepository.save(request.toEntity());
                dataCnt++;
            } else if (DetailStatus.UPDATE.getCode().equals(request.getDetailStatus())) {
                CodeGrpMng codeGrpMng = codeGrpMngRepository.findById(request.getGrpCd())
                    .orElseThrow(NoSuchElementException::new);
                codeGrpMng.updateCodeGrpMng(request);
                dataCnt++;
            }
        }

        return dataCnt;
    }

    /**
     * <p>공통 코드 수정</p>
     *
     * @param request (공통 코드 정보)
     * @return CodeMngResponse (응답 정보)
     */
    @Transactional
    public CodeMngResponse updateCodeMngData(CodeMngRequest request) {
        JSONArray codeMngArray = request.changeJsonData();
        CodeMngDataStatus dataStatus;
        int dataCnt = 0;

        if (codeMngArray.size() > 0) {
            dataStatus = validateCodeMngData(codeMngArray);
            if (CodeMngDataStatus.SUCCESS_UPDATE == dataStatus) {
                dataCnt = applyCodeMngData(codeMngArray);
            }
        } else {
            dataStatus = CodeMngDataStatus.NOT_UPDATE;
        }
        return new CodeMngResponse(dataCnt, dataStatus, dataStatus.getTitle());
    }

    /**
     * <p>공통 코드 유효성 체크</p>
     *
     * @param codeMngArray (공통 코드 JSONArray 정보)
     * @return CodeMngDataStatus (유효성 체크 결과)
     */
    private CodeMngDataStatus validateCodeMngData(JSONArray codeMngArray) {
        String arrComCd = "";

        for (int i = 0; i < codeMngArray.size(); i++) {
            JSONObject codeMngObject = codeMngArray.getJSONObject(i);
            CodeMngRequest request = (CodeMngRequest) JSONObject.toBean(codeMngObject,
                CodeMngRequest.class);
            String grpCd = request.getGrpCd();
            String comCd = request.getComCd();
            String comNm = request.getComNm();
            String detailStatus = request.getDetailStatus();

            // 1. 공백 여부 체크
            if (!StringUtil.isBlank(detailStatus)) {
                if (StringUtil.isBlank(comCd) || StringUtil.isBlank(comNm)) {
                    return CodeMngDataStatus.BLANK;
                }
            }

            // 2. 코드 간 중복 체크
            arrComCd += comCd + ",";
            if (i == codeMngArray.size() - 1) {
                if (StringUtil.findDuplicateValue(arrComCd)) {
                    return CodeMngDataStatus.DUPLICATE;
                }
            }

            // 3. 중복 공통 코드 체크
            if (DetailStatus.INSERT.getCode().equals(detailStatus)) {
                if (codeMngRepository.existsCodeMngByComCd(grpCd, comCd)) {
                    return CodeMngDataStatus.INSERT_DUPLICATE;
                }
            }
        }

        return CodeMngDataStatus.SUCCESS_UPDATE;
    }

    /**
     * <p>공통 코드 변경 및 적용</p>
     *
     * @param codeMngArray (공통 코드 JSONArray 정보)
     * @return int (변경된 row 수)
     */
    private int applyCodeMngData(JSONArray codeMngArray) {
        CodeMngRequest request;
        int dataCnt = 0;

        for (int i = 0; i < codeMngArray.size(); i++) {
            JSONObject codeMngObject = codeMngArray.getJSONObject(i);
            request = (CodeMngRequest) JSONObject.toBean(codeMngObject,
                CodeMngRequest.class);
            if (DetailStatus.INSERT.getCode().equals(request.getDetailStatus())) {
                codeMngRepository.save(request.toEntity());
                dataCnt++;
            } else if (DetailStatus.UPDATE.getCode().equals(request.getDetailStatus())) {
                CodeMng codeMng = codeMngRepository.findById(
                        CodeMngId.createCodeMngId(request.getGrpCd(), request.getComCd()))
                    .orElseThrow(NoSuchElementException::new);
                codeMng.updateCodeMng(request);
                dataCnt++;
            }
        }

        return dataCnt;
    }

    /**
     * <p>그룹 코드 삭제</p>
     *
     * @param request (그룹 코드 정보)
     * @return CodeMngResponse (응답 정보)
     */
    @Transactional
    public CodeMngResponse deleteCodeGrpMngData(CodeGrpMngRequest request) {
        String[] arrGrpCd = StringUtil.split(request.getGrpCd());
        CodeMngDataStatus dataStatus;
        int dataCnt = 0;

        if (arrGrpCd.length > 0) {
            dataStatus = existsCodeMngData(arrGrpCd);
            if (CodeMngDataStatus.SUCCESS_DELETE == dataStatus) {
                dataCnt = removeCodeGrpMngData(arrGrpCd);
            }
        } else {
            dataStatus = CodeMngDataStatus.NOT_UPDATE;
        }
        return new CodeMngResponse(dataCnt, dataStatus, dataStatus.getTitle());
    }

    /**
     * <p>공통 코드 확인</p>
     *
     * @param arrGrpCd (그룹 코드 정보)
     * @return CodeMngDataStatus (유효성 체크 결과)
     */
    private CodeMngDataStatus existsCodeMngData(String[] arrGrpCd) {
        for (String grpCd : arrGrpCd) {
            if (codeGrpMngRepository.existsCodeMngByGrpCd(grpCd)) { // 그룹 코드 내 공통 코드가 존재할 경우
                return CodeMngDataStatus.CONSTRAINT;
            }
        }

        return CodeMngDataStatus.SUCCESS_DELETE;
    }

    /**
     * <p>그룹 코드 삭제 처리</p>
     *
     * @param arrGrpCd (그룹 코드 정보)
     * @return int (변경된 row 수)
     */
    private int removeCodeGrpMngData(String[] arrGrpCd) {
        int dataCnt = 0;

        for (String grpCd : arrGrpCd) {
            codeGrpMngRepository.deleteById(grpCd);
            dataCnt++;
        }

        return dataCnt;
    }

    /**
     * <p>공통 코드 삭제</p>
     *
     * @param request (공통 코드 정보)
     * @return CodeMngResponse (응답 정보)
     */
    @Transactional
    public CodeMngResponse deleteCodeMngData(CodeMngRequest request) {
        String[] arrComCd = StringUtil.split(request.getComCd());
        CodeMngDataStatus dataStatus;
        int dataCnt = 0;

        if (arrComCd.length > 0) {
            dataCnt = removeCodeMngData(request.getGrpCd(), arrComCd);
            dataStatus = CodeMngDataStatus.SUCCESS_DELETE;
        } else {
            dataStatus = CodeMngDataStatus.NOT_UPDATE;
        }

        return new CodeMngResponse(dataCnt, dataStatus, dataStatus.getTitle());
    }

    /**
     * <p>공통 코드 삭제 처리</p>
     *
     * @param grpCd    (그룹 코드 정보)
     * @param arrComCd (공통 코드 정보)
     * @return int (변경된 row 수)
     */
    private int removeCodeMngData(String grpCd, String[] arrComCd) {
        int dataCnt = 0;

        for (String comCd : arrComCd) {
            codeMngRepository.deleteById(CodeMngId.createCodeMngId(grpCd, comCd));
            dataCnt++;
        }

        return dataCnt;
    }

    /**
     * <p>그룹 코드 엑셀 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (그룹 코드 목록)
     */
    public List<CodeGrpMngListDTO> selectCodeGrpMngExcelList(CodeMngSearchDTO searchDTO) {
        return codeGrpMngRepository.searchCodeGrpMngExcelList(searchDTO.getSearchCd(),
            searchDTO.getSearchWord());
    }

    /**
     * <p>공통 코드 엑셀 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (공통 코드 목록)
     */
    public List<CodeMngListDTO> selectCodeMngExcelList(CodeMngSearchDTO searchDTO) {
        return codeMngRepository.searchCodeMngExcelList(searchDTO.getGrpCd(),
            searchDTO.getSearchCd(), searchDTO.getSearchWord());
    }
}
