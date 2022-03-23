package com.jsplan.drp.domain.sys.usermng.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequestBuilder;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngResponse;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto.UserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto.UserGrpMngListDto;
import com.jsplan.drp.domain.sys.usermng.repository.UserGrpMngRepository;
import com.jsplan.drp.global.obj.entity.BaseEntity;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    @Test
    @DisplayName("그룹 목록 조회 테스트")
    public void selectGrpMngList() throws Exception {
        // given
        String grpNm = "사용자";
        PageRequest pageRequest = PageRequest.of(0, 20);

        List<UserGrpMngListDto> list = new ArrayList<>();
        list.add(new UserGrpMngListDto("GRP_USER", "사용자 그룹", "설명", "시스템 관리자",
            LocalDateTime.now(), "시스템 관리자", LocalDateTime.now()));
        Page<UserGrpMngListDto> pageList = new PageImpl<>(list);

        // mocking
        given(userGrpMngRepository.searchPageList(grpNm, pageRequest)).willReturn(pageList);

        // when
        Page<UserGrpMngListDto> resultList = userGrpMngService.selectGrpMngList(grpNm,
            pageRequest);

        // then
        assertThat(resultList.getNumberOfElements()).isEqualTo(1);
        assertThat(resultList.stream().findFirst().get().getGrpNm()).contains(grpNm);
    }

    @Test
    @DisplayName("그룹 상세 조회 테스트")
    public void selectGrpMngDetail() throws Exception {
        // given
        String grpCd = "GRP_TEST";
        String regUser = "시스템 관리자";
        UserGrpMngDetailDto detailDto = new UserGrpMngDetailDto(grpCd, "테스트 그룹", "설명",
            regUser, LocalDateTime.now(), regUser, LocalDateTime.now());

        // mocking
        given(userGrpMngRepository.findByGrpCd(any())).willReturn(detailDto);

        // when
        UserGrpMngDetailDto findDetail = userGrpMngService.selectGrpMngDetail(grpCd);

        // then
        assertThat(findDetail.getGrpCd()).isEqualTo(grpCd);
        assertThat(findDetail.getRegUser()).isEqualTo(regUser);
        assertThat(LocalDateTime.parse(findDetail.getRegDate(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("그룹 등록 테스트")
    public void insertGrpMngData() throws Exception {
        // given
        String grpCd = "GRP_TEST";
        String grpNm = "테스트 그룹";
        String grpDesc = "설명";
        String regUser = "sys_app";
        LocalDateTime regDate = LocalDateTime.now();
        String modUser = "sys_app";
        LocalDateTime modDate = LocalDateTime.now();

        UserGrpMngRequest request = UserGrpMngRequestBuilder.build(grpCd, grpNm, grpDesc);

        UserGrpMng userGrpMng = UserGrpMng.builder().grpCd(grpCd).grpNm(grpNm)
            .grpDesc(grpDesc).build();
        ReflectionTestUtils.setField(userGrpMng, BaseEntity.class, "regUser", regUser,
            String.class);
        ReflectionTestUtils.setField(userGrpMng, BaseTimeEntity.class, "regDate", regDate,
            LocalDateTime.class);
        ReflectionTestUtils.setField(userGrpMng, BaseEntity.class, "modUser", modUser,
            String.class);
        ReflectionTestUtils.setField(userGrpMng, BaseTimeEntity.class, "modDate", modDate,
            LocalDateTime.class);

        UserGrpMngDetailDto detailDto = new UserGrpMngDetailDto(grpCd, grpNm, grpDesc, regUser,
            regDate, modUser, modDate);

        // mocking
        given(userGrpMngRepository.save(any())).willReturn(userGrpMng);
        given(userGrpMngRepository.findByGrpCd(any())).willReturn(detailDto);

        // when
        UserGrpMngResponse response = userGrpMngService.insertGrpMngData(request);
        String findGrpCd = userGrpMngService.selectGrpMngDetail(response.getGrpCd()).getGrpCd();

        // then
        assertThat(findGrpCd).isEqualTo(grpCd);
    }

    @Test
    @DisplayName("그룹 수정 테스트")
    public void updateGrpMngData() throws Exception {
        // given
        String grpCd = "GRP_TEST";
        String grpNm = "테스트 그룹";
        String grpDesc = "설명";
        String regUser = "sys_app";
        LocalDateTime regDate = LocalDateTime.now();
        String modUser = "sys_app";
        LocalDateTime modDate = LocalDateTime.now();

        UserGrpMngRequest request = UserGrpMngRequestBuilder.build("GRP_TEST", "그룹 수정", "설명 수정");

        UserGrpMng userGrpMng = UserGrpMng.builder().grpCd(grpCd).grpNm(grpNm)
            .grpDesc(grpDesc).build();
        ReflectionTestUtils.setField(userGrpMng, BaseEntity.class, "regUser", regUser,
            String.class);
        ReflectionTestUtils.setField(userGrpMng, BaseTimeEntity.class, "regDate", regDate,
            LocalDateTime.class);
        ReflectionTestUtils.setField(userGrpMng, BaseEntity.class, "modUser", modUser,
            String.class);
        ReflectionTestUtils.setField(userGrpMng, BaseTimeEntity.class, "modDate", modDate,
            LocalDateTime.class);

        // mocking
        when(userGrpMngRepository.findById(any())).thenReturn(Optional.of(userGrpMng));

        // when
        userGrpMngService.updateGrpMngData(request);

        // then
        assertThat(userGrpMng.getGrpNm()).isEqualTo(request.getGrpNm());
        assertThat(userGrpMng.getGrpDesc()).isEqualTo(request.getGrpDesc());
    }
}
