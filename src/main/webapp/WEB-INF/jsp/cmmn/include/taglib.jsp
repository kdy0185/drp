<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    /**
     * @FileName : taglib.jsp
     * @Author : KDW
     * @Date : 2022-01-21
     * @Description : taglib import
     */
%>

<!-- message.properties -->
<%@ include file="/WEB-INF/jsp/cmmn/include/msglib.jsp" %>

<!-- fineUploader -->
<link rel="stylesheet" href="/fineUploader/fine-uploader-new.css" type="text/css"/>
<script type="text/javascript" src="/fineUploader/fine-uploader.js"></script>

<!-- jQuery -->
<script type="text/javascript" src="/js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/js/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.timepicker.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.mCustomScrollbar.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.qtip.min.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.util.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.validate.extend.js"></script>

<!-- JS -->
<script type="text/javascript" src="/js/datePicker.js"></script>
<script type="text/javascript" src="/js/json2.js"></script>
<script type="text/javascript" src="/fineUploader/fine-uploader.js"></script>

<!-- core CSS -->
<link rel="stylesheet" href="/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<link rel="stylesheet" href="/css/fonts.css">
<link rel="stylesheet" href="/css/epicfont.css">
<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/jquery-ui.css">
<link rel="stylesheet" href="/css/jquery.timepicker.css">
<link rel="stylesheet" href="/css/jquery.mCustomScrollbar.css">
<link rel="stylesheet" href="/css/jquery.qtip.min.css">
<link rel="stylesheet" href="/fineUploader/fine-uploader-new.css"/>

<!-- User Define CSS -->
<link rel="stylesheet" href="/css/user_define.css">

<!-- favicon -->
<link rel="icon" href="/img/icon.ico" type="image/x-icon">
