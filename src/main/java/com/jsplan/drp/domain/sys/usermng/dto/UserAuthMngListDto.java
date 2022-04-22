package com.jsplan.drp.domain.sys.usermng.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Class : UserAuthMngListDto
 * @Author : KDW
 * @Date : 2022-04-21
 * @Description : 사용자 권한 목록 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"authCd", "authNm", "authYn", "lastYn"})
public class UserAuthMngListDto {

    private String authCd; // 권한 코드
    private String authNm; // 권한명
    private String authYn; // 권한 여부
    private String lastYn; // 최하위 자식 노드 여부
}
