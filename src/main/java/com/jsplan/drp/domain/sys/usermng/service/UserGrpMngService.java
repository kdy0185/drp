package com.jsplan.drp.domain.sys.usermng.service;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngResponse;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngSearchDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto.UserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto.UserGrpMngListDto;
import com.jsplan.drp.domain.sys.usermng.repository.UserGrpMngRepository;
import com.jsplan.drp.global.exception.entity.ErrorStatus;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : UserGrpMngService
 * @Author : KDW
 * @Date : 2022-03-24
 * @Description : 그룹 관리 Service
 */
@Service
@RequiredArgsConstructor
public class UserGrpMngService {

    private final UserGrpMngRepository userGrpMngRepository;

    /**
     * <p>그룹 목록</p>
     *
     * @param searchDto (조회 조건)
     * @return Page (페이징 목록)
     */
    public Page<UserGrpMngListDto> selectGrpMngList(UserGrpMngSearchDto searchDto) {
        PageRequest pageRequest = PageRequest.of(searchDto.getPageNo(), searchDto.getPageSize());
        return userGrpMngRepository.searchPageList(searchDto.getGrpNm(), pageRequest);
    }

    /**
     * <p>그룹 상세</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngDto (그룹 DTO)
     */
    public UserGrpMngDetailDto selectGrpMngDetail(UserGrpMngRequest request) {
        return userGrpMngRepository.findByGrpCd(request.getGrpCd());
    }

    /**
     * <p>그룹 등록</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (그룹 코드)
     */
    @Transactional
    public UserGrpMngResponse insertGrpMngData(UserGrpMngRequest request) {
        validateDupGrpMngData(request);
        UserGrpMng userGrpMng = userGrpMngRepository.save(request.toEntity());
        return new UserGrpMngResponse(userGrpMng.getGrpCd());
    }

    /**
     * <p>중복 그룹 체크</p>
     *
     * @param request (그룹 정보)
     */
    private void validateDupGrpMngData(UserGrpMngRequest request) {
        Optional<UserGrpMng> findData = userGrpMngRepository.findById(request.getGrpCd());
        findData.ifPresent(findUser -> {
            throw new DataIntegrityViolationException(ErrorStatus.DUPLICATED_KEY.getMessage());
        });
    }

    /**
     * <p>그룹 수정</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (그룹 코드)
     */
    @Transactional
    public UserGrpMngResponse updateGrpMngData(UserGrpMngRequest request) {
        UserGrpMng userGrpMng = userGrpMngRepository.findById(request.getGrpCd())
            .orElseThrow(NoSuchElementException::new);
        userGrpMng.update(request);
        return new UserGrpMngResponse(userGrpMng.getGrpCd());
    }

    /**
     * <p>그룹 삭제</p>
     *
     * @param request (그룹 정보)
     */
    @Transactional
    public void deleteGrpMngData(UserGrpMngRequest request) {
        userGrpMngRepository.deleteById(request.getGrpCd());
    }
}
