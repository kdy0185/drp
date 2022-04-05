package com.jsplan.drp.domain.sys.usermng.controller;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngResponse;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngSearchDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDto.UserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDto.UserGrpMngListDto;
import com.jsplan.drp.domain.sys.usermng.service.UserGrpMngService;
import com.jsplan.drp.global.obj.entity.ComsMenuVO;
import com.jsplan.drp.global.obj.entity.ComsVO;
import com.jsplan.drp.global.obj.service.ComsService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final ComsService comsService;

    /**
     * <p>그룹 관리</p>
     *
     * @param comsMenuVO (메뉴 VO)
     * @return ModelAndView (그룹 관리 페이지 정보)
     */
    @PostMapping(value = "/sys/usermng/userGrpMngList.do")
    public ModelAndView userGrpMngList(@ModelAttribute ComsMenuVO comsMenuVO) {
        ModelAndView mav = new ModelAndView("sys/usermng/userGrpMngList");

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuVO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            comsMenuVO = comsService.selectComsMenuDetail(comsMenuVO.getMenuCd());
            mav.addObject("comsMenuVO", comsMenuVO);
            // ***************************** MENU : E *****************************

            // ***************************** PAGE : S *****************************
            List<ComsVO> pageList = comsService.selectComsCodeList("PAGE_SIZE");
            mav.addObject("pageList", pageList);
            // ***************************** PAGE : E *****************************

            mav.addObject("searchDto", new UserGrpMngSearchDto());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>그룹 조회</p>
     *
     * @param searchDto (조회 조건)
     * @return Page (페이징 목록)
     */
    @GetMapping(value = "/sys/usermng/userGrpMngSearch.do")
    public @ResponseBody
    Page<UserGrpMngListDto> userGrpMngSearch(@ModelAttribute UserGrpMngSearchDto searchDto) {
        return userGrpMngService.selectUserGrpMngList(searchDto);
    }

    /**
     * <p>그룹 상세</p>
     *
     * @param request (그룹 정보)
     * @return ModelAndView (그룹 상세 페이지 정보)
     */
    @PostMapping(value = "/sys/usermng/userGrpMngDetail.do")
    public ModelAndView userGrpMngDetail(@ModelAttribute UserGrpMngRequest request) {
        ModelAndView mav = new ModelAndView("sys/usermng/userGrpMngDetail");
        UserGrpMngDetailDto detailDto = new UserGrpMngDetailDto();

        if ("U".equals(request.getState())) {
            detailDto = userGrpMngService.selectUserGrpMngDetail(request);
        }

        detailDto.setState(request.getState());
        mav.addObject("detailDto", detailDto);
        return mav;
    }

    /**
     * <p>그룹 등록</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (그룹 코드)
     */
    @PostMapping(value = "/sys/usermng/userGrpMngInsert.do")
    public @ResponseBody
    UserGrpMngResponse userGrpMngInsert(@ModelAttribute @Valid UserGrpMngRequest request) {
        return userGrpMngService.insertUserGrpMngData(request);
    }

    /**
     * <p>그룹 수정</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (그룹 코드)
     */
    @PutMapping(value = "/sys/usermng/userGrpMngUpdate.do")
    public @ResponseBody
    UserGrpMngResponse userGrpMngUpdate(@ModelAttribute @Valid UserGrpMngRequest request) {
        return userGrpMngService.updateUserGrpMngData(request);
    }

    /**
     * <p>그룹 삭제</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (그룹 코드)
     */
    @DeleteMapping(value = "/sys/usermng/userGrpMngDelete.do")
    public @ResponseBody
    UserGrpMngResponse userGrpMngDelete(@ModelAttribute UserGrpMngRequest request) {
        return userGrpMngService.deleteUserGrpMngData(request);
    }
}
