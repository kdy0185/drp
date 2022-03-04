package com.jsplan.drp.domain.sys.usermng;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void selectGrpMngList() throws Exception {
        // given
        String grpNm = "그룹";
        PageRequest pageRequest = PageRequest.of(0, 2);

        // when
        Page<UserGrpMng> pageList = userGrpMngRepository.searchPageList(grpNm, pageRequest);

        // then
        assertThat(pageList.getSize()).isEqualTo(2);
    }

    @Test
    public void selectGrpMngDetail() throws Exception {
        // given
        String grpCd = "GRP_MNG";

        // when
        UserGrpMng userGrpMng = userGrpMngRepository.findByGrpCd(grpCd);

        // then
        assertThat(userGrpMng).isNotNull();
    }
}
