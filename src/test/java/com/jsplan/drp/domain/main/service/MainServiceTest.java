package com.jsplan.drp.domain.main.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequestBuilder;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.domain.sys.usermng.repository.UserMngRepository;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import com.jsplan.drp.global.obj.vo.UseStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @Class : MainServiceTest
 * @Author : KDW
 * @Date : 2022-05-19
 * @Description : 메인 화면 Service Test
 */
@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @InjectMocks
    MainService mainService;

    @Mock
    UserMngRepository userMngRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    UserMngRequest request;
    UserMng userMng;
    UserMngDetailDTO detailDTO;

    String grpCd, grpNm, userId, userNm, userPw, mobileNum, email, userType, authCd;
    UseStatus useYn;
    LocalDateTime regDate, modDate;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        grpCd = "GRP_USER";
        grpNm = "사용자 그룹";
        userId = "123456";
        userNm = "테스트";
        userPw = "123456";
        mobileNum = "010-0000-0000";
        email = "test@mail.com";
        userType = "T";
        useYn = UseStatus.Y;
        authCd = "AUTH_NORMAL";
        regDate = LocalDateTime.now();
        modDate = LocalDateTime.now();

        request = UserMngRequestBuilder.build(grpCd, userId, userNm, userPw, mobileNum, email,
            userType, useYn, authCd);
        userMng = UserMng.builder().userId(userId).userNm(userNm).userPw(userPw)
            .mobileNum(mobileNum).email(email).userType(userType).useYn(useYn)
            .userGrpMng(UserGrpMng.builder().grpCd(grpCd).build()).userAuthMng(authCd).build();
        ReflectionTestUtils.setField(userMng, BaseTimeEntity.class, "regDate", regDate,
            LocalDateTime.class);
        ReflectionTestUtils.setField(userMng, BaseTimeEntity.class, "modDate", modDate,
            LocalDateTime.class);

        detailDTO = new UserMngDetailDTO(grpCd, grpNm, userId, userNm, mobileNum, email, userType,
            useYn, regDate, modDate);
    }

    @Test
    @DisplayName("사용자 상세 조회 테스트")
    public void selectMyInfoDetail() {
        // mocking
        given(userMngRepository.findUserMngByUserId(anyString())).willReturn(detailDTO);

        // when
        UserMngDetailDTO findDetail = mainService.selectMyInfoDetail(request);

        // then
        assertThat(findDetail.getUserId()).isEqualTo(userId);
        assertThat(findDetail.getEmail()).isEqualTo(email);
        assertThat(LocalDateTime.parse(findDetail.getRegDate(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("사용자 수정 테스트")
    public void updateMyInfoData() {
        // given
        request = UserMngRequestBuilder.build(grpCd, userId, "이름 수정", userPw, mobileNum,
            "test2@mail.com", userType, useYn, authCd);

        // mocking
        when(userMngRepository.findById(any())).thenReturn(Optional.of(userMng));

        // when
        mainService.updateMyInfoData(request);

        // then
        assertThat(userMng.getUserNm()).isEqualTo(request.getUserNm());
        assertThat(userMng.getEmail()).isEqualTo(request.getEmail());
    }
}
