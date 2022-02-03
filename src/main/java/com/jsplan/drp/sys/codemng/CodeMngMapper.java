package com.jsplan.drp.sys.codemng;

import com.jsplan.drp.cmmn.obj.AbstractDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @Class : CodeMngMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 코드 관리 Mapper
 */
@Repository("CodeMngMapper")
public class CodeMngMapper extends AbstractDAO {

    String namespace = "CodeMng.";

    /**
     * <p>그룹 코드 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CodeMngVO> selectGrpMngList(CodeMngVO codeMngVO) throws Exception {
        return (List<CodeMngVO>) selectList(namespace + "selectGrpMngList", codeMngVO);
    }

    /**
     * <p>그룹 코드 목록 수</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectGrpMngListCnt(CodeMngVO codeMngVO) throws Exception {
        return (Integer) selectOne(namespace + "selectGrpMngListCnt", codeMngVO);
    }

    /**
     * <p>공통 코드 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CodeMngVO> selectCodeMngList(CodeMngVO codeMngVO) throws Exception {
        return (List<CodeMngVO>) selectList(namespace + "selectCodeMngList", codeMngVO);
    }

    /**
     * <p>공통 코드 목록 수</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectCodeMngListCnt(CodeMngVO codeMngVO) throws Exception {
        return (Integer) selectOne(namespace + "selectCodeMngListCnt", codeMngVO);
    }

    /**
     * <p>그룹 코드 등록</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertGrpMngData(CodeMngVO codeMngVO) throws Exception {
        return (Integer) update(namespace + "insertGrpMngData", codeMngVO);
    }

    /**
     * <p>공통 코드 등록</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertCodeMngData(CodeMngVO codeMngVO) throws Exception {
        return (Integer) update(namespace + "insertCodeMngData", codeMngVO);
    }

    /**
     * <p>그룹 코드 상세</p>
     *
     * @param codeMngVO
     * @return CodeMngVO
     * @throws Exception throws Exception
     */
    public CodeMngVO selectGrpMngDetail(CodeMngVO codeMngVO) throws Exception {
        return (CodeMngVO) selectOne(namespace + "selectGrpMngDetail", codeMngVO);
    }

    /**
     * <p>공통 코드 상세</p>
     *
     * @param codeMngVO
     * @return CodeMngVO
     * @throws Exception throws Exception
     */
    public CodeMngVO selectCodeMngDetail(CodeMngVO codeMngVO) throws Exception {
        return (CodeMngVO) selectOne(namespace + "selectCodeMngDetail", codeMngVO);
    }

    /**
     * <p>그룹 코드 수정</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int updateGrpMngData(CodeMngVO codeMngVO) throws Exception {
        return (Integer) update(namespace + "updateGrpMngData", codeMngVO);
    }

    /**
     * <p>공통 코드 수정</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int updateCodeMngData(CodeMngVO codeMngVO) throws Exception {
        return (Integer) update(namespace + "updateCodeMngData", codeMngVO);
    }

    /**
     * <p>그룹 코드 삭제</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteGrpMngData(CodeMngVO codeMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteGrpMngData", codeMngVO);
    }

    /**
     * <p>공통 코드 삭제</p>
     *
     * @param codeMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteCodeMngData(CodeMngVO codeMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteCodeMngData", codeMngVO);
    }

    /**
     * <p>그룹 코드 엑셀 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CodeMngVO> selectGrpMngExcelList(CodeMngVO codeMngVO) throws Exception {
        return (List<CodeMngVO>) selectList(namespace + "selectGrpMngExcelList", codeMngVO);
    }

    /**
     * <p>공통 코드 엑셀 목록</p>
     *
     * @param codeMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CodeMngVO> selectCodeMngExcelList(CodeMngVO codeMngVO) throws Exception {
        return (List<CodeMngVO>) selectList(namespace + "selectCodeMngExcelList", codeMngVO);
    }
}
