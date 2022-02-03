<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : menuMngDetail.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 메뉴 정보 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  // Ext Tree Store 정의 - 권한
  var menuAuthMngStore = Ext.create("Ext.data.TreeStore", {
    root: {
      id: "",
      text: "root",
      leaf: false,
      expanded: true
    },
    nodeParam: "authCd",
    proxy: {
      type: "ajax",
      url: "/sys/menumng/menuAuthMngSearch.do",
      reader: {
        type: "json",
        root: "menuAuthMngList"
      },
    },
    listeners: {
      beforeload: function (store, operation, eOpts) {
        var form = $('form[name="menuMngDetailForm"]');
        operation.params.menuCd = $(form).find('input[name="menuCd"]').val();
        operation.params.authCd = operation.node.get("authCd");
      }
    }
  });

  Ext.onReady(function () {
    // Ext Tree 정의 - 권한
    var menuAuthMngTree = Ext.create("Ext.tree.Panel", {
      rootVisible: false,
      multiSelect: true,
      store: menuAuthMngStore,
      height: comPopTreeHeight,
      renderTo: "menuAuthMngTree",
      viewConfig: {
        stripeRows: true
      }
    });

    subTree = menuAuthMngTree;
  });

  $(function () {
    $.util.setScrollbar();
  });

  // 로그인 메뉴 권한 제어 차단
  // function fixLoginAuth() {
  //   var form = $('form[name="menuMngDetailForm"]');
  //   var menuUrl = $(form).find('input[name="menuUrl"]').val();
  //   if (menuUrl === "/main/login/login.do") {
  //     $(form).find('input:checkbox[name="authCd"]').each(function (i) {
  //       $(this).attr("disabled", true);
  //     });
  //   }
  // }

  // 메뉴 정보 자동 추출
  function getMenuInfo() {
    var menuReg = /^[A-Z]\d{4}$/;
    var form = $('form[name="menuMngDetailForm"]');
    var menuCd = $(form).find('input[name="menuCd"]').val();
    if (menuReg.test(menuCd)) {
      var cd1 = menuCd.substr(0, 1);
      var cd2 = menuCd.substr(1, 2);
      var cd3 = menuCd.substr(3, 2);
      var menuLv = Number(cd3) === 0 ? (Number(cd2) === 0 ? 1 : 2) : 3;
      var upperMenuCd = cd3 === "00" ? (cd2 === "00" ? "" : cd1 + "0000") : cd1 + cd2 + "00";
      $(form).find('input[name="menuLv"]').val(menuLv);
      $(form).find('input[name="upperMenuCd"]').val(upperMenuCd);
    } else {
      $(form).find('input[name="upperMenuCd"]').val("");
    }
  }

  // 수정
  function updateMenuMng() {
    var form = $('form[name="menuMngDetailForm"]');
    var state = $(form).find('input[name="state"]').val();
    var msg = state === "I" ? "등록" : "수정";
    var menuAuthItems = subTree.view.getChecked();
    var authCd = "";
    $(menuAuthItems).each(function (i) {
      authCd += menuAuthItems[i].data.id + ",";
    });

    $(form).validateForm();

    if ($(form).valid()) {
      if (confirm(msg + " 하시겠습니까?")) {
        $(form).find('input[name="authCd"]').val(authCd);
        $.ajax({
          type: "post",
          url: "/sys/menumng/menuMngUpdate.do",
          data: $(form).serialize(),
          success: function (code) {
            if (code === "S") {
              alert(msg + " 되었습니다.");
              $.util.closeDialog();
              moveList();
            } else if (code === "N") {
              alert(msg + "된 데이터가 없습니다.");
            } else {
              alert("오류가 발생하였습니다.\ncode : " + code);
            }
          }
        });
      }
    }
  }

  // 삭제
  function deleteMenuMng() {
    var form = $('form[name="menuMngDetailForm"]');
    if (confirm("삭제 하시겠습니까?")) {
      $.ajax({
        type: "post",
        url: "/sys/menumng/menuMngDelete.do",
        data: $(form).serialize(),
        success: function (code) {
          if (code === "S") {
            alert("삭제 되었습니다.");
            $.util.closeDialog();
            moveList();
          } else if (code === "N") {
            alert("삭제된 데이터가 없습니다.");
          } else {
            alert("오류가 발생하였습니다.\ncode : " + code);
          }
        }
      });
    }
  }
</script>
<form:form modelAttribute="menuMngVO" name="menuMngDetailForm" method="post">
    <form:hidden path="state"/>
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
            <th class="top-line"><span class="star-mark">메뉴 코드</span></th>
            <td class="top-line">
                <form:input path="menuCd" cssClass="form-control input-sm width_42 menuCdReq"
                            readonly="${menuMngVO.state eq 'U' ? 'true' : 'false'}" onblur="getMenuInfo();"/>
            </td>
            <th class="top-line" rowspan="5"><span class="star-unmark">권한</span></th>
            <td class="top-line padding_none" rowspan="5">
                <div id="menuAuthMngTree"></div>
            </td>
        </tr>
        <tr>
            <th><span class="star-unmark">상위 메뉴 코드</span></th>
            <td><form:input path="upperMenuCd" cssClass="form-control input-sm width_42" readonly="true"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">메뉴 수준</span></th>
            <td><form:input path="menuLv" cssClass="form-control input-sm width_42 digitsReq" readonly="true"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">메뉴 순서</span></th>
            <td><form:input path="menuOrd" cssClass="form-control input-sm width_42 digitsReq"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">사용 여부</span></th>
            <td>
                <div class="radio-box">
                    <form:radiobutton path="useYn" cssClass="required" value="Y" label="사용"/>
                    <form:radiobutton path="useYn" cssClass="required" value="N" label="미사용"/>
                </div>
            </td>
        </tr>
        <tr>
            <th><span class="star-mark">메뉴명</span></th>
            <td><form:input path="menuNm" cssClass="form-control input-sm width_95 required"/></td>
            <th><span class="star-unmark">메뉴명(영문)</span></th>
            <td><form:input path="menuEngNm" cssClass="form-control input-sm width_95"/></td>
        </tr>
        <tr>
            <th><span class="star-unmark">이동 주소</span></th>
            <td colspan="3"><form:input path="menuUrl" cssClass="form-control input-sm width_982"/></td>
        </tr>
        <tr>
            <th class="bottom-line"><span class="star-unmark">메뉴 설명</span></th>
            <td class="bottom-line" colspan="3">
                <form:textarea path="menuDesc" rows="5" cols="40" cssClass="form-control input-sm width_981"/>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="val-check-area"></div>
    <div class="btn-center-area">
        <c:if test="${menuMngVO.state eq 'I'}">
            <button type="button" onclick="updateMenuMng();" class="btn btn-red">
                <i class="fa fa-pencil-square-o"></i>등록
            </button>
        </c:if>
        <c:if test="${menuMngVO.state eq 'U'}">
            <button type="button" onclick="updateMenuMng();" class="btn btn-red">
                <i class="fa fa-floppy-o"></i>수정
            </button>
            <button type="button" onclick="deleteMenuMng();" class="btn btn-red">
                <i class="fa fa-trash"></i>삭제
            </button>
        </c:if>
    </div>
</form:form>
