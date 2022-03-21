package com.jsplan.drp.global.config;

import com.jsplan.drp.global.obj.entity.UserVO;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Class : JpaAuditConfig
 * @Author : KDW
 * @Date : 2022-03-21
 * @Description : JPA Auditing 설정
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

    /**
     * <p>Auditor bean 설정</p>
     *
     * @return AuditorAware (AuditorAware 객체)
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    private static class AuditorAwareImpl implements AuditorAware<String> {

        /**
         * <p>등록자, 수정자 정보 조회</p>
         *
         * @return Optional (사용자 아이디)
         */
        @Override
        public Optional<String> getCurrentAuditor() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (null == auth || !auth.isAuthenticated()) {
                return Optional.empty();
            }
            String userId = ((UserVO) auth.getPrincipal()).getUserId();
            return Optional.of(userId);
        }
    }
}
