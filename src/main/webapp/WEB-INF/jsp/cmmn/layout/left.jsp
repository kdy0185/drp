<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
    /**
     * @FileName : left.jsp
     * @Author : KDW
     * @Date : 2022-01-21
     * @Description : 화면 left 부분
     */
%>
<sec:authorize access="isAuthenticated()">
    <div id="accordion" class="left-side col-md-2 col-sm-2 col-xs-12">
        <ul id="ulAccordion" class="left-menu panel color_${fn:substring(comsMenuDTO.menuCd, 0, 1)}">

            <!-- 2 depth 메뉴 조회 : S -->
            <c:forEach var="menu2DTO" items="${menuList}" varStatus="menu2Status">
                <c:if test="${menu2DTO.menuLv eq 2 && fn:substring(menu2DTO.menuCd, 0, 1) eq fn:substring(comsMenuDTO.menuCd, 0, 1)}">
                    <sec:authorize access="hasAnyAuthority(${menu2DTO.authCd})">
                        <c:choose>
                            <c:when test="${menu2DTO.lastYn eq 'Y'}">
                                <li class="non_depth">
                                    <a href="javascript:$.util.moveMenu('${menu2DTO.menuUrl}', '${menu2DTO.menuCd}', '${_csrf.parameterName}', '${_csrf.token}');"
                                       class="bg bg2 collapsed ${menu2DTO.menuCd eq comsMenuDTO.menuCd ? 'active' : ''}">
                                        <span>${pageContext.response.locale.language eq 'ko' ? menu2DTO.menuNm : menu2DTO.menuEngNm}</span>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <a data-toggle="collapse" data-parent="#accordion" href="#collapse${menu2Status.count}"
                                       class="bg bg2 collapsed ${fn:substring(menu2DTO.menuCd, 0, 4) eq fn:substring(comsMenuDTO.upperMenuCd, 0, 4) ? 'active' : ''}">
                                        <i class="fa fa-home"></i>
                                        <span>${pageContext.response.locale.language eq 'ko' ? menu2DTO.menuNm : menu2DTO.menuEngNm}</span>
                                    </a>
                                </li>
                                <div id="sub_accordion${menu2Status.count}">
                                    <ul id="collapse${menu2Status.count}"
                                        class="left-subMenu collapse${fn:substring(menu2DTO.menuCd, 0, 4) eq fn:substring(comsMenuDTO.upperMenuCd, 0, 4) ? '1 active' : ''}">

                                        <!-- 3 depth 메뉴 조회 : S -->
                                        <c:forEach var="menu3DTO" items="${menuList}" varStatus="menu3Status">
                                            <c:if test="${menu3DTO.menuLv eq 3 && menu3DTO.upperMenuCd eq menu2DTO.menuCd}">
                                                <sec:authorize access="hasAnyAuthority(${menu3DTO.authCd})">
                                                    <li class="non_depth">
                                                        <a href="javascript:$.util.moveMenu('${menu3DTO.menuUrl}', '${menu3DTO.menuCd}', '${_csrf.parameterName}', '${_csrf.token}');"
                                                           class="bg bg2 collapsed ${menu3DTO.menuCd eq comsMenuDTO.menuCd ? 'active' : ''}">
                                                            <span>${pageContext.response.locale.language eq 'ko' ? menu3DTO.menuNm : menu3DTO.menuEngNm}</span>
                                                        </a>
                                                    </li>
                                                </sec:authorize>
                                            </c:if>
                                        </c:forEach>
                                        <!-- 3 depth 메뉴 조회 : E -->

                                    </ul>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </sec:authorize>
                </c:if>
            </c:forEach>
            <!-- 2 depth 메뉴 조회 : E -->

        </ul>
    </div>
</sec:authorize>
