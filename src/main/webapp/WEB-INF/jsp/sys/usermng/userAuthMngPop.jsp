<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : userAuthMngPop.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 권한 설정 팝업
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
      }
    },
    listeners: {
      beforeload: function (store, operation, eOpts) {
        var form = $('form[name="userAuthMngForm"]');
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

  // 권한 적용
  function selectAuth() {
    var form = $('form[name="userAuthMngForm"]');
    var userAuthItems = mainTree.view.getChecked();
    var authCd = "";
    $(userAuthItems).each(function (i) {
      authCd += userAuthItems[i].data.id + ",";
    });

    if (confirm("선택한 권한을 적용 하시겠습니까?")) {
      $(form).find('input[name="authCd"]').val(authCd);
      $.ajax({
        type: "post",
        url: "/sys/usermng/userAuthMngUpdate.do",
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

<form:form modelAttribute="userMngVO" name="userAuthMngForm" method="post">
    <form:hidden path="userId"/>
    <form:hidden path="authCd"/>
    <div class="container-fluid">
        <div class="row">
            <ul class="nav nav-pills">
                <li class="text-left">
                    <p class="contents-title"><i class="fa fa-arrow-circle-right"></i>사용자별 권한 설정
                </li>
            </ul>
            <div id="userAuthMngTree"></div>
            <div class="btn-center-area">
                <button type="button" onclick="selectAuth();" class="btn btn-red">
                    <i class="fa fa-floppy-o"></i>적용
                </button>
            </div>
        </div>
    </div>
</form:form>
