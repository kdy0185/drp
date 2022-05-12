package com.jsplan.drp.domain.pl.ctgopt.dto;

import com.jsplan.drp.global.obj.entity.UserVO;
import com.jsplan.drp.global.obj.vo.UseStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Class : PlanCtgOptSearchDTO
 * @Author : KDW
 * @Date : 2022-05-12
 * @Description : 분류 옵션 설정 Search DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PlanCtgOptSearchDTO {

    private String rtneCtgCd; // 루틴 분류 코드
    private String userId; // 사용자 아이디
    private String userNm; // 성명
    private UseStatus useYn; // 사용 여부

    // 슈퍼관리자가 아닌 경우 로그인 사용자 정보 고정
    public void setUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!isAuthAdmin(auth)) {
            this.userId = ((UserVO) auth.getPrincipal()).getUserId();
            this.userNm = ((UserVO) auth.getPrincipal()).getUserNm();
        }
    }

    // 슈퍼관리자 권한 여부
    public boolean isAuthAdmin(Authentication auth) {
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("AUTH_ADMIN"));
    }
}
