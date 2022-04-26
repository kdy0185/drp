<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : menuAuthMngPop.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 권한 설정 팝업
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
      }
    },
    listeners: {
      beforeload: function (store, operation, eOpts) {
        var form = $('form[name="menuAuthMngForm"]');
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

  // 권한 적용
  function selectAuth() {
    var form = $('form[name="menuAuthMngForm"]');

    if (confirm("선택한 권한을 적용 하시겠습니까?")) {
      $(form).find('input[name="authCd"]').val(getAuthCd());
      $.ajax({
        type: "put",
        url: "/sys/menumng/menuAuthMngUpdate.do",
        data: $(form).serialize(),
        success: function (res) {
          if (res.dataStatus === "SUCCESS") {
            alert("적용 되었습니다.");
            $.util.closeSubDialog();
          } else {
            alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
          }
        }
      });
    }
  }

  // 권한 조회
  function getAuthCd() {
    var menuAuthItems = subTree.view.getChecked();
    var authCd = "";
    $(menuAuthItems).each(function (i) {
      authCd += menuAuthItems[i].data.id + ",";
    });
    return authCd;
  }
</script>

<form:form modelAttribute="detailDTO" name="menuAuthMngForm" method="post">
    <form:hidden path="menuCd"/>
    <form:hidden path="authCd"/>
    <div class="container-fluid">
        <div class="row">
            <ul class="nav nav-pills">
                <li class="text-left">
                    <p class="contents-title"><i class="fa fa-arrow-circle-right"></i>메뉴별 권한 설정
                </li>
            </ul>
            <div id="menuAuthMngTree"></div>
            <div class="btn-center-area">
                <button type="button" onclick="selectAuth();" class="btn btn-red">
                    <i class="fa fa-floppy-o"></i>적용
                </button>
            </div>
        </div>
    </div>
</form:form>
