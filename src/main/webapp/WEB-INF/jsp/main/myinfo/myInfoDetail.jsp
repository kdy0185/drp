<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : myInfoDetail.jsp
     * @Author : KDW
     * @Date : 2022-01-28
     * @Description : 정보 수정 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  // 수정
  function updateMyInfo() {
    var form = $('form[name="myInfoDetailForm"]');
    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm("수정 하시겠습니까?")) {
        $.ajax({
          type: "post",
          url: "/main/myinfo/myInfoUpdate.do",
          data: $(form).serialize(),
          success: function (code) {
            if (code === "S") {
              alert("수정 되었습니다.");
              $.util.closeDialog();
            } else if (code === "N") {
              alert("수정된 데이터가 없습니다.");
            } else {
              alert("오류가 발생하였습니다.\ncode : " + code);
            }
          }
        });
      }
    }
  }
</script>
<form:form modelAttribute="mainVO" name="myInfoDetailForm" method="post">
    <table class="table blue-base-table">
        <colgroup>
            <col style="width: 15%"/>
            <col style="width: 35%"/>
            <col style="width: 15%"/>
            <col style="width: 35%"/>
        </colgroup>
        <tbody>
        <tr>
            <th class="top-line"><span class="star-mark">그룹</span></th>
            <td class="top-line">
                <form:hidden path="grpCd"/>
                <form:input path="grpNm" cssClass="form-control input-sm width_66 required" readonly="true"/>
            </td>
            <th class="top-line"><span class="star-unmark">비밀번호</span></th>
            <td class="top-line">
                <form:password path="userPw" cssClass="form-control input-sm width_95 pwCheck"/>
            </td>
        </tr>
        <tr>
            <th><span class="star-mark">아이디</span></th>
            <td><form:input path="userId" cssClass="form-control input-sm width_66 required" readonly="true"/></td>
            <th><span class="star-unmark">비밀번호 확인</span></th>
            <td><form:password path="userPwDup" cssClass="form-control input-sm width_95 pwEqualTo"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">성명</span></th>
            <td><form:input path="userNm" cssClass="form-control input-sm width_66 required"/></td>
            <th><span class="star-unmark">휴대폰 번호</span></th>
            <td><form:input path="mobileNum" cssClass="form-control input-sm width_66 mobileNum"/></td>
        </tr>
        <tr>
            <th><span class="star-unmark">이메일</span></th>
            <td colspan="3"><form:input path="email" cssClass="form-control input-sm width_982 email"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">사용자 유형</span></th>
            <td>
                <form:select path="userType" cssClass="form-control input-sm width_66 required" disabled="true">
                    <form:option value="" label="선택"/>
                    <c:forEach var="userTypeVO" items="${userTypeList}" varStatus="status">
                        <form:option value="${userTypeVO.comCd}" label="${userTypeVO.comNm}"/>
                    </c:forEach>
                </form:select>
            </td>
            <th><span class="star-mark">사용 여부</span></th>
            <td>
                <div class="radio-box">
                    <form:radiobutton path="useYn" cssClass="required" value="Y" label="사용" disabled="true"/>
                    <form:radiobutton path="useYn" cssClass="required" value="N" label="미사용" disabled="true"/>
                </div>
            </td>
        </tr>
        <tr>
            <th class="bottom-line"><span class="star-mark">등록 일시</span></th>
            <td class="bottom-line">
                <form:input path="regDate" cssClass="form-control input-sm width_66 required" readonly="true"/>
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
        <button type="button" onclick="updateMyInfo();" class="btn btn-red">
            <i class="fa fa-floppy-o"></i>수정
        </button>
    </div>
</form:form>
