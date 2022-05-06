package com.jsplan.drp.domain.sys.codemng.repository;

import static com.jsplan.drp.domain.sys.codemng.entity.QCodeGrpMng.codeGrpMng;
import static com.jsplan.drp.domain.sys.codemng.entity.QCodeMng.codeMng;

import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.QCodeGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.QCodeGrpMngListDTO;
import com.jsplan.drp.domain.sys.codemng.entity.CodeGrpMng;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.jsplan.drp.global.util.StringUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : CodeGrpMngCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-05-02
 * @Description : 그룹 코드 관리 Custom Repository Impl
 */
public class CodeGrpMngCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    CodeGrpMngCustomRepository {

    /**
     * <p>그룹 코드 관리 Custom Repository Impl 생성자</p>
     */
    public CodeGrpMngCustomRepositoryImpl() {
        super(CodeGrpMng.class);
    }

    /**
     * <p>그룹 코드 목록</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param pageable   (페이징 정보)
     * @return Page (그룹 코드 목록)
     */
    @Override
    public Page<CodeGrpMngListDTO> searchCodeGrpMngList(String searchCd, String searchWord,
        Pageable pageable) {
        return applyPagination(pageable,
            contentQuery -> getCodeGrpMngListQuery(searchCd, searchWord, contentQuery),
            countQuery -> getCodeGrpMngCountQuery(null, searchCd, searchWord, countQuery)
        );
    }

    /**
     * <p>그룹 코드 목록 쿼리</p>
     *
     * @param searchCd     (검색 조건)
     * @param searchWord   (검색어)
     * @param contentQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<CodeGrpMngListDTO> getCodeGrpMngListQuery(String searchCd, String searchWord,
        JPAQueryFactory contentQuery) {
        return contentQuery.select(new QCodeGrpMngListDTO(
                codeGrpMng.grpCd,
                codeGrpMng.grpNm,
                codeGrpMng.useYn,
                codeGrpMng.detl,
                Expressions.simpleTemplate(String.class, "getUserNm({0})", codeGrpMng.regUser),
                codeGrpMng.regDate,
                Expressions.simpleTemplate(String.class, "getUserNm({0})", codeGrpMng.modUser),
                codeGrpMng.modDate))
            .from(codeGrpMng)
            .where(codeGrpLike(searchCd, searchWord))
            .orderBy(codeGrpMng.grpNm.asc());
    }

    /**
     * <p>그룹 코드 목록 카운트 쿼리</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param countQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<Long> getCodeGrpMngCountQuery(String grpCd, String searchCd, String searchWord,
        JPAQueryFactory countQuery) {
        return countQuery.select(codeGrpMng.count())
            .from(codeGrpMng)
            .where(grpCdEq(grpCd), codeGrpLike(searchCd, searchWord));
    }

    /**
     * <p>그룹 코드 목록 조회 조건 : 그룹 코드</p>
     *
     * @param grpCd (그룹 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression grpCdEq(String grpCd) {
        return !StringUtil.isEmpty(grpCd) ? codeGrpMng.grpCd.eq(grpCd) : null;
    }

    /**
     * <p>그룹 코드 목록 조회 조건 : 그룹 코드 정보</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression codeGrpLike(String searchCd, String searchWord) {
        if (!StringUtil.isEmpty(searchWord)) {
            if ("grpCd".equals(searchCd)) {
                return codeGrpMng.grpCd.contains(searchWord);
            }
            if ("grpNm".equals(searchCd)) {
                return codeGrpMng.grpNm.contains(searchWord);
            }
        }
        return null;
    }

    /**
     * <p>그룹 코드 상세</p>
     *
     * @param grpCd (그룹 코드)
     * @return CodeGrpMngDetailDTO (그룹 코드 DTO)
     */
    @Override
    public CodeGrpMngDetailDTO findCodeGrpMngByGrpCd(String grpCd) {
        return select(new QCodeGrpMngDetailDTO(
            codeGrpMng.grpCd,
            codeGrpMng.grpNm,
            codeGrpMng.useYn,
            codeGrpMng.detl,
            Expressions.simpleTemplate(String.class, "getUserNm({0})", codeGrpMng.regUser),
            codeGrpMng.regDate,
            Expressions.simpleTemplate(String.class, "getUserNm({0})", codeGrpMng.modUser),
            codeGrpMng.modDate))
            .from(codeGrpMng)
            .where(grpCdEq(grpCd))
            .fetchOne();
    }

    /**
     * <p>그룹 코드 확인</p>
     *
     * @param grpCd (그룹 코드)
     * @return boolean (그룹 코드 존재 여부)
     */
    @Override
    public boolean existsCodeGrpMngByGrpCd(String grpCd) {
        Long codeGrpMngCnt = getCodeGrpMngCountQuery(grpCd, null, null,
            getQueryFactory()).fetchOne();
        return codeGrpMngCnt != null && codeGrpMngCnt > 0;
    }

    /**
     * <p>공통 코드 확인</p>
     *
     * @param grpCd (그룹 코드)
     * @return boolean (공통 코드 존재 여부)
     */
    @Override
    public boolean existsCodeMngByGrpCd(String grpCd) {
        Long codeMngCnt = select(codeMng.count())
            .from(codeMng)
            .where(codeMng.codeMngId.grpCd.eq(grpCd))
            .fetchOne();
        return codeMngCnt != null && codeMngCnt > 0;
    }

    /**
     * <p>그룹 코드 엑셀 목록</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return List (그룹 코드 목록)
     */
    @Override
    public List<CodeGrpMngListDTO> searchCodeGrpMngExcelList(String searchCd, String searchWord) {
        List<CodeGrpMngListDTO> excelList = getCodeGrpMngListQuery(searchCd, searchWord,
            getQueryFactory()).fetch();
        return addRowNum(excelList, 1, excelList.size());
    }
}
