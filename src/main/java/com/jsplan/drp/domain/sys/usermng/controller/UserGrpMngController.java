package com.jsplan.drp.domain.sys.usermng.controller;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngListDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngResponse;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngSearchDTO;
import com.jsplan.drp.domain.sys.usermng.service.UserGrpMngService;
import com.jsplan.drp.global.obj.entity.ComsMenuVO;
import com.jsplan.drp.global.obj.entity.ComsVO;
import com.jsplan.drp.global.obj.vo.DetailStatus;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.util.ExcelUtil;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

            mav.addObject("searchDTO", new UserGrpMngSearchDTO());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>그룹 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (그룹 목록)
     */
    @GetMapping(value = "/sys/usermng/userGrpMngSearch.do")
    public @ResponseBody
    Page<UserGrpMngListDTO> userGrpMngSearch(@ModelAttribute UserGrpMngSearchDTO searchDTO) {
        return userGrpMngService.selectUserGrpMngList(searchDTO);
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
        UserGrpMngDetailDTO detailDTO = new UserGrpMngDetailDTO();

        if (DetailStatus.UPDATE.equals(request.getDetailStatus())) {
            detailDTO = userGrpMngService.selectUserGrpMngDetail(request);
        }

        detailDTO.setDetailStatus(request.getDetailStatus());
        mav.addObject("detailDTO", detailDTO);
        return mav;
    }

    /**
     * <p>그룹 등록</p>
     *
     * @param request (그룹 정보)
     * @return UserGrpMngResponse (응답 정보)
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
     * @return UserGrpMngResponse (응답 정보)
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
     * @return UserGrpMngResponse (응답 정보)
     */
    @DeleteMapping(value = "/sys/usermng/userGrpMngDelete.do")
    public @ResponseBody
    UserGrpMngResponse userGrpMngDelete(@ModelAttribute UserGrpMngRequest request) {
        return userGrpMngService.deleteUserGrpMngData(request);
    }

    /**
     * <p>그룹 엑셀 출력</p>
     *
     * @param searchDTO (조회 조건)
     * @param map (엑셀 출력 정보)
     * @return ExcelUtil (엑셀 다운로드)
     */
    @PostMapping(value = "/sys/usermng/userGrpMngExcel.do")
    public ExcelUtil userGrpMngExcel(@ModelAttribute UserGrpMngSearchDTO searchDTO, ModelMap map) {
        List<String> colName = new ArrayList<>();
        List<Integer> colWidth = new ArrayList<>();
        List<String[]> colValue = new ArrayList<>();

        // 데이터 조회
        List<UserGrpMngListDTO> excelList = userGrpMngService.selectUserGrpMngExcelList(searchDTO);

        // 컬럼명 설정
        colName.add("순번");
        colName.add("그룹 코드");
        colName.add("그룹명");
        colName.add("그룹 설명");
        colName.add("등록자");
        colName.add("등록 일시");
        colName.add("수정자");
        colName.add("수정 일시");

        // 컬럼 사이즈 설정
        colWidth.add(2000);
        colWidth.add(4000);
        colWidth.add(6000);
        colWidth.add(8000);
        colWidth.add(4000);
        colWidth.add(4000);
        colWidth.add(4000);
        colWidth.add(4000);

        // 데이터 설정
        for (UserGrpMngListDTO listDTO : excelList) {
            String rn = String.valueOf(listDTO.getRn());
            String grpCd = listDTO.getGrpCd();
            String grpNm = listDTO.getGrpNm();
            String grpDesc = listDTO.getGrpDesc();
            String regUser = listDTO.getRegUser();
            String regDate = listDTO.getRegDate();
            String modUser = listDTO.getModUser();
            String modDate = listDTO.getModDate();
            String[] arr = {rn, grpCd, grpNm, grpDesc, regUser, regDate, modUser, modDate};
            colValue.add(arr);
        }

        map.put("excelName", "그룹 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
