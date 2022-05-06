package com.jsplan.drp.domain.sys.codemng.repository;

import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngListDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : CodeGrpMngCustomRepository
 * @Author : KDW
 * @Date : 2022-05-02
 * @Description : 그룹 코드 관리 Custom Repository
 */
public interface CodeGrpMngCustomRepository {

    /**
     * <p>그룹 코드 목록</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param pageable   (페이징 정보)
     * @return Page (그룹 코드 목록)
     */
    Page<CodeGrpMngListDTO> searchCodeGrpMngList(String searchCd, String searchWord,
        Pageable pageable);

    /**
     * <p>그룹 코드 상세</p>
     *
     * @param grpCd (그룹 코드)
     * @return CodeGrpMngDetailDTO (그룹 코드 DTO)
     */
    CodeGrpMngDetailDTO findCodeGrpMngByGrpCd(String grpCd);

    /**
     * <p>그룹 코드 확인</p>
     *
     * @param grpCd (그룹 코드)
     * @return boolean (그룹 코드 존재 여부)
     */
    boolean existsCodeGrpMngByGrpCd(String grpCd);

    /**
     * <p>공통 코드 확인</p>
     *
     * @param grpCd (그룹 코드)
     * @return boolean (공통 코드 존재 여부)
     */
    boolean existsCodeMngByGrpCd(String grpCd);

    /**
     * <p>그룹 코드 엑셀 목록</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return List (그룹 코드 목록)
     */
    List<CodeGrpMngListDTO> searchCodeGrpMngExcelList(String searchCd, String searchWord);
}
