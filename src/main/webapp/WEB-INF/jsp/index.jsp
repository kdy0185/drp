<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
    /**
     * @FileName : index.jsp
     * @Author : KDW
     * @Date : 2022-01-20
     * @Description : 최초 화면 이동
     */
%>
<sec:authorize access="isAnonymous()">
    <c:set var="actionUrl" value="/main/login/login.do"/>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
    <c:set var="actionUrl" value="/main/main/main.do"/>
</sec:authorize>
<c:redirect url="${actionUrl}"/>
