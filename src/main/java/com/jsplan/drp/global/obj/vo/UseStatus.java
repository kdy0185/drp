package com.jsplan.drp.global.obj.vo;

import com.jsplan.drp.global.obj.repository.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : UseStatus
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
