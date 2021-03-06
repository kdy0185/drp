<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : userMngDetail.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 사용자 정보 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  // Ext Tree Store 정의 - 권한
  var userAuthMngStore = Ext.create("Ext.data.TreeStore", {
    root: {
      id: "",
      text: "root",
      leaf: false,
      expanded: true
    },
    nodeParam: "authCd",
    proxy: {
      type: "ajax",
      url: "/sys/usermng/userAuthMngSearch.do",
      reader: {
        type: "json",
        root: "userAuthMngList"
      },
    },
    listeners: {
      beforeload: function (store, operation, eOpts) {
        var form = $('form[name="userMngDetailForm"]');
        operation.params.userId = $(form).find('input[name="userId"]').val();
        operation.params.authCd = operation.node.get("authCd");
      }
    }
  });

  Ext.onReady(function () {
    // Ext Tree 정의 - 권한
    var userAuthMngTree = Ext.create("Ext.tree.Panel", {
      rootVisible: false,
      multiSelect: true,
      store: userAuthMngStore,
      height: comPopTreeHeight,
      renderTo: "userAuthMngTree",
      viewConfig: {
        stripeRows: true
      }
    });

    mainTree = userAuthMngTree;
  });

  // 등록
  function insertUserMng() {
    var form = $('form[name="userMngDetailForm"]');
    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm("등록 하시겠습니까?")) {
        $(form).find('select[name="grpCd"]').removeAttr("disabled");
        $(form).find('input[name="authCd"]').val(getAuthCd());
        $.ajax({
          type: "post",
          url: "/sys/usermng/userMngInsert.do",
          data: $(form).serialize(),
          success: function (res) {
            if (res.dataStatus === "SUCCESS") {
              alert("등록 되었습니다.");
              $.util.closeDialog();
              userMngStore.reload();
            } else if (res.dataStatus === "DUPLICATE") {
              alert("중복된 아이디입니다.");
            } else {
              alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
            }
          }
        });
      }
    }
  }

  // 수정
  function updateUserMng() {
    var form = $('form[name="userMngDetailForm"]');
    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm("수정 하시겠습니까?")) {
        $(form).find('select[name="grpCd"]').removeAttr("disabled");
        $(form).find('input[name="authCd"]').val(getAuthCd());
        $.ajax({
          type: "put",
          url: "/sys/usermng/userMngUpdate.do",
          data: $(form).serialize(),
          success: function (res) {
            if (res.dataStatus === "SUCCESS") {
              alert("수정 되었습니다.");
              $.util.closeDialog();
              userMngStore.reload();
            } else {
              alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
            }
          }
        });
      }
    }
  }

  // 삭제
  function deleteUserMng() {
    var form = $('form[name="userMngDetailForm"]');

    if (confirm("삭제 하시겠습니까?")) {
      $(form).find('select[name="grpCd"]').removeAttr("disabled");
      $.ajax({
        type: "delete",
        url: "/sys/usermng/userMngDelete.do",
        data: $(form).serialize(),
        success: function (res) {
          if (res.dataStatus === "SUCCESS") {
            alert("삭제 되었습니다.");
            $.util.closeDialog();
            userMngStore.reload();
          } else {
            alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
          }
        }
      });
    }
  }

  // 권한 조회
  function getAuthCd() {
    var userAuthItems = mainTree.view.getChecked();
    var authCd = "";
    $(userAuthItems).each(function (i) {
      authCd += userAuthItems[i].data.id + ",";
    });
    return authCd;
  }
</script>

<form:form modelAttribute="detailDTO" name="userMngDetailForm" method="post">
    <form:hidden path="detailStatus"/>
    <form:hidden path="authCd"/>
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
                <form:select path="grpCd" cssClass="form-control input-sm width_66 required"
                             disabled="${detailDTO.detailStatus eq 'UPDATE' ? 'true' : 'false'}">
                    <form:option value="" label="선택"/>
                    <c:forEach var="grpVO" items="${grpList}" varStatus="status">
                        <form:option value="${grpVO.grpCd}" label="${grpVO.grpNm}"/>
                    </c:forEach>
                </form:select>
            </td>
            <th class="top-line" rowspan="5"><span class="star-unmark">권한</span></th>
            <td class="top-line padding_none" rowspan="5">
                <div id="userAuthMngTree" class=""></div>
            </td>
        </tr>
        <tr>
            <th><span class="star-mark">아이디</span></th>
            <td><form:input path="userId"
                            cssClass="form-control input-sm width_66 ${detailDTO.detailStatus eq 'INSERT' ? 'idDupCheck' : 'required'}"
                            readonly="${detailDTO.detailStatus eq 'UPDATE' ? 'true' : 'false'}"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">성명</span></th>
            <td><form:input path="userNm" cssClass="form-control input-sm width_66 required"/></td>
        </tr>
        <tr>
            <th><span class="${detailDTO.detailStatus eq 'INSERT' ? 'star-mark' : 'star-unmark'}">비밀번호</span></th>
            <td><form:password path="userPw"
                               cssClass="form-control input-sm width_66 ${detailDTO.detailStatus eq 'INSERT' ? 'pwCheck' : 'pwCheck'}"/></td>
        </tr>
        <tr>
            <th><span class="star-unmark">비밀번호 확인</span></th>
            <td><form:password path="userPwDup" cssClass="form-control input-sm width_66 pwEqualTo"/></td>
        </tr>
        <tr>
            <th><span class="star-unmark">휴대폰 번호</span></th>
            <td><form:input path="mobileNum" cssClass="form-control input-sm width_66 mobileNum"/></td>
            <th><span class="star-unmark">이메일</span></th>
            <td><form:input path="email" cssClass="form-control input-sm width_95 email"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">사용자 유형</span></th>
            <td>
                <form:select path="userType" cssClass="form-control input-sm width_66 required">
                    <form:option value="" label="선택"/>
                    <c:forEach var="userTypeVO" items="${userTypeList}" varStatus="status">
                        <form:option value="${userTypeVO.comCd}" label="${userTypeVO.comNm}"/>
                    </c:forEach>
                </form:select>
            </td>
            <th><span class="star-mark">사용 여부</span></th>
            <td>
                <div class="radio-box">
                    <form:radiobutton path="useYn" cssClass="required" value="Y" label="사용"/>
                    <form:radiobutton path="useYn" cssClass="required" value="N" label="미사용"/>
                </div>
            </td>
        </tr>
        <tr>
            <th class="bottom-line"><span class="star-mark">등록 일시</span></th>
            <td class="bottom-line">
                <form:input path="regDate" cssClass="form-control input-sm width_66" readonly="true"/>
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
        <c:if test="${detailDTO.detailStatus eq 'INSERT'}">
            <button type="button" onclick="insertUserMng();" class="btn btn-red">
                <i class="fa fa-pencil-square-o"></i>등록
            </button>
        </c:if>
        <c:if test="${detailDTO.detailStatus eq 'UPDATE'}">
            <button type="button" onclick="updateUserMng();" class="btn btn-red">
                <i class="fa fa-floppy-o"></i>수정
            </button>
            <button type="button" onclick="deleteUserMng();" class="btn btn-red">
                <i class="fa fa-trash"></i>삭제
            </button>
        </c:if>
    </div>
</form:form>
