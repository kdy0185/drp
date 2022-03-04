package com.jsplan.drp.domain.main;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Class : MainMapper
 * @Author : KDW
 * @Date : 2022-01-19
 * @Description : 메인 화면 Mapper
 */
@Repository
@Mapper
public interface MainMapper {

    /**
     * <p>사용자 상세</p>
     *
     * @param mainVO
     * @return MainVO
     * @throws Exception throws Exception
     */
    MainVO selectMyInfoDetail(MainVO mainVO) throws Exception;

    /**
     * <p>사용자 수정</p>
     *
     * @param mainVO
     * @return int
     * @throws Exception throws Exception
     */
    int updateMyInfoData(MainVO mainVO) throws Exception;
}
