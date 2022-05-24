package com.jsplan.drp.domain.pl.ctgopt.repository;

import com.jsplan.drp.domain.pl.ctgopt.entity.PlanCtgOpt;
import com.jsplan.drp.domain.pl.ctgopt.entity.RtneCtgId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : PlanCtgOptRepository
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 분류 옵션 설정 Repository
 */
public interface PlanCtgOptRepository extends JpaRepository<PlanCtgOpt, RtneCtgId>,
    PlanCtgOptCustomRepository {

}
