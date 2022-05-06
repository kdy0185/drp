package com.jsplan.drp.domain.sys.codemng.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : CodeMngId
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 공통 코드 복합키 Class
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CodeMngId implements Serializable {

    @Column(name = "GRP_CD", length = 20)
    private String grpCd; // 그룹 코드

    @Column(name = "COM_CD", length = 20)
    private String comCd; // 공통 코드

    // 복합키 생성
    public static CodeMngId createCodeMngId(String grpCd, String comCd) {
        CodeMngId codeMngId = new CodeMngId();
        codeMngId.setGrpCd(grpCd);
        codeMngId.setComCd(comCd);
        return codeMngId;
    }
}
