<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : planCtgOptList.jsp
     * @Author : KDW
     * @Date : 2022-01-29
     * @Description : 분류 옵션 설정
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
        if ("${planCtgOptVO.authAdmin}" == "Y") $.util.getSearchCondition();
        searchPlanCtgOpt();
      });

      // Ext Tree Model 정의 - 분류 옵션 설정
      Ext.define("planCtgOptModel", {
        extend: "Ext.data.Model",
        fields: [{
          name: "rtneCtgNm", // 분류
          type: "String",
          useNull: true
        }, {
          name: "rtneCtgCd", // 분류 코드
          type: "String",
          useNull: true
        }, {
          name: "wtVal", // 가중치
          type: "float",
          useNull: true
        }, {
          name: "recgMinTime", // 권장 시간
          type: "String",
          useNull: true
        }, {
          name: "rtneStartDate", // 적용 기간
          type: "String",
          useNull: true
        }, {
          name: "useYn", // 사용 여부
          type: "String",
          useNull: true
        }, {
          name: "planUser", // 담당자
          type: "String",
          useNull: true
        }]
      });

      // Ext Tree Store 정의 - 분류 옵션 설정
      var planCtgOptStore = Ext.create("Ext.data.TreeStore", {
        model: "planCtgOptModel",
        nodeParam: "rtneCtgCd",
        root: {
          leaf: false,
          expanded: true
        },
        proxy: {
          type: "ajax",
          url: "/pl/ctgopt/planCtgOptSearch.do",
          reader: {
            type: "json",
            root: "planCtgOptList"
          }
		},
        listeners: {
          beforeload: function (store, operation, eOpts) {
            var searchArea = $("div.search-area");
            $.util.setSearchStyle(searchArea);

            var form = $('form[name="planCtgOptSearchForm"]');
            operation.node.data.expanded = false; // 확장 여부 값 초기화
            operation.params.rtneCtgCd = operation.node.get("rtneCtgCd");
            operation.params.userId = $(form).find('input[name="userId"]').val();
            operation.params.useYn = $(form).find('select[name="useYn"] option:selected').val();
          },
          load: function (store, node, records, successful, eOpts) {
            if (node.isRoot() && !node.isExpanded()) {
              node.expand();
            }
          }
        }
      });

      Ext.onReady(function () {
        // Ext Tree 정의 - 분류 옵션 설정
        var planCtgOptTree = Ext.create("Ext.tree.Panel", {
          store: planCtgOptStore,
          rootVisible: false,
          multiSelect: true,
          plugins: "bufferedrenderer",
          selType: "checkboxmodel",
          columnLines: true,
          forceFit: true,
          columns: [{
            text: "분류",
            width: 30,
            style: "text-align:center",
            sortable: true,
            dataIndex: "rtneCtgNm",
            xtype: "treecolumn",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "분류 코드",
            width: 15,
            align: "center",
            sortable: true,
            dataIndex: "rtneCtgCd",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "가중치",
            width: 15,
            align: "center",
            sortable: true,
            dataIndex: "wtVal",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "권장 시간",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "recgMinTime",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "적용 기간",
            width: 45,
            align: "center",
            sortable: true,
            dataIndex: "rtneStartDate",
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
          renderTo: "planCtgOptTree",
          viewConfig: {
            stripeRows: true,
            enableTextSelection: true,
            listeners: {
              itemclick: function (view, node, htmlElement) {
                var form = $('form[name="planCtgOptForm"]');
                $(form).find('input[name="rtneCtgCd"]').val(node.data.rtneCtgCd);
                $(form).find('input[name="planUser"]').val(node.data.planUser);
              },
              itemdblclick: function (view, node, htmlElement) {
                var form = $('form[name="planCtgOptForm"]');
                $(form).find('input[name="rtneCtgCd"]').val(node.data.rtneCtgCd);
                $(form).find('input[name="planUser"]').val(node.data.planUser);
                readPlanCtgOpt('U');
              }
            }
          },
          enableKeyEvents: true
        });
        mainTree = planCtgOptTree;
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
              searchPlanCtgOpt();
            } else {
              openUserPop(obj);
            }
          }
        });
      }

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
      function searchPlanCtgOpt() {
        planCtgOptStore.load();
        $.util.setSearchCondition();
      }

      // 상세
      function readPlanCtgOpt(state) {
        var form = $('form[name="planCtgOptForm"]');
        var planCtgOptCnt = mainTree.getSelectionModel().getCount();
        var rtneCtgCd = state === "U" ? $(form).find('input[name="rtneCtgCd"]').val() : "";
        var planUser = state === "U" ? $(form).find('input[name="planUser"]').val() : "";
        if (state === "U" && planCtgOptCnt === 0) {
          alert("분류를 선택하세요.");
        } else if (state === "U" && planCtgOptCnt > 1) {
          alert("1개의 분류만 선택하세요.");
        } else {
          var title = state === "I" ? "분류 옵션 등록" : "분류 옵션 정보";
          var width = 750;
          $.ajax({
            type: "post",
            url: "/pl/ctgopt/planCtgOptDetail.do",
            data: {
              rtneCtgCd: rtneCtgCd,
              planUser: planUser,
              state: state
            },
            success: function (data, textStatus) {
              $("#popLayout").html(data);
              $.util.openDialog(title, width);
            }
          });
        }
      }

      // 목록
      function moveList() {
        var url = "/pl/ctgopt/planCtgOptList.do";
        var menuCd = "${comsMenuVO.menuCd}";
        var csrfParam = "${_csrf.parameterName}";
        var csrfToken = "${_csrf.token}";
        $.util.moveMenu(url, menuCd, csrfParam, csrfToken);
      }

      // 분류 옵션 엑셀
      function excelPlanCtgOpt() {
        var form = $('form[name="planCtgOptSearchForm"]');
        $(form).attr({
          action: "/pl/ctgopt/planCtgOptExcel.do"
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

            <form:form modelAttribute="planCtgOptVO" name="planCtgOptSearchForm" method="post">
                <div class="contents-box search-area margin_none">
                    <div class="row">
                        <div class="col-md-3 col-sm-12 col-xs-12 padding_l25 padding_r0">
                            <div class="col-md-2 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">담당자</span>
                            </div>
                            <div class="col-md-10 col-sm-12 col-xs-12 padding_none code_search_box">
                                <form:input path="userId" cssClass="form-control input-sm"
                                            onkeydown="${planCtgOptVO.authAdmin eq 'Y' ? 'javascript:if(event.keyCode==13){searchUser(this);}':''}"
                                            placeholder="아이디" readonly="${planCtgOptVO.authAdmin eq 'N' ? 'true' : ''}"/>
                                <form:input path="userNm" cssClass="form-control input-sm"
                                            onkeydown="${planCtgOptVO.authAdmin eq 'Y' ? 'javascript:if(event.keyCode==13){searchUser(this);}':''}"
                                            placeholder="성명" readonly="${planCtgOptVO.authAdmin eq 'N' ? 'true' : ''}"/>
                                <c:if test="${planCtgOptVO.authAdmin eq 'Y'}">
                                    <button type="button" onclick="openUserPop(this);" class="btn btn-green search">
                                        <i class="fa fa-search margin_none"></i>
                                    </button>
                                </c:if>
                            </div>
                        </div>
                        <div class="col-md-offset-2 col-md-3 col-sm-12 col-xs-12">
                            <div class="col-md-2 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">사용 여부</span>
                            </div>
                            <div class="col-md-4 col-sm-12 col-xs-12 padding_none">
                                <form:select path="useYn" cssClass="form-control input-sm">
                                    <form:option value="" label="선택"/>
                                    <form:option value="Y" label="사용"/>
                                    <form:option value="N" label="미사용"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="col-md-offset-3 col-md-1 col-sm-12 col-xs-12">
                            <div class="search-btn-area">
                                <button type="button" onclick="searchPlanCtgOpt();" class="btn btn-gray">
                                    <i class="fa fa-search"></i>조회
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>

            <form:form modelAttribute="planCtgOptVO" name="planCtgOptForm" method="post">
                <form:hidden path="rtneCtgCd"/>
                <form:hidden path="planUser"/>
                <div class="contents-box grid-area outline_none">
                    <div class="grid-box">
                        <ul class="nav nav-pills">
                            <li class="text-left">
                                <p class="contents-title"><i class="fa fa-arrow-circle-right"></i>분류 목록</p>
                            </li>
                            <li class="pull-right">
                                <div class="text-right">
                                    <div class="grid-option-box">
                                        <button type="button" onclick="excelPlanCtgOpt();" class="btn btn-green margin_none">
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
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div id="planCtgOptTree"></div>
                        <div class="btn-right-area">
                            <button type="button" onclick="readPlanCtgOpt('I');" class="btn btn-red">
                                <i class="fa fa-pencil-square-o"></i>등록
                            </button>
                            <button type="button" onclick="readPlanCtgOpt('U');" class="btn btn-red">
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
