package com.jsplan.drp.domain.sys.authmng.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.domain.sys.authmng.dto.AuthMenuMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngDetailDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngRequest;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngRequestBuilder;
import com.jsplan.drp.domain.sys.authmng.dto.AuthUserMngListDTO;
import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import com.jsplan.drp.global.obj.vo.UseStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : AuthMngRepositoryTest
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 관리 Repository Test
 */
@SpringBootTest
@Transactional
class AuthMngRepositoryTest {

    @Autowired
    AuthMngRepository authMngRepository;

    AuthMngRequest request;
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
    }

    @Test
    @DisplayName("권한 목록 조회 테스트")
    public void searchAuthMngList() {
        // given
        String authCd = "AUTH_ADMIN";
        String searchCd = "authNm";
        String searchWord = "";

        // when
        List<AuthMngListDTO> authMngList = authMngRepository.searchAuthMngList(authCd, searchCd,
            searchWord, useYn);

        // then
        assertThat(authMngList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("권한 상세 조회 테스트")
    public void findAuthMngByAuthCd() {
        // given
        String authCd = "AUTH_ADMIN";

        // when
        AuthMngDetailDTO detailDTO = authMngRepository.findAuthMngByAuthCd(authCd);

        // then
        assertThat(detailDTO.getAuthNm()).isEqualTo("슈퍼 관리자 권한");
    }

    @Test
    @DisplayName("권한 등록 테스트")
    public void insertAuthMngData() {
        // when
        AuthMng savedAuthMng = authMngRepository.save(request.toEntity());
        String findAuthCd = authMngRepository.findAuthMngByAuthCd(savedAuthMng.getAuthCd())
            .getAuthCd();

        // then
        assertThat(findAuthCd).isEqualTo(authCd);
    }

    @Test
    @DisplayName("권한 수정 테스트")
    public void updateAuthMngData() {
        // given
        String authCd = "AUTH_NORMAL";
        String authNm = "일반 회원 권한 수정";
        LocalDateTime beforeDate = LocalDateTime.now();

        // when
        AuthMngRequest request = AuthMngRequestBuilder.build(authCd, upperAuthCd, authNm, null,
            authLv, authOrd);
        AuthMng authMng = authMngRepository.findById(request.getAuthCd())
            .orElseThrow(NoSuchElementException::new);
        authMng.updateAuthMng(request);
        authMngRepository.flush();

        // then
        assertThat(authMng.getAuthNm()).isEqualTo(authNm);
//        assertThat(authMng.getModDate()).isAfter(beforeDate);
    }

    @Test
    @DisplayName("권한 삭제 테스트")
    public void deleteAuthMngData() {
        // when
        authMngRepository.saveAndFlush(request.toEntity());

        authMngRepository.deleteById(authCd);
        AuthMngDetailDTO detailDTO = authMngRepository.findAuthMngByAuthCd(authCd);

        // then
        assertThat(detailDTO).isNull();
    }

    @Test
    @DisplayName("권한별 사용자 목록 조회 테스트")
    public void searchAuthUserMngList() {
        // given
        String authCd = "AUTH_ADMIN";
        List<String> authCdList = List.of(authCd.split(","));
        String grpCd = "GRP_MNG";
        String searchCd = "authNm";
        String searchWord = "";
        PageRequest pageRequest = PageRequest.of(0, 200);

        // when
        Page<AuthUserMngListDTO> authUserMngList = authMngRepository.searchAuthUserMngList(
            authCdList, grpCd, searchCd, searchWord, pageRequest);

        // then
        for (AuthUserMngListDTO listDTO : authUserMngList) {
            System.out.println("===================================");
            System.out.println(listDTO.toString());
            System.out.println("===================================");
        }
    }

    @Test
    @DisplayName("권한별 메뉴 목록 조회 테스트")
    public void searchAuthMenuMngList() {
        // given
        String authCd = "AUTH_NORMAL";
        List<String> authCdList = List.of(authCd.split(","));
        String menuCd = "S0000";

        // when
        List<AuthMenuMngListDTO> authMenuMngList = authMngRepository.searchAuthMenuMngList(
            authCdList, menuCd);

        // then
        for (AuthMenuMngListDTO listDTO : authMenuMngList) {
            System.out.println("===================================");
            System.out.println(listDTO.toString());
            System.out.println("===================================");
        }
    }
}
