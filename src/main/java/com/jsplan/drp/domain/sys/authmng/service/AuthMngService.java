package com.jsplan.drp.domain.sys.authmng.service;

import com.jsplan.drp.domain.sys.authmng.dto.AuthMenuMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngDetailDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngRequest;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngResponse;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngSearchDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthUserMngListDTO;
import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import com.jsplan.drp.domain.sys.authmng.repository.AuthMngRepository;
import com.jsplan.drp.global.obj.vo.DataStatus;
import com.jsplan.drp.global.obj.vo.UseStatus;
import com.jsplan.drp.global.util.StringUtil;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : AuthMngService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 권한 관리 Service
 */
@Service
@RequiredArgsConstructor
public class AuthMngService {

    private final AuthMngRepository authMngRepository;

    /**
     * <p>권한 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return JSONArray (권한 목록)
     */
    public JSONArray selectAuthMngList(AuthMngSearchDTO searchDTO) {
        JSONArray authMngArray = new JSONArray();
        JSONObject authMngObject;

        // 하위 권한 조회
        List<AuthMngListDTO> authMngList = authMngRepository.searchAuthMngList(
            searchDTO.getAuthCd(), searchDTO.getSearchCd(), searchDTO.getSearchWord(),
            searchDTO.getUseYn());
        for (AuthMngListDTO listDTO : authMngList) {
            authMngObject = new JSONObject();
            authMngObject.put("authNm", listDTO.getAuthNm());
            authMngObject.put("authCd", listDTO.getAuthCd());
            authMngObject.put("authDesc", listDTO.getAuthDesc());
            authMngObject.put("authLv", listDTO.getAuthLv());
            authMngObject.put("authOrd", listDTO.getAuthOrd());
            authMngObject.put("useYn", listDTO.getUseYn());
            authMngObject.put("leaf", "Y".equals(listDTO.getLastYn()));
            authMngObject.put("expanded", !"Y".equals(listDTO.getLastYn()));

            authMngArray.add(authMngObject);
        }

        return authMngArray;
    }

    /**
     * <p>상위 권한 목록</p>
     *
     * @return List (권한 목록)
     */
    public List<AuthMngListDTO> selectUpperAuthMngList() {
        return authMngRepository.findUpperAuthMngByUseYn(UseStatus.Y).stream()
            .map(v -> new AuthMngListDTO(v.getAuthCd(), v.getAuthNm()))
            .collect(Collectors.toList());
    }

    /**
     * <p>권한 상세</p>
     *
     * @param request (권한 정보)
     * @return AuthMngDetailDTO (권한 DTO)
     */
    public AuthMngDetailDTO selectAuthMngDetail(AuthMngRequest request) {
        return authMngRepository.findAuthMngByAuthCd(request.getAuthCd());
    }

    /**
     * <p>권한 등록</p>
     *
     * @param request (권한 정보)
     * @return AuthMngResponse (응답 정보)
     */
    @Transactional
    public AuthMngResponse insertAuthMngData(AuthMngRequest request) {
        if (validateAuthMngDupData(request)) {
            return new AuthMngResponse(null, DataStatus.DUPLICATE);
        } else {
            AuthMng authMng = authMngRepository.save(request.toEntity());
            return new AuthMngResponse(authMng.getAuthCd(), DataStatus.SUCCESS);
        }
    }

    /**
     * <p>중복 권한 체크</p>
     *
     * @param request (권한 정보)
     * @return boolean (중복 여부)
     */
    private boolean validateAuthMngDupData(AuthMngRequest request) {
        Optional<AuthMng> optionalAuthMng = authMngRepository.findById(request.getAuthCd());
        return optionalAuthMng.isPresent();
    }

    /**
     * <p>권한 수정</p>
     *
     * @param request (권한 정보)
     * @return AuthMngResponse (응답 정보)
     */
    @Transactional
    public AuthMngResponse updateAuthMngData(AuthMngRequest request) {
        AuthMng authMng = authMngRepository.findById(request.getAuthCd())
            .orElseThrow(NoSuchElementException::new);
        authMng.updateAuthMng(request);
        return new AuthMngResponse(authMng.getAuthCd(), DataStatus.SUCCESS);
    }

