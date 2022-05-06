package com.jsplan.drp.global.obj.entity;

import com.jsplan.drp.global.obj.repository.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : CodeMngDataStatus
 * @Author : KDW
 * @Date : 2022-05-03
 * @Description : CRUD 상태 코드 관리 Enum (코드 관리)
 */
@Getter
@RequiredArgsConstructor
public enum CodeMngDataStatus implements EnumMapperType {
    SUCCESS_UPDATE("저장 되었습니다."),
    SUCCESS_DELETE("삭제 되었습니다."),
    BLANK("코드(명)을 입력하세요."),
    DUPLICATE("중복된 코드입니다."),
    INSERT_DUPLICATE("이미 등록된 코드입니다."),
    NOT_UPDATE("수정된 데이터가 없습니다."),
    CONSTRAINT("공통 코드가 있는 그룹 코드는 삭제할 수 없습니다."),
    ERROR("오류가 발생하였습니다.");

    private final String title; // Enum 설명

    @Override
    public String getCode() {
        return name();
    }
}
