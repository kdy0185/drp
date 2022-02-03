<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    /**
     * @FileName : msglib.jsp
     * @Author : KDW
     * @Date : 2022-01-21
     * @Description : 메시지 import
     */
%>
<script type="text/javascript">
  /** 메시지 추가 : S */
  var globalMsg = [];

  // validate
  globalMsg["js.valid.msg.idDupCheck"] = '<spring:message code="js.valid.msg.idDupCheck" />';
  globalMsg["js.valid.msg.required"] = '<spring:message code="js.valid.msg.required" />';
  globalMsg["js.valid.msg.pwCheck"] = '<spring:message code="js.valid.msg.pwCheck" />';
  globalMsg["js.valid.msg.pwCheckReq"] = '<spring:message code="js.valid.msg.pwCheckReq" />';
  globalMsg["js.valid.msg.pwEqualTo"] = '<spring:message code="js.valid.msg.pwEqualTo" />';
  globalMsg["js.valid.msg.number"] = '<spring:message code="js.valid.msg.number" />';
  globalMsg["js.valid.msg.digits"] = '<spring:message code="js.valid.msg.digits" />';
  globalMsg["js.valid.msg.digitsReq"] = '<spring:message code="js.valid.msg.digitsReq" />';
  globalMsg["js.valid.msg.alphaNumeric"] = '<spring:message code="js.valid.msg.alphaNumeric" />';
  globalMsg["js.valid.msg.date"] = '<spring:message code="js.valid.msg.date" />';
  globalMsg["js.valid.msg.dateReq"] = '<spring:message code="js.valid.msg.dateReq" />';
  globalMsg["js.valid.msg.dateTime"] = '<spring:message code="js.valid.msg.dateTime" />';
  globalMsg["js.valid.msg.dateTimeReq"] = '<spring:message code="js.valid.msg.dateTimeReq" />';
  globalMsg["js.valid.msg.mobileNum"] = '<spring:message code="js.valid.msg.mobileNum" />';
  globalMsg["js.valid.msg.email"] = '<spring:message code="js.valid.msg.email" />';
  globalMsg["js.valid.msg.url"] = '<spring:message code="js.valid.msg.url" />';

  // fineUpload
  globalMsg["js.fine.button.addFile"] = '<spring:message code="js.fine.button.addFile" />';
  globalMsg["js.fine.button.upload"] = '<spring:message code="js.fine.button.upload" />';
  globalMsg["js.fine.button.confirm"] = '<spring:message code="js.fine.button.confirm" />';
  globalMsg["js.fine.button.cancel"] = '<spring:message code="js.fine.button.cancel" />';
  globalMsg["js.fine.msg.confirm.delete"] = '<spring:message code="js.fine.msg.confirm.delete" />';
  globalMsg["js.fine.msg.delete"] = '<spring:message code="js.fine.msg.delete" />';
  globalMsg["js.fine.msg.typeError"] = '<spring:message code="js.fine.msg.typeError" />';
  globalMsg["js.fine.msg.sizeError"] = '<spring:message code="js.fine.msg.sizeError" />';
  globalMsg["js.fine.msg.resError"] = '<spring:message code="js.fine.msg.resError" />';
  globalMsg["js.fine.msg.singleFile"] = '<spring:message code="js.fine.msg.singleFile" />';
  globalMsg["js.fine.msg.notBrowser"] = '<spring:message code="js.fine.msg.notBrowser" />';
  globalMsg["js.fine.msg.manyItems"] = '<spring:message code="js.fine.msg.manyItems" />';
  globalMsg["js.fine.msg.onLeave"] = '<spring:message code="js.fine.msg.onLeave" />';
  globalMsg["js.fine.msg.error"] = '<spring:message code="js.fine.msg.error" />';
  globalMsg["js.fine.msg.notExist"] = '<spring:message code="js.fine.msg.notExist" />';
  /** 메시지 추가 : E */
</script>
