package com.jsplan.drp.domain.sys.usermng;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserGrpMngCustomRepository {

    List<UserGrpMng> searchExcel();

    Page<UserGrpMng> searchPageList(String grpNm, Pageable pageable);
}
