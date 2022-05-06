package com.jsplan.drp.domain.sys.codemng.repository;

import com.jsplan.drp.domain.sys.codemng.dto.CodeMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngListDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : CodeMngCustomRepository
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 공통 코드 관리 Custom Repository
 */
public interface CodeMngCustomRepository {

    /**
     * <p>공통 코드 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param pageable   (페이징 정보)
     * @return Page (공통 코드 목록)
     */
    Page<CodeMngListDTO> searchCodeMngList(String grpCd, String searchCd, String searchWord,
        Pageable pageable);

    /**
     * <p>공통 코드 상세</p>
     *
     * @param grpCd (그룹 코드)
     * @param comCd (공통 코드)
     * @return CodeMngDetailDTO (공통 코드 DTO)
     */
    CodeMngDetailDTO findCodeMngByComCd(String grpCd, String comCd);

    /**
     * <p>공통 코드 확인</p>
     *
     * @param grpCd (그룹 코드)
     * @param comCd (공통 코드)
     * @return boolean (공통 코드 존재 여부)
     */
    boolean existsCodeMngByComCd(String grpCd, String comCd);

    /**
     * <p>공통 코드 엑셀 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return List (공통 코드 목록)
     */
    List<CodeMngListDTO> searchCodeMngExcelList(String grpCd, String searchCd, String searchWord);
}
