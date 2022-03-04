package com.jsplan.drp.global.obj;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "UserService")
    private UserService userService;

    /**
     * <p>로그인</p>
     *
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/main/login/login.do")
    public ModelAndView login(@ModelAttribute UserVO userVO) throws Exception {
        return new ModelAndView("main/login/login");
    }

    /**
     * <p>로그인</p>
     *
     * @param userVO
     * @return UserVO
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/main/login/loginProc.do")
    public UserVO loginProc(@ModelAttribute UserVO userVO) throws Exception {
        String userId = userVO.getUserId();

        try {
            userVO = userService.loadUserByUsername(userId);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return userVO;
    }
}
