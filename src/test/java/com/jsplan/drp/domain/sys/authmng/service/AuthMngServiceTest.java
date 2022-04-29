package com.jsplan.drp.domain.sys.authmng.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jsplan.drp.domain.sys.authmng.dto.AuthMenuMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngDetailDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngRequest;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngRequestBuilder;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngSearchDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngSearchDTOBuilder;
import com.jsplan.drp.domain.sys.authmng.dto.AuthUserMngListDTO;
import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import com.jsplan.drp.domain.sys.authmng.repository.AuthMngRepository;
import com.jsplan.drp.global.obj.entity.UseStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import net.sf.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * @Class : AuthMngServiceTest
 * @Author : KDW
 * @Date : 2022-04-28
 * @Description : 권한 관리 Service Test
 */
@ExtendWith(MockitoExtension.class)
class AuthMngServiceTest {

    @InjectMocks
    AuthMngService authMngService;

    @Mock
    AuthMngRepository authMngRepository;

    AuthMngRequest request;
    AuthMng authMng;
    AuthMngSearchDTO searchDTO;
    AuthMngListDTO listDTO;
    AuthMngDetailDTO detailDTO;

    String authCd, upperAuthCd, authNm;
    Integer authLv, authOrd;
    UseStatus useYn;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        authCd = "AUTH_TEST";
        upperAuthCd = "AUTH_ADMIN";
        authNm = "테스트 권한";
        authLv = 2;
        authOrd = 2;
        useYn = UseStatus.Y;

        request = AuthMngRequestBuilder.build(authCd, upperAuthCd, authNm, null, authLv, authOrd);
        authMng = AuthMng.builder().authCd(authCd)
            .upperAuthMng(AuthMng.builder().authCd(upperAuthCd).build()).authNm(authNm)
            .authLv(authLv).authOrd(authOrd).build();