    /**
     * <p>권한 삭제</p>
     *
     * @param request (권한 정보)
     * @return AuthMngResponse (응답 정보)
     */
    @Transactional
    public AuthMngResponse deleteAuthMngData(AuthMngRequest request) {
        if (existsAuthUserMngData(request)) { // 권한별 사용자가 존재할 경우
            return new AuthMngResponse(request.getAuthCd(), DataStatus.CONSTRAINT);
        } else if (existsAuthMenuMngData(request)) { // 권한별 메뉴가 존재할 경우
            return new AuthMngResponse(request.getAuthCd(), DataStatus.CONSTRAINT);
        } else {
            authMngRepository.deleteById(request.getAuthCd());
            return new AuthMngResponse(request.getAuthCd(), DataStatus.SUCCESS);
        }
    }

    /**
     * <p>권한별 사용자 확인</p>
     *
     * @param request (권한 정보)
     * @return boolean (사용자 존재 여부)
     */
    private boolean existsAuthUserMngData(AuthMngRequest request) {
        return authMngRepository.existsAuthUserMngByAuthCd(request.getAuthCd());
    }

    /**
     * <p>권한별 메뉴 확인</p>
     *
     * @param request (권한 정보)
     * @return boolean (메뉴 존재 여부)
     */
    private boolean existsAuthMenuMngData(AuthMngRequest request) {
        return authMngRepository.existsAuthMenuMngByAuthCd(request.getAuthCd());
    }

    /**
     * <p>권한별 사용자 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (사용자 목록)
     */
    public Page<AuthUserMngListDTO> selectAuthUserMngList(AuthMngSearchDTO searchDTO) {
        PageRequest pageRequest = PageRequest.of(searchDTO.getPageNo(), searchDTO.getPageSize());
        return authMngRepository.searchAuthUserMngList(List.of(searchDTO.getAuthCd().split(",")),
            searchDTO.getGrpCd(), searchDTO.getSearchCd(), searchDTO.getSearchWord(), pageRequest);
    }

    /**
     * <p>사용자 설정 적용</p>
     *
     * @param authCdList (권한 목록)
     * @param userIdList (사용자 아이디 목록)
     * @param authYn     (권한 허용 여부)
     * @return AuthMngResponse (응답 정보)
     */
    @Transactional
    public AuthMngResponse updateAuthUserMngData(String authCdList, String userIdList,
        String authYn) {
        for (String authCd : StringUtil.split(authCdList)) {
            AuthMng authMng = authMngRepository.findById(authCd)
                .orElseThrow(NoSuchElementException::new);
            authMng.updateAuthUserMng(userIdList, authYn);
        }
        return new AuthMngResponse(authCdList, DataStatus.SUCCESS);
    }

    /**
     * <p>권한별 메뉴 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return JSONArray (메뉴 목록)
     */
    public JSONArray selectAuthMenuMngList(AuthMngSearchDTO searchDTO) {
        JSONArray menuArray = new JSONArray();
        JSONObject menuObject;
        List<String> authCdList = List.of(searchDTO.getAuthCd().split(","));

        // 하위 메뉴 조회
        List<AuthMenuMngListDTO> authMenuMngList = authMngRepository.searchAuthMenuMngList(
            authCdList, searchDTO.getMenuCd());
        for (AuthMenuMngListDTO listDTO : authMenuMngList) {
            menuObject = new JSONObject();
            menuObject.put("id", listDTO.getMenuCd());
            menuObject.put("text", listDTO.getMenuNm());
            menuObject.put("leaf", "Y".equals(listDTO.getLastYn()));
            menuObject.put("expanded", !"Y".equals(listDTO.getLastYn()));
            menuObject.put("checked", "Y".equals(listDTO.getAuthYn()));
            menuArray.add(menuObject);
        }

        return menuArray;
    }

    /**
     * <p>메뉴 설정 적용</p>
     *
     * @param authCdList (권한 목록)
     * @param menuCdList (메뉴 코드 목록)
     * @return AuthMngResponse (응답 정보)
     */
    @Transactional
    public AuthMngResponse updateAuthMenuMngData(String authCdList, String menuCdList) {
        for (String authCd : StringUtil.split(authCdList)) {
            AuthMng authMng = authMngRepository.findById(authCd)
                .orElseThrow(NoSuchElementException::new);
            authMng.updateAuthMenuMng(menuCdList);
        }
        return new AuthMngResponse(authCdList, DataStatus.SUCCESS);
    }

    /**
     * <p>권한 엑셀 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (권한 목록)
     */
    public List<AuthMngListDTO> selectAuthMngExcelList(AuthMngSearchDTO searchDTO) {
        return authMngRepository.searchAuthMngExcelList(searchDTO.getAuthCd(),
            searchDTO.getSearchCd(), searchDTO.getSearchWord(), searchDTO.getUseYn());
    }
}
