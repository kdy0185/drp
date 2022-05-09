<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : authMngList.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 권한 관리
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

      // Ext Tree Model 정의 - 권한
      Ext.define("authMngModel", {
        extend: "Ext.data.Model",
        fields: [{
          name: "authNm", // 권한
          type: "String",
          useNull: true
        }, {
          name: "authCd", // 권한 코드
          type: "String",
          useNull: true
        }, {
          name: "authDesc", // 권한 설명
          type: "String",
          useNull: true
        }, {
          name: "authLv", // 권한 수준
          type: "String",
          useNull: true
        }, {
          name: "authOrd", // 권한 순서
          type: "String",
          useNull: true
        }, {
          name: "useYn", // 사용 여부
          type: "String",
          useNull: true
        }]
      });

      // Ext Tree Store 정의 - 권한
      var authMngStore = Ext.create("Ext.data.TreeStore", {
        model: "authMngModel",
        nodeParam: "authCd",
        root: {
          leaf: false,
          expanded: true
        },
        proxy: {
          type: "ajax",
          url: "/sys/authmng/authMngSearch.do",
          reader: {
            type: "json",
            root: "authMngList"
          }
        },
        listeners: {
          beforeload: function (store, operation, eOpts) {
            var searchArea = $("div.search-area");
            $.util.setSearchStyle(searchArea);

            var form = $('form[name="authMngSearchForm"]');
            operation.node.data.expanded = false; // 확장 여부 값 초기화
            operation.params.authCd = operation.node.get("authCd");
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
        // Ext Tree 정의 - 권한
        var authMngTree = Ext.create("Ext.tree.Panel", {
          store: authMngStore,
          rootVisible: false,
          multiSelect: true,
          plugins: "bufferedrenderer",
          selType: "checkboxmodel",
          columnLines: true,
          forceFit: true,
          columns: [{
            text: "권한",
            width: 60,
            style: "text-align:center",
            sortable: true,
            dataIndex: "authNm",
            xtype: "treecolumn",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "권한 코드",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "authCd",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "권한 설명",
            width: 90,
            style: "text-align:center",
            sortable: true,
            dataIndex: "authDesc",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "권한 수준",
            width: 15,
            align: "center",
            sortable: true,
            dataIndex: "authLv",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "권한 순서",
            width: 15,
            align: "center",
            sortable: true,
            dataIndex: "authOrd",
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
          renderTo: "authMngTree",
          viewConfig: {
            stripeRows: true,
            enableTextSelection: true,
            listeners: {
              itemclick: function (view, node, htmlElement) {
                var form = $('form[name="authMngForm"]');
                $(form).find('input[name="authCd"]').val(node.data.authCd);
              },
              itemdblclick: function (view, node, htmlElement) {
                var form = $('form[name="authMngForm"]');
                $(form).find('input[name="authCd"]').val(node.data.authCd);
                readAuthMng('UPDATE');
              }
            }
          },
          enableKeyEvents: true
        });
        mainTree = authMngTree;
      });

      // 조회
      function searchAuthMng() {
        authMngStore.load();
        $.util.setSearchCondition();
      }

      // 상세
      function readAuthMng(detailStatus) {
        var form = $('form[name="authMngForm"]');
        var authMngCnt = mainTree.getSelectionModel().getCount();
        var authCd = detailStatus === "UPDATE" ? $(form).find('input[name="authCd"]').val() : "";
        if (detailStatus === "UPDATE" && authMngCnt === 0) {
          alert("권한를 선택하세요.");
        } else if (detailStatus === "UPDATE" && authMngCnt > 1) {
          alert("1개의 권한만 선택하세요.");
        } else {
          var title = detailStatus === "INSERT" ? "권한 등록" : "권한 정보";
          var width = 750;
          $.ajax({
            type: "post",
            url: "/sys/authmng/authMngDetail.do",
            data: {
              authCd: authCd,
              detailStatus: detailStatus
            },
            success: function (data, textStatus) {
              $("#popLayout").html(data);
              $.util.openDialog(title, width);
            }
          });
        }
      }

      // 사용자 설정 팝업
      function openUserPop() {
        var form = $('form[name="authMngForm"]');
        var authMngCnt = mainTree.getSelectionModel().getCount();
        var authMngItems = mainTree.getSelectionModel().getSelection();
        var authCd = "";
        $(authMngItems).each(function (i) {
          authCd += authMngItems[i].data.authCd + ",";
        });

        if (authMngCnt === 0) {
          alert("권한을 선택하세요.");
        } else {
          var title = "사용자 설정";
          var width = 750;
          $.ajax({
            type: "post",
            url: "/sys/authmng/authUserMngPop.do",
            data: {
              authCd: authCd
            },
            success: function (data, textStatus) {
              $("#popLayout").html(data);
              $.util.openDialog(title, width);
            }
          });
        }
      }

      // 메뉴 설정 팝업
      function openMenuPop() {
        var form = $('form[name="authMngForm"]');
        var authMngCnt = mainTree.getSelectionModel().getCount();
        var authMngItems = mainTree.getSelectionModel().getSelection();
        var authCd = "";
        $(authMngItems).each(function (i) {
          authCd += authMngItems[i].data.authCd + ",";
        });

        if (authMngCnt === 0) {
          alert("권한을 선택하세요.");
        } else {
          var title = "메뉴 설정";
          var width = 360;
          $.ajax({
            type: "post",
            url: "/sys/authmng/authMenuMngPop.do",
            data: {
              authCd: authCd
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
        var url = "/sys/authmng/authMngList.do";
        var menuCd = "${comsMenuVO.menuCd}";
        var csrfParam = "${_csrf.parameterName}";
        var csrfToken = "${_csrf.token}";
        $.util.moveMenu(url, menuCd, csrfParam, csrfToken);
      }

      // 권한 엑셀
      function excelAuthMng() {
        var form = $('form[name="authMngSearchForm"]');
        $(form).attr({
          action: "/sys/authmng/authMngExcel.do"
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

            <form:form modelAttribute="searchDTO" name="authMngSearchForm" method="post">
                <div class="contents-box search-area margin_none">
                    <div class="row">
                        <div class="col-md-5 col-sm-12 col-xs-12 padding_l25">
                            <div class="col-md-1 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">권한</span>
                            </div>
                            <div class="col-md-2 col-sm-12 col-xs-12 padding_none">
                                <form:select path="searchCd" cssClass="form-control input-sm pull-left">
                                    <form:option value="authCd" label="권한 코드"/>
                                    <form:option value="authNm" label="권한명"/>
                                </form:select>
                            </div>
                            <div class="col-md-4 col-sm-12 col-xs-12 padding_r0">
                                <form:input path="searchWord" cssClass="form-control input-sm"
                                            onkeydown="if(event.keyCode==13){searchAuthMng(); return false;}"/>
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
                                <button type="button" onclick="searchAuthMng();" class="btn btn-gray">
                                    <i class="fa fa-search"></i>조회
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>

            <form:form modelAttribute="searchDTO" name="authMngForm" method="post">
                <form:hidden path="authCd"/>
                <div class="contents-box grid-area outline_none">
                    <div class="grid-box">
                        <ul class="nav nav-pills">
                            <li class="text-left">
                                <p class="contents-title"><i class="fa fa-arrow-circle-right"></i>권한 목록</p>
                            </li>
                            <li class="pull-right">
                                <div class="text-right">
                                    <div class="grid-option-box">
                                        <button type="button" onclick="excelAuthMng();" class="btn btn-green margin_none">
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
                        <div id="authMngTree"></div>
                        <div class="btn-right-area">
                            <button type="button" onclick="readAuthMng('INSERT');" class="btn btn-red">
                                <i class="fa fa-pencil-square-o"></i>등록
                            </button>
                            <button type="button" onclick="readAuthMng('UPDATE');" class="btn btn-red">
                                <i class="fa fa-file-text-o"></i>상세
                            </button>
                            <button type="button" onclick="openUserPop();" class="btn btn-red">
                                <i class="fa fa-cogs"></i>사용자 설정
                            </button>
                            <button type="button" onclick="openMenuPop();" class="btn btn-red">
                                <i class="fa fa-cogs"></i>메뉴 설정
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
