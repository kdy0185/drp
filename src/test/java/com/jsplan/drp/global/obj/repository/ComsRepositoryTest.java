package com.jsplan.drp.global.obj.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.global.obj.dto.ComsDTO;
import com.jsplan.drp.global.obj.dto.ComsMenuDTO;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : ComsRepositoryTest
 * @Author : KDW
 * @Date : 2022-05-23
 * @Description : 공통 Repository Test
 */
@SpringBootTest
@Transactional
class ComsRepositoryTest {

    @Autowired
    ComsRepository comsRepository;

    @Test
    @DisplayName("메뉴 목록 조회 테스트")
    public void selectComsMenuList() {
        // when
        List<ComsMenuDTO> menuList = comsRepository.selectComsMenuList();

        // print
        System.out.println("\n================================================\n");
        for (ComsMenuDTO comsMenuDTO : menuList) {
            System.out.println(
                "urlAuth : [" + comsMenuDTO.getMenuCd() + "] / [" + comsMenuDTO.getMenuNm()
                    + "] / [" + comsMenuDTO.getUpperMenuCd() + "] / [" + comsMenuDTO.getMenuUrl()
                    + "] / [" + comsMenuDTO.getMenuLv() + "] / [" + comsMenuDTO.getLastYn()
                    + "] / [" + comsMenuDTO.getAuthCd() + "]");
        }
    }

    @Test
    @DisplayName("메뉴 상세 조회 테스트")
    public void selectComsMenuDetail() {
        // given
        String menuCd = "P0201";

        // when
        ComsMenuDTO comsMenuDTO = comsRepository.selectComsMenuDetail(menuCd);

        // then
        assertThat(comsMenuDTO.getMenuCd()).isEqualTo(menuCd);
    }

    @Test
    @DisplayName("URL별 권한 목록 조회 테스트")
    public void selectComsUrlAuthList() {
        // when
        List<ComsMenuDTO> urlAuthList = comsRepository.selectComsUrlAuthList();

        // print
        System.out.println("\n================================================\n");
        for (ComsMenuDTO comsMenuDTO : urlAuthList) {
            System.out.println(
                "urlAuth : [" + comsMenuDTO.getMenuUrl() + "] / [" + comsMenuDTO.getAuthCd() + "]");
        }
    }

    @Test
    @DisplayName("계층화 권한 목록 조회 테스트")
    public void selectHierarchicalAuthList() {
        // when
        List<ComsMenuDTO> hierarchicalAuthList = comsRepository.selectHierarchicalAuthList();

        // print
        System.out.println("\n================================================\n");
        for (ComsMenuDTO comsMenuDTO : hierarchicalAuthList) {
            System.out.println(
                "authCd : [" + comsMenuDTO.getAuthCd() + "] / [" + comsMenuDTO.getUpperAuthCd()
                    + "]");
        }
    }

    @Test
    @DisplayName("공통 코드 목록 조회 테스트")
    public void selectComsCodeList() {
        // given
        String grpCd = "CONC_RATE";

        // when
        List<ComsDTO> comsCodeList = comsRepository.selectComsCodeList(grpCd);

        // print
        System.out.println("\n================================================\n");
        for (ComsDTO comsDTO : comsCodeList) {
            System.out.println(
                "code : [" + comsDTO.getComCd() + "] / [" + comsDTO.getComNm() + "]");
        }
    }

    @Test
    @DisplayName("그룹 목록 조회 테스트")
    public void selectComsGrpList() {
        // when
        List<ComsDTO> grpList = comsRepository.selectComsGrpList();

        // print
        System.out.println("\n================================================\n");
        for (ComsDTO comsDTO : grpList) {
            System.out.println(
                "group : [" + comsDTO.getGrpCd() + "] / [" + comsDTO.getGrpNm() + "]");
        }
    }

    @Test
    @DisplayName("담당자 목록 조회 테스트")
    public void selectComsUserList() {
        // given
        String grpCd = "GRP_MNG";

        // when
        List<ComsDTO> userList = comsRepository.selectComsUserList(grpCd, "", "");

        // print
        System.out.println("\n================================================\n");
        for (ComsDTO comsDTO : userList) {
            System.out.println(
                "user : [" + comsDTO.getGrpCd() + "] / [" + comsDTO.getGrpNm() + "] / ["
                    + comsDTO.getUserId() + "] / [" + comsDTO.getUserNm() + "]");
        }
    }
}
