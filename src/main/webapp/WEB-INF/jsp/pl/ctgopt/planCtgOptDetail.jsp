<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : planCtgOptDetail.jsp
     * @Author : KDW
     * @Date : 2022-02-02
     * @Description : 분류 옵션 설정 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  // 분류 코드 자동 추출
  function getRtneCtgCd() {
    var rtneCtgCdReg = /^[A-Z](\d{2}|\d{4}|\d{6})$/;
    var form = $('form[name="planCtgOptDetailForm"]');
    var rtneCtgCd = $(form).find('input[name="rtneCtgCd"]').val();
    if (rtneCtgCdReg.test(rtneCtgCd)) {
      var cd1 = rtneCtgCd.substr(0, 3);
      var cd2 = rtneCtgCd.substr(3, 2);
      var cd3 = rtneCtgCd.substr(5, 2);
      var upperRtneCtgCd = $.isEmpty(cd3) ? ($.isEmpty(cd2) ? "" : cd1) : cd1 + cd2;
      $(form).find('input[name="upperRtneCtgCd"]').val(upperRtneCtgCd);
    } else {
      $(form).find('input[name="upperRtneCtgCd"]').val("");
    }
  }

  // 담당자 선택 팝업
  function openUserSubPop(obj) {
    var title = "담당자 선택";
    var width = 750;
    var selectFormNm = $(obj).closest("form").attr("name");
    var selectInputId1 = $(obj).parent().find("input:eq(0)").attr("id");
    var selectInputId2 = $(obj).parent().find("input:eq(1)").attr("id");
    var userId = $("form[name=" + selectFormNm + "]").find('input[name="userId"]').val();
    var userNm = $("form[name=" + selectFormNm + "]").find('input[name="userNm"]').val();

    $.ajax({
      type: "post",
      url: "/coms/comsUserPop.do",
      data: {
        selectFormNm: selectFormNm,
        selectInputId1: selectInputId1,
        selectInputId2: selectInputId2,
        searchCd: !$.isEmpty(userId) && $.isEmpty(userNm) ? "userId" : "userNm",
        searchWord: !$.isEmpty(userId) && $.isEmpty(userNm) ? userId : userNm
      },
      success: function (data, textStatus) {
        $("#subPopLayout").html(data);
        $.util.openSubDialog(title, width);
      }
    });
  }

  // 등록
  function insertPlanCtgOpt() {
    var form = $('form[name="planCtgOptDetailForm"]');
    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm("등록 하시겠습니까?")) {
        $.ajax({
          type: "post",
          url: "/pl/ctgopt/planCtgOptInsert.do",
          data: $(form).serialize(),
          success: function (res) {
            if (res.dataStatus === "SUCCESS") {
              alert("등록 되었습니다.");
              $.util.closeDialog();
              searchPlanCtgOpt();
            } else if (res.dataStatus === "DUPLICATE") {
              alert("중복된 분류 옵션입니다.");
            } else {
              alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
            }
          }
        });
      }
    }
  }

  // 수정
  function updatePlanCtgOpt() {
    var form = $('form[name="planCtgOptDetailForm"]');
    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm("수정 하시겠습니까?")) {
        $.ajax({
          type: "put",
          url: "/pl/ctgopt/planCtgOptUpdate.do",
          data: $(form).serialize(),
          success: function (res) {
            if (res.dataStatus === "SUCCESS") {
              alert("수정 되었습니다.");
              $.util.closeDialog();
              searchPlanCtgOpt();
            } else {
              alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
            }
          }
        });
      }
    }
  }

  // 삭제
  function deletePlanCtgOpt() {
    var form = $('form[name="planCtgOptDetailForm"]');

    if (confirm("삭제 하시겠습니까?")) {
      $.ajax({
        type: "delete",
        url: "/pl/ctgopt/planCtgOptDelete.do",
        data: $(form).serialize(),
        success: function (res) {
          if (res.dataStatus === "SUCCESS") {
            alert("삭제 되었습니다.");
            $.util.closeDialog();
            searchPlanCtgOpt();
          } else if (res.dataStatus === "CONSTRAINT") {
            alert("이미 적용된 일과가 있어 삭제할 수 없습니다.");
          } else {
            alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
          }
        }
      });
    }
  }
</script>
<form:form modelAttribute="detailDTO" name="planCtgOptDetailForm" method="post">
    <form:hidden path="detailStatus"/>
    <table class="table blue-base-table">
        <colgroup>
            <col style="width: 15%"/>
            <col style="width: 35%"/>
            <col style="width: 15%"/>
            <col style="width: 35%"/>
        </colgroup>
        <tbody>
        <tr>
            <th class="top-line"><span class="star-mark">분류 코드</span></th>
            <td class="top-line">
                <form:input path="rtneCtgCd" cssClass="form-control input-sm width_42 required"
                            readonly="${detailDTO.detailStatus eq 'UPDATE' ? 'true' : 'false'}"
                            onblur="getRtneCtgCd();"/>
            </td>
            <th class="top-line"><span class="star-unmark">상위 분류 코드</span></th>
            <td class="top-line">
                <form:input path="upperRtneCtgCd" cssClass="form-control input-sm width_42"
                            readonly="true"/>
            </td>
        </tr>
        <tr>
            <th><span class="star-mark">분류명</span></th>
            <td><form:input path="rtneCtgNm"
                            cssClass="form-control input-sm width_66 required"/></td>
            <th><span class="star-mark">가중치</span></th>
            <td><form:input path="wtVal" cssClass="form-control input-sm width_28 digitsReq"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">최소 권장 시간</span></th>
            <td>
                <form:input path="recgMinTime" id="recgMaxTime"
                            cssClass="form-control input-sm width_28 lessThan"/>
            </td>
            <th><span class="star-mark">최대 권장 시간</span></th>
            <td>
                <form:input path="recgMaxTime" id="recgMinTime"
                            cssClass="form-control input-sm width_28 moreThan" maxlength="2"/>
            </td>
        </tr>
        <tr>
            <th class="bottom-line"><span class="star-mark">사용 여부</span></th>
            <td class="bottom-line">
                <div class="radio-box">
                    <form:radiobutton path="useYn" cssClass="required" value="Y" label="사용"/>
                    <form:radiobutton path="useYn" cssClass="required" value="N" label="미사용"/>
                </div>
            </td>
            <th class="bottom-line"><span class="star-mark">담당자</span></th>
            <td class="bottom-line">
                <form:hidden path="userId"/>
                <form:input path="userNm"
                            cssClass="form-control input-sm pull-left width_66 required"
                            readonly="true"/>
                <c:if test="${detailDTO.authAdmin eq 'Y' && detailDTO.detailStatus eq 'INSERT'}">
                    <button type="button" onclick="openUserSubPop(this);"
                            class="btn btn-green search">
                        <i class="fa fa-search margin_none"></i>
                    </button>
                </c:if>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="val-check-area"></div>
    <div class="btn-center-area">
        <c:if test="${detailDTO.detailStatus eq 'INSERT'}">
            <button type="button" onclick="insertPlanCtgOpt();" class="btn btn-red">
                <i class="fa fa-pencil-square-o"></i>등록
            </button>
        </c:if>
        <c:if test="${detailDTO.detailStatus eq 'UPDATE'}">
            <button type="button" onclick="updatePlanCtgOpt();" class="btn btn-red">
                <i class="fa fa-floppy-o"></i>수정
            </button>
            <button type="button" onclick="deletePlanCtgOpt();" class="btn btn-red">
                <i class="fa fa-trash"></i>삭제
            </button>
        </c:if>
    </div>
</form:form>
