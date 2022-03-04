package com.jsplan.drp.global.obj;

import com.jsplan.drp.global.bean.AvailableRoleHierarchy;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Resource(name = "ComsService")
    private ComsService comsService;

    @Resource(name = "LoginAttemptService")
    private LoginAttemptService loginAttemptService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    private AvailableRoleHierarchy availableRoleHierarchy;

    /**
     * <p>사용자 정보</p>
     *
     * @param userId
     * @return UserVO
     * @throws UsernameNotFoundException throws UsernameNotFoundException
     */
    @Override
    public UserVO loadUserByUsername(String userId)
        throws UsernameNotFoundException {
        // 로그인 시도 횟수에 따른 접근 차단
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        // 입력한 ID로 사용자 정보 확인
        UserVO userVO = userMapper.selectUserDetail(userId);

        if (userVO != null) {
            userVO.setAuthorities(loadUserAuthorities(userId)); // 권한 정보 설정
        } else {
            logger.debug("Query returned no results for user '" + userId + "'");
            UsernameNotFoundException ue = new UsernameNotFoundException("ID {0} not found");
            throw ue;
        }

        return userVO;
    }

    /**
     * <p>사용자 권한 정보</p>
     *
     * @param userId
     * @return List<GrantedAuthority>
     */
    public List<GrantedAuthority> loadUserAuthorities(String userId) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        List<GrantedAuthority> hierarchyAuthorities = new ArrayList<GrantedAuthority>();

        List<UserVO> userAuthList = userMapper.selectUserAuthList(userId);
        for (UserVO userAuthVO : userAuthList) {
            authorities.add(new SimpleGrantedAuthority(userAuthVO.getAuthCd()));
        }

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
