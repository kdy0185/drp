<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : menuMngList.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 메뉴 관리
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
      });

      // Ext Tree Model 정의 - 메뉴
      Ext.define("menuMngModel", {
        extend: "Ext.data.Model",
        fields: [{
          name: "menuNm", // 메뉴
          type: "String",
          useNull: true
        }, {
          name: "menuCd", // 메뉴 코드
          type: "String",
          useNull: true
        }, {
          name: "menuUrl", // 이동 주소
          type: "String",
          useNull: true
        }, {
          name: "menuLv", // 메뉴 수준
          type: "String",
          useNull: true
        }, {
          name: "menuOrd", // 메뉴 순서
          type: "String",
          useNull: true
        }, {
          name: "useYn", // 사용 여부
          type: "String",
          useNull: true
        }]
      });

      // Ext Tree Store 정의 - 메뉴
      var menuMngStore = Ext.create("Ext.data.TreeStore", {
        model: "menuMngModel",
        nodeParam: "menuCd",
        root: {
          leaf: false,
          expanded: true
        },
        proxy: {
          type: "ajax",
          url: "/sys/menumng/menuMngSearch.do",
          reader: {
            type: "json",
            root: "menuMngList"
          }
        },
        listeners: {
          beforeload: function (store, operation, eOpts) {
            var searchArea = $("div.search-area");
            $.util.setSearchStyle(searchArea);

            var form = $('form[name="menuMngSearchForm"]');
            operation.node.data.expanded = false; // 확장 여부 값 초기화
            operation.params.menuCd = operation.node.get("menuCd");
            operation.params.searchCd = $(form).find('select[name="searchCd"] option:selected').val();
            operation.params.searchWord = $(form).find('input[name="searchWord"]').val();
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
        // Ext Tree 정의 - 메뉴
        var menuMngTree = Ext.create("Ext.tree.Panel", {
          store: menuMngStore,
          rootVisible: false,
          multiSelect: true,
          plugins: "bufferedrenderer",
          selType: "checkboxmodel",
          columnLines: true,
          forceFit: true,
          columns: [{
            text: "메뉴",
            width: 60,
            style: "text-align:center",
            sortable: true,
            dataIndex: "menuNm",
            xtype: "treecolumn",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "메뉴 코드",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "menuCd",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "이동 주소",
            width: 90,
            style: "text-align:center",
            sortable: true,
            dataIndex: "menuUrl",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "메뉴 수준",
            width: 15,
            align: "center",
            sortable: true,
            dataIndex: "menuLv",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "메뉴 순서",
            width: 15,
            align: "center",
            sortable: true,
            dataIndex: "menuOrd",
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
          }],
          height: comHeight,
          autoWidth: true,
          renderTo: "menuMngTree",
          viewConfig: {
            stripeRows: true,
            enableTextSelection: true,
            listeners: {
              itemclick: function (view, node, htmlElement) {
                var form = $('form[name="menuMngForm"]');
                $(form).find('input[name="menuCd"]').val(node.data.menuCd);
              },
              itemdblclick: function (view, node, htmlElement) {
                var form = $('form[name="menuMngForm"]');
                $(form).find('input[name="menuCd"]').val(node.data.menuCd);
                readMenuMng('U');
              }
            }
          },
          enableKeyEvents: true
        });
        mainTree = menuMngTree;
      });

      // 조회
      function searchMenuMng() {
        menuMngStore.load();
        $.util.setSearchCondition();
      }

      // 상세
      function readMenuMng(state) {
        var form = $('form[name="menuMngForm"]');
        var menuMngCnt = mainTree.getSelectionModel().getCount();
        var menuCd = state === "U" ? $(form).find('input[name="menuCd"]').val() : "";
        if (state === "U" && menuMngCnt === 0) {
          alert("메뉴를 선택하세요.");
        } else if (state === "U" && menuMngCnt > 1) {
          alert("1개의 메뉴만 선택하세요.");
        } else {
          var title = state === "I" ? "메뉴 등록" : "메뉴 정보";
          var width = 750;
          $.ajax({
            type: "post",
            url: "/sys/menumng/menuMngDetail.do",
            data: {
              menuCd: menuCd,
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
        var form = $('form[name="menuMngForm"]');
        var menuMngCnt = mainTree.getSelectionModel().getCount();
        var menuMngItems = mainTree.getSelectionModel().getSelection();
        var menuCd = "";
        $(menuMngItems).each(function (i) {
          menuCd += menuMngItems[i].data.menuCd + ",";
        });

        if (menuMngCnt === 0) {
          alert("메뉴를 선택하세요.");
        } else {
          var title = "권한 설정";
          var width = 360;
          $.ajax({
            type: "post",
            url: "/sys/menumng/menuAuthMngPop.do",
            data: {
              menuCd: menuCd
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
        var url = "/sys/menumng/menuMngList.do";
        var menuCd = "${comsMenuVO.menuCd}";
        var csrfParam = "${_csrf.parameterName}";
        var csrfToken = "${_csrf.token}";
        $.util.moveMenu(url, menuCd, csrfParam, csrfToken);
      }

      // 메뉴 엑셀
      function excelMenuMng() {
        var form = $('form[name="menuMngForm"]');
        $(form).attr({
          action: "/sys/menumng/menuMngExcel.do"
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

            <form:form modelAttribute="menuMngVO" name="menuMngSearchForm" method="post">
                <div class="contents-box search-area margin_none">
                    <div class="row">
                        <div class="col-md-5 col-sm-12 col-xs-12 padding_l25">
                            <div class="col-md-1 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">메뉴</span>
                            </div>
                            <div class="col-md-2 col-sm-12 col-xs-12 padding_none">
                                <form:select path="searchCd" cssClass="form-control input-sm pull-left">
                                    <form:option value="menuCd" label="메뉴 코드"/>
                                    <form:option value="menuNm" label="메뉴명"/>
                                </form:select>
                            </div>
                            <div class="col-md-4 col-sm-12 col-xs-12 padding_r0">
                                <form:input path="searchWord" cssClass="form-control input-sm"
                                            onkeydown="if(event.keyCode==13){searchMenuMng(); return false;}"/>
                            </div>
                        </div>
                        <div class="col-md-offset-1 col-md-3 col-sm-12 col-xs-12">
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
                        <div class="col-md-offset-2 col-md-1 col-sm-12 col-xs-12">
                            <div class="search-btn-area">
                                <button type="button" onclick="searchMenuMng();" class="btn btn-gray">
                                    <i class="fa fa-search"></i>조회
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>

            <form:form modelAttribute="menuMngVO" name="menuMngForm" method="post">
                <form:hidden path="menuCd"/>
                <div class="contents-box grid-area outline_none">
                    <div class="grid-box">
                        <ul class="nav nav-pills">
                            <li class="text-left">
                                <p class="contents-title"><i class="fa fa-arrow-circle-right"></i>메뉴 목록</p>
                            </li>
                            <li class="pull-right">
                                <div class="text-right">
                                    <div class="grid-option-box">
                                        <button type="button" onclick="excelMenuMng();" class="btn btn-green margin_none">
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
                        <div id="menuMngTree"></div>
                        <div class="btn-right-area">
                            <button type="button" onclick="readMenuMng('I');" class="btn btn-red">
                                <i class="fa fa-pencil-square-o"></i>등록
                            </button>
                            <button type="button" onclick="readMenuMng('U');" class="btn btn-red">
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
