package com.jsplan.drp.global.obj.vo;

import com.jsplan.drp.global.obj.entity.UserVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Class : AuthVO
 * @Author : KDW
 * @Date : 2022-05-13
 * @Description : 권한 VO
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthVO {

    private final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    /* 사용자 */
    private String userId; // 사용자 아이디
    private String userNm; // 성명

    /* 권한 */
    private String authAdmin; // 슈퍼관리자 권한 여부

    protected AuthVO(String userId, String userNm) {
        this.userId = userId;
        this.userNm = userNm;
    }

    public void setUserInfo() {
        if (null != auth && auth.isAuthenticated()) {
            userId = ((UserVO) auth.getPrincipal()).getUserId();
            userNm = ((UserVO) auth.getPrincipal()).getUserNm();
        }
    }

    public String getAuthAdmin() {
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("AUTH_ADMIN"));
        return isAdmin ? "Y" : "N";
    }
}
