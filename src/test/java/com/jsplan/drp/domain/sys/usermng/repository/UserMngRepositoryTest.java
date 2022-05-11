package com.jsplan.drp.domain.sys.usermng.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.domain.sys.usermng.dto.UserAuthMngListDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngListDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequestBuilder;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : UserMngRepositoryTest
 * @Author : KDW
 * @Date : 2022-04-08
 * @Description : 사용자 관리 Repository Test
 */
@SpringBootTest
@Transactional
class UserMngRepositoryTest {

    @Autowired
    UserMngRepository userMngRepository;

    UserMngRequest request;
    String grpCd, userId, userNm, userPw, mobileNum, email, userType, authCd;
    UseStatus useYn;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        grpCd = "GRP_USER";
        userId = "123456";
        userNm = "테스트";
        userPw = "{bcrypt}$2a$10$gAlQx3W6H27NoAWBCDOSsexy5JH.1tlhB4Z7mqAt8fS/WofiNW69m";
        mobileNum = "010-0000-0000";
        email = "test@mail.com";
        userType = "T";
        useYn = UseStatus.Y;
        authCd = "AUTH_NORMAL";

        request = UserMngRequestBuilder.build(grpCd, userId, userNm, userPw, mobileNum, email,
            userType, useYn, authCd);
    }

    @Test
    @DisplayName("사용자 목록 조회 테스트")
    public void searchUserMngList() {
        // given
        String grpCd = "GRP_USER";
        String searchCd = "userNm";
        String searchWord = "정";
        UseStatus useYn = UseStatus.Y;
        PageRequest pageRequest = PageRequest.of(0, 200);

        // when
        Page<UserMngListDTO> userMngList = userMngRepository.searchUserMngList(grpCd, searchCd,
            searchWord, useYn, pageRequest);

        // then
        assertThat(userMngList.getNumberOfElements()).isEqualTo(28);
    }

    @Test
    @DisplayName("사용자 상세 조회 테스트")
    public void findUserMngByUserId() {
        // given
        String userId = "sys_app";

        // when
        UserMngDetailDTO detailDTO = userMngRepository.findUserMngByUserId(userId);

        // then
        assertThat(detailDTO.getUserNm()).isEqualTo("시스템 관리자");
        assertThat(detailDTO.getEmail()).isEqualTo("system@mail.com");
    }

    @Test
    @DisplayName("사용자별 권한 목록 조회 테스트")
    public void searchUserAuthMngList() {
        // given
        String userId = "075082,424981,784252,885235";
        List<String> userIdList = List.of(userId.split(","));

        // when
        List<UserAuthMngListDTO> authMngList = userMngRepository.searchUserAuthMngList(userIdList,
            "AUTH_ADMIN");

        // then
        for (UserAuthMngListDTO listDTO : authMngList) {
            System.out.println("===================================");
            System.out.println(listDTO.toString());
            System.out.println("===================================");
        }
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 등록 테스트")
    public void insertUserMngData() {
        // when
        UserMng savedUserMng = userMngRepository.save(request.toEntity());
        String findUserId = userMngRepository.findUserMngByUserId(savedUserMng.getUserId())
            .getUserId();

        // then
        assertThat(findUserId).isEqualTo(userId);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 수정 테스트")
    public void updateUserMngData() {
        // given
        String userId = "sys_app";
        String userNm = "관리자";
        String email = "test@mail.com";
        String userType = "T";
        UseStatus useYn = UseStatus.Y;
        LocalDateTime beforeDate = LocalDateTime.now();

        // when
        UserMngRequest request = UserMngRequestBuilder.build(null, userId, userNm, null, null,
            email, userType, useYn, authCd);
        UserMng userMng = userMngRepository.findById(request.getUserId()).orElseThrow(
            NoSuchElementException::new);
        userMng.updateUserMng(request);
        userMngRepository.flush();

        // then
        assertThat(userMng.getUserNm()).isEqualTo(userNm);
        assertThat(userMng.getEmail()).isEqualTo(email);
        assertThat(userMng.getModDate()).isAfter(beforeDate);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("사용자 삭제 테스트")
    public void deleteUserMngData() {
        // when
        userMngRepository.saveAndFlush(request.toEntity());

        userMngRepository.deleteById(userId);
        UserMngDetailDTO detailDTO = userMngRepository.findUserMngByUserId(userId);

        // then
        assertThat(detailDTO).isNull();
    }
}
