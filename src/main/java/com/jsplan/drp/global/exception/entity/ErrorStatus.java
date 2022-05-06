package com.jsplan.drp.global.exception.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : ErrorStatus
 * @Author : KDW
 * @Date : 2022-03-28
 * @Description : 예외 정보가 담긴 enum Class
 */
@Getter
@RequiredArgsConstructor
public enum ErrorStatus {
    MESSAGE_NOT_READABLE("M400", "입력 형식이 올바르지 않습니다."),
    METHOD_NOT_VALID("V400", "요청 값이 올바르지 않습니다."),
    DUPLICATED_KEY("D409", "PK 값이 중복되었습니다."),
    ROOT_EXCEPTION("E500", "알 수 없는 오류가 발생하였습니다.");

    private final String code; // 예외 코드
    private final String message; // 예외 메시지
}
