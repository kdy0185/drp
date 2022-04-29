<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : authUserMngPop.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 사용자 설정 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  // Ext Grid Model 정의 - 사용자
  Ext.define("authUserMngModel", {
    extend: "Ext.data.Model",
    fields: [{
      name: "rn", // 순번
      type: "int",
      useNull: true
    }, {
      name: "userId", // 아이디
      type: "String",
      useNull: true
    }, {
      name: "userNm", // 성명
      type: "String",
      useNull: true
    }, {
      name: "mobileNum", // 전화번호
      type: "String",
      useNull: true
    }, {
      name: "authYn", // 권한
      type: "String",
      useNull: true
    }]
  });

  // Ext Grid Store 정의 - 사용자
  var authUserMngStore = Ext.create("Ext.data.Store", {
    pageSize: defaultPageSize,
    autoLoad: false,
    model: "authUserMngModel",
    proxy: {
      type: "ajax",
      url: "/sys/authmng/authUserMngSearch.do",
      reader: {
        type: "json",
        root: "content",
        totalProperty: "totalElements"
      },
      simpleSortMode: true
    },
    listeners: {
      load: function (dataStore) {
        if (dataStore.pageSize > dataStore.totalCount) dataStore.pageSize = dataStore.totalCount;
        var textPoint = $("#authUserMngGrid").prev().find(".sub-title-info");
        textPoint.empty();
        var em = '<em class="sub-title-info-em">' + dataStore.pageSize + '</em>건 / 전체 '
            + dataStore.totalCount + '건';
        textPoint.append(em);
      },
      scope: this
    }
  });

  // 페이지 이동 시 파라미터 재생성 - 사용자
  authUserMngStore.on("beforeload", function (store, operation) {
    var form = $('form[name="authUserMngForm"]');
    var pageSize = $(form).find('select[name="pageSize"] option:selected').val();
    var authCd = $(form).find('input[name="authCd"]').val();
    var grpCd = $(form).find('select[name="grpCd"] option:selected').val();
    var searchCd = $(form).find('select[name="searchCd"] option:selected').val();
    var searchWord = $(form).find('input[name="searchWord"]').val();
    store.pageSize = pageSize;
    operation.params = {
      pageNo: store.currentPage - 1,
      pageSize: pageSize,
      authCd: authCd,
      grpCd: grpCd,
      searchCd: searchCd,
      searchWord: searchWord
    };
  }, popGrid);

  Ext.onReady(function () {
    Ext.QuickTips.init();

    // Ext Grid 정의 - 사용자
    var authUserMngGrid = Ext.create("Ext.grid.Panel", {
      store: authUserMngStore,
      loadMask: true,
      plugins: "bufferedrenderer",
      callbackKey: "callback",
      selType: "checkboxmodel",
      columnLines: true,
      forceFit: true,
      columns: [{
        text: "순번",
        width: 15,
        align: "center",
        sortable: true,
        dataIndex: "rn",
        renderer: function (val, metadata, record) {
          metadata.style = "cursor: pointer;";
          return val;
        }
      }, {
        text: "아이디",
        width: 30,
        align: "center",
        sortable: true,
        dataIndex: "userId",
        renderer: function (val, metadata, record) {
          metadata.style = "cursor: pointer;";
          return val;
        }
      }, {
        text: "성명",
        width: 20,
        align: "center",
        sortable: true,
        dataIndex: "userNm",
        renderer: function (val, metadata, record) {
          metadata.style = "cursor: pointer;";
          return val;
        }
      }, {
        text: "전화번호",
        width: 30,
        align: "center",
        sortable: true,
        dataIndex: "mobileNum",
        renderer: function (val, metadata, record) {
          metadata.style = "cursor: pointer;";
          return val;
        }
      }, {
        text: "권한",
        width: 20,
        align: "center",
        sortable: true,
        dataIndex: "authYn",
        renderer: function (val, metadata, record) {
          metadata.style = "cursor: pointer;";
          return val;
        }
      }],
      height: comPopHeight,
      autoWidth: true,
      renderTo: "authUserMngGrid",
      viewConfig: {
        stripeRows: true,
        enableTextSelection: true
      },
      dockedItems: [{
        xtype: "pagingtoolbar",
        store: authUserMngStore,
        dock: "bottom",
        displayInfo: true
      }],
      enableKeyEvents: true
    });
    popGrid = authUserMngGrid;

    searchUser();
  });

  // 조회
  function searchUser() {
    authUserMngStore.loadPage(1);
  }

  // 사용자 적용
  function selectUser(authYn) {
    var form = $('form[name="authUserMngForm"]');
    var authCd = $(form).find('input[name="authCd"]').val();
    var gbn = authYn === "Y" ? "허용" : "해제";
    var authUserCnt = popGrid.getSelectionModel().getCount();

    if (authUserCnt === 0) {
      alert("사용자를 선택하세요.");
    } else {
      if (confirm("선택한 사용자의 접근 권한을 " + gbn + " 하시겠습니까?")) {
        $.ajax({
          type: "put",
          url: "/sys/authmng/authUserMngUpdate.do",
          data: {
            authCd: authCd,
            userId: getUserId(),
            authYn: authYn
          },
          success: function (res) {
            if (res.dataStatus === "SUCCESS") {
              alert("적용 되었습니다.");
              searchUser();
            } else {
              alert("오류가 발생하였습니다.\ncode : " + res.dataStatus);
            }
          }
        });
      }
    }
  }

  // 사용자 조회
  function getUserId() {
    var authUserItems = popGrid.getSelectionModel().getSelection();
    var userId = "";
    $(authUserItems).each(function (i) {
      userId += authUserItems[i].data.userId + ",";
    });
    return userId;
  }
