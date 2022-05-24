package com.jsplan.drp.global.obj.controller;

import com.jsplan.drp.global.obj.entity.UserVO;
import com.jsplan.drp.global.obj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : UserController
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 사용자 계정 Controller
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * <p>로그인</p>
     *
     * @param userVO (사용자 계정 VO)
     * @return ModelAndView (로그인 페이지 정보)
     */
    @RequestMapping(value = "/main/login/login.do")
    public ModelAndView login(@ModelAttribute UserVO userVO) {
        return new ModelAndView("main/login/login");
    }

    /**
     * <p>로그인 처리</p>
     *
     * @param userVO (사용자 계정 VO)
     * @return UserVO (사용자 정보)
     */
    @RequestMapping(value = "/main/login/loginProc.do")
    public UserVO loginProc(@ModelAttribute UserVO userVO) {
        return userService.loadUserByUsername(userVO.getUserId());
    }
}
