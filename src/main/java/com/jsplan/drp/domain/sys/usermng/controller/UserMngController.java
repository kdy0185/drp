package com.jsplan.drp.domain.sys.usermng.controller;

import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngListDto;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngResponse;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngSearchDto;
import com.jsplan.drp.domain.sys.usermng.service.UserMngService;
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
 * @Class : UserMngController
 * @Author : KDW
 * @Date : 2022-01-25
 * @Description : 사용자 관리 Controller
 */
@Controller
@RequiredArgsConstructor
public class UserMngController {

    private final UserMngService userMngService;
    private final ComsService comsService;

    /**
     * <p>사용자 관리</p>
     *
     * @param comsMenuVO (메뉴 VO)
     * @return ModelAndView (사용자 관리 페이지 정보)
     */
    @PostMapping(value = "/sys/usermng/userMngList.do")
    public ModelAndView userMngList(@ModelAttribute ComsMenuVO comsMenuVO) {
        ModelAndView mav = new ModelAndView("sys/usermng/userMngList");

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

            // 공통 코드 : 그룹
            List<ComsVO> grpList = comsService.selectComsGrpList();
            mav.addObject("grpList", grpList);

            // 기본 검색 조건 설정
            mav.addObject("searchDto", new UserMngSearchDto("userNm"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>사용자 조회</p>
     *
     * @param searchDto (조회 조건)
     * @return Page (페이징 목록)
     */
    @GetMapping(value = "/sys/usermng/userMngSearch.do")
    public @ResponseBody
    Page<UserMngListDto> userMngSearch(@ModelAttribute UserMngSearchDto searchDto) {
        return userMngService.selectUserMngList(searchDto);
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param request (사용자 정보)
     * @param comsVO  (공통 코드 정보)
     * @return ModelAndView (사용자 상세 페이지 정보)
     */
    @PostMapping(value = "/sys/usermng/userMngDetail.do")
    public ModelAndView userMngDetail(@ModelAttribute UserMngRequest request, ComsVO comsVO) {
        ModelAndView mav = new ModelAndView("sys/usermng/userMngDetail");
        UserMngDetailDto detailDto = new UserMngDetailDto();

        try {
            // 공통 코드 : 그룹
            List<ComsVO> grpList = comsService.selectComsGrpList();
            mav.addObject("grpList", grpList);

            // 공통 코드 : 사용자 유형
            List<ComsVO> userTypeList = comsService.selectComsCodeList("USER_TYPE");
            mav.addObject("userTypeList", userTypeList);

            if ("U".equals(request.getState())) {
                detailDto = userMngService.selectUserMngDetail(request);
            }

            detailDto.setState(request.getState());
            mav.addObject("detailDto", detailDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>사용자 아이디 중복 체크</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @PostMapping(value = "/sys/usermng/userMngIdDupCheck.do")
    public @ResponseBody
    UserMngResponse userMngIdDupCheck(@ModelAttribute UserMngRequest request) {
        return userMngService.selectUserMngDupCnt(request);
    }

    /**
     * <p>사용자 등록</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @PostMapping(value = "/sys/usermng/userMngInsert.do")
    public @ResponseBody
    UserMngResponse userMngInsert(@ModelAttribute @Valid UserMngRequest request) {
        return userMngService.insertUserMngData(request);
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/usermng/userMngUpdate.do")
    public @ResponseBody
    UserMngResponse userMngUpdate(@ModelAttribute @Valid UserMngRequest request) {
        return userMngService.updateUserMngData(request);
    }

    /**
     * <p>사용자 삭제</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @DeleteMapping(value = "/sys/usermng/userMngDelete.do")
    public @ResponseBody
    UserMngResponse userMngDelete(@ModelAttribute UserMngRequest request) {
        return userMngService.deleteUserMngData(request);
    }
}
