package com.jsplan.drp.domain.main;

import com.jsplan.drp.global.obj.entity.ComsMenuVO;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.obj.entity.ComsVO;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "MainService")
    private MainService mainService;

    @Resource(name = "ComsService")
    private ComsService comsService;

    /**
     * <p>메인 화면</p>
     *
     * @param mainVO
     * @return MainVO
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/main/main/main.do")
    public ModelAndView main(@ModelAttribute MainVO mainVO, ComsMenuVO comsMenuVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("main/main/main");

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuVO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            // ***************************** MENU : E *****************************

            // 일정 목록
            JSONArray mainSkedList = mainService.selectMainSkedList(mainVO);
            mav.addObject("mainSkedList", mainSkedList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param mainVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/main/myinfo/myInfoDetail.do")
    public ModelAndView myInfoDetail(@ModelAttribute MainVO mainVO) throws Exception {
        ModelAndView mav = new ModelAndView("main/myinfo/myInfoDetail");

        try {
            // 공통 코드 : 사용자 유형
            List<ComsVO> userTypeList = comsService.selectComsCodeList("USER_TYPE");
            mav.addObject("userTypeList", userTypeList);

            // 사용자 기본 정보
            MainVO vo = mainService.selectMyInfoDetail(mainVO);
            mav.addObject("mainVO", vo);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param mainVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/main/myinfo/myInfoUpdate.do")
    public @ResponseBody
    String myInfoUpdate(@ModelAttribute MainVO mainVO) throws Exception {
        String code = null;

        try {
            code = mainService.updateMyInfoData(mainVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }
}
