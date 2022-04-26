package com.jsplan.drp.domain.sys.menumng.repository;

import com.jsplan.drp.domain.sys.menumng.entity.MenuMng;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Class : MenuMngRepository
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 관리 Repository
 */
public interface MenuMngRepository extends JpaRepository<MenuMng, String>, MenuMngCustomRepository {

}
