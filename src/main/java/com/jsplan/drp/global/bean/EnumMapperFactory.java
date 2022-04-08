package com.jsplan.drp.global.bean;

import com.jsplan.drp.global.obj.entity.EnumMapperVO;
import com.jsplan.drp.global.obj.repository.EnumMapperType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Class : EnumMapperFactory
 * @Author : KDW
 * @Date : 2022-04-06
 * @Description : Enum 생성 및 관리
 */
@Getter
@AllArgsConstructor
public class EnumMapperFactory {

    private Map<String, List<EnumMapperVO>> factory;

    /**
     * <p>새 Enum 추가</p>
     *
     * @param key (Enum 명)
     * @param e   (Enum Class)
     */
    public void put(String key, Class<? extends EnumMapperType> e) {
        factory.put(key, toEnumValues(e));
    }

    /**
     * <p>특정 Enum 항목 조회</p>
     *
     * @param key (Enum 명)
     * @return List (Enum 항목 목록)
     */
    public List<EnumMapperVO> get(String key) {
        return factory.get(key);
    }

    /**
     * <p>Enum → List 변환</p>
     *
     * @param e (Enum Class)
     * @return List (Enum 항목 목록)
     */
    private List<EnumMapperVO> toEnumValues(Class<? extends EnumMapperType> e) {
        return Arrays.stream(e.getEnumConstants()).map(EnumMapperVO::new)
            .collect(Collectors.toList());
    }
}
