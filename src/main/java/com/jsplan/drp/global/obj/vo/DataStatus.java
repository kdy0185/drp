package com.jsplan.drp.global.obj.vo;

import com.jsplan.drp.global.obj.repository.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : DataStatus
 * @Author : KDW
 * @Date : 2022-04-06
 * @Description : CRUD 상태 코드 관리 Enum
 */
@Getter
@RequiredArgsConstructor
public enum DataStatus implements EnumMapperType {
    SUCCESS("성공"),
    DUPLICATE("중복"),
    CONSTRAINT("제약 조건 위배"),
    ERROR("기타 오류");

    private final String title; // Enum 설명

    @Override
    public String getCode() {
        return name();
    }

    /**
     * @Class : DataStatus
     * @Author : KDW
     * @Date : 2022-04-06
     * @Description : 사용 여부 관리 Enum
     */
    @Getter
    @RequiredArgsConstructor
    public enum UseStatus implements EnumMapperType {
        Y("사용"),
        N("미사용");

        private final String title; // Enum 설명

        @Override
        public String getCode() {
            return name();
        }
    }
}
