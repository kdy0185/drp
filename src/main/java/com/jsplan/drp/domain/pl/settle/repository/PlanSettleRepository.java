package com.jsplan.drp.domain.pl.settle.repository;

import com.jsplan.drp.domain.pl.settle.entity.PlanSettle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : PlanSettleRepository
 * @Author : KDW
 * @Date : 2022-05-17
 * @Description : 일일 결산 Repository
 */
public interface PlanSettleRepository extends JpaRepository<PlanSettle, Long>,
    PlanSettleCustomRepository {

}
