package com.jsplan.drp.domain.main.service;

import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngResponse;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.domain.sys.usermng.repository.UserMngRepository;
import com.jsplan.drp.global.obj.vo.DataStatus;
import com.jsplan.drp.global.util.StringUtil;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : MainService
 * @Author : KDW
 * @Date : 2022-01-19
 * @Description : 메인 화면 Service
 */
@Service
@RequiredArgsConstructor
public class MainService {

    private final UserMngRepository userMngRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * <p>일정 목록</p>
     *
     * @return JSONArray (일정 목록)
     */
    public JSONArray selectMainSkedList() {
        JSONArray mainSkedArray = new JSONArray();
        JSONObject eventObject;

        // JSON 변환
        /* 일정별 색상 설정
         * "B" == "#ab6da5"
         * "P" == 루틴 관리 == "#4489e4"
         * "M" == "#2a9a7a"
         * "E" == "#eb5f70"
         * "L" == "#f0a638"
         * "S" == "#45b9bc"
         * "Z" == 시스템 관리 == "#afafaf"
         */

        eventObject = new JSONObject();
        eventObject.put("id", "B1");
        eventObject.put("title", "캐주얼 미팅");
        eventObject.put("start", "2022-06-01 17:00");
        eventObject.put("end", "2022-06-02 18:30");
        eventObject.put("color", "#2a9a7a");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "P1");
        eventObject.put("title", "사이드 프로젝트 빌드");
        eventObject.put("start", "2022-05-27 00:00");
        eventObject.put("end", "2022-05-31 24:00");
        eventObject.put("color", "#4489e4");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "P2");
        eventObject.put("title", "그리드 화면 적용");
        eventObject.put("start", "2022-06-04 00:00");
        eventObject.put("end", "2022-06-08 24:00");
        eventObject.put("color", "#4489e4");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "M1");
        eventObject.put("title", "JPA 정리");
        eventObject.put("start", "2022-06-11 00:00");
        eventObject.put("end", "2022-06-15 24:00");
        eventObject.put("color", "#2a9a7a");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "E1");
        eventObject.put("title", "계약 및 공고 정리");
        eventObject.put("start", "2022-06-21 00:00");
        eventObject.put("end", "2022-06-22 24:00");
        eventObject.put("color", "#eb5f70");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "L1");
        eventObject.put("title", "모임 관리");
        eventObject.put("start", "2022-06-25 21:00");
        eventObject.put("end", "2022-06-27 12:00");
        eventObject.put("color", "#f0a638");
        mainSkedArray.add(eventObject);

        return mainSkedArray;
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param request (사용자 정보)
     * @return UserMngDetailDTO (사용자 DTO)
     */
    public UserMngDetailDTO selectMyInfoDetail(UserMngRequest request) {
        return userMngRepository.findUserMngByUserId(request.getUserId());
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @Transactional
    public UserMngResponse updateMyInfoData(UserMngRequest request) {
        // BCrypt 패스워드 암호화
        if (!StringUtil.isBlank(request.getUserPw())) {
            String encodePw = passwordEncoder.encode(request.getUserPw());
            request.setUserPw(encodePw);
        }

        UserMng userMng = userMngRepository.findById(request.getUserId())
            .orElseThrow(NoSuchElementException::new);
        userMng.updateMyInfo(request);
        return new UserMngResponse(userMng.getUserId(), DataStatus.SUCCESS);
    }
}
