package com.jsplan.drp.domain.sys.usermng.repository;

import static com.jsplan.drp.domain.sys.usermng.entity.QUserGrpMng.userGrpMng;

import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

public class UserGrpMngCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    UserGrpMngCustomRepository {

    public UserGrpMngCustomRepositoryImpl() {
        super(UserGrpMng.class);
    }

    @Override
    public List<UserGrpMng> searchExcel() {
        return null;
    }

    public Page<UserGrpMng> searchPageList(String grpNm, Pageable pageable) {
        return applyPagination(pageable, contentQuery ->
            contentQuery.selectFrom(userGrpMng)
                .where(grpNmLike(grpNm)
                ), countQuery ->
            countQuery.select(userGrpMng.count())
                .from(userGrpMng)
                .where(grpNmLike(grpNm))
        );
    }

    private BooleanExpression grpNmLike(String grpNm) {
        return StringUtils.hasText(grpNm) ? userGrpMng.grpNm.likeIgnoreCase(grpNm) : null;
    }
}
