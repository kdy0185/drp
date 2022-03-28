package com.jsplan.drp.domain.sys.usermng.controller;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngResponse;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngSearchDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto.UserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto.UserGrpMngListDto;
import com.jsplan.drp.domain.sys.usermng.service.UserGrpMngService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        return new ModelAndView("sys/usermng/userGrpMngList");
    }

    /**
     * <p>그룹 조회</p>
     *
     * @param searchDto (조회 조건)
     * @return Page (페이징 목록)
     */
    @PostMapping(value = "/sys/usermng/userGrpMngSearch.do")
    public @ResponseBody
    Page<UserGrpMngListDto> userGrpMngSearch(@RequestBody UserGrpMngSearchDto searchDto) {
        return userGrpMngService.selectGrpMngList(searchDto);
    }

    /**
     * <p>그룹 상세</p>
     *
     * @param request (그룹 정보)
     * @return ModelAndView (그룹 상세 페이지 정보)
     */
    @PostMapping(value = "/sys/usermng/userGrpMngDetail.do")
    public ModelAndView userGrpMngDetail(@RequestBody UserGrpMngRequest request) {
        ModelAndView mav = new ModelAndView("sys/usermng/userGrpMngDetail");
        UserGrpMngDetailDto detailDto = userGrpMngService.selectGrpMngDetail(request);
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
    UserGrpMngResponse userGrpMngInsert(@RequestBody @Valid UserGrpMngRequest request) {
        return userGrpMngService.insertGrpMngData(request);
    }

    /**
     * <p>그룹 수정</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (그룹 코드)
     */
    @PutMapping(value = "/sys/usermng/userGrpMngUpdate.do")
    public @ResponseBody
    UserGrpMngResponse userGrpMngUpdate(@RequestBody @Valid UserGrpMngRequest request) {
        return userGrpMngService.updateGrpMngData(request);
    }

    /**
     * <p>그룹 삭제</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (그룹 코드)
     */
    @DeleteMapping(value = "/sys/usermng/userGrpMngDelete.do")
    public @ResponseBody
    UserGrpMngResponse userGrpMngDelete(@RequestBody UserGrpMngRequest request) {
        userGrpMngService.deleteGrpMngData(request);
        return new UserGrpMngResponse(request.getGrpCd());
    }
}
