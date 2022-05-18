package com.jsplan.drp.global.config;

import com.jsplan.drp.domain.pl.report.vo.MainStatus;
import com.jsplan.drp.domain.sys.codemng.vo.CodeMngDataStatus;
import com.jsplan.drp.global.bean.EnumMapperFactory;
import com.jsplan.drp.global.obj.vo.DataStatus;
import com.jsplan.drp.global.obj.vo.DetailStatus;
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
        enumMapperFactory.put("DataStatus", DataStatus.class); // CRUD 상태 코드 관리
        enumMapperFactory.put("UseStatus", UseStatus.class); // 사용 여부 관리
        enumMapperFactory.put("DetailStatus", DetailStatus.class); // 등록/수정 구분
        enumMapperFactory.put("CodeMngDataStatus", CodeMngDataStatus.class); // CRUD 상태 코드 관리 (코드 관리)
        enumMapperFactory.put("MainStatus", MainStatus.class); // 주요 일정 여부 관리
        return enumMapperFactory;
    }
}
