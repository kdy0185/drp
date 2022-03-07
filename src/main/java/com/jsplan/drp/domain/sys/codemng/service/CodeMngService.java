package com.jsplan.drp.domain.sys.codemng.service;

import com.jsplan.drp.domain.sys.codemng.mapper.CodeMngMapper;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMngVO;
import com.jsplan.drp.global.util.FilterUtil;
import com.jsplan.drp.global.util.StringUtil;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : CodeMngService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 코드 관리 Service
 */
@Service("CodeMngService")
public class CodeMngService {

    @Resource
    private CodeMngMapper codeMngMapper;

    /**
     * <p>그룹 코드 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<CodeMngVO> selectGrpMngList(CodeMngVO codeMngVO) throws Exception {
        return codeMngMapper.selectGrpMngList(codeMngVO);
    }

    /**
     * <p>그룹 코드 목록 수</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectGrpMngListCnt(CodeMngVO codeMngVO) throws Exception {
        return codeMngMapper.selectGrpMngListCnt(codeMngVO);
    }

    /**
     * <p>공통 코드 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<CodeMngVO> selectCodeMngList(CodeMngVO codeMngVO) throws Exception {
        return codeMngMapper.selectCodeMngList(codeMngVO);
    }

    /**
     * <p>공통 코드 목록 수</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectCodeMngListCnt(CodeMngVO codeMngVO) throws Exception {
        return codeMngMapper.selectCodeMngListCnt(codeMngVO);
    }

    /**
     * <p>그룹 코드 상세</p>
     *
     * @param codeMngVO
     * @return CodeMngVO
     * @throws Exception throws Exception
     */
    public CodeMngVO selectGrpMngDetail(CodeMngVO codeMngVO) throws Exception {
        return codeMngMapper.selectGrpMngDetail(codeMngVO);
    }

    /**
     * <p>공통 코드 상세</p>
     *
     * @param codeMngVO
     * @return CodeMngVO
     * @throws Exception throws Exception
     */
    public CodeMngVO selectCodeMngDetail(CodeMngVO codeMngVO) throws Exception {
        return codeMngMapper.selectCodeMngDetail(codeMngVO);
    }

