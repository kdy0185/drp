<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : authMenuMngPop.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 메뉴 설정 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  // Ext Tree Store 정의 - 메뉴
  var authMenuMngStore = Ext.create("Ext.data.TreeStore", {
    root: {
      id: "",
      text: "root",
      leaf: false,
      expanded: true
    },
    nodeParam: "menuCd",
    proxy: {
      type: "ajax",
      url: "/sys/authmng/authMenuMngSearch.do",
      reader: {
        type: "json",
        root: "authMenuMngList"
      }
    },
    listeners: {
      beforeload: function (store, operation, eOpts) {
        var form = $('form[name="authMenuMngForm"]');
        operation.params.authCd = $(form).find('input[name="authCd"]').val();
        operation.params.menuCd = operation.node.get("menuCd");
      }
    }
  });

  Ext.onReady(function () {
    // Ext Tree 정의 - 메뉴
    var authMenuMngTree = Ext.create("Ext.tree.Panel", {
      rootVisible: false,
      multiSelect: true,
      store: authMenuMngStore,
      height: comPopTreeHeight,
      renderTo: "authMenuMngTree",
      viewConfig: {
        stripeRows: true
      }
    });

    subTree = authMenuMngTree;
  });

  // 메뉴 적용
  function selectMenu() {
    var form = $('form[name="authMenuMngForm"]');
    var authMenuItems = subTree.view.getChecked();
    var menuCd = "";
    $(authMenuItems).each(function (i) {
      menuCd += authMenuItems[i].data.id + ",";
    });

    if (confirm("선택한 메뉴로의 접근 권한을 허용 하시겠습니까?")) {
      $(form).find('input[name="menuCd"]').val(menuCd);
      $.ajax({
        type: "post",
        url: "/sys/authmng/authMenuMngUpdate.do",
        data: $(form).serialize(),
        success: function (code) {
          if (code === "S") {
            alert("적용 되었습니다.");
            $.util.closeSubDialog();
          } else if (code === "N") {
            alert("수정된 데이터가 없습니다.");
          } else {
            alert("오류가 발생하였습니다.\ncode : " + code);
          }
        }
      });
    }
  }
</script>

<form:form modelAttribute="authMngVO" name="authMenuMngForm" method="post">
    <form:hidden path="authCd"/>
    <form:hidden path="menuCd"/>
    <div class="container-fluid">
        <div class="row">
            <ul class="nav nav-pills">
                <li class="text-left">
                    <p class="contents-title"><i class="fa fa-arrow-circle-right"></i>권한별 메뉴 설정
                </li>
            </ul>
            <div id="authMenuMngTree"></div>
            <div class="btn-center-area">
                <button type="button" onclick="selectMenu();" class="btn btn-red">
                    <i class="fa fa-floppy-o"></i>적용
                </button>
            </div>
        </div>
    </div>
</form:form>
