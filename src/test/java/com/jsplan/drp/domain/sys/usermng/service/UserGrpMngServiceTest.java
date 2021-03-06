package com.jsplan.drp.domain.sys.usermng.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequestBuilder;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngSearchDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngSearchDTOBuilder;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngListDTO;
import com.jsplan.drp.domain.sys.usermng.repository.UserGrpMngRepository;
import com.jsplan.drp.global.obj.entity.BaseEntity;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @Class : UserGrpMngServiceTest
 * @Author : KDW
 * @Date : 2022-03-22
 * @Description : 그룹 관리 Service Test
 */
@ExtendWith(MockitoExtension.class)
class UserGrpMngServiceTest {

    @InjectMocks
    UserGrpMngService userGrpMngService;

    @Mock
    UserGrpMngRepository userGrpMngRepository;

    UserGrpMngRequest request;
    UserGrpMng userGrpMng;
    UserGrpMngSearchDTO searchDTO;
    UserGrpMngListDTO listDTO;
    UserGrpMngDetailDTO detailDTO;

    String grpCd, grpNm, grpDesc, regUser, modUser;
    LocalDateTime regDate, modDate;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        grpCd = "GRP_TEST";
        grpNm = "테스트 그룹";
        grpDesc = "설명";
        regUser = "sys_app";
        regDate = LocalDateTime.now();
        modUser = "sys_app";
        modDate = LocalDateTime.now();

        request = UserGrpMngRequestBuilder.build(grpCd, grpNm, grpDesc);

        userGrpMng = UserGrpMng.builder().grpCd(grpCd).grpNm(grpNm).grpDesc(grpDesc).build();
        ReflectionTestUtils.setField(userGrpMng, BaseEntity.class, "regUser", regUser,
            String.class);
        ReflectionTestUtils.setField(userGrpMng, BaseTimeEntity.class, "regDate", regDate,
            LocalDateTime.class);
        ReflectionTestUtils.setField(userGrpMng, BaseEntity.class, "modUser", modUser,
            String.class);
        ReflectionTestUtils.setField(userGrpMng, BaseTimeEntity.class, "modDate", modDate,
            LocalDateTime.class);

        searchDTO = UserGrpMngSearchDTOBuilder.build(0, 20, null, grpNm);

        listDTO = new UserGrpMngListDTO(grpCd, grpNm, grpDesc, regUser, regDate, modUser, modDate);
        detailDTO = new UserGrpMngDetailDTO(grpCd, grpNm, grpDesc, regUser, regDate, modUser,
            modDate);
    }

    @Test
    @DisplayName("그룹 목록 조회 테스트")
    public void selectUserGrpMngList() {
        // given
        List<UserGrpMngListDTO> list = new ArrayList<>();
        list.add(listDTO);
        Page<UserGrpMngListDTO> pageList = new PageImpl<>(list);

        // mocking
        given(userGrpMngRepository.searchUserGrpMngList(anyString(), any())).willReturn(pageList);

        // when
        Page<UserGrpMngListDTO> userGrpMngList = userGrpMngService.selectUserGrpMngList(searchDTO);

        // then
        assertThat(userGrpMngList.getNumberOfElements()).isEqualTo(1);
        assertThat(userGrpMngList.stream().findFirst().orElseThrow(NoSuchElementException::new)
            .getGrpNm()).contains(grpNm);
    }

    @Test
    @DisplayName("그룹 상세 조회 테스트")
    public void selectUserGrpMngDetail() {
        // mocking
        given(userGrpMngRepository.findUserGrpMngByGrpCd(anyString())).willReturn(detailDTO);

        // when
        UserGrpMngDetailDTO findDetail = userGrpMngService.selectUserGrpMngDetail(request);

        // then
        assertThat(findDetail.getGrpCd()).isEqualTo(grpCd);
        assertThat(findDetail.getRegUser()).isEqualTo(regUser);
        assertThat(LocalDateTime.parse(findDetail.getRegDate(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("그룹 등록 테스트")
    public void insertUserGrpMngData() {
        // mocking
        given(userGrpMngRepository.save(any())).willReturn(userGrpMng);
        given(userGrpMngRepository.findUserGrpMngByGrpCd(anyString())).willReturn(detailDTO);

        // when
        userGrpMngService.insertUserGrpMngData(request);
        String findGrpCd = userGrpMngService.selectUserGrpMngDetail(request).getGrpCd();

        // then
        assertThat(findGrpCd).isEqualTo(grpCd);
    }

    @Test
    @DisplayName("그룹 수정 테스트")
    public void updateUserGrpMngData() {
        // given
        request = UserGrpMngRequestBuilder.build("GRP_TEST", "그룹 수정", "설명 수정");

        // mocking
        when(userGrpMngRepository.findById(any())).thenReturn(Optional.of(userGrpMng));

        // when
        userGrpMngService.updateUserGrpMngData(request);

        // then
        assertThat(userGrpMng.getGrpNm()).isEqualTo(request.getGrpNm());
        assertThat(userGrpMng.getGrpDesc()).isEqualTo(request.getGrpDesc());
    }

    @Test
    @DisplayName("그룹 삭제 테스트")
    public void deleteUserGrpMngData() {
        // when
        userGrpMngService.deleteUserGrpMngData(request);

        // then
        verify(userGrpMngRepository, times(1)).deleteById(request.getGrpCd());
    }
}
