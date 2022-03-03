package com.jsplan.drp.cmmn.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @Class : DatabaseConfig
 * @Author : KDW
 * @Date : 2022-01-19
 * @Description : DB 환경 설정
 */
@Configuration
@MapperScan(value = "com.jsplan.drp")
public class DatabaseConfig {

    @Value("${project.properties.package}")
    private String rootPackage;

    /**
     * <p>SqlSessionFactory 설정</p>
     *
     * @param dataSource (DataSource 객체)
     * @return SqlSessionFactory (SqlSessionFactory 객체)
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(
            resolver.getResources("classpath:/mapper/**/*Mapper.xml"));
        sessionFactory.setTypeAliasesPackage(rootPackage);
        return sessionFactory.getObject();
    }

    /**
     * <p>SqlSessionTemplate 설정</p>
     *
     * @param sqlSessionFactory (SqlSessionFactory 객체)
     * @return SqlSessionTemplate (SqlSessionTemplate 객체)
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory)
        throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
