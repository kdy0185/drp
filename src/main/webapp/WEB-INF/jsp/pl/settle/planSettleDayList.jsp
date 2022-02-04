<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : planSettleDayList.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 일일 결산
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
        if ("${planSettleVO.authAdmin}" == "Y") $.util.getSearchCondition();
        searchPlanSettleDay();
      });

      // Ext Grid Model 정의 - 일일 결산
      Ext.define("planSettleDayModel", {
        extend: "Ext.data.Model",
        fields: [{
          name: "rn", // 순번
          type: "int",
          useNull: true
        }, {
          name: "rtneDate", // 일자
          type: "String",
          useNull: true
        }, {
          name: "achvRate", // 달성률
          type: "String",
          useNull: true
        }, {
          name: "concRate", // 몰입도
          type: "String",
          useNull: true
        }, {
          name: "dailyScore", // 점수
          type: "float",
          useNull: true
        }, {
          name: "memo", // 메모
          type: "String",
          useNull: true
        }, {
          name: "planUser", // 담당자
          type: "String",
          useNull: true
        }]
      });

      // Ext Grid Store 정의 - 일일 결산
      var planSettleDayStore = Ext.create("Ext.data.Store", {
        pageSize: defaultPageSize,
        autoLoad: false,
        model: "planSettleDayModel",
        proxy: {
          type: "ajax",
          url: "/pl/settle/planSettleDaySearch.do",
          reader: {
            type: "json",
            root: "planSettleDayList",
            totalProperty: "cnt"
          },
          simpleSortMode: true
        },
        listeners: {
          load: function (dataStore) {
            if (dataStore.pageSize > dataStore.totalCount) dataStore.pageSize = dataStore.totalCount;
            var textPoint = $("#planSettleDayGrid").prev().find(".sub-title-info");
            textPoint.empty();
            var em = '<em class="sub-title-info-em">' + dataStore.pageSize + '</em>건 / 전체 '
                + dataStore.totalCount + '건';
            textPoint.append(em);
          },
          scope: this
        }
      });

      // 페이지 이동 시 파라미터 재생성 - 일일 결산
      planSettleDayStore.on("beforeload", function (store, operation) {
        var searchArea = $("div.search-area");
        $.util.setSearchStyle(searchArea);

        var pageSize = $('select[name="pageSize"] option:selected').val();
        var userId = $('input[name="userId"]').val();
        var rtneStartDate = $('input[name="rtneStartDate"]').val();
        var rtneEndDate = $('input[name="rtneEndDate"]').val();
        store.pageSize = pageSize;
        operation.params = {
          pageNo: store.currentPage,
          pageSize: pageSize,
          userId: userId,
          rtneStartDate: rtneStartDate,
          rtneEndDate: rtneEndDate
        };
      }, mainGrid);

      Ext.onReady(function () {
        Ext.QuickTips.init();

        // Ext Grid 정의 - 일일 결산
        var planSettleDayGrid = Ext.create("Ext.grid.Panel", {
          store: planSettleDayStore,
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
            text: "일자",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "rtneDate",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "달성률",
            width: 20,
            align: "center",
            sortable: true,
            dataIndex: "achvRate",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "몰입도",
            width: 20,
            align: "center",
            sortable: true,
            dataIndex: "concRate",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "점수",
            width: 20,
            align: "center",
            sortable: true,
            dataIndex: "dailyScore",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "메모",
            width: 60,
            style: "text-align:center",
            sortable: true,
            dataIndex: "memo",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "담당자",
            width: 10,
            align: "center",
            sortable: true,
            hidden: true,
            dataIndex: "planUser",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }],
          height: comHeight,
          autoWidth: true,
          renderTo: "planSettleDayGrid",
          viewConfig: {
            stripeRows: true,
            enableTextSelection: true,
            listeners: {
              cellclick: function (grid, htmlElement, columnIndex, dataRecord) {
                var form = $('form[name="planSettleDayForm"]');
                $(form).find('input[name="rtneDate"]').val(dataRecord.data.rtneDate);
                $(form).find('input[name="planUser"]').val(dataRecord.data.planUser);
                readPlanSettleDay();
              }
            }
          },
          dockedItems: [{
            xtype: "pagingtoolbar",
            store: planSettleDayStore,
            dock: "bottom",
            displayInfo: true
          }],
          enableKeyEvents: true
        });

        // 그리드 바인딩
        planSettleDayGrid.getSelectionModel().on("selectionchange",
            function (sm, selectedRecord) {
              if (selectedRecord.length) {
                var form = $('form[name="planSettleDayForm"]');
                $(form).find('input[name="rtneDate"]').val(selectedRecord[0].data.rtneDate);
                $(form).find('input[name="planUser"]').val(selectedRecord[0].data.planUser);
                readPlanSettleDay();
              }
            });

        mainGrid = planSettleDayGrid;
      });

      // 담당자 조회
      function searchUser(obj) {
        var searchCd = $(obj).attr("name");
        var searchWord = $(obj).val();

        $.ajax({
          type: "post",
          url: "/coms/comsUserSearch.do",
          data: {
            searchCd: searchCd,
            searchWord: searchWord
          },
          success: function (result) {
            if (result.length === 1) {
              var parentEl = $(obj).parent();
              $(parentEl).find("input:eq(0)").val(result[0].userId);
              $(parentEl).find("input:eq(1)").val(result[0].userNm);
              searchPlanSettleDay();
            } else {
              openUserPop(obj);
            }
          }
        });
      };

      // 담당자 선택 팝업
      function openUserPop(obj) {
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
            $("#popLayout").html(data);
            $.util.openDialog(title, width);
          }
        });
      }

      // 조회
      function searchPlanSettleDay() {
        planSettleDayStore.loadPage(1);
        $.util.setSearchCondition();
      }

      // 상세
      function readPlanSettleDay() {
        var form = $('form[name="planSettleDayForm"]');
        var rtneDate = $(form).find('input[name="rtneDate"]').val();
		var planUser = $(form).find('input[name="planUser"]').val();
        if (rtneDate === "") {
          alert("일일 결산 내역을 선택하세요.");
        } else {
          $.ajax({
            type: "post",
            url: "/pl/settle/planSettleDayDetail.do",
            data: {
              rtneDate: rtneDate,
              planUser: planUser
            },
            success: function (data, textStatus) {
              $("#planSettleDayDetail").html(data);
            }
          });
        }
      }

      // 일일 결산 엑셀
      function excelPlanSettleDay() {
        var form = $('form[name="planSettleDaySearchForm"]');
        $(form).attr({
          action: "/pl/settle/planSettleDayExcel.do"
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

            <form:form modelAttribute="planSettleVO" name="planSettleDaySearchForm" method="post">
                <div class="contents-box search-area margin_none">
                    <div class="row">
                        <div class="col-md-3 col-sm-12 col-xs-12 padding_l25 padding_r0">
                            <div class="col-md-2 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">담당자</span>
                            </div>
                            <div class="col-md-10 col-sm-12 col-xs-12 padding_none code_search_box">
                                <form:input path="userId" cssClass="form-control input-sm"
                                            onkeydown="${planSettleVO.authAdmin eq 'Y' ? 'javascript:if(event.keyCode==13){searchUser(this);}':''}"
                                            placeholder="아이디" readonly="${planSettleVO.authAdmin eq 'N' ? 'true' : ''}"/>
                                <form:input path="userNm" cssClass="form-control input-sm"
                                            onkeydown="${planSettleVO.authAdmin eq 'Y' ? 'javascript:if(event.keyCode==13){searchUser(this);}':''}"
                                            placeholder="성명" readonly="${planSettleVO.authAdmin eq 'N' ? 'true' : ''}"/>
                                <c:if test="${planSettleVO.authAdmin eq 'Y'}">
                                    <button type="button" onclick="openUserPop(this);" class="btn btn-green search">
                                        <i class="fa fa-search margin_none"></i>
                                    </button>
                                </c:if>
                            </div>
                        </div>
                        <div class="col-md-offset-1 col-md-4 col-sm-12 col-xs-12">
                            <div class="col-md-2 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">일자</span>
                            </div>
                            <div id="searchDate" class="col-md-7 col-sm-12 col-xs-12 padding_l5">
                                <form:input path="rtneStartDate"
                                            cssClass="cel width_449 form-control input-sm pull-left margin_none"
                                            onclick="setDatePicker(this);"/>
                                <span class="float_l cel_text">~</span>
                                <form:input path="rtneEndDate"
                                            cssClass="cel width_449 form-control input-sm pull-left margin_none"
                                            onclick="setDatePicker(this);"/>
                            </div>
                        </div>
                        <div class="col-md-offset-3 col-md-1 col-sm-12 col-xs-12">
                            <div class="search-btn-area">
                                <button type="button" onclick="searchPlanSettleDay();" class="btn btn-gray">
                                    <i class="fa fa-search"></i>조회
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>

            <form:form modelAttribute="planSettleVO" name="planSettleDayForm" method="post">
                <form:hidden path="rtneDate"/>
                <form:hidden path="planUser"/>
                <div class="contents-box grid-area outline_none">
                    <div class="grid-box2">
                        <ul class="nav nav-pills">
                            <li class="text-left">
                                <p class="contents-title">
                                    <i class="fa fa-arrow-circle-right"></i>일일 결산 목록
                                    <span class="sub-title-info"></span>
                                </p>
                            </li>
                            <li class="pull-right">
                                <div class="text-right">
                                    <div class="grid-option-box">
                                        <button type="button" onclick="excelPlanSettleDay();" class="btn btn-green">
                                            <i class="fa fa-file-excel-o" aria-hidden="true"></i><span>EXCEL</span>
                                        </button>
                                        <button type="button" onclick="$.util.screenSlide();" class="btn btn-green height-slider">
                                            <i class="fa fa-chevron-up"></i>
                                        </button>
                                        <button type="button" onclick="$.util.screenSlide();" class="btn btn-green height-slider" style="display: none;">
                                            <i class="fa fa-chevron-down"></i>
                                        </button>
                                        <form:select path="pageSize" cssClass="form-control input-sm pull-right"
                                                     onchange="planSettleDayStore.loadPage(1);">
                                            <c:forEach var="pageVO" items="${pageList}" varStatus="status">
                                                <form:option value="${pageVO.comCd}" label="${pageVO.comNm}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div id="planSettleDayGrid"></div>
                    </div>
                    <div class="grid-box2" id="planSettleDayDetail"></div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/cmmn/layout/footer.jsp" %>
</body>
</html>
