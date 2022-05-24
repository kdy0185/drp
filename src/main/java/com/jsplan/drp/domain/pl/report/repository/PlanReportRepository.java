package com.jsplan.drp.domain.pl.report.repository;

import com.jsplan.drp.domain.pl.report.entity.PlanReport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : PlanReportRepository
 * @Author : KDW
 * @Date : 2022-05-13
 * @Description : 데일리 리포트 Repository
 */
public interface PlanReportRepository extends JpaRepository<PlanReport, Long>,
    PlanReportCustomRepository {

}
