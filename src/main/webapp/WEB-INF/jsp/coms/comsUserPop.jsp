<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : comsUserPop.jsp
     * @Author : KDW
     * @Date : 2022-01-27
     * @Description : 담당자 선택 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  $(function () {
    $.util.setScrollbar();
    searchUserPop();

  });
  // 담당자 조회
  function searchUserPop() {
    var form = $('form[name="comsUserForm"]');
    var searchCd = $(form).find('select[name="searchCd"] option:selected').val();
    var searchWord = $(form).find('input[name="searchWord"]').val();
    var grpCd = $(form).find('input[name="grpCd"]').val();

    $.ajax({
      type: "post",
      url: "/coms/comsUserSearch.do",
      data: {
        searchCd: searchCd,
        searchWord: searchWord,
        grpCd: grpCd
      },
      success: function (result) {
        var comsUserTable = $("table#comsUserTable");
        var comsUserHeader = $("table#comsUserHeader");

        // 동적 컬럼 적용
        var colCont = "";
        colCont += "<colgroup>";
        colCont += "<col style='width: 10%'/>";
        colCont += "<col style='width: 20%'/>";
        colCont += "<col style='width: 30%'/>";
        colCont += "<col style='width: 30%'/>";

        colCont += "</colgroup>";

        var headerCont = "";
        comsUserHeader.empty();
        headerCont += "<thead>";
        headerCont += "<tr>";
        headerCont += "<th>순번</th>";
        headerCont += "<th>그룹</th>";
        headerCont += "<th>아이디</th>";
        headerCont += "<th>성명</th>";
        headerCont += "</tr>";
        headerCont += "</thead>";
        comsUserHeader.append(colCont + headerCont);

        // 내용 적용
        var userCont = "";
        comsUserTable.empty();
        userCont += "<tbody>";
        for (var i = 0; i < result.length; i++) {
          if (i % 2 === 0) userCont += "<tr onclick='selectUserPop(\"" + result[i].userId
              + "\", \"" + result[i].userNm + "\");'>";
          if (i % 2 === 1) userCont += "<tr class='back' onclick='selectUserPop(\"" + result[i].userId
              + "\", \"" + result[i].userNm + "\");'>";
          userCont += "<td>" + Number(i + 1) + "</td>";
          userCont += "<td>" + result[i].grpNm + "</td>";
          userCont += "<td>" + result[i].userId + "</td>";
          userCont += "<td>" + result[i].userNm + "</td>";
          userCont += "</tr>";
        }
        userCont += "</tbody>";
        comsUserTable.append(colCont + userCont);
      }
    });
  }

  // 담당자 선택 (1개일 경우)
  function enterUserPop() {
    var userTr = $("#comsUserTable").find("tr");
    if ($(userTr).length === 1) {
      var userId = $(userTr).find("td:eq(2)").text();
      var userNm = $(userTr).find("td:eq(3)").text();
      selectUserPop(userId, userNm);
    }
  }

  // 담당자 선택
  function selectUserPop(userId, userNm) {
    var form = $('form[name="comsUserForm"]');
    var selectFormNm = $(form).find('input[name="selectFormNm"]').val();
    var selectInputId1 = $(form).find('input[name="selectInputId1"]').val();
    var selectInputId2 = $(form).find('input[name="selectInputId2"]').val();

    form = $("form[name=" + selectFormNm + "]");
    $(form).find("input[id=" + selectInputId1 + "]").val(userId);
    $(form).find("input[id=" + selectInputId2 + "]").val(userNm).focus();
    $.util.closeSubDialog();
  }
</script>

<form:form modelAttribute="comsVO" name="comsUserForm" method="post">
    <form:hidden path="selectFormNm"/>
    <form:hidden path="selectInputId1"/>
    <form:hidden path="selectInputId2"/>
    <form:hidden path="grpCd"/>

    <div class="container-fluid">
        <div class="row">
            <div class="margin_t10 col-md-12 col-sm-12 col-xs-12">
                <span class="search-icon">구분</span>
                <div class="form-group float_left width_70per">
                    <form:select path="searchCd" cssClass="form-control input-sm pull-left width_20"
                                 onkeydown="if(event.keyCode==13){enterUserPop(this); return false;}">
                        <form:option value="userId" label="아이디"/>
                        <form:option value="userNm" label="성명"/>
                    </form:select>
                    <form:input path="searchWord"
                                cssClass="form-control input-sm pull-left width_50"
                                onkeydown="if(event.keyCode==13){searchUserPop(); return false;}"/>
                    <div class="search-btn-area text_r">
                        <button type="button" onclick="searchUserPop();" class="btn btn-gray margin_none">
                            <i class="fa fa-search"></i>조회
                        </button>
                    </div>
                </div>
            </div>
            <div class="margin_b10 col-md-12 col-sm-12 col-xs-12">
                <table class="table table-mini margin_b0" id="comsUserHeader"></table>
                <div class="table_over scrollbar" data-mcs-theme="rounded-dots">
                    <table class="table table-mini" id="comsUserTable"></table>
                </div>
            </div>
        </div>
    </div>
</form:form>
