<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    /**
     * @FileName : header.jsp
     * @Author : KDW
     * @Date : 2022-01-21
     * @Description : 화면 header 부분
     */
%>
<script type="text/javascript">
  // 메인
  function moveMain() {
    $.util.moveMenu("/main/main/main.do", "A0100", "${_csrf.parameterName}", "${_csrf.token}");
  }

  // 로그아웃
  function logout() {
    location.href = "/main/login/logout.do";
  }

  // 정보 수정
  function moveMyInfoDetail() {
    var title = "사용자 정보";
    var width = 750;
    $.ajax({
      type: "post",
      url: "/main/myinfo/myInfoDetail.do",
      success: function (data, textStatus) {
        $("#popLayout").html(data);
        $.util.openDialog(title, width);
      }
    });
  }
</script>

<div id="header" class="header_box">
    <spring:message code="layout.header.intro" var="headerIntro"/>
    <spring:message code="layout.header.info" var="headerInfo"/>
    <spring:message code="layout.header.logout" var="headerLogout"/>
    <h1 class="logo">
        <img src="/img/logo.png" onclick="moveMain();" style="cursor: pointer;" alt="">
    </h1>
    <div class="function_btn">
        <c:if test="${fn:substring(comsMenuDTO.menuCd, 0, 1) ne 'A'}">
            <button type="button" onclick="$.util.menuSlide();" class="btn btn-green width-slider">
                <i class="icon-arrow-thin-left"></i>
            </button>
            <button type="button" onclick="$.util.menuSlide();" class="btn btn-green width-slider" style="display: none;">
                <i class="icon-arrow-thin-right"></i>
            </button>
        </c:if>
    </div>
    <ul class="menu">
        <c:forEach var="menu1DTO" items="${menuList}" varStatus="menu1Status">
            <c:if test="${menu1DTO.menuLv eq 1}">
                <sec:authorize access="hasAnyAuthority(${menu1DTO.authCd})">

                    <!-- 2 depth 최상단 메뉴 조회 : S -->
                    <c:set var="menu2Loop" value="true"/>
                    <c:forEach var="menu2DTO" items="${menuList}" varStatus="menu2Status">
                        <c:if test="${menu2DTO.menuLv eq 2 && menu2DTO.upperMenuCd eq menu1DTO.menuCd && menu2Loop}">
                            <sec:authorize access="hasAnyAuthority(${menu2DTO.authCd})">
                                <c:choose>
                                    <c:when test="${menu2DTO.lastYn eq 'Y'}">
                                        <li class="color_${fn:substring(menu2DTO.menuCd, 0, 1)}">
                                            <a href="javascript:$.util.moveMenu('${menu2DTO.menuUrl}', '${menu2DTO.menuCd}', '${_csrf.parameterName}', '${_csrf.token}');"
                                               class="bg bg2 collapsed ${fn:substring(menu2DTO.menuCd, 0, 1) eq fn:substring(comsMenuDTO.menuCd, 0, 1) ? 'active' : ''}">
                                                <i class="icon-file-list icon${fn:substring(menu2DTO.menuCd, 0, 1)}"></i>
                                                <span>${pageContext.response.locale.language eq 'ko' ? menu1DTO.menuNm : menu1DTO.menuEngNm}</span>
                                            </a>
                                        </li>
                                        <c:set var="menu2Loop" value="false"/>
                                    </c:when>
                                    <c:otherwise>

                                        <!-- 3 depth 최상단 메뉴 조회 : S -->
                                        <c:set var="menu3Loop" value="true"/>
                                        <c:forEach var="menu3DTO" items="${menuList}" varStatus="menu3Status">
                                            <c:if test="${menu3DTO.menuLv eq 3 && menu3DTO.upperMenuCd eq menu2DTO.menuCd && menu3Loop}">
                                                <sec:authorize access="hasAnyAuthority(${menu3DTO.authCd})">
                                                    <li class="color_${fn:substring(menu2DTO.menuCd, 0, 1)}">
                                                        <a href="javascript:$.util.moveMenu('${menu3DTO.menuUrl}', '${menu3DTO.menuCd}', '${_csrf.parameterName}', '${_csrf.token}');"
                                                           class="bg bg2 collapsed ${fn:substring(menu3DTO.menuCd, 0, 1) eq fn:substring(comsMenuDTO.menuCd, 0, 1) ? 'active' : ''}">
                                                            <i class="icon-file-list icon${fn:substring(menu2DTO.menuCd, 0, 1)}"></i>
                                                            <span>${pageContext.response.locale.language eq 'ko' ? menu1DTO.menuNm : menu1DTO.menuEngNm}</span>
                                                        </a>
                                                    </li>
                                                    <c:set var="menu3Loop" value="false"/>
                                                    <c:set var="menu2Loop" value="false"/>
                                                </sec:authorize>
                                            </c:if>
                                        </c:forEach>
                                        <!-- 3 depth 최상단 메뉴 조회 : E -->

                                    </c:otherwise>
                                </c:choose>
                            </sec:authorize>
                        </c:if>
                    </c:forEach>
                    <!-- 2 depth 최상단 메뉴 조회 : E -->

                </sec:authorize>
            </c:if>
        </c:forEach>
    </ul>
    <ul class="support_btn">
        <li>
            <p class="user_id">
                <span><sec:authentication property="principal.userNm"/></span>
                <span>${headerIntro}</span>
            </p>
        </li>
        <li>
            <button type="button" onclick="moveMyInfoDetail();" class="btn btn-green width-slider2 width_100" title="${headerInfo}">
                <i class="icon-user-login-line" aria-hidden="true"></i>
                <p class="margin_none font_w500">${headerInfo}</p>
            </button>
        </li>
        <li>
            <button type="button" onclick="logout();" class="btn btn-green width-slider2" title="${headerLogout}">
                <i class="icon-share-right" aria-hidden="true"></i>
                <p class="margin_none font_w500">${headerLogout}</p>
            </button>
        </li>
    </ul>

</div>

<!-- popLayout -->
<div style="display: none;">
    <div id="popLayout"></div>
</div>
<!-- subPopLayout -->
<div style="display: none;">
    <div id="subPopLayout"></div>
</div>
