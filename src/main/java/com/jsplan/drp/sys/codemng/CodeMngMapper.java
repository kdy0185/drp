package com.jsplan.drp.sys.codemng;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Class : CodeMngMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 코드 관리 Mapper
 */
@Repository
@Mapper
public interface CodeMngMapper {

    /**
     * <p>그룹 코드 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<CodeMngVO> selectGrpMngList(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>그룹 코드 목록 수</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectGrpMngListCnt(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>공통 코드 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<CodeMngVO> selectCodeMngList(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>공통 코드 목록 수</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectCodeMngListCnt(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>그룹 코드 등록</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertGrpMngData(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>공통 코드 등록</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertCodeMngData(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>그룹 코드 상세</p>
     *
     * @param codeMngVO
     * @return CodeMngVO
     * @throws Exception throws Exception
     */
    CodeMngVO selectGrpMngDetail(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>공통 코드 상세</p>
     *
     * @param codeMngVO
     * @return CodeMngVO
     * @throws Exception throws Exception
     */
    CodeMngVO selectCodeMngDetail(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>그룹 코드 수정</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int updateGrpMngData(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>공통 코드 수정</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int updateCodeMngData(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>그룹 코드 삭제</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteGrpMngData(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>공통 코드 삭제</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteCodeMngData(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>그룹 코드 엑셀 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<CodeMngVO> selectGrpMngExcelList(CodeMngVO codeMngVO) throws Exception;

    /**
     * <p>공통 코드 엑셀 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<CodeMngVO> selectCodeMngExcelList(CodeMngVO codeMngVO) throws Exception;
}
