package com.jsplan.drp.global.exception.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jsplan.drp.global.exception.entity.vo.ErrorStatus;
import java.util.Map;
import lombok.Getter;

/**
 * @Class : ErrorResponse
 * @Author : KDW
 * @Date : 2022-03-28
 * @Description : 예외 응답 정보가 담긴 Class
 */
@Getter
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    private final String code; // 예외 코드
    private final String message; // 예외 메시지
    private Map<String, String> errors; // 오류 정보 (필드 + 메시지 형태)

    public ErrorResponse(ErrorStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public ErrorResponse(ErrorStatus status, Map<String, String> errors) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.errors = errors;
    }
}
