package com.jsplan.drp.cmmn.config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

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

    @Value("${spring.jpa.database-platform}")
    private String dialect;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    /**
     * <p>EntityManagerFactoryBean 설정</p>
     *
     * @param dataSource (DataSource 객체)
     * @return LocalContainerEntityManagerFactoryBean (LocalContainerEntityManagerFactoryBean 객체)
     */
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceUnitName("entityManagerFactory");
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(rootPackage); // Entity 클래스 스캔

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        entityManagerFactoryBean.setJpaPropertyMap(properties);

        return entityManagerFactoryBean;
    }

    /**
     * <p>JPA TransactionManager 설정</p>
     *
     * @param entityManagerFactory (LocalContainerEntityManagerFactoryBean 객체)
     * @return PlatformTransactionManager (PlatformTransactionManager 객체)
     */
    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(
        LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

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
