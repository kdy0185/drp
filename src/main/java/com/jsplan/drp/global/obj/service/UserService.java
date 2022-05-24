package com.jsplan.drp.global.obj.service;

import com.jsplan.drp.global.bean.AvailableRoleHierarchy;
import com.jsplan.drp.global.obj.entity.UserVO;
import com.jsplan.drp.global.obj.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Class : UserService
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 사용자 계정 Service
 */
@Service("UserService")
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ComsService comsService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AvailableRoleHierarchy availableRoleHierarchy;

    /**
     * <p>사용자 정보</p>
     *
     * @param userId (사용자 아이디)
     * @return UserVO (사용자 정보)
     * @throws UsernameNotFoundException throws UsernameNotFoundException
     */
    @Override
    public UserVO loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 로그인 시도 횟수에 따른 접근 차단
        if (loginAttemptService.isBlocked(getClientIP())) {
            throw new RuntimeException("blocked");
        }

        // 입력한 ID로 사용자 정보 확인
        UserVO userVO = userRepository.searchUserDetail(userId);

        if (userVO != null) {
            userVO.setAuthorities(loadUserAuthorities(userId)); // 권한 정보 설정
        } else {
            logger.debug("Query returned no results for user '" + userId + "'");
            throw new UsernameNotFoundException("ID {0} not found");
        }

        return userVO;
    }

    /**
     * <p>사용자 권한 정보</p>
     *
     * @param userId (사용자 아이디)
     * @return List (사용자 권한 목록)
     */
    public List<GrantedAuthority> loadUserAuthorities(String userId) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<GrantedAuthority> hierarchyAuthorities = new ArrayList<>();

        List<String> userAuthList = userRepository.searchUserAuthList(userId);
        userAuthList.forEach(authCd -> authorities.add(new SimpleGrantedAuthority(authCd)));

        // 하위 계층 권한 포함 조회
        try {
            availableRoleHierarchy.setComsService(comsService);
            hierarchyAuthorities = availableRoleHierarchy.getReachableAuthorities(authorities);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hierarchyAuthorities;
    }

    /**
     * <p>사용자 IP 추출</p>
     *
     * @return String (사용자 IP)
     */
    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
