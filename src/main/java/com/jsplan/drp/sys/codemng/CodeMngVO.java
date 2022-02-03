package com.jsplan.drp.sys.codemng;

import com.jsplan.drp.cmmn.obj.ComsVO;
import java.io.Serializable;

/**
 * @Class : CodeMngVO
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 코드 관리 VO
 */
public class CodeMngVO extends ComsVO implements Serializable {

    private static final long serialVersionUID = 6914988375706605076L;

    /* 목록 */
    private String grpCd; // 그룹 코드
    private String exGrpCd; // 그룹 코드 (저장 전 key)
    private String grpNm; // 그룹 코드명
    private String comCd; // 공통 코드
    private String exComCd; // 공통 코드 (저장 전 key)
    private String comNm; // 공통 코드명
    private String useYn; // 사용 여부
    private String detl; // 비고
    private String ord; // 정렬 순서
    private String regUser; // 등록자
    private String regDate; // 등록 일시
    private String modUser; // 수정자
    private String modDate; // 수정 일시

    /* 저장 */
    private String jsonData; // 저장할 JSON 데이터

    public String getGrpCd() {
        return grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getExGrpCd() {
        return exGrpCd;
    }

    public void setExGrpCd(String exGrpCd) {
        this.exGrpCd = exGrpCd;
    }

    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    public String getComCd() {
        return comCd;
    }

    public void setComCd(String comCd) {
        this.comCd = comCd;
    }

    public String getExComCd() {
        return exComCd;
    }

    public void setExComCd(String exComCd) {
        this.exComCd = exComCd;
    }

    public String getComNm() {
        return comNm;
    }

    public void setComNm(String comNm) {
        this.comNm = comNm;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getDetl() {
        return detl;
    }

    public void setDetl(String detl) {
        this.detl = detl;
    }

    public String getOrd() {
        return ord;
    }

    public void setOrd(String ord) {
        this.ord = ord;
    }

    public String getRegUser() {
        return regUser;
    }

    public void setRegUser(String regUser) {
        this.regUser = regUser;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getModUser() {
        return modUser;
    }

    public void setModUser(String modUser) {
        this.modUser = modUser;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

}