</script>
<form:form modelAttribute="searchDTO" name="authUserMngForm" method="post">
    <form:hidden path="authCd"/>

    <div class="contents-box search-area padding_l26 padding_r26">
        <div class="row">
            <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="form-group float_left width_70per">
                    <div class="col-md-4 col-sm-4 col-xs-4 padding_l0">
                        <span class="search-icon">그룹</span>
                        <form:select path="grpCd" cssClass="form-control input-sm width_70">
                            <form:option value="" label="전체"/>
                            <c:forEach var="grpVO" items="${grpList}" varStatus="status">
                                <form:option value="${grpVO.grpCd}" label="${grpVO.grpNm}"/>
                            </c:forEach>
                        </form:select>
                    </div>
                    <div class="col-md-6 col-sm-6 col-xs-6 padding_l0">
                        <span class="search-icon">구분</span>
                        <form:select path="searchCd"
                                     cssClass="form-control input-sm pull-left width_25">
                            <form:option value="userId" label="아이디"/>
                            <form:option value="userNm" label="성명"/>
                        </form:select>
                        <form:input path="searchWord" cssClass="form-control input-sm pull-left width_40"
                                    onkeydown="if(event.keyCode==13){searchUser(); return false;}"/>
                    </div>
                    <div class="col-md-2 col-sm-2 col-xs-2 padding_none">
                        <div class="search-btn-area text_r">
                            <button type="button" onclick="searchUser();" class="btn btn-blue margin_none">
                                <i class="fa fa-search"></i>조회
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="contents-box grid-area border_none margin_none">
        <div class="grid-box">
            <ul class="nav nav-pills">
                <li class="text-left">
                    <p class="contents-title"><i class="fa fa-arrow-circle-right"></i>권한별 사용자 목록<span class="sub-title-info"></span></p>
                </li>
                <li class="pull-right">
                    <div class="text-right">
                        <div class="grid-option-box">
                            <form:select path="pageSize" cssClass="form-control input-sm pull-right" onchange="searchUser();">
                                <c:forEach var="pageVO" items="${pageList}" varStatus="status">
                                    <form:option value="${pageVO.comCd}" label="${pageVO.comNm}"/>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </li>
            </ul>
            <div id="authUserMngGrid"></div>
            <div class="btn-center-area">
                <button type="button" onclick="selectUser('Y');" class="btn btn-red">
                    <i class="fa fa-floppy-o"></i>권한 허용
                </button>
                <button type="button" onclick="selectUser('N');" class="btn btn-red">
                    <i class="fa fa-floppy-o"></i>권한 해제
                </button>
            </div>
        </div>
    </div>
</form:form>
