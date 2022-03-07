package com.jsplan.drp.global.obj.entity;

/**
 * @Class : ComsMenuVO
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 메뉴 VO
 */

public class ComsMenuVO {

	private String menuCd; // 메뉴 코드
	private String menuNm; // 메뉴명
	private String menuEngNm; // 메뉴명(영문)
	private String upperMenuCd; // 상위 메뉴 코드
	private String upperMenuNm; // 상위 메뉴명
	private String menuUrl; // 이동 주소
	private String menuLv; // 메뉴 수준
	private String lastYn; // 하위 메뉴 존재 여부
	private String authCd; // 권한 코드
	private String upperAuthCd; // 상위 권한 코드

	public String getMenuCd() {
		return menuCd;
	}

	public void setMenuCd(String menuCd) {
		this.menuCd = menuCd;
	}

	public String getMenuNm() {
		return menuNm;
	}

	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}

	public String getMenuEngNm() {
		return menuEngNm;
	}

	public void setMenuEngNm(String menuEngNm) {
		this.menuEngNm = menuEngNm;
	}

	public String getUpperMenuCd() {
		return upperMenuCd;
	}

	public void setUpperMenuCd(String upperMenuCd) {
		this.upperMenuCd = upperMenuCd;
	}

	public String getUpperMenuNm() {
		return upperMenuNm;
	}

	public void setUpperMenuNm(String upperMenuNm) {
		this.upperMenuNm = upperMenuNm;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuLv() {
		return menuLv;
	}

	public void setMenuLv(String menuLv) {
		this.menuLv = menuLv;
	}

	public String getLastYn() {
		return lastYn;
	}

	public void setLastYn(String lastYn) {
		this.lastYn = lastYn;
	}

	public String getAuthCd() {
		return authCd;
	}

	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}

	public String getUpperAuthCd() {
		return upperAuthCd;
	}

	public void setUpperAuthCd(String upperAuthCd) {
		this.upperAuthCd = upperAuthCd;
	}

}
