package com.jsplan.drp.global.config;

import com.jsplan.drp.global.bean.EnumMapperFactory;
import com.jsplan.drp.global.obj.vo.DataStatus;
import com.jsplan.drp.global.obj.vo.UseStatus;
import java.util.LinkedHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Class : EnumMapperConfig
 * @Author : KDW
 * @Date : 2022-04-06
 * @Description : Enum 초기 설정
 */
@Configuration
public class EnumMapperConfig {

    /**
     * <p>Enum 관리 Factory 생성</p>
     *
     * @return EnumMapperFactory (Enum 관리 Factory)
     */
    @Bean
    public EnumMapperFactory createEnumMapperFactory() {
        EnumMapperFactory enumMapperFactory = new EnumMapperFactory(new LinkedHashMap<>());
        enumMapperFactory.put("DataStatus", DataStatus.class);
        enumMapperFactory.put("UseStatus", UseStatus.class);
        return enumMapperFactory;
    }
}