    /**
     * <p>그룹 코드 수정</p>
     *
     * @param codeMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateGrpMngData(CodeMngVO codeMngVO) throws Exception {
        String code = null;
        String jsonData = codeMngVO.getJsonData();
        jsonData = FilterUtil.filterXssReverse(jsonData);
        jsonData = StringUtil.clean(jsonData);
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        int cnt = jsonArray.size();

        if (cnt > 0) {
            boolean isBlankGrp = false;
            boolean isDupGrp = false;
            boolean isDupInsertGrp = false;
            boolean isDupUpdateGrp = false;
            String arrGrpCd = null;

            // Validation Check
            for (int i = 0; i < cnt; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                codeMngVO = (CodeMngVO) JSONObject.toBean(jsonObject, CodeMngVO.class);
                String grpCd = codeMngVO.getGrpCd();
                String exGrpCd = codeMngVO.getExGrpCd();
                String grpNm = codeMngVO.getGrpNm();
                String state = codeMngVO.getState();

                // 1. 공백 여부
                if (!StringUtil.isBlank(state)) {
                    if (StringUtil.isBlank(grpCd) || StringUtil.isBlank(grpNm)) {
                        isBlankGrp = true;
                    }
                }

                // 2. 코드 간 중복 여부
                arrGrpCd += grpCd + ",";
                if (i == cnt - 1) {
                    isDupGrp = StringUtil.findDuplicateValue(arrGrpCd);
                }

                // 3. 등록 코드 중복 여부
                if ("I".equals(state)) {
                    if (codeMngMapper.selectGrpMngListCnt(codeMngVO) > 0) {
                        isDupInsertGrp = true;
                    }
                }

                // 4. 수정 코드 중복 여부
                if ("U".equals(state) && !grpCd.equals(exGrpCd)) {
                    if (codeMngMapper.selectGrpMngListCnt(codeMngVO) > 0) {
                        isDupUpdateGrp = true;
                    }
                }
            }

            if (isBlankGrp) {
                code = "B";
            } else if (isDupGrp) {
                code = "D";
            } else if (isDupInsertGrp) {
                code = "I";
            } else if (isDupUpdateGrp) {
                code = "U";
            } else {
                for (int i = 0; i < cnt; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    codeMngVO = (CodeMngVO) JSONObject.toBean(jsonObject, CodeMngVO.class);
                    if ("I".equals(codeMngVO.getState())) {
                        codeMngMapper.insertGrpMngData(codeMngVO);
                    } else if ("U".equals(codeMngVO.getState())) {
                        codeMngMapper.updateGrpMngData(codeMngVO);
                    }
                }
                code = "S";
            }
        } else {
            code = "N";
        }
        return code;
    }

    /**
     * <p>공통 코드 수정</p>
     *
     * @param codeMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateCodeMngData(CodeMngVO codeMngVO) throws Exception {
        String code = null;
        String jsonData = codeMngVO.getJsonData();
        jsonData = FilterUtil.filterXssReverse(jsonData);
        jsonData = StringUtil.clean(jsonData);
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        int cnt = jsonArray.size();

        if (cnt > 0) {
            boolean isBlankCode = false;
            boolean isDupCode = false;
            boolean isDupInsertCode = false;
            boolean isDupUpdateCode = false;
            String arrCd = null;

            // Validation Check
            for (int i = 0; i < cnt; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                codeMngVO = (CodeMngVO) JSONObject.toBean(jsonObject, CodeMngVO.class);
                String comCd = codeMngVO.getComCd();
                String exComCd = codeMngVO.getExComCd();
                String comNm = codeMngVO.getComNm();
                String state = codeMngVO.getState();

                // 1. 공백 여부
                if (!StringUtil.isBlank(state)) {
                    if (StringUtil.isBlank(comCd) || StringUtil.isBlank(comNm)) {
                        isBlankCode = true;
                    }
                }

                // 2. 코드 간 중복 여부
                arrCd += comCd + ",";
                if (i == cnt - 1) {
                    isDupCode = StringUtil.findDuplicateValue(arrCd);
                }

                // 3. 등록 코드 중복 여부
                if ("I".equals(state)) {
                    if (codeMngMapper.selectCodeMngListCnt(codeMngVO) > 0) {
                        isDupInsertCode = true;
                    }
                }

                // 4. 수정 코드 중복 여부
                if ("U".equals(state) && !comCd.equals(exComCd)) {
                    if (codeMngMapper.selectCodeMngListCnt(codeMngVO) > 0) {
                        isDupUpdateCode = true;
                    }
                }
            }

            if (isBlankCode) {
                code = "B";
            } else if (isDupCode) {
                code = "D";
            } else if (isDupInsertCode) {
                code = "I";
            } else if (isDupUpdateCode) {
                code = "U";
            } else {
                for (int i = 0; i < cnt; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    codeMngVO = (CodeMngVO) JSONObject.toBean(jsonObject, CodeMngVO.class);
                    if ("I".equals(codeMngVO.getState())) {
                        codeMngMapper.insertCodeMngData(codeMngVO);
                    } else if ("U".equals(codeMngVO.getState())) {
                        codeMngMapper.updateCodeMngData(codeMngVO);
                    }
                }
                code = "S";
            }
        } else {
            code = "N";
        }
        return code;
    }

    /**
     * <p>그룹 코드 삭제</p>
     *
     * @param codeMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String deleteGrpMngData(CodeMngVO codeMngVO) throws Exception {
        String code = null;
        int cnt = 0;
        boolean isDupGrp = false;
        String[] arrGrpCd = StringUtil.split(codeMngVO.getGrpCd());

        // 공통 코드 체크
        for (String grpCd : arrGrpCd) {
            codeMngVO.setGrpCd(grpCd);
            if (codeMngMapper.selectCodeMngListCnt(codeMngVO) > 0) {
                isDupGrp = true;
                break;
            }
        }

        if (isDupGrp) {
            code = "D";
        } else {
            for (String grpCd : arrGrpCd) {
                codeMngVO.setGrpCd(grpCd);
                cnt += codeMngMapper.deleteGrpMngData(codeMngVO);
            }
            code = cnt > 0 ? "S" : "N";
        }
        return code;
    }

    /**
     * <p>공통 코드 삭제</p>
     *
     * @param codeMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String deleteCodeMngData(CodeMngVO codeMngVO) throws Exception {
        String code = null;
        int cnt = 0;
        String[] arrComCd = StringUtil.split(codeMngVO.getComCd());

        for (String comCd : arrComCd) {
            codeMngVO.setComCd(comCd);
            cnt += codeMngMapper.deleteCodeMngData(codeMngVO);
        }
        code = cnt > 0 ? "S" : "N";

        return code;
    }

    /**
     * <p>그룹 코드 엑셀 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<CodeMngVO> selectGrpMngExcelList(CodeMngVO codeMngVO) throws Exception {
        return codeMngMapper.selectGrpMngExcelList(codeMngVO);
    }

    /**
     * <p>공통 코드 엑셀 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<CodeMngVO> selectCodeMngExcelList(CodeMngVO codeMngVO) throws Exception {
        return codeMngMapper.selectCodeMngExcelList(codeMngVO);
    }
}
