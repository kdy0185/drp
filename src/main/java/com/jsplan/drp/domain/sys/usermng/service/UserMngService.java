package com.jsplan.drp.domain.sys.usermng.service;

import com.jsplan.drp.domain.sys.usermng.dto.UserAuthMngListDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngListDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngResponse;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngSearchDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.domain.sys.usermng.repository.UserMngRepository;
import com.jsplan.drp.global.obj.entity.DataStatus;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : UserMngService
 * @Author : KDW
 * @Date : 2022-01-25
 * @Description : 사용자 관리 Service
 */
@Service
@RequiredArgsConstructor
public class UserMngService {

    private final UserMngRepository userMngRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * <p>사용자 목록</p>
     *
     * @param searchDto (조회 조건)
     * @return Page (페이징 목록)
     */
    public Page<UserMngListDto> selectUserMngList(UserMngSearchDto searchDto) {
        PageRequest pageRequest = PageRequest.of(searchDto.getPageNo(), searchDto.getPageSize());
        return userMngRepository.searchPageList(searchDto.getGrpCd(), searchDto.getSearchCd(),
            searchDto.getSearchWord(), searchDto.getUseYn(), pageRequest);
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param request (사용자 정보)
     * @return UserMngDto (사용자 DTO)
     */
    public UserMngDetailDto selectUserMngDetail(UserMngRequest request) {
        return userMngRepository.findByUserId(request.getUserId());
    }

    /**
     * <p>사용자 권한 목록</p>
     *
     * @param request (권한 정보)
     * @return JSONArray (권한 목록)
     */
    public JSONArray selectUserAuthMngList(UserMngRequest request) {
        JSONArray authArray = new JSONArray();
        JSONObject authObject;
        List<String> userIdList = List.of(request.getUserId().split(","));

        // 하위 권한 조회
        List<UserAuthMngListDto> userAuthMngList = userMngRepository.searchUserAuthMngList(
            userIdList, request.getAuthCd());
        for (UserAuthMngListDto listDto : userAuthMngList) {
            authObject = new JSONObject();
            authObject.put("id", listDto.getAuthCd());
            authObject.put("text", listDto.getAuthNm());
            authObject.put("leaf", "Y".equals(listDto.getLastYn()));
            authObject.put("expanded", !"Y".equals(listDto.getLastYn()));
            authObject.put("checked", "Y".equals(listDto.getAuthYn()));
            authArray.add(authObject);
        }

        return authArray;
    }

    /**
     * <p>사용자 아이디 중복 체크</p>
     *
     * @param request (사용자 정보)
     * @return String
     */
    public UserMngResponse selectUserMngDupCnt(UserMngRequest request) {
        DataStatus status = validateDupMngData(request) ? DataStatus.DUPLICATE : DataStatus.SUCCESS;
        return new UserMngResponse(request.getUserId(), status);
    }

    /**
     * <p>사용자 등록</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @Transactional
    public UserMngResponse insertUserMngData(UserMngRequest request) {
        if (validateDupMngData(request)) {
            return new UserMngResponse(null, DataStatus.DUPLICATE);
        } else {
            // BCrypt 패스워드 암호화
            String encodePw = passwordEncoder.encode(request.getUserPw());
            request.setUserPw(encodePw);

            UserMng userMng = userMngRepository.save(request.toEntity());

            return new UserMngResponse(userMng.getUserId(), DataStatus.SUCCESS);
        }
    }

    /**
     * <p>중복 사용자 체크</p>
     *
     * @param request (사용자 정보)
     * @return boolean (중복 여부)
     */
    private boolean validateDupMngData(UserMngRequest request) {
        Optional<UserMng> optionalUserMng = userMngRepository.findById(request.getUserId());
        return optionalUserMng.isPresent();
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @Transactional
    public UserMngResponse updateUserMngData(UserMngRequest request) {
        // BCrypt 패스워드 암호화
        String encodePw = passwordEncoder.encode(request.getUserPw());
        request.setUserPw(encodePw);

        UserMng userMng = userMngRepository.findById(request.getUserId())
            .orElseThrow(NoSuchElementException::new);
        userMng.update(request);
        return new UserMngResponse(userMng.getUserId(), DataStatus.SUCCESS);
    }

    /**
     * <p>사용자 삭제</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @Transactional
    public UserMngResponse deleteUserMngData(UserMngRequest request) {
        userMngRepository.deleteById(request.getUserId());
        return new UserMngResponse(request.getUserId(), DataStatus.SUCCESS);
    }

    /**
     * <p>사용자 엑셀 목록</p>
     *
     * @param searchDto (조회 조건)
     * @return List (사용자 목록)
     */
    public List<UserMngListDto> selectUserMngExcelList(UserMngSearchDto searchDto) {
        return userMngRepository.searchExcelList(searchDto.getGrpCd(), searchDto.getSearchCd(),
            searchDto.getSearchWord(), searchDto.getUseYn());
    }
}
