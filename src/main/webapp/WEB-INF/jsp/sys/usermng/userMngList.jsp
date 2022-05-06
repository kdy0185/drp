<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : userMngList.jsp
     * @Author : KDW
     * @Date : 2022-01-25
     * @Description : 사용자 관리
     */
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="author" content="KDW">
    <%@ include file="/WEB-INF/jsp/cmmn/include/taglib.jsp" %>
    <title><spring:message code="browser.title"/></title>

    <!-- ExtJS -->
    <link rel="stylesheet" href="/extjs/resources/css/ext-all.css" type="text/css"/>
    <script type="text/javascript" src="/extjs/ext-all.js"></script>

    <script type="text/javascript">
      $(function () {
        $.util.getSearchCondition();
        searchUserMng();
      });

      // Ext Grid Model 정의 - 사용자
      Ext.define("userMngModel", {
        extend: "Ext.data.Model",
        fields: [{
          name: "rn", // 순번
          type: "int",
          useNull: true
        }, {
          name: "grpCd", // 그룹 코드
          type: "String",
          useNull: true
        }, {
          name: "grpNm", // 그룹명
          type: "String",
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
          name: "mobileNum", // 휴대폰 번호
          type: "String",
          useNull: true
        }, {
          name: "email", // 이메일
          type: "String",
          useNull: true
        }, {
          name: "userType", // 사용자 유형
          type: "String",
          useNull: true
        }, {
          name: "useYn", // 사용 여부
          type: "String",
          useNull: true
        }, {
          name: "regDate", // 등록 일자
          type: "String",
          useNull: true
        }, {
          name: "modDate", // 수정 일자
          type: "String",
          useNull: true
        }]
      });

      // Ext Grid Store 정의 - 사용자
      var userMngStore = Ext.create("Ext.data.Store", {
        pageSize: defaultPageSize,
        autoLoad: false,
        model: "userMngModel",
        proxy: {
          type: "ajax",
          url: "/sys/usermng/userMngSearch.do",
          reader: {
            type: "json",
            root: "content",
            totalProperty: "totalElements"
          },
          simpleSortMode: true
        },
        listeners: {
          load: function (dataStore) {
            if (dataStore.pageSize
                > dataStore.totalCount) dataStore.pageSize = dataStore.totalCount;
            var textPoint = $("#userMngGrid").prev().find(".sub-title-info");
            textPoint.empty();
            var em = '<em class="sub-title-info-em">' + dataStore.pageSize + '</em>건 / 전체 '
                + dataStore.totalCount + '건';
            textPoint.append(em);
          },
          scope: this
        }
      });

      // 페이지 이동 시 파라미터 재생성 - 사용자
      userMngStore.on("beforeload", function (store, operation) {
        var searchArea = $("div.search-area");
        $.util.setSearchStyle(searchArea);

        var pageSize = $('select[name="pageSize"] option:selected').val();
        var grpCd = $('select[name="grpCd"] option:selected').val();
        var searchCd = $('select[name="searchCd"] option:selected').val();
        var searchWord = $('input[name="searchWord"]').val();
        var useYn = $('select[name="useYn"] option:selected').val();
        store.pageSize = pageSize;
        operation.params = {
          pageNo: store.currentPage - 1,
          pageSize: pageSize,
          grpCd: grpCd,
          searchCd: searchCd,
          searchWord: searchWord,
          useYn: useYn
        };
      }, mainGrid);

      Ext.onReady(function () {
        Ext.QuickTips.init();

        // Ext Grid 정의 - 사용자
        var userMngGrid = Ext.create("Ext.grid.Panel", {
          store: userMngStore,
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
            text: "그룹 코드",
            width: 10,
            align: "center",
            sortable: true,
            hidden: true,
            dataIndex: "grpCd",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "그룹명",
            width: 10,
            align: "center",
            sortable: true,
            hidden: true,
            dataIndex: "grpNm",
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
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "userNm",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "휴대폰 번호",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "mobileNum",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "이메일",
            width: 60,
            style: "text-align:center",
            sortable: true,
            dataIndex: "email",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "사용자 유형",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "userType",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "사용 여부",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "useYn",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "등록 일자",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "regDate",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "수정 일자",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "modDate",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }],
          height: comHeight,
          autoWidth: true,
          renderTo: "userMngGrid",
          viewConfig: {
            stripeRows: true,
            enableTextSelection: true,
            listeners: {
              cellclick: function (grid, htmlElement, columnIndex, dataRecord) {
                var form = $('form[name="userMngForm"]');
                $(form).find('input[name="userId"]').val(dataRecord.data.userId);
              },
              celldblclick: function (grid, htmlElement, columnIndex, dataRecord) {
                var form = $('form[name="userMngForm"]');
                $(form).find('input[name="userId"]').val(dataRecord.data.userId);
                readUserMng('U');
              }
            }
          },
          dockedItems: [{
            xtype: "pagingtoolbar",
            store: userMngStore,
            dock: "bottom",
            displayInfo: true
          }],
          enableKeyEvents: true
        });

        mainGrid = userMngGrid;
      });

      // 조회
      function searchUserMng() {
        userMngStore.loadPage(1);
        $.util.setSearchCondition();
      }

      // 상세
      function readUserMng(state) {
        var form = $('form[name="userMngForm"]');
        var userMngCnt = mainGrid.getSelectionModel().getCount();
        var userId = state === "U" ? $(form).find('input[name="userId"]').val() : "";
        if (state === "U" && userMngCnt === 0) {
          alert("사용자를 선택하세요.");
        } else if (state === "U" && userMngCnt > 1) {
          alert("1명의 사용자만 선택하세요.");
        } else {
          var title = state === "I" ? "사용자 등록" : "사용자 정보";
          var width = 750;
          $.ajax({
            type: "post",
            url: "/sys/usermng/userMngDetail.do",
            data: {
              userId: userId,
              state: state
            },
            success: function (data, textStatus) {
              $("#popLayout").html(data);
              $.util.openDialog(title, width);
            }
          });
        }
      }

      // 권한 설정 팝업
      function openAuthPop() {
        var form = $('form[name="userMngForm"]');
        var userMngCnt = mainGrid.getSelectionModel().getCount();
        var userMngItems = mainGrid.getSelectionModel().getSelection();
        var userId = "";
        $(userMngItems).each(function (i) {
          userId += userMngItems[i].data.userId + ",";
        });

        if (userMngCnt === 0) {
          alert("사용자를 선택하세요.");
        } else {
          var title = "권한 설정";
          var width = 360;
          $.ajax({
            type: "post",
            url: "/sys/usermng/userAuthMngPop.do",
            data: {
              userId: userId
            },
            success: function (data, textStatus) {
              $("#popLayout").html(data);
              $.util.openDialog(title, width);
            }
          });
        }
      }

      // 사용자 엑셀
      function excelUserMng() {
        var form = $('form[name="userMngSearchForm"]');
        $(form).attr({
          action: "/sys/usermng/userMngExcel.do"
        }).submit();
      }
    </script>
