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
          name: "upperAuthCd", // 상위 권한 코드
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
            var form = $('form[name="authMngForm"]');
            operation.params.authCd = operation.node.get("authCd");
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
            text: "상위 권한 코드",
            width: 10,
            align: "center",
            sortable: true,
            hidden: true,
            dataIndex: "upperMenuCd",
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
                readAuthMng('U');
              }
            }
          },
          enableKeyEvents: true
        });
        mainTree = authMngTree;
      });

      // 상세
      function readAuthMng(state) {
        var form = $('form[name="authMngForm"]');
        var authMngCnt = mainTree.getSelectionModel().getCount();
        var authCd = state === "U" ? $(form).find('input[name="authCd"]').val() : "";
        if (state === "U" && authMngCnt === 0) {
          alert("권한를 선택하세요.");
        } else if (state === "U" && authMngCnt > 1) {
          alert("1개의 권한만 선택하세요.");
        } else {
          var title = state === "I" ? "권한 등록" : "권한 정보";
          var width = 750;
          $.ajax({
            type: "post",
            url: "/sys/authmng/authMngDetail.do",
            data: {
              authCd: authCd,
              state: state
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
        var form = $('form[name="authMngForm"]');
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

            <form:form modelAttribute="authMngVO" name="authMngForm" method="post">
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
                            <button type="button" onclick="readAuthMng('I');" class="btn btn-red">
                                <i class="fa fa-pencil-square-o"></i>등록
                            </button>
                            <button type="button" onclick="readAuthMng('U');" class="btn btn-red">
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
