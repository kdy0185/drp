<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : 500.jsp
     * @Author : KDW
     * @Date : 2022-01-20
     * @Description : 500 error 페이지
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
    <title>500 Error</title>
    <script type="text/javascript">
      function goBack() {
        window.history.back();
      }
    </script>
</head>

<body>
<div class="wrap">
    <div class="imgBox">
        <img src="/img/error/500img.png"
             alt="이용에 불편을 드려 죄송합니다. 요청하신 내역을 처리하는 중에 오류가 발생했습니다. 관련 문의사항은 관리자에게 문의 하여 주시기 바랍니다. 감사합니다."/>
        <button class="btn" onclick="goBack();">이전 페이지로</button>
    </div>
</div>
</body>
</html>
