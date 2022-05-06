package com.jsplan.drp.global.obj.repository;

import com.jsplan.drp.global.obj.entity.BaseListDTO;
import com.jsplan.drp.global.util.RowNumUtil;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.util.List;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * @Class : Querydsl5RepositorySupport
 * @Author : KDW
 * @Date : 2022-03-03
 * @Description : Querydsl 5.x 지원 라이브러리
 */
@Repository
public abstract class Querydsl5RepositorySupport {

    private final Class<?> domainClass;
    private Querydsl querydsl;
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    /**
     * <p>도메인별 QuerydslRepositorySupport 인스턴스 생성</p>
     *
     * @param domainClass (Class 객체)
     */
    public Querydsl5RepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        this.domainClass = domainClass;
    }

    /**
     * <p>EntityManager Injection 을 위한 Setter</p>
     *
     * @param entityManager (EntityManager 객체)
     */
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        JpaEntityInformation<?, ?> entityInformation =
            JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<?> path = resolver.createPath(entityInformation.getJavaType());
        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, new
            PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * <p>Configuration 이후 유효성 검사</p>
     */
    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }

    /**
     * <p>QueryFactory Getter</p>
     *
     * @return JPAQueryFactory (JPAQueryFactory 객체)
     */
    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

    /**
     * <p>Querydsl Getter</p>
     *
     * @return Querydsl (Querydsl 객체)
     */
    protected Querydsl getQuerydsl() {
        return querydsl;
    }

    /**
     * <p>EntityManager Getter</p>
     *
     * @return EntityManager (EntityManager 객체)
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * <p>Select JPAQuery 생성</p>
     *
     * @param expr (Expression 객체)
     * @return JPAQuery (JPAQuery 객체)
     */
    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getQueryFactory().select(expr);
    }

    /**
     * <p>Select From JPAQuery 생성</p>
     *
     * @param from (EntityPath 객체)
     * @return JPAQuery (JPAQuery 객체)
     */
    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return getQueryFactory().selectFrom(from);
    }

    /**
     * <p>Update JPAQuery 생성</p>
     *
     * @param path (EntityPath 객체)
     * @return JPAUpdateClause (JPAUpdateClause 객체)
     */
    protected JPAUpdateClause update(EntityPath<?> path) {
        return getQueryFactory().update(path);
    }

    /**
     * <p>목록 쿼리 + 카운트 쿼리 + Paging + RowNum 적용</p>
     *
     * @param pageable     (Pageable 객체)
     * @param contentQuery (Function 객체)
     * @param countQuery   (Function 객체)
     * @return Page (페이징 목록)
     */
    protected <T extends BaseListDTO> Page<T> applyPagination(Pageable pageable,
        Function<JPAQueryFactory, JPAQuery<T>> contentQuery,
        Function<JPAQueryFactory, JPAQuery<Long>> countQuery) {
        JPAQuery<T> jpaContentQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable, jpaContentQuery).fetch();
        content = addRowNum(content, pageable.getPageNumber() + 1, pageable.getPageSize());

        JPAQuery<Long> countResult = countQuery.apply(getQueryFactory());

        return PageableExecutionUtils.getPage(content, pageable, countResult::fetchOne);
    }

    /**
     * <p>RowNum 추가</p>
     *
     * @param content  (조회한 목록)
     * @param pageNo   (현재 페이지 번호)
     * @param pageSize (페이지당 데이터 출력 수)
     * @return List (RowNum 추가 목록)
     */
    protected <T extends BaseListDTO> List<T> addRowNum(List<T> content, long pageNo,
        int pageSize) {
        RowNumUtil rowNumUtil = new RowNumUtil(content.size(), pageNo, pageSize);
        content.forEach(v -> v.setRn(rowNumUtil.getRn()));
        return content;
    }
}
