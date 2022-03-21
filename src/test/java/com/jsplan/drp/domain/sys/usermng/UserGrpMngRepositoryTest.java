package com.jsplan.drp.domain.sys.usermng;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto;
import com.jsplan.drp.domain.sys.usermng.repository.UserGrpMngRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

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
        Page<UserGrpMngDto.List> pageList = userGrpMngRepository.searchPageList(grpNm, pageRequest);

        // then
        assertThat(pageList.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("그룹 상세 조회 테스트")
    public void selectGrpMngDetail() throws Exception {
        // given
        String grpCd = "GRP_MNG";

        // when
        UserGrpMngDto.Detail userGrpMng = userGrpMngRepository.findByGrpCd(grpCd);

        // then
        assertThat(userGrpMng.getGrpNm()).isEqualTo("관리자 그룹");
        assertThat(userGrpMng.getRegUser()).isEqualTo("시스템 관리자");
        assertThat(userGrpMng.getRegDate()).isEqualTo("2022-01-25 11:47:06");
    }
}
