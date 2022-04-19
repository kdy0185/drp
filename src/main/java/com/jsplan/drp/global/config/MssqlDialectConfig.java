package com.jsplan.drp.global.config;

import org.hibernate.dialect.SQLServer2016Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 * @Class : MssqlDialectConfig
 * @Author : KDW
 * @Date : 2022-03-21
 * @Description : MSSQL 함수 설정
 */
public class MssqlDialectConfig extends SQLServer2016Dialect {

    public MssqlDialectConfig() {
        super();

        // 사용자명 조회 함수 : FN_GET_DRP_USER_NM(USER_ID)
        registerFunction("getUserNm",
            new SQLFunctionTemplate(StandardBasicTypes.STRING, "DBO.FN_GET_DRP_USER_NM(?1)"));

        // 공통 코드명 조회 함수 : FN_GET_CODE_NM(GRP_CD, COM_CD)
        registerFunction("getCodeNm",
            new SQLFunctionTemplate(StandardBasicTypes.STRING, "DBO.FN_GET_CODE_NM(?1, ?2)"));
    }
}
