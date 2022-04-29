<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : authMngDetail.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 권한 정보 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  // 등록
  function insertAuthMng() {
    var form = $('form[name="authMngDetailForm"]');
    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm("등록 하시겠습니까?")) {
        $.ajax({
          type: "post",
          url: "/sys/authmng/authMngInsert.do",
          data: $(form).serialize(),
          success: function (res) {
            if (res.dataStatus === "SUCCESS") {
              alert("등록 되었습니다.");
              $.util.closeDialog();
              moveList();
            } else if (res.dataStatus === "DUPLICATE") {
              alert("중복된 권한입니다.");
            } else {
              alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
            }
          }
        });
      }
    }
  }

  // 수정
  function updateAuthMng() {
    var form = $('form[name="authMngDetailForm"]');
    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm("수정 하시겠습니까?")) {
        $.ajax({
          type: "put",
          url: "/sys/authmng/authMngUpdate.do",
          data: $(form).serialize(),
          success: function (res) {
            if (res.dataStatus === "SUCCESS") {
              alert("수정 되었습니다.");
              $.util.closeDialog();
              moveList();
            } else {
              alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
            }
          }
        });
      }
    }
  }

  // 삭제
  function deleteAuthMng() {
    var form = $('form[name="authMngDetailForm"]');

    if (confirm("삭제 하시겠습니까?")) {
      $.ajax({
        type: "delete",
        url: "/sys/authmng/authMngDelete.do",
        data: $(form).serialize(),
        success: function (res) {
          if (res.dataStatus === "SUCCESS") {
            alert("삭제 되었습니다.");
            $.util.closeDialog();
            moveList();
          } else if (res.dataStatus === "CONSTRAINT") {
            alert("권한별 사용자 또는 메뉴가 적용되어 삭제할 수 없습니다.");
          } else {
            alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
          }
        }
      });
    }
  }
</script>
<form:form modelAttribute="detailDTO" name="authMngDetailForm" method="post">
    <form:hidden path="state"/>
    <table class="table blue-base-table">
        <colgroup>
            <col style="width: 15%"/>
            <col style="width: 35%"/>
            <col style="width: 15%"/>
            <col style="width: 35%"/>
        </colgroup>
        <tbody>
        <tr>
            <th class="top-line"><span class="star-mark">권한 코드</span></th>
            <td class="top-line">
                <form:input path="authCd" cssClass="form-control input-sm width_95 required"
                            readonly="${detailDTO.state eq 'U' ? 'true' : 'false'}"/>
            </td>
            <th class="top-line"><span class="star-unmark">상위 권한 코드</span></th>
            <td class="top-line">
                <form:select path="upperAuthCd" cssClass="form-control input-sm width_954">
                    <form:option value="" label="선택"/>
                    <c:forEach var="authVO" items="${authList}" varStatus="status">
                        <form:option value="${authVO.authCd}" label="${authVO.authNm}"/>
                    </c:forEach>
                </form:select>
            </td>
        </tr>
        <tr>
            <th><span class="star-mark">권한명</span></th>
            <td colspan="3"><form:input path="authNm" cssClass="form-control input-sm width_982 required"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">권한 수준</span></th>
            <td><form:input path="authLv" cssClass="form-control input-sm width_42 digitsReq"/></td>
            <th><span class="star-mark">권한 순서</span></th>
            <td><form:input path="authOrd" cssClass="form-control input-sm width_42 digitsReq"/></td>
        </tr>
        <tr>
            <th class="bottom-line"><span class="star-unmark">권한 설명</span></th>
            <td class="bottom-line" colspan="3">
                <form:textarea path="authDesc" rows="5" cols="40" cssClass="form-control input-sm width_981"/>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="val-check-area"></div>
    <div class="btn-center-area">
        <c:if test="${detailDTO.state eq 'I'}">
            <button type="button" onclick="insertAuthMng();" class="btn btn-red">
                <i class="fa fa-pencil-square-o"></i>등록
            </button>
        </c:if>
        <c:if test="${detailDTO.state eq 'U'}">
            <button type="button" onclick="updateAuthMng();" class="btn btn-red">
                <i class="fa fa-floppy-o"></i>수정
            </button>
            <c:if test="${detailDTO.useYn eq 'Y'}">
                <button type="button" onclick="deleteAuthMng();" class="btn btn-red">
                    <i class="fa fa-trash"></i>삭제
                </button>
            </c:if>
        </c:if>
    </div>
</form:form>
