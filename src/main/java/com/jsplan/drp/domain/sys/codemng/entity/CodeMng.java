package com.jsplan.drp.domain.sys.codemng.entity;

import com.jsplan.drp.domain.sys.codemng.dto.CodeMngRequest;
import com.jsplan.drp.global.obj.entity.BaseEntity;
import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

/**
 * @Class : CodeMng
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 공통 코드 관리 Entity
 */
@Entity
@Table(name = "SYS_COM_CODE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeMng extends BaseEntity implements Persistable<CodeMngId> {

    @EmbeddedId
    private CodeMngId codeMngId; // 그룹 코드 + 공통 코드

    @Column(name = "COM_NM", nullable = false)
    private String comNm; // 공통 코드명

    @Column(name = "USE_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseStatus useYn; // 사용 여부

    @Column(name = "DETL", length = 4000)
    private String detl; // 비고

    @Column(name = "ORD", nullable = false)
    private Integer ord; // 정렬 순서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRP_CD", insertable = false, updatable = false)
    private CodeGrpMng codeGrpMng; // 그룹 코드 엔티티

    @Builder
    public CodeMng(CodeMngId codeMngId, String comNm, UseStatus useYn, String detl, Integer ord) {
        this.codeMngId = codeMngId;
        this.comNm = comNm;
        this.useYn = useYn;
        this.detl = detl;
        this.ord = ord;
    }

    @Override
    public CodeMngId getId() {
        return codeMngId;
    }

    @Override
    public boolean isNew() {
        return getRegDate() == null;
    }

    // 공통 코드 수정
    public void updateCodeMng(CodeMngRequest request) {
        this.comNm = request.getComNm();
        this.useYn = UseStatus.valueOf(request.getUseYn());
        this.detl = request.getDetl();
        this.ord = request.getOrd();
    }
}
