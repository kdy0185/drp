package com.jsplan.drp.global.obj.entity;

import com.jsplan.drp.global.obj.repository.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : DetailStatus
 * @Author : KDW
 * @Date : 2022-05-09
 * @Description : 등록/수정 구분 Enum
 */
@Getter
@RequiredArgsConstructor
public enum DetailStatus implements EnumMapperType {
    INSERT("등록"),
    UPDATE("수정");

    private final String title; // Enum 설명

    @Override
    public String getCode() {
        return name();
    }
}
