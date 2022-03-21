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
        registerFunction("getUserNm",
            new SQLFunctionTemplate(StandardBasicTypes.STRING, "DBO.FN_GET_DRP_USER_NM(?1)"));
    }
}
