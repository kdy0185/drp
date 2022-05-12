package com.jsplan.drp.domain.pl.report.vo;

import com.jsplan.drp.global.obj.repository.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : MainStatus
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 주요 일정 여부 관리 Enum
 */
@Getter
@RequiredArgsConstructor
public enum MainStatus implements EnumMapperType {
    Y("주요 일정"),
    N("일반 일정");

    private final String title; // Enum 설명

    @Override
    public String getCode() {
        return name();
    }
}
