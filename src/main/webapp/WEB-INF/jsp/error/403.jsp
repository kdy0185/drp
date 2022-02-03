<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : 403.jsp
     * @Author : KDW
     * @Date : 2022-01-20
     * @Description : 403 error 페이지
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
    <link rel="icon" href="/img/icon.ico" type="image/x-icon">
    <link rel="stylesheet" href="/css/error.css"/>
    <title>403 Error</title>
    <script type="text/javascript">
      function goLogin() {
        location.href = "/main/login/login.do";
      }
    </script>
</head>

<body>
<div class="wrap">
    <div class="imgBox">
        <img src="/img/error/403img.png"
             alt="페이지 접근 권한이 없습니다. 이동하려는 페이지에 대한 권한이 없어 이 페이지를 볼 수 없습니다. 해당 페이지의 사용이 허가된 아이디로 로그인 해주시기 바랍니다. 감사합니다."/>
        <button class="btn" onclick="goLogin();">로그인</button>
    </div>
</div>
</body>
</html>
