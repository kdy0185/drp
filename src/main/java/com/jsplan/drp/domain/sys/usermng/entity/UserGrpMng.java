package com.jsplan.drp.domain.sys.usermng.entity;

import com.jsplan.drp.global.obj.entity.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

/**
 * @Class : UserGrpMng
 * @Author : KDW
 * @Date : 2022-03-04
 * @Description : 그룹 Entity
 */
@Entity
@Table(name = "SYS_DRP_GRP")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"grpCd", "grpNm", "grpDesc"})
public class UserGrpMng extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "GRP_CD", length = 20)
    private String grpCd; // 그룹 코드

    @Column(name = "GRP_NM", nullable = false, length = 50)
    private String grpNm; // 그룹명

    @Column(name = "GRP_DESC", length = 1000)
    private String grpDesc; // 그룹 설명

    @OneToMany(mappedBy = "userGrpMng")
    private List<UserMng> userMng = new ArrayList<>(); // 사용자 엔티티

    @Override
    public String getId() {
        return grpCd;
    }

    @Override
    public boolean isNew() {
        return getRegDate() == null;
    }
}
