<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    /**
     * @FileName : login.jsp
     * @Author : KDW
     * @Date : 2022-01-21
     * @Description : 로그인
     */
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="author" content="KDW">
    <title><spring:message code="browser.title"/></title>

    <!-- jQuery -->
    <script type="text/javascript" src="/js/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="/js/jquery/jquery.cookie.js"></script>

    <!-- core CSS -->
    <link rel="stylesheet" href="/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/css/fonts.css"/>
    <link rel="stylesheet" href="/css/login.css"/>

    <!-- favicon -->
    <link rel="icon" href="/img/icon.ico" type="image/x-icon">

    <script type="text/javascript">
      $(function () {
        getCookie();
      });

      // 쿠키 가져오기
      function getCookie() {
        var form = $('form[name="loginForm"]');
        var userId = $(form).find('input[name="userId"]');
        var saveIdChk = $(form).find('input[name="saveIdChk"]');
        var cookieUserId = $.cookie("cookieUserId");
        if (cookieUserId != null) {
          if ("${param.failure}" !== "true") $(userId).val(cookieUserId);
          $(saveIdChk).prop("checked", true);
        }
      }

      // 로그인
      function login() {
        var form = $('form[name="loginForm"]');
        var userId = $(form).find('input[name="userId"]');
        var userPw = $(form).find('input[name="userPw"]');

        if ($(userId).val() === "") {
          alert("아이디를 입력하세요.");
          $(userId).focus();
        } else if ($(userPw).val() === "") {
          alert("비밀번호를 입력하세요.");
          $(userPw).focus();
        } else {
          $(form).attr({
            action: "/main/login/loginProc.do"
          }).submit();
        }
      }
    </script>
</head>
<body>
<form:form modelAttribute="userVO" name="loginForm" method="post" cssClass="login_allbox">
    <div class="login-box">
        <div class="login-header">
            <img src="/img/login/login.png" alt="Login">
        </div>
        <div class="login-contents">
            <p>
                <label><b></b>
                    <form:input path="userId"
                                onkeydown="if(event.keyCode===13){login(); return false;}"
                                placeholder="아이디"/>
                </label>
            </p>
            <p>
                <label><b></b>
                    <form:password path="userPw"
                                   onkeydown="if(event.keyCode===13){login(); return false;}"
                                   placeholder="비밀번호"/>
                </label>
            </p>
            <div class="autologin">
                <span>
                    <input type="checkbox" name="saveIdChk" id="saveIdChk">
                    <label for="saveIdChk">아이디 저장</label>
                </span>
                <span>
                    <input type="checkbox" name="autoLogin" id="autoLogin">
                    <label for="autoLogin">자동 로그인</label>
                </span>
            </div>
            <p>
                <c:if test="${not empty secExMsg}">${secExMsg}</c:if>
            </p>
            <p>
                <input type="button" class="login_btn" onclick="login(); return false;" value="로그인"/>
                <form:hidden path="loginUrl"/>
            </p>
        </div>
    </div>
</form:form>
</body>
</html>
