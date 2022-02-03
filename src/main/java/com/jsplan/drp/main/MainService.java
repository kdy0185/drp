package com.jsplan.drp.main;

import com.jsplan.drp.cmmn.util.StringUtil;
import javax.annotation.Resource;
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
@Service("MainService")
public class MainService {

    @Resource
    private MainMapper mainMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * <p>일정 목록</p>
     *
     * @param mainVO
     * @return JSONArray
     * @throws Exception throws Exception
     */
    public JSONArray selectMainSkedList(MainVO mainVO) throws Exception {
        JSONArray mainSkedArray = new JSONArray();
        JSONObject eventObject = null;

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
        eventObject.put("start", "2022-01-27 17:00");
        eventObject.put("end", "2022-01-27 18:30");
        eventObject.put("color", "#2a9a7a");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "P1");
        eventObject.put("title", "사이드 프로젝트 빌드");
        eventObject.put("start", "2022-01-03 00:00");
        eventObject.put("end", "2022-01-21 24:00");
        eventObject.put("color", "#4489e4");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "P2");
        eventObject.put("title", "그리드 화면 적용");
        eventObject.put("start", "2022-01-31 00:00");
        eventObject.put("end", "2022-02-04 24:00");
        eventObject.put("color", "#4489e4");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "M1");
        eventObject.put("title", "JPA 정리");
        eventObject.put("start", "2022-02-07 00:00");
        eventObject.put("end", "2022-02-11 24:00");
        eventObject.put("color", "#2a9a7a");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "E1");
        eventObject.put("title", "계약 및 공고 정리");
        eventObject.put("start", "2022-02-24 00:00");
        eventObject.put("end", "2022-02-25 24:00");
        eventObject.put("color", "#eb5f70");
        mainSkedArray.add(eventObject);

        eventObject = new JSONObject();
        eventObject.put("id", "L1");
        eventObject.put("title", "모임 관리");
        eventObject.put("start", "2022-02-06 21:00");
        eventObject.put("end", "2022-02-07 12:00");
        eventObject.put("color", "#f0a638");
        mainSkedArray.add(eventObject);

        return mainSkedArray;
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param mainVO
     * @return MainVO
     * @throws Exception throws Exception
     */
    public MainVO selectMyInfoDetail(MainVO mainVO) throws Exception {
        return mainMapper.selectMyInfoDetail(mainVO);
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param mainVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateMyInfoData(MainVO mainVO) throws Exception {
        // BCrypt 패스워드 암호화
        String userPw = mainVO.getUserPw();
        if (!StringUtil.isEmpty(userPw)) {
            String encodePw = passwordEncoder.encode(userPw);
            mainVO.setUserPw(encodePw);
        }

        return mainMapper.updateMyInfoData(mainVO) > 0 ? "S" : "N";
    }
}
