<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : userGrpMngList.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 그룹 관리
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
        searchUserGrpMng();
      });

      // Ext Grid Model 정의 - 그룹
      Ext.define("userGrpMngModel", {
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
          name: "grpDesc", // 그룹 설명
          type: "String",
          useNull: true
        }, {
          name: "regUser", // 등록자
          type: "String",
          useNull: true
        }, {
          name: "regDate", // 등록 일시
          type: "String",
          useNull: true
        }, {
          name: "modUser", // 수정자
          type: "String",
          useNull: true
        }, {
          name: "modDate", // 수정 일시
          type: "String",
          useNull: true
        }]
      });

      // Ext Grid Store 정의 - 그룹
      var userGrpMngStore = Ext.create("Ext.data.Store", {
        pageSize: defaultPageSize,
        autoLoad: false,
        model: "userGrpMngModel",
        proxy: {
          type: "ajax",
          url: "/sys/usermng/userGrpMngSearch.do",
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
            var textPoint = $("#userGrpMngGrid").prev().find(".sub-title-info");
            textPoint.empty();
            var em = '<em class="sub-title-info-em">' + dataStore.pageSize + '</em>건 / 전체 '
                + dataStore.totalCount + '건';
            textPoint.append(em);
          },
          scope: this
        }
      });

      // 페이지 이동 시 파라미터 재생성 - 그룹
      userGrpMngStore.on("beforeload", function (store, operation) {
        var searchArea = $("div.search-area");
        $.util.setSearchStyle(searchArea);

        var pageSize = $('select[name="pageSize"] option:selected').val();
        var grpNm = $('input[name="grpNm"]').val();
        store.pageSize = pageSize;
        operation.params = {
          pageNo: store.currentPage - 1,
          pageSize: pageSize,
          grpNm: grpNm
        };
      }, mainGrid);

      Ext.onReady(function () {
        Ext.QuickTips.init();

        // Ext Grid 정의 - 그룹
        var userGrpMngGrid = Ext.create("Ext.grid.Panel", {
          store: userGrpMngStore,
          loadMask: true,
          plugins: "bufferedrenderer",
          callbackKey: "callback",
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
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "grpCd",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "그룹명",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "grpNm",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "그룹 설명",
            width: 90,
            style: "text-align:center",
            sortable: true,
            dataIndex: "grpDesc",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "등록자",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "regUser",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "등록 일시",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "regDate",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "수정자",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "modUser",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "수정 일시",
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
          renderTo: "userGrpMngGrid",
          viewConfig: {
            stripeRows: true,
            enableTextSelection: true,
            listeners: {
              cellclick: function (grid, htmlElement, columnIndex, dataRecord) {
                var form = $('form[name="userGrpMngForm"]');
                $(form).find('input[name="grpCd"]').val(dataRecord.data.grpCd);
              },
              celldblclick: function (grid, htmlElement, columnIndex, dataRecord) {
                var form = $('form[name="userGrpMngForm"]');
                $(form).find('input[name="grpCd"]').val(dataRecord.data.grpCd);
                readUserGrpMng('UPDATE');
              }
            }
          },
          dockedItems: [{
            xtype: "pagingtoolbar",
            store: userGrpMngStore,
            dock: "bottom",
            displayInfo: true
          }],
          enableKeyEvents: true
        });
        mainGrid = userGrpMngGrid;
      });

      // 조회
      function searchUserGrpMng() {
        userGrpMngStore.loadPage(1);
        $.util.setSearchCondition();
      }

      // 상세
      function readUserGrpMng(detailStatus) {
        var form = $('form[name="userGrpMngForm"]');
        var grpCd = detailStatus === "UPDATE" ? $(form).find('input[name="grpCd"]').val() : "";
        if (detailStatus === "UPDATE" && grpCd === "") {
          alert("그룹을 선택하세요.");
        } else {
          var title = detailStatus === "INSERT" ? "그룹 등록" : "그룹 정보";
          var width = 750;
          $.ajax({
            type: "post",
            url: "/sys/usermng/userGrpMngDetail.do",
            data: {
              grpCd: grpCd,
              detailStatus: detailStatus
            },
            success: function (data, textStatus) {
              $("#popLayout").html(data);
              $.util.openDialog(title, width);
            }
          });
        }
      }

      // 그룹 엑셀
      function excelUserGrpMng() {
        var form = $('form[name="userGrpMngSearchForm"]');
        $(form).attr({
          action: "/sys/usermng/userGrpMngExcel.do"
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
                <span>${comsMenuDTO.menuNm}</span><em class="pull-right">${comsMenuDTO.upperMenuNm} &gt; ${comsMenuDTO.menuNm}</em>
            </div>

            <form:form modelAttribute="searchDTO" name="userGrpMngSearchForm" method="post">
                <div class="contents-box search-area margin_none">
                    <div class="row">
                        <div class="col-md-8 col-sm-12 col-xs-12 padding_l25">
                            <div class="col-md-1 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">그룹명</span>
                            </div>
                            <div class="col-md-11 col-sm-12 col-xs-12 padding_none">
                                <form:input path="grpNm" cssClass="form-control input-sm"
                                            onkeydown="if(event.keyCode==13){searchUserGrpMng(); return false;}"/>
                            </div>
                        </div>
                        <div class="col-md-offset-3 col-md-1 col-sm-12 col-xs-12">
                            <div class="search-btn-area">
                                <button type="button" onclick="searchUserGrpMng();" class="btn btn-gray">
                                    <i class="fa fa-search"></i>조회
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>

            <form:form modelAttribute="searchDTO" name="userGrpMngForm" method="post">
                <form:hidden path="grpCd"/>
                <div class="contents-box grid-area outline_none">
                    <div class="grid-box">
                        <ul class="nav nav-pills">
                            <li class="text-left">
                                <p class="contents-title">
                                    <i class="fa fa-arrow-circle-right"></i>그룹 목록
                                    <span class="sub-title-info"></span>
                                </p>
                            </li>
                            <li class="pull-right">
                                <div class="text-right">
                                    <div class="grid-option-box">
                                        <button type="button" onclick="excelUserGrpMng();" class="btn btn-green">
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
                                                     onchange="userGrpMngStore.loadPage(1);">
                                            <c:forEach var="pageVO" items="${pageList}" varStatus="status">
                                                <form:option value="${pageVO.comCd}" label="${pageVO.comNm}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div id="userGrpMngGrid"></div>
                        <div class="btn-right-area">
                            <button type="button" onclick="readUserGrpMng('INSERT');" class="btn btn-red">
                                <i class="fa fa-pencil-square-o"></i>등록
                            </button>
                            <button type="button" onclick="readUserGrpMng('UPDATE');" class="btn btn-red">
                                <i class="fa fa-file-text-o"></i>상세
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
