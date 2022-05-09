package com.jsplan.drp.domain.sys.usermng.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jsplan.drp.domain.sys.usermng.dto.UserAuthMngListDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngListDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequestBuilder;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngSearchDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngSearchDTOBuilder;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.domain.sys.usermng.repository.UserMngRepository;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import com.jsplan.drp.global.obj.entity.UseStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @Class : UserMngServiceTest
 * @Author : KDW
 * @Date : 2022-04-18
 * @Description : 사용자 관리 Service Test
 */
@ExtendWith(MockitoExtension.class)
class UserMngServiceTest {

    @InjectMocks
    UserMngService userMngService;

    @Mock
    UserMngRepository userMngRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    UserMngRequest request;
    UserMng userMng;
    UserMngSearchDTO searchDTO;
    UserMngListDTO listDTO;
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

        searchDTO = UserMngSearchDTOBuilder.build(0, 20, grpCd, null, "userNm", "", useYn);

        listDTO = new UserMngListDTO(grpCd, grpNm, userId, userNm, mobileNum, email, userType,
            useYn, regDate, modDate);
        detailDTO = new UserMngDetailDTO(grpCd, userId, userNm, mobileNum, email, userType, useYn,
            regDate, modDate);
    }

    @Test
    @DisplayName("사용자 목록 조회 테스트")
    public void selectUserMngList() {
        // given
        List<UserMngListDTO> list = new ArrayList<>();
        list.add(listDTO);
        Page<UserMngListDTO> pageList = new PageImpl<>(list);

        // mocking
        given(userMngRepository.searchUserMngList(anyString(), anyString(), anyString(), any(),
            any())).willReturn(pageList);

        // when
        Page<UserMngListDTO> userMngList = userMngService.selectUserMngList(searchDTO);

        // then
        assertThat(userMngList.getNumberOfElements()).isEqualTo(1);
        assertThat(userMngList.stream().findFirst().orElseThrow(NoSuchElementException::new)
            .getUserNm()).contains(userNm);
    }

    @Test
    @DisplayName("사용자 상세 조회 테스트")
    public void selectUserMngDetail() {
        // mocking
        given(userMngRepository.findUserMngByUserId(anyString())).willReturn(detailDTO);

        // when
        UserMngDetailDTO findDetail = userMngService.selectUserMngDetail(request);

        // then
        assertThat(findDetail.getUserId()).isEqualTo(userId);
        assertThat(findDetail.getEmail()).isEqualTo(email);
        assertThat(LocalDateTime.parse(findDetail.getRegDate(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("사용자별 권한 목록 조회 테스트")
    public void selectUserAuthMngList() {
        // given
        request.setUserId("075082,424981,784252,885235");
        request.setAuthCd("AUTH_ADMIN");

        List<UserAuthMngListDTO> userAuthMngList = new ArrayList<>();
        userAuthMngList.add(new UserAuthMngListDTO("AUTH_NORMAL", "일반 회원 권한", "Y", "N"));

        // mocking
        given(userMngRepository.searchUserAuthMngList(anyList(), anyString())).willReturn(
            userAuthMngList);

        // when
        JSONArray authArray = userMngService.selectUserAuthMngList(request);

        // then
        assertThat(authArray.getJSONObject(0).get("id")).isEqualTo("AUTH_NORMAL");
        assertThat(authArray.getJSONObject(0).get("leaf")).isEqualTo(false);
        assertThat(authArray.getJSONObject(0).get("checked")).isEqualTo(true);
    }

    @Test
    @DisplayName("사용자 등록 테스트")
    public void insertUserMngData() {
        // mocking
        given(userMngRepository.save(any())).willReturn(userMng);
        given(userMngRepository.findUserMngByUserId(anyString())).willReturn(detailDTO);
        given(passwordEncoder.encode(anyString())).willReturn(userPw);

        // when
        userMngService.insertUserMngData(request);
        String findUserId = userMngService.selectUserMngDetail(request).getUserId();

        // then
        assertThat(findUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("사용자 수정 테스트")
    public void updateUserMngData() {
        // given
        request = UserMngRequestBuilder.build(grpCd, userId, "이름 수정", userPw, mobileNum,
            "test2@mail.com", userType, useYn, authCd);

        // mocking
        when(userMngRepository.findById(any())).thenReturn(Optional.of(userMng));

        // when
        userMngService.updateUserMngData(request);

        // then
        assertThat(userMng.getUserNm()).isEqualTo(request.getUserNm());
        assertThat(userMng.getEmail()).isEqualTo(request.getEmail());
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    public void deleteUserMngData() {
        // when
        userMngService.deleteUserMngData(request);

        // then
        verify(userMngRepository, times(1)).deleteById(request.getUserId());
    }

    @Test
    @DisplayName("권한 설정 적용 테스트")
    public void updateUserAuthMngData() {
        // given
        String userIdList = "123456";
        String authCdList = "AUTH_NORMAL";

        // mocking
        when(userMngRepository.findById(any())).thenReturn(Optional.of(userMng));

        // when
        userMngService.updateUserAuthMngData(userIdList, authCdList);

        // then
        assertThat(userMng.getUserId()).isEqualTo(userIdList);
        assertThat(userMng.getUserAuthMng().get(0).getAuthMng().getAuthCd()).isEqualTo(authCdList);
    }
}
