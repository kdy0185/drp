package com.jsplan.drp.global.obj.entity;

import com.jsplan.drp.global.obj.repository.EnumMapperType;
import lombok.Getter;

/**
 * @Class : EnumMapperVO
 * @Author : KDW
 * @Date : 2022-04-06
 * @Description : Enum Mapper VO
 */
@Getter
public class EnumMapperVO {

    private final String code; // Enum 상숫값
    private final String title; // Enum 설명

    public EnumMapperVO(EnumMapperType enumMapperType) {
        code = enumMapperType.getCode();
        title = enumMapperType.getTitle();
    }
}
