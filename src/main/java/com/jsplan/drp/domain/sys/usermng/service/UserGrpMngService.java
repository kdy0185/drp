package com.jsplan.drp.domain.sys.usermng.service;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngResponse;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngSearchDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDto.UserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDto.UserGrpMngListDto;
import com.jsplan.drp.domain.sys.usermng.repository.UserGrpMngRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
    public Page<UserGrpMngListDto> selectUserGrpMngList(UserGrpMngSearchDto searchDto) {
        PageRequest pageRequest = PageRequest.of(searchDto.getPageNo(), searchDto.getPageSize());
        return userGrpMngRepository.searchPageList(searchDto.getGrpNm(), pageRequest);
    }

    /**
     * <p>그룹 상세</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngDto (그룹 DTO)
     */
    public UserGrpMngDetailDto selectUserGrpMngDetail(UserGrpMngRequest request) {
        return userGrpMngRepository.findByGrpCd(request.getGrpCd());
    }

    /**
     * <p>그룹 등록</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (응답 정보)
     */
    @Transactional
    public UserGrpMngResponse insertUserGrpMngData(UserGrpMngRequest request) {
        if (validateDupGrpMngData(request)) {
            return new UserGrpMngResponse(null, "D");
        } else {
            UserGrpMng userGrpMng = userGrpMngRepository.save(request.toEntity());
            return new UserGrpMngResponse(userGrpMng.getGrpCd(), "S");
        }
    }

    /**
     * <p>중복 그룹 체크</p>
     *
     * @param request (그룹 정보)
     * @return boolean (중복 여부)
     */
    private boolean validateDupGrpMngData(UserGrpMngRequest request) {
        Optional<UserGrpMng> optionalUserGrpMng = userGrpMngRepository.findById(request.getGrpCd());
        return optionalUserGrpMng.isPresent();
//        optionalUserGrpMng.ifPresent(userGrpMng -> {
//            throw new DataIntegrityViolationException(ErrorStatus.DUPLICATED_KEY.getMessage());
//        });
    }

    /**
     * <p>그룹 수정</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (응답 정보)
     */
    @Transactional
    public UserGrpMngResponse updateUserGrpMngData(UserGrpMngRequest request) {
        UserGrpMng userGrpMng = userGrpMngRepository.findById(request.getGrpCd())
            .orElseThrow(NoSuchElementException::new);
        userGrpMng.update(request);
        return new UserGrpMngResponse(userGrpMng.getGrpCd(), "S");
    }

    /**
     * <p>그룹 삭제</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (응답 정보)
     */
    @Transactional
    public UserGrpMngResponse deleteUserGrpMngData(UserGrpMngRequest request) {
        if (existsUserMngData(request)) { // 그룹 내 사용자가 존재할 경우
            return new UserGrpMngResponse(request.getGrpCd(), "F");
        } else {
            userGrpMngRepository.deleteById(request.getGrpCd());
            return new UserGrpMngResponse(request.getGrpCd(), "S");
        }
    }

    /**
     * <p>사용자 확인</p>
     *
     * @param request (그룹 정보)
     * @return boolean (사용자 존재 여부)
     */
    private boolean existsUserMngData(UserGrpMngRequest request) {
        return userGrpMngRepository.existsUserMngByGrpCd(request.getGrpCd());
    }
}
