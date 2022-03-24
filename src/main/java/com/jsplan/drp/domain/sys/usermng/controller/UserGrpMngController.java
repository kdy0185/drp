package com.jsplan.drp.domain.sys.usermng.controller;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngSearchDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto.UserGrpMngListDto;
import com.jsplan.drp.domain.sys.usermng.service.UserGrpMngService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : UserGrpMngController
 * @Author : KDW
 * @Date : 2022-03-24
 * @Description : 그룹 관리 Controller
 */
@Controller
@RequiredArgsConstructor
public class UserGrpMngController {

    private final UserGrpMngService userGrpMngService;

    /**
     * <p>그룹 관리</p>
     *
     * @return ModelAndView
     */
    @PostMapping(value = "/sys/usermng/userGrpMngList.do")
    public ModelAndView userGrpMngList() {
        ModelAndView mav = new ModelAndView("sys/usermng/userGrpMngList");
        mav.addObject("userGrpMngList", new ArrayList<>());
        return mav;
    }

    /**
     * <p>그룹 조회</p>
     *
     * @param userGrpMngSearchDto (조회 조건)
     * @return Page (페이징 목록)
     */
    @PostMapping(value = "/sys/usermng/userGrpMngSearch.do")
    public @ResponseBody
    Page<UserGrpMngListDto> userGrpMngSearch(@RequestBody UserGrpMngSearchDto userGrpMngSearchDto) {
        return userGrpMngService.selectGrpMngList(userGrpMngSearchDto);
    }
}