</head>
<body>
<%@ include file="/WEB-INF/jsp/cmmn/layout/header.jsp" %>
<div id="contents" class="container-fluid">
    <div class="row bgdark">
        <%@ include file="/WEB-INF/jsp/cmmn/layout/left.jsp" %>
        <div class="contents-area col-md-10 col-sm-10 col-xs-12">
            <div class="sc-title">
                <span>${comsMenuVO.menuNm}</span><em class="pull-right">${comsMenuVO.upperMenuNm} &gt; ${comsMenuVO.menuNm}</em>
            </div>

            <form:form modelAttribute="searchDTO" name="userMngSearchForm" method="post">
                <div class="contents-box search-area margin_none">
                    <div class="row">
                        <div class="col-md-2 col-sm-12 col-xs-12 padding_l25">
                            <div class="col-md-3 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">그룹</span>
                            </div>
                            <div class="col-md-9 col-sm-12 col-xs-12 padding_none">
                                <form:select path="grpCd" cssClass="form-control input-sm">
                                    <form:option value="" label="전체"/>
                                    <c:forEach var="grpVO" items="${grpList}" varStatus="status">
                                        <form:option value="${grpVO.grpCd}" label="${grpVO.grpNm}"/>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                        <div class="col-md-offset-1 col-md-4 col-sm-12 col-xs-12">
                            <div class="col-md-2 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">사용자</span>
                            </div>
                            <div class="col-md-3 col-sm-12 col-xs-12 padding_none">
                                <form:select path="searchCd" cssClass="form-control input-sm pull-left">
                                    <form:option value="userId" label="아이디"/>
                                    <form:option value="userNm" label="성명"/>
                                </form:select>
                            </div>
                            <div class="col-md-6 col-sm-12 col-xs-12 padding_r0">
                                <form:input path="searchWord" cssClass="form-control input-sm"
                                            onkeydown="if(event.keyCode==13){searchUserMng(); return false;}"/>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-4 col-xs-12 padding_l25">
                            <div class="col-md-3 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">사용 여부</span>
                            </div>
                            <div class="col-md-4 col-sm-12 col-xs-12 padding_none">
                                <form:select path="useYn" cssClass="form-control input-sm">
                                    <form:option value="" label="전체"/>
                                    <form:option value="Y" label="사용"/>
                                    <form:option value="N" label="미사용"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="col-md-offset-1 col-md-1 col-sm-12 col-xs-12 askbtn">
                            <div class="search-btn-area text_r">
                                <button type="button" onclick="searchUserMng();" class="btn btn-gray margin_none">
                                    <i class="fa fa-search"></i>조회
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>

            <form:form modelAttribute="searchDTO" name="userMngForm" method="post">
                <form:hidden path="userId"/>
                <div class="contents-box grid-area outline_none">
                    <div class="grid-box">
                        <ul class="nav nav-pills">
                            <li class="text-left">
                                <p class="contents-title">
                                    <i class="fa fa-arrow-circle-right"></i>사용자 목록
                                    <span class="sub-title-info"></span>
                                </p>
                            </li>
                            <li class="pull-right">
                                <div class="text-right">
                                    <div class="grid-option-box">
                                        <button type="button" onclick="excelUserMng();" class="btn btn-green">
                                            <i class="fa fa-file-excel-o" aria-hidden="true"></i><span>EXCEL</span>
                                        </button>
                                        <button type="button" onclick="$.util.screenSlide();"
                                                class="btn btn-green height-slider">
                                            <i class="fa fa-chevron-up"></i>
                                        </button>
                                        <button type="button" onclick="$.util.screenSlide();"
                                                class="btn btn-green height-slider" style="display: none;">
                                            <i class="fa fa-chevron-down"></i>
                                        </button>
                                        <form:select path="pageSize"
                                                     cssClass="form-control input-sm pull-right"
                                                     onchange="userMngStore.loadPage(1);">
                                            <c:forEach var="pageVO" items="${pageList}" varStatus="status">
                                                <form:option value="${pageVO.comCd}" label="${pageVO.comNm}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div id="userMngGrid"></div>
                        <div class="btn-right-area">
                            <button type="button" onclick="readUserMng('I');" class="btn btn-red">
                                <i class="fa fa-pencil-square-o"></i>등록
                            </button>
                            <button type="button" onclick="readUserMng('U');" class="btn btn-red">
                                <i class="fa fa-file-text-o"></i>상세
                            </button>
                            <button type="button" onclick="openAuthPop();" class="btn btn-red">
                                <i class="fa fa-cogs"></i>권한 설정
                            </button>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/cmmn/layout/footer.jsp" %>
</body>
</html>
