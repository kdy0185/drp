package com.jsplan.drp.global.obj.controller;

import com.jsplan.drp.global.obj.dto.ComsDTO;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.util.StringUtil;
import com.jsplan.drp.global.util.io.FileDownload;
import com.jsplan.drp.global.util.io.FileUtil;
import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : ComsController
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 공통 Controller
 */

@Controller
@RequiredArgsConstructor
public class ComsController {

    private final ComsService comsService;

    @Value("${project.path.upload}")
    private String uploadPath;

    /**
     * <p>담당자 선택 팝업</p>
     *
     * @param comsDTO (담당자 정보)
     * @return ModelAndView (담당자 선택 페이지 정보)
     */
    @PostMapping(value = "/coms/comsUserPop.do")
    public ModelAndView comsUserPop(@ModelAttribute ComsDTO comsDTO) {
        return new ModelAndView("coms/comsUserPop");
    }

    /**
     * <p>담당자 조회</p>
     *
     * @param comsDTO (담당자 정보)
     * @return List (담당자 목록)
     */
    @GetMapping(value = "/coms/comsUserSearch.do")
    public @ResponseBody List<ComsDTO> comsUserSearch(@ModelAttribute ComsDTO comsDTO) {
        return comsService.selectComsUserList(comsDTO);
    }

    /**
     * <p>공통 파일 다운로드</p>
     *
     * @param comsDTO (파일 정보)
     */
    @PostMapping(value = "/coms/comsFileDownLoad.do")
    public void comsFileDownLoad(@ModelAttribute ComsDTO comsDTO, HttpServletRequest request,
        HttpServletResponse response) {
        // 파일을 찾는데 필요한 초기값 세팅
        String filePath = comsDTO.getPath();
        String orginFileNm = comsDTO.getName();
        String uuidFileNm = comsDTO.getUuid();

        // uuid가 없지만 다운로드는 해야하는 경우 uuid에 name을 넣어서 사용
        if (StringUtil.isEmpty(
            FileUtil.getFileExt(uuidFileNm))) { // 확장자가 있으면 파일명, 확장자가 없으면 uuid값을 사용한다고 판단
            String fileExt = orginFileNm.substring(orginFileNm.lastIndexOf("."));
            uuidFileNm = uuidFileNm + fileExt;
        }

        // 상대 경로 → 절대 경로 변경
        if (!new File(filePath + uuidFileNm).isAbsolute()) {
            filePath = uploadPath + filePath;
        }

        FileDownload fileDownload = new FileDownload();
        fileDownload.download(request, response, filePath, uuidFileNm, orginFileNm);
    }
}
