<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : 404.jsp
     * @Author : KDW
     * @Date : 2022-01-20
     * @Description : 404 error 페이지
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
    <title>404 Error</title>
    <script type="text/javascript">
      function goBack() {
        window.history.back();
      }
    </script>
</head>

<body>
<div class="wrap">
    <div class="imgBox">
        <img src="/img/error/404img.png"
             alt="잘못된 경로로 접속하셨습니다. 방문하시려는 웹 페이지의 주소가 잘못 입력되었거나,변경 또는 삭제되어 요청하신 페이지를 찾을 수 없습니다. 입력하신 주소가 정확한지 다시 한 번 확인해 주시기 바랍니다. 감사합니다."/>
        <button class="btn" onclick="goBack();">이전 페이지로</button>
    </div>
</div>
</body>
</html>
