package com.jsplan.drp.global.obj.controller;

import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.obj.entity.ComsVO;
import com.jsplan.drp.global.util.StringUtil;
import com.jsplan.drp.global.util.io.FileDownload;
import com.jsplan.drp.global.util.io.FileUtil;
import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : ComsController
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 공통 Controller
 */

@Controller
public class ComsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "ComsService")
    private ComsService comsService;

    @Value("${project.path.upload}")
    private String uploadPath;

    /**
     * <p>담당자 선택 팝업</p>
     *
     * @param comsVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/coms/comsUserPop.do")
    public ModelAndView comsUserPop(@ModelAttribute ComsVO comsVO) throws Exception {
        ModelAndView mav = new ModelAndView("coms/comsUserPop");
        return mav;
    }

    /**
     * <p>담당자 조회</p>
     *
     * @param comsVO
     * @return List
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/coms/comsUserSearch.do")
    public @ResponseBody
    List<ComsVO> comsUserSearch(@ModelAttribute ComsVO comsVO) throws Exception {
        List<ComsVO> userList = null;

        try {
            userList = comsService.selectComsUserList(comsVO);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return userList;
    }

    /**
     * <p>공통 파일 다운로드</p>
     *
     * @param comsVO
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/coms/comsFileDownLoad.do")
    public void comsFileDownLoad(@ModelAttribute ComsVO comsVO, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        // 파일을 찾는데 필요한 초기값 세팅
        String filePath = comsVO.getPath();
        String orginFileNm = comsVO.getName();
        String uuidFileNm = comsVO.getUuid();

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
