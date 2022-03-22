package com.jsplan.drp.domain.sys.usermng.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngResponse;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.repository.UserGrpMngRepository;
import com.jsplan.drp.global.obj.entity.BaseEntity;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

        // mocking
        UserGrpMngRequest request = UserGrpMngRequest.builder().grpCd(grpCd).grpNm(grpNm)
            .grpDesc(grpDesc).build();
        UserGrpMng userGrpMng = UserGrpMng.builder().grpCd(grpCd).grpNm(grpNm)
            .grpDesc(grpDesc).build();
        ReflectionTestUtils.setField(userGrpMng, BaseEntity.class, "regUser", regUser, String.class);
        ReflectionTestUtils.setField(userGrpMng, BaseTimeEntity.class, "regDate", regDate, LocalDateTime.class);
        ReflectionTestUtils.setField(userGrpMng, BaseEntity.class, "modUser", modUser, String.class);
        ReflectionTestUtils.setField(userGrpMng, BaseTimeEntity.class, "modDate", modDate, LocalDateTime.class);

        given(userGrpMngRepository.save(any())).willReturn(userGrpMng);

        // when
        UserGrpMngResponse response = userGrpMngService.insertGrpMngData(request);

        // then
        assertThat(response.getGrpCd()).isEqualTo(userGrpMng.getGrpCd());
    }
}
