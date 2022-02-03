package com.jsplan.drp.cmmn.obj;

import com.jsplan.drp.cmmn.util.PagingUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Class : ComsVO
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 공통 VO
 */
public class ComsVO extends PagingUtil {

	private Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	private Object principal = auth.getPrincipal();

	/* 공통 */
	private int rn; // 순번
	private String searchCd; // 검색 조건
	private String searchWord; // 검색어
	private String selectFormNm; // 팝업 호출 전 form
	private String selectInputId1; // 팝업 호출 전 input id1
	private String selectInputId2; // 팝업 호출 전 input id2
	private String selectInputId3; // 팝업 호출 전 input id3
	private String grpCd; // 그룹 코드
	private String grpNm; // 그룹명
	private String comCd; // 공통 코드
	private String comNm; // 공통 코드명
    private String userId; // 사용자 아이디
    private String userNm; // 성명
	private String state; // 등록/수정 (등록 : I, 수정 : U)
	private String loginId; // 로그인 아이디

	/* 권한 */
	private String authAdmin; // 슈퍼관리자 권한 여부

	/* 파일 업로드 */
	private String fileType; // 파일 타입(파일 : File, 엑셀 : Excel)
	private String fileExt; // 파일 확장자 선택 (이미지만, 엑셀만, 한글 문서만, ppt만 등등..)
	private String fileSize; // 파일 size 제한
	private String fileCnt; // 파일 개수 (연결 개수 1이면 단일파일 업로드)
	private String fileNo; // 업로드 컴포넌트 No.
	private String menuUrl; // menuUrl 경로
	private String isUuid; // uuid 사용 여부
	private String isSave; // 파일 저장 여부
	private String isCallback; // 파일 저장 여부
	private String isAddDelAuth; // 파일 저장 여부

	/* fineUploader */
	private String uuid; // 고유값 확장자 포함(fineupload 필수)
	private String size; // 파일 크기 (findupload 고유)
	private String name; // 원본 파일명 확장자 포함 (fineupload 필수)
	private String path; // 파일이 저장된 경로
	private String params; // 처리에 필요한 파라미터
	
	/* 프로시저 공용 변수 */
	private String procParamXml; // xml 구조 String
	private String procReturnCd; // 프로시저 처리 후 상태 반환값

	public Object getPrincipal() {
		return principal;
	}

	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

	public int getRn() {
		return rn;
	}

	public void setRn(int rn) {
		this.rn = rn;
	}

	public String getSearchCd() {
		return searchCd;
	}

	public void setSearchCd(String searchCd) {
		this.searchCd = searchCd;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

	public String getSelectFormNm() {
		return selectFormNm;
	}

	public void setSelectFormNm(String selectFormNm) {
		this.selectFormNm = selectFormNm;
	}

	public String getSelectInputId1() {
		return selectInputId1;
	}

	public void setSelectInputId1(String selectInputId1) {
		this.selectInputId1 = selectInputId1;
	}

	public String getSelectInputId2() {
		return selectInputId2;
	}

	public void setSelectInputId2(String selectInputId2) {
		this.selectInputId2 = selectInputId2;
	}

	public String getSelectInputId3() {
		return selectInputId3;
	}

	public void setSelectInputId3(String selectInputId3) {
		this.selectInputId3 = selectInputId3;
	}

	public String getGrpCd() { return grpCd; }

	public void setGrpCd(String grpCd) { this.grpCd = grpCd; }

	public String getGrpNm() { return grpNm; }

	public void setGrpNm(String grpNm) { this.grpNm = grpNm; }

	public String getComCd() { return comCd; }

	public void setComCd(String comCd) { this.comCd = comCd; }

	public String getComNm() { return comNm; }

	public void setComNm(String comNm) { this.comNm = comNm; }

	public String getUserId() { return userId; }

	public void setUserId(String userId) { this.userId = userId; }

	public String getUserNm() { return userNm; }

	public void setUserNm(String userNm) { this.userNm = userNm; }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLoginId() {
		loginId = ((UserVO) principal).getUserId();
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getAuthAdmin() {
		boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("AUTH_ADMIN"));
		authAdmin = isAdmin ? "Y" : "N";
		return authAdmin;
	}

	public void setAuthAdmin(String authAdmin) {
		this.authAdmin = authAdmin;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileCnt() {
		return fileCnt;
	}

	public void setFileCnt(String fileCnt) {
		this.fileCnt = fileCnt;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getIsUuid() {
		return isUuid;
	}

	public void setIsUuid(String isUuid) {
		this.isUuid = isUuid;
	}

	public String getIsSave() {
		return isSave;
	}

	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}

	public String getIsCallback() {
		return isCallback;
	}

	public void setIsCallback(String isCallback) {
		this.isCallback = isCallback;
	}

	public String getIsAddDelAuth() {
		return isAddDelAuth;
	}

	public void setIsAddDelAuth(String isAddDelAuth) {
		this.isAddDelAuth = isAddDelAuth;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getProcParamXml() {
		return procParamXml;
	}

	public void setProcParamXml(String procParamXml) {
		this.procParamXml = procParamXml;
	}

	public String getProcReturnCd() {
		return procReturnCd;
	}

	public void setProcReturnCd(String procReturnCd) {
		this.procReturnCd = procReturnCd;
	}

}
