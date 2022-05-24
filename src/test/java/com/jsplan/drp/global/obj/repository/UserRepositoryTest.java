package com.jsplan.drp.global.obj.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.jsplan.drp.global.obj.entity.UserVO;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : UserRepositoryTest
 * @Author : KDW
 * @Date : 2022-05-19
 * @Description : 사용자 계정 Repository Test
 */
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    String userId;

    @BeforeEach
    public void setUp() {
        setTestModel();
    }

    private void setTestModel() {
        userId = "kdy0185";
    }

    @Test
    @DisplayName("사용자 정보 조회 테스트")
    public void searchUserDetail() {
        // when
        UserVO userVO = userRepository.searchUserDetail(userId);

        // then
        assertThat(userVO.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("사용자 권한 정보 조회 테스트")
    public void searchUserAuthList() {
        // when
        List<String> userAuthList = userRepository.searchUserAuthList(userId);

        // then
        assertThat(userAuthList.size()).isEqualTo(2);
        assertThat(userAuthList.stream().findFirst()
            .orElseThrow(NoSuchElementException::new)).isEqualTo("IS_AUTHENTICATED_FULLY");

        // print
        System.out.println("\n================================================\n");
        for (String authCd : userAuthList) {
            System.out.println("authCd : " + authCd);
        }
    }
}
