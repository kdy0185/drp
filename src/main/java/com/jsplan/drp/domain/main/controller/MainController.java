package com.jsplan.drp.domain.main.controller;

import com.jsplan.drp.domain.main.service.MainService;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngResponse;
import com.jsplan.drp.global.obj.dto.ComsDTO;
import com.jsplan.drp.global.obj.dto.ComsMenuDTO;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.obj.vo.AuthVO;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : MainController
 * @Author : KDW
 * @Date : 2022-01-19
 * @Description : 메인 화면 Controller
 */
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final ComsService comsService;

    /**
     * <p>메인 화면</p>
     *
     * @param comsMenuDTO (메뉴 정보)
     * @return ModelAndView (메인 페이지 정보)
     */
    @RequestMapping(value = "/main/main/main.do")
    public ModelAndView main(@ModelAttribute ComsMenuDTO comsMenuDTO) {
        ModelAndView mav = new ModelAndView("main/main/main");

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuDTO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            // ***************************** MENU : E *****************************

            // 일정 목록
            JSONArray mainSkedList = mainService.selectMainSkedList();
            mav.addObject("mainSkedList", mainSkedList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param request (사용자 정보)
     * @return ModelAndView (사용자 상세 페이지 정보)
     */
    @PostMapping(value = "/main/myinfo/myInfoDetail.do")
    public ModelAndView myInfoDetail(@ModelAttribute UserMngRequest request) {
        ModelAndView mav = new ModelAndView("main/myinfo/myInfoDetail");
        request.setUserId(new AuthVO().setUserInfo().getUserId());

        try {
            // 공통 코드 : 사용자 유형
            List<ComsDTO> userTypeList = comsService.selectComsCodeList("USER_TYPE");
            mav.addObject("userTypeList", userTypeList);

            // 사용자 기본 정보
            UserMngDetailDTO detailDTO = mainService.selectMyInfoDetail(request);
            mav.addObject("detailDTO", detailDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @PutMapping(value = "/main/myinfo/myInfoUpdate.do")
    public @ResponseBody UserMngResponse myInfoUpdate(
        @ModelAttribute @Valid UserMngRequest request) {
        return mainService.updateMyInfoData(request);
    }
}
