package com.jsplan.drp.domain.sys.usermng.service;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngResponse;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.repository.UserGrpMngRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserGrpMngService {

    private final UserGrpMngRepository userGrpMngRepository;

    /**
     * <p>그룹 등록</p>
     *
     * @param request (등록할 그룹 정보)
     * @return UserGrpMngResponse (등록한 그룹 코드)
     * @throws Exception throws Exception
     */
    @Transactional
    public UserGrpMngResponse insertGrpMngData(UserGrpMngRequest request) throws Exception {
        UserGrpMng userGrpMng = userGrpMngRepository.save(request.toEntity());
        return new UserGrpMngResponse(userGrpMng.getGrpCd());
    }
}
