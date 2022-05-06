<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : userGrpMngDetail.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 그룹 정보 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  // 등록
  function insertUserGrpMng() {
    var form = $('form[name="userGrpMngDetailForm"]');
    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm("등록 하시겠습니까?")) {
        $.ajax({
          type: "post",
          url: "/sys/usermng/userGrpMngInsert.do",
          data: $(form).serialize(),
          success: function (res) {
            if (res.dataStatus === "SUCCESS") {
              alert("등록 되었습니다.");
              $.util.closeDialog();
              searchUserGrpMng();
            } else if (res.dataStatus === "DUPLICATE") {
              alert("중복된 그룹 코드입니다.");
            } else {
              alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
            }
          }
        });
      }
    }
  }

  // 수정
  function updateUserGrpMng() {
    var form = $('form[name="userGrpMngDetailForm"]');
    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm("수정 하시겠습니까?")) {
        $.ajax({
          type: "put",
          url: "/sys/usermng/userGrpMngUpdate.do",
          data: $(form).serialize(),
          success: function (res) {
            if (res.dataStatus === "SUCCESS") {
              alert("수정 되었습니다.");
              $.util.closeDialog();
              searchUserGrpMng();
            } else {
              alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
            }
          }
        });
      }
    }
  }

  // 삭제
  function deleteUserGrpMng() {
    var form = $('form[name="userGrpMngDetailForm"]');

    if (confirm("삭제 하시겠습니까?")) {
      $.ajax({
        type: "delete",
        url: "/sys/usermng/userGrpMngDelete.do",
        data: $(form).serialize(),
        success: function (res) {
          if (res.dataStatus === "SUCCESS") {
            alert("삭제 되었습니다.");
            $.util.closeDialog();
            searchUserGrpMng();
          } else if (res.dataStatus === "CONSTRAINT") {
            alert("사용자가 등록된 그룹은 삭제할 수 없습니다.");
          } else {
            alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
          }
        }
      });
    }
  }
</script>
<form:form modelAttribute="detailDTO" name="userGrpMngDetailForm" method="post">
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
            <th class="top-line"><span class="star-mark">그룹 코드</span></th>
            <td class="top-line">
                <form:input path="grpCd" cssClass="form-control input-sm width_66 required"
                            readonly="${detailDTO.state eq 'I' ? false : true}"/>
            </td>
            <th class="top-line"><span class="star-mark">그룹명</span></th>
            <td class="top-line">
                <form:input path="grpNm" cssClass="form-control input-sm width_95 required"/>
            </td>
        </tr>
        <tr>
            <th><span class="star-unmark">그룹 설명</span></th>
            <td colspan="3">
                <form:textarea path="grpDesc" rows="5" cols="40" cssClass="form-control input-sm width_981"/>
            </td>
        </tr>
        <tr>
            <th><span class="star-mark">등록자</span></th>
            <td>
                <form:input path="regUser" cssClass="form-control input-sm width_66" readonly="true"/>
            </td>
            <th><span class="star-mark">등록 일시</span></th>
            <td>
                <form:input path="regDate" cssClass="form-control input-sm width_66" readonly="true"/>
            </td>
        </tr>
        <tr>
            <th class="bottom-line"><span class="star-unmark">수정자</span></th>
            <td class="bottom-line">
                <form:input path="modUser" cssClass="form-control input-sm width_66" readonly="true"/>
            </td>
            <th class="bottom-line"><span class="star-unmark">수정 일시</span></th>
            <td class="bottom-line">
                <form:input path="modDate" cssClass="form-control input-sm width_66" readonly="true"/>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="val-check-area"></div>
    <div class="btn-center-area">
        <c:if test="${detailDTO.state eq 'I'}">
            <button type="button" onclick="insertUserGrpMng();" class="btn btn-red">
                <i class="fa fa-pencil-square-o"></i>등록
            </button>
        </c:if>
        <c:if test="${detailDTO.state eq 'U'}">
            <button type="button" onclick="updateUserGrpMng();" class="btn btn-red">
                <i class="fa fa-floppy-o"></i>수정
            </button>
            <button type="button" onclick="deleteUserGrpMng();" class="btn btn-red">
                <i class="fa fa-trash"></i>삭제
            </button>
        </c:if>
    </div>
</form:form>
