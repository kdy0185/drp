package com.jsplan.drp.domain.sys.codemng.repository;

import static com.jsplan.drp.domain.sys.codemng.entity.QCodeGrpMng.codeGrpMng;
import static com.jsplan.drp.domain.sys.codemng.entity.QCodeMng.codeMng;

import com.jsplan.drp.domain.sys.codemng.dto.CodeMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.QCodeMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.QCodeMngListDTO;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMng;
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
 * @Class : CodeMngCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 공통 코드 관리 Custom Repository Impl
 */
public class CodeMngCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    CodeMngCustomRepository {

    /**
     * <p>공통 코드 관리 Custom Repository Impl 생성자</p>
     */
    public CodeMngCustomRepositoryImpl() {
        super(CodeMng.class);
    }

    /**
     * <p>공통 코드 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param pageable   (페이징 정보)
     * @return Page (공통 코드 목록)
     */
    @Override
    public Page<CodeMngListDTO> searchCodeMngList(String grpCd, String searchCd, String searchWord,
        Pageable pageable) {
        return applyPagination(pageable,
            contentQuery -> getCodeMngListQuery(grpCd, searchCd, searchWord, contentQuery),
            countQuery -> getCodeMngCountQuery(grpCd, null, searchCd, searchWord, countQuery)
        );
    }

    /**
     * <p>공통 코드 목록 쿼리</p>
     *
     * @param grpCd        (그룹 코드)
     * @param searchCd     (검색 조건)
     * @param searchWord   (검색어)
     * @param contentQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<CodeMngListDTO> getCodeMngListQuery(String grpCd, String searchCd,
        String searchWord, JPAQueryFactory contentQuery) {
        return contentQuery.select(new QCodeMngListDTO(
                codeMng.codeGrpMng.grpCd,
                codeMng.codeGrpMng.grpNm,
                codeMng.codeMngId.comCd,
                codeMng.comNm,
                codeMng.useYn,
                codeMng.detl,
                codeMng.ord,
                Expressions.simpleTemplate(String.class, "getUserNm({0})", codeMng.regUser),
                codeMng.regDate,
                Expressions.simpleTemplate(String.class, "getUserNm({0})", codeMng.modUser),
                codeMng.modDate))
            .from(codeMng)
            .join(codeMng.codeGrpMng, codeGrpMng)
            .where(grpCdEq(grpCd), codeLike(searchCd, searchWord))
            .orderBy(codeMng.ord.asc());
    }

    /**
     * <p>공통 코드 목록 카운트 쿼리</p>
     *
     * @param grpCd      (그룹 코드)
     * @param comCd      (공통 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param countQuery (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<Long> getCodeMngCountQuery(String grpCd, String comCd, String searchCd,
        String searchWord, JPAQueryFactory countQuery) {
        return countQuery.select(codeMng.count())
            .from(codeMng)
            .join(codeMng.codeGrpMng, codeGrpMng)
            .where(grpCdEq(grpCd), comCdEq(comCd), codeLike(searchCd, searchWord));
    }

    /**
     * <p>공통 코드 목록 조회 조건 : 그룹 코드</p>
     *
     * @param grpCd (그룹 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression grpCdEq(String grpCd) {
        return !StringUtil.isEmpty(grpCd) ? codeGrpMng.grpCd.eq(grpCd) : null;
    }

    /**
     * <p>공통 코드 목록 조회 조건 : 공통 코드</p>
     *
     * @param comCd (공통 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression comCdEq(String comCd) {
        return !StringUtil.isEmpty(comCd) ? codeMng.codeMngId.comCd.eq(comCd) : null;
    }

    /**
     * <p>공통 코드 목록 조회 조건 : 공통 코드 정보</p>
     *
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression codeLike(String searchCd, String searchWord) {
        if (!StringUtil.isEmpty(searchWord)) {
            if ("grpCd".equals(searchCd)) {
                return codeGrpMng.grpCd.contains(searchWord);
            }
            if ("grpNm".equals(searchCd)) {
                return codeGrpMng.grpNm.contains(searchWord);
            }
            if ("comCd".equals(searchCd)) {
                return codeMng.codeMngId.comCd.contains(searchWord);
            }
            if ("comNm".equals(searchCd)) {
                return codeMng.comNm.contains(searchWord);
            }
        }
        return null;
    }

    /**
     * <p>공통 코드 상세</p>
     *
     * @param grpCd (그룹 코드)
     * @param comCd (공통 코드)
     * @return CodeMngDetailDTO (공통 코드 DTO)
     */
    @Override
    public CodeMngDetailDTO findCodeMngByComCd(String grpCd, String comCd) {
        return select(new QCodeMngDetailDTO(
            codeGrpMng.grpCd,
            codeGrpMng.grpNm,
            codeMng.codeMngId.comCd,
            codeMng.comNm,
            codeMng.useYn,
            codeMng.detl,
            codeMng.ord,
            Expressions.simpleTemplate(String.class, "getUserNm({0})", codeMng.regUser),
            codeMng.regDate,
            Expressions.simpleTemplate(String.class, "getUserNm({0})", codeMng.modUser),
            codeMng.modDate))
            .from(codeMng)
            .join(codeMng.codeGrpMng, codeGrpMng)
            .where(grpCdEq(grpCd), comCdEq(comCd))
            .fetchOne();
    }

    /**
     * <p>공통 코드 확인</p>
     *
     * @param grpCd (그룹 코드)
     * @param comCd (공통 코드)
     * @return boolean (공통 코드 존재 여부)
     */
    @Override
    public boolean existsCodeMngByComCd(String grpCd, String comCd) {
        Long codeMngCnt = getCodeMngCountQuery(grpCd, comCd, null, null,
            getQueryFactory()).fetchOne();
        return codeMngCnt != null && codeMngCnt > 0;
    }

    /**
     * <p>공통 코드 엑셀 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return List (공통 코드 목록)
     */
    @Override
    public List<CodeMngListDTO> searchCodeMngExcelList(String grpCd, String searchCd,
        String searchWord) {
        List<CodeMngListDTO> excelList = getCodeMngListQuery(grpCd, searchCd, searchWord,
            getQueryFactory()).fetch();
        return addRowNum(excelList, 1, excelList.size());
    }
}
