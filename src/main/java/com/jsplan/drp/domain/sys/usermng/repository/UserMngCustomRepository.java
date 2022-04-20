package com.jsplan.drp.domain.sys.usermng.repository;

import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngListDto;
import com.jsplan.drp.global.obj.entity.UseStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : UserMngCustomRepository
 * @Author : KDW
 * @Date : 2022-04-08
 * @Description : 사용자 관리 Custom Repository
 */
public interface UserMngCustomRepository {

    /**
     * <p>사용자 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @param pageable   (페이징 정보)
     * @return Page (페이징 목록)
     */
    Page<UserMngListDto> searchPageList(String grpCd, String searchCd, String searchWord,
        UseStatus useYn, Pageable pageable);

    /**
     * <p>사용자 상세</p>
     *
     * @param userId (사용자 아이디)
     * @return UserMngDetailDto (사용자 DTO)
     */
    UserMngDetailDto findByUserId(String userId);

    /**
     * <p>사용자 엑셀 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (사용자 목록)
     */
    List<UserMngListDto> searchExcelList(String grpCd, String searchCd, String searchWord,
        UseStatus useYn);
}