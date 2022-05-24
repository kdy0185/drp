package com.jsplan.drp.domain.sys.codemng.entity;

import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngRequest;
import com.jsplan.drp.global.obj.entity.BaseEntity;
import com.jsplan.drp.global.obj.vo.UseStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

/**
 * @Class : CodeGrpMng
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 그룹 코드 관리 Entity
 */
@Entity
@Table(name = "SYS_GRP_CODE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeGrpMng extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "GRP_CD", length = 20)
    private String grpCd; // 그룹 코드

    @Column(name = "GRP_NM", nullable = false)
    private String grpNm; // 그룹 코드명

    @Column(name = "USE_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseStatus useYn; // 사용 여부

    @Column(name = "DETL", length = 4000)
    private String detl; // 비고

    @Builder
    public CodeGrpMng(String grpCd, String grpNm, UseStatus useYn, String detl) {
        this.grpCd = grpCd;
        this.grpNm = grpNm;
        this.useYn = useYn;
        this.detl = detl;
    }

    @Override
    public String getId() {
        return grpCd;
    }

    @Override
    public boolean isNew() {
        return getRegDate() == null;
    }

    // 그룹 코드 수정
    public void updateCodeGrpMng(CodeGrpMngRequest request) {
        this.grpNm = request.getGrpNm();
        this.useYn = UseStatus.valueOf(request.getUseYn());
        this.detl = request.getDetl();
    }
}
