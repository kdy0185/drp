package com.jsplan.drp.global.obj;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Class : AbstractDAO
 * @Program : KDW
 * @Date : 2022-01-19
 * @Description : Abstract DAO Class
 */
public class AbstractDAO {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Autowired
    private SqlSessionTemplate batchSqlSession;

    protected void printQueryId(String queryId) {
        if (logger.isInfoEnabled()) {
            logger.info("\t QueryId  \t:  " + queryId);
        }
    }

    public Object insert(String queryId, Object params) {
        printQueryId(queryId);
        return sqlSession.insert(queryId, params);
    }

    public Object update(String queryId, Object params) {
        printQueryId(queryId);
        return sqlSession.update(queryId, params);
    }

    public Object delete(String queryId, Object params) {
        printQueryId(queryId);
        return sqlSession.delete(queryId, params);
    }

    public Object selectOne(String queryId) {
        printQueryId(queryId);
        return sqlSession.selectOne(queryId);
    }

    public Object selectOne(String queryId, Object params) {
        printQueryId(queryId);
        return sqlSession.selectOne(queryId, params);
    }

    @SuppressWarnings("rawtypes")
    public List selectList(String queryId) {
        printQueryId(queryId);
        return sqlSession.selectList(queryId);
    }

    @SuppressWarnings("rawtypes")
    public List selectList(String queryId, Object params) {
        printQueryId(queryId);
        return sqlSession.selectList(queryId, params);
    }

    public Object batchInsert(String queryId, Object params) {
        printQueryId(queryId);
        return batchSqlSession.insert(queryId, params);
    }
}
