package com.jsplan.drp.global.obj.entity;

import com.jsplan.drp.global.obj.vo.UseStatus;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * @Class : UserVO
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 사용자 계정 VO
 */
@Getter
@Setter
@NoArgsConstructor
public class UserVO implements UserDetails {

    private static final long serialVersionUID = -8753121922083733816L;

    /* 로그인 */
    private String grpCd; // 그룹 코드
    private String userId; // 아이디
    private String userPw; // 비밀번호
    private String saveIdCheck; // 아이디 저장
    private String autoLogin; // 자동 로그인
    private String loginUrl; // 로그인 후 이동 URL

    /* 사용자 정보 */
    private String userNm; // 사용자 이름
    private UseStatus useYn; // 사용 여부
    private String authCd; // 권한 코드
    private Set<GrantedAuthority> authorities; // 권한 목록

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    @Override
    public String getUsername() {
        return getUserId();
    }

    @Override
    public String getPassword() {
        return getUserPw();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return UseStatus.Y.equals(getUseYn());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        return (userId != null ? userId.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserVO)) {
            return false;
        }
        UserVO vo = (UserVO) obj;
        return (this.userId != null || vo.getUserId() == null)
            && (this.userId == null || this.userId.equals(vo.getUserId()));
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(
        Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
            new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority,
                "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

}