        listDTO = new AuthMngListDTO(authCd, authNm, null, authLv, authOrd, useYn, "Y");
        detailDTO = new AuthMngDetailDTO(authCd, upperAuthCd, authNm, null, authLv, authOrd, useYn);
    }

    @Test
    @DisplayName("권한 목록 조회 테스트")
    public void selectAuthMngList() throws Exception {
        // given
        List<AuthMngListDTO> authMngList = new ArrayList<>();
        authMngList.add(listDTO);
        searchDTO = AuthMngSearchDTOBuilder.buildAuthMng(authCd, "authCd", "", useYn);

        // mocking
        given(authMngRepository.searchAuthMngList(anyString(), anyString(), anyString(),
            any())).willReturn(authMngList);

        // when
        JSONArray authMngArray = authMngService.selectAuthMngList(searchDTO);

        // then
        assertThat(authMngArray.getJSONObject(0).get("authCd")).isEqualTo(authCd);
        assertThat(authMngArray.getJSONObject(0).get("authNm")).isEqualTo(authNm);
        assertThat(authMngArray.getJSONObject(0).get("leaf")).isEqualTo(true);
    }

    @Test
    @DisplayName("상위 권한 목록 조회 테스트")
    public void selectUpperAuthMngList() throws Exception {
        // given
        List<AuthMng> authMngList = new ArrayList<>();
        authMngList.add(AuthMng.builder().authCd("AUTH_ADMIN").authNm("슈퍼 관리자 권한").build());
        authMngList.add(AuthMng.builder().authCd("AUTH_NORMAL").authNm("일반 회원 권한").build());
        authMngList.add(AuthMng.builder().authCd("AUTH_SNS").authNm("SNS 회원 권한").build());

        // mocking
        given(authMngRepository.findUpperAuthMngByUseYn(any())).willReturn(authMngList);

        // when
        List<AuthMngListDTO> upperAuthMngList = authMngService.selectUpperAuthMngList();

        // then
        assertThat(upperAuthMngList.get(0).getAuthCd()).isEqualTo("AUTH_ADMIN");
        assertThat(upperAuthMngList.get(1).getAuthCd()).isEqualTo("AUTH_NORMAL");
        assertThat(upperAuthMngList.get(2).getAuthCd()).isEqualTo("AUTH_SNS");
    }

    @Test
    @DisplayName("권한 상세 조회 테스트")
    public void selectAuthMngDetail() throws Exception {
        // mocking
        given(authMngRepository.findAuthMngByAuthCd(anyString())).willReturn(detailDTO);

        // when
        AuthMngDetailDTO findDetail = authMngService.selectAuthMngDetail(request);

        // then
        assertThat(findDetail.getAuthCd()).isEqualTo(authCd);
        assertThat(findDetail.getAuthNm()).isEqualTo(authNm);
    }

    @Test
    @DisplayName("권한 등록 테스트")
    public void insertAuthMngData() throws Exception {
        // mocking
        given(authMngRepository.save(any())).willReturn(authMng);
        given(authMngRepository.findAuthMngByAuthCd(anyString())).willReturn(detailDTO);

        // when
        authMngService.insertAuthMngData(request);
        String findAuthCd = authMngService.selectAuthMngDetail(request).getAuthCd();

        // then
        assertThat(findAuthCd).isEqualTo(authCd);
    }

    @Test
    @DisplayName("권한 수정 테스트")
    public void updateAuthMngData() throws Exception {
        // given
        request = AuthMngRequestBuilder.build(authCd, upperAuthCd, "권한 수정", null, authLv, authOrd);

        // mocking
        when(authMngRepository.findById(any())).thenReturn(Optional.of(authMng));

        // when
        authMngService.updateAuthMngData(request);

        // then
        assertThat(authMng.getAuthNm()).isEqualTo(request.getAuthNm());
    }

    @Test
    @DisplayName("권한 삭제 테스트")
    public void deleteAuthMngData() throws Exception {
        // when
        authMngService.deleteAuthMngData(request);

        // then
        verify(authMngRepository, times(1)).deleteById(request.getAuthCd());
    }

    @Test
    @DisplayName("권한별 사용자 목록 조회 테스트")
    public void selectAuthUserMngList() throws Exception {
        // given
        List<AuthUserMngListDTO> list = new ArrayList<>();
        list.add(new AuthUserMngListDTO("sys_app", "TEST1", "010-1111-1111"));
        list.add(new AuthUserMngListDTO("kdy0185", "TEST2", "010-2222-2222"));
        Page<AuthUserMngListDTO> pageList = new PageImpl<>(list);
        searchDTO = AuthMngSearchDTOBuilder.buildAuthUserMng(0, 200, "AUTH_ADMIN", "GRP_MNG",
            "authCd", "");

        // mocking
        given(authMngRepository.searchAuthUserMngList(anyList(), anyString(), anyString(),
            anyString(), any())).willReturn(pageList);

        // when
        Page<AuthUserMngListDTO> authUserMngList = authMngService.selectAuthUserMngList(searchDTO);

        // then
        assertThat(authUserMngList.getNumberOfElements()).isEqualTo(2);
        assertThat(authUserMngList.stream().findFirst().orElseThrow(NoSuchElementException::new)
            .getUserNm()).contains("TEST");
    }

    @Test
    @DisplayName("사용자 설정 적용 테스트")
    public void updateAuthUserMngData() throws Exception {
        // given
        String authCdList = "AUTH_TEST";
        String userIdList = "sys_app";
        String authYn = "Y";

        // mocking
        when(authMngRepository.findById(any())).thenReturn(Optional.of(authMng));

        // when
        authMngService.updateAuthUserMngData(authCdList, userIdList, authYn);

        // then
        assertThat(authMng.getAuthCd()).isEqualTo(authCdList);
        assertThat(authMng.getAuthUserMng().get(0).getUserMng().getUserId()).isEqualTo(userIdList);
    }


    @Test
    @DisplayName("권한별 메뉴 목록 조회 테스트")
    public void selectAuthMenuMngList() throws Exception {
        // given
        List<AuthMenuMngListDTO> authMenuMngList = new ArrayList<>();
        authMenuMngList.add(new AuthMenuMngListDTO("S0100", "일정 통계", "Y", "Y"));
        authMenuMngList.add(new AuthMenuMngListDTO("S0200", "거래 통계", "Y", "Y"));
        searchDTO = AuthMngSearchDTOBuilder.buildAuthMenuMng("AUTH_NORMAL", "S0000");

        // mocking
        given(authMngRepository.searchAuthMenuMngList(anyList(), anyString())).willReturn(
            authMenuMngList);

        // when
        JSONArray menuArray = authMngService.selectAuthMenuMngList(searchDTO);

        // then
        assertThat(menuArray.getJSONObject(0).get("id")).isEqualTo("S0100");
        assertThat(menuArray.getJSONObject(0).get("leaf")).isEqualTo(true);
        assertThat(menuArray.getJSONObject(0).get("checked")).isEqualTo(true);
    }

    @Test
    @DisplayName("메뉴 설정 적용 테스트")
    public void updateAuthMenuMngData() throws Exception {
        // given
        String authCdList = "AUTH_TEST";
        String menuCdList = "S0100";

        // mocking
        when(authMngRepository.findById(any())).thenReturn(Optional.of(authMng));

        // when
        authMngService.updateAuthMenuMngData(authCdList, menuCdList);

        // then
        assertThat(authMng.getAuthCd()).isEqualTo(authCdList);
        assertThat(authMng.getAuthMenuMng().get(0).getMenuMng().getMenuCd()).isEqualTo(menuCdList);
    }
}
