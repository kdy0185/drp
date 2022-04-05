package com.jsplan.drp.domain.sys.usermng.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequestBuilder;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDto.UserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDto.UserGrpMngListDto;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : UserGrpMngRepositoryTest
 * @Author : KDW
 * @Date : 2022-03-23
 * @Description : 그룹 관리 Repository Test
 */
@SpringBootTest
@Transactional
class UserGrpMngRepositoryTest {

    @Autowired
    UserGrpMngRepository userGrpMngRepository;

    @Test
    @DisplayName("그룹 목록 조회 테스트")
    public void selectGrpMngList() throws Exception {
        // given
        String grpNm = "사용자";
        PageRequest pageRequest = PageRequest.of(0, 20);

        // when
        Page<UserGrpMngListDto> pageList = userGrpMngRepository.searchPageList(grpNm, pageRequest);

        // then
        assertThat(pageList.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("그룹 상세 조회 테스트")
    public void selectGrpMngDetail() throws Exception {
        // given
        String grpCd = "GRP_MNG";

        // when
        UserGrpMngDetailDto userGrpMng = userGrpMngRepository.findByGrpCd(grpCd);

        // then
        assertThat(userGrpMng.getGrpNm()).isEqualTo("관리자 그룹");
        assertThat(userGrpMng.getRegUser()).isEqualTo("시스템 관리자");
        assertThat(userGrpMng.getRegDate()).isEqualTo("2022-01-25 11:47:06");
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 등록 테스트")
    public void insertGrpMngData() throws Exception {
        // given
        String grpCd = "GRP_TEST";
        String grpNm = "테스트 그룹";
        String grpDesc = "설명";

        // when
        UserGrpMngRequest request = UserGrpMngRequestBuilder.build(grpCd, grpNm, grpDesc);

        UserGrpMng savedUserGrpMng = userGrpMngRepository.save(request.toEntity());
        String findGrpCd = userGrpMngRepository.findByGrpCd(savedUserGrpMng.getGrpCd()).getGrpCd();

        // then
        assertThat(findGrpCd).isEqualTo(grpCd);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 수정 테스트")
    public void updateGrpMngData() throws Exception {
        // given
        String grpCd = "GRP_MNG";
        String grpNm = "관리자 그룹 수정";
        String grpDesc = "설명 수정";
        LocalDateTime beforeDate = LocalDateTime.now();

        // when
        UserGrpMngRequest request = UserGrpMngRequestBuilder.build(grpCd, grpNm, grpDesc);
        UserGrpMng userGrpMng = userGrpMngRepository.findById(request.getGrpCd()).orElseThrow(
            NoSuchElementException::new);
        userGrpMng.update(request);
        userGrpMngRepository.flush();

        // then
        assertThat(userGrpMng.getGrpNm()).isEqualTo(grpNm);
        assertThat(userGrpMng.getGrpDesc()).isEqualTo(grpDesc);
        assertThat(userGrpMng.getModDate()).isAfter(beforeDate);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "UserService", value = "sys_app")
    @DisplayName("그룹 삭제 테스트")
    public void deleteGrpMngData() throws Exception {
        // given
        String grpCd = "GRP_TEST";
        String grpNm = "테스트 그룹";
        String grpDesc = "설명";

        // when
        UserGrpMngRequest request = UserGrpMngRequestBuilder.build(grpCd, grpNm, grpDesc);
        userGrpMngRepository.saveAndFlush(request.toEntity());

        userGrpMngRepository.deleteById(grpCd);
        UserGrpMngDetailDto detailDto = userGrpMngRepository.findByGrpCd(grpCd);

        // then
        assertThat(detailDto).isNull();
    }
}
