package com.jsplan.drp.main;

import com.jsplan.drp.cmmn.obj.AbstractDAO;
import org.springframework.stereotype.Repository;

/**
 * @Class : MainMapper
 * @Author : KDW
 * @Date : 2022-01-19
 * @Description : 메인 화면 Mapper
 */
@Repository("MainMapper")
public class MainMapper extends AbstractDAO {

    String namespace = "Main.";

    /**
     * <p>사용자 상세</p>
     *
     * @param mainVO
     * @return MainVO
     * @throws Exception throws Exception
     */
    public MainVO selectMyInfoDetail(MainVO mainVO) throws Exception {
        return (MainVO) selectOne(namespace + "selectMyInfoDetail", mainVO);
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param mainVO
     * @return int
     * @throws Exception throws Exception
     */
    public int updateMyInfoData(MainVO mainVO) throws Exception {
        return (Integer) update(namespace + "updateMyInfoData", mainVO);
    }
}
