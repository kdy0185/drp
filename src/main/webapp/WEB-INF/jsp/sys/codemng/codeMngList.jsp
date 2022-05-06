<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : codeMngList.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 코드 관리
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
        searchAll();
      });

      // Ext Grid Model 정의 - 그룹 코드
      Ext.define("codeGrpMngModel", {
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
          name: "grpNm", // 그룹 코드명
          type: "String",
          useNull: true
        }, {
          name: "useYn", // 사용 여부
          type: "String",
          useNull: true
        }, {
          name: "detl", // 비고
          type: "String",
          useNull: true
        }, {
          name: "state", // 상태 : insert / update
          type: "String",
          useNull: true
        }]
      });

      // Ext Grid Model 정의 - 공통 코드
      Ext.define("codeMngModel", {
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
          name: "comCd", // 공통 코드
          type: "String",
          useNull: true
        }, {
          name: "comNm", // 공통 코드명
          type: "String",
          useNull: true
        }, {
          name: "ord", // 정렬 순서
          type: "int",
          useNull: true
        }, {
          name: "useYn", // 사용 여부
          type: "String",
          useNull: true
        }, {
          name: "detl", // 비고
          type: "String",
          useNull: true
        }, {
          name: "state", // 상태 : insert / update
          type: "String",
          useNull: true
        }]
      });

      // Ext Grid Store 정의 - 그룹 코드
      var codeGrpMngStore = Ext.create("Ext.data.Store", {
        pageSize: defaultPageSize,
        autoLoad: false,
        model: "codeGrpMngModel",
        proxy: {
          type: "ajax",
          url: "/sys/codemng/codeGrpMngSearch.do",
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
            var textPoint = $("#codeGrpMngGrid").prev().find(".sub-title-info");
            textPoint.empty();
            var em = '<em class="sub-title-info-em">' + dataStore.pageSize + '</em>건 / 전체 '
                + dataStore.totalCount + '건';
            textPoint.append(em);
          },
          scope: this
        }
      });

      // Ext Grid Store 정의 - 공통 코드
      var codeMngStore = Ext.create("Ext.data.Store", {
        pageSize: defaultPageSize,
        autoLoad: false,
        model: "codeMngModel",
        proxy: {
          type: "ajax",
          url: "/sys/codemng/codeMngSearch.do",
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
            var textPoint = $("#codeMngGrid").prev().find(".sub-title-info");
            textPoint.empty();
            var em = '<em class="sub-title-info-em">' + dataStore.pageSize + '</em>건 / 전체 '
                + dataStore.totalCount + '건';
            textPoint.append(em);
          },
          scope: this
        }
      });

      // 페이지 이동 시 파라미터 재생성 - 그룹 코드
      codeGrpMngStore.on("beforeload", function (store, operation) {
        var searchArea = $("div.search-area");
        $.util.setSearchStyle(searchArea);

        var pageSize = $('select[name="pageSize"] option:selected').val();
        var searchCd = $('select[name="searchCd"] option:selected').val();
        var searchWord = $('input[name="searchWord"]').val();
        store.pageSize = pageSize;
        operation.params = {
          pageNo: store.currentPage - 1,
          pageSize: pageSize,
          searchCd: searchCd,
          searchWord: searchWord
        };
      }, subGrid);

      // 페이지 이동 시 파라미터 재생성 - 공통 코드
      codeMngStore.on("beforeload", function (store, operation) {
        var searchArea = $("div.search-area");
        $.util.setSearchStyle(searchArea);

        var pageSize = $('select[name="pageSize"] option:selected').val();
        var grpCd = $('input[name="grpCd"]').val();
        var searchCd = $('select[name="searchCd"] option:selected').val();
        var searchWord = $('input[name="searchWord"]').val();
        store.pageSize = pageSize;
        operation.params = {
          pageNo: store.currentPage - 1,
          pageSize: pageSize,
          grpCd: grpCd,
          searchCd: searchCd,
          searchWord: searchWord
        };
      }, subGrid2);

      Ext.onReady(function () {
        Ext.QuickTips.init();

        // edit plugin 추가
        var grpCellEditing = Ext.create("Ext.grid.plugin.CellEditing", {
          clicksToEdit: 1,
          listeners: {
            beforeEdit: function (editor, e, eOpts) {
              if (e.field === "grpCd") {
                var fieldType = e.record.data.state === "I" ? "textfield" : "displayfield";
                e.column.setEditor({xtype: fieldType});
              }
            }
          }
        });
        var codeCellEditing = Ext.create("Ext.grid.plugin.CellEditing", {
          clicksToEdit: 1,
          listeners: {
            beforeEdit: function (editor, e, eOpts) {
              if (e.field === "comCd") {
                var fieldType = e.record.data.state === "I" ? "textfield" : "displayfield";
                e.column.setEditor({xtype: fieldType});
              }
            }
          }
        });

        // combo + renderer 추가
        var useStore = new Ext.data.SimpleStore({
          fields: ["Key", "Name"],
          data: [["Y", "사용"], ["N", "미사용"]]
        });
        var grpCombo = new Ext.form.ComboBox({
          store: useStore,
          valueField: "Key",
          displayField: "Name",
          editable: false
        });
        var codeCombo = new Ext.form.ComboBox({
          store: useStore,
          valueField: "Key",
          displayField: "Name",
          editable: false
        });
        Ext.namespace("Ext.ux");
        Ext.util.Format.comboRenderer = function (combo) {
          return function (value) {
            var record = combo.findRecord(combo.valueField || combo.displayField, value);
            return record ? record.get(combo.displayField) : combo.valueNotFoundText;
          };
        };

        // Ext Grid 정의 - 그룹 코드
        var codeGrpMngGrid = Ext.create("Ext.grid.Panel", {
          store: codeGrpMngStore,
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
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "grpCd",
            editor: {
              xtype: "textfield"
            },
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "그룹 코드명",
            width: 60,
            style: "text-align:center",
            sortable: true,
            dataIndex: "grpNm",
            editor: {
              xtype: "textfield"
            },
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "사용 여부",
            width: 20,
            align: "center",
            sortable: true,
            dataIndex: "useYn",
            editor: grpCombo,
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              var combo = metadata.column.getEditor();
              var index = combo.store.findExact(combo.valueField, val);
              if (index >= 0) {
                return combo.store.getAt(index).get(combo.displayField);
              }
              return val;
            }
          }, {
            text: "비고",
            width: 90,
            style: "text-align:center",
            sortable: true,
            hidden: true,
            dataIndex: "detl",
            editor: {
              xtype: "textfield"
            },
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "상태",
            width: 10,
            align: "center",
            sortable: true,
            hidden: true,
            dataIndex: "state",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }],
          height: comHeight,
          autoWidth: true,
          renderTo: "codeGrpMngGrid",
          viewConfig: {
            stripeRows: true,
            enableTextSelection: true,
            listeners: {
              celldblclick: function (grid, htmlElement, columnIndex, dataRecord) {
                if (columnIndex !== 0) readCodeGrpMng();
              }
            }
          },
          plugins: [grpCellEditing],
          dockedItems: [{
            xtype: "pagingtoolbar",
            store: codeGrpMngStore,
            dock: "bottom",
            displayInfo: true
          }],
          enableKeyEvents: true
        });

        // 그리드 바인딩
        codeGrpMngGrid.getSelectionModel().on("selectionchange",
            function (sm, selectedRecord) {
              if (selectedRecord.length) {
                $('input[name="grpCd"]').val(selectedRecord[0].data.grpCd);
                codeMngStore.loadPage(1);
              }
            });

        codeGrpMngGrid.on("edit", function (editor, e) {
          e.record.data.state = e.record.data.state === "I" ? "I" : "U";
          e.record.commit();
        });
        subGrid = codeGrpMngGrid;

        // Ext Grid 정의 - 공통 코드
        var codeMngGrid = Ext.create("Ext.grid.Panel", {
          store: codeMngStore,
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
            width: 30,
            align: "center",
            sortable: true,
            hidden: true,
            dataIndex: "grpCd",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "공통 코드",
            width: 30,
            align: "center",
            sortable: true,
            dataIndex: "comCd",
            editor: {
              xtype: "textfield"
            },
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "공통 코드명",
            width: 60,
            style: "text-align:center",
            sortable: true,
            dataIndex: "comNm",
            editor: {
              xtype: "textfield"
            },
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "정렬 순서",
            width: 15,
            style: "text-align:center",
            align: "right",
            sortable: true,
            dataIndex: "ord",
            xtype: "numbercolumn",
            editor: {
              xtype: "numberfield",
              minValue: 1,
              format: "0,000"
            },
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return Ext.util.Format.number(val, "0,000");
            }
          }, {
            text: "사용 여부",
            width: 20,
            align: "center",
            sortable: true,
            dataIndex: "useYn",
            editor: codeCombo,
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              var combo = metadata.column.getEditor();
              var index = combo.store.findExact(combo.valueField, val);
              if (index >= 0) {
                return combo.store.getAt(index).get(combo.displayField);
              }
              return val;
            }
          }, {
            text: "비고",
            width: 90,
            style: "text-align:center",
            sortable: true,
            hidden: true,
            dataIndex: "detl",
            editor: {
              xtype: "textfield"
            },
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }, {
            text: "상태",
            width: 10,
            align: "center",
            sortable: true,
            hidden: true,
            dataIndex: "state",
            renderer: function (val, metadata, record) {
              metadata.style = "cursor: pointer;";
              return val;
            }
          }],
          height: comHeight,
          autoWidth: true,
          renderTo: "codeMngGrid",
          viewConfig: {
            stripeRows: true,
            enableTextSelection: true,
            listeners: {
              cellclick: function (grid, htmlElement, columnIndex, dataRecord) {
                $('input[name="grpCd"]').val(dataRecord.data.grpCd);
                $('input[name="comCd"]').val(dataRecord.data.comCd);
              },
              celldblclick: function (grid, htmlElement, columnIndex, dataRecord) {
                if (columnIndex !== 0) readCodeMng();
              }
            }
          },
          plugins: [codeCellEditing],
          dockedItems: [{
            xtype: "pagingtoolbar",
            store: codeMngStore,
            dock: "bottom",
            displayInfo: true
          }],
          enableKeyEvents: true
        });
        codeMngGrid.on("edit", function (editor, e) {
          e.record.data.state = e.record.data.state === "I" ? "I" : "U";
          e.record.commit();
        });
        subGrid2 = codeMngGrid;
      });

      // 단일 조회
      function searchOne() {
        var searchCd = $('select[name="searchCd"] option:selected').val();
        var searchWord = $('input[name="searchWord"]').val();
        if (searchWord !== "") {
          if (searchCd === "grpCd" || searchCd === "grpNm") {
            codeGrpMngStore.loadPage(1);
          }
          if (searchCd === "comCd" || searchCd === "comNm") {
            codeMngStore.loadPage(1);
          }
        } else {
          searchAll();
        }
        $.util.setSearchCondition();
      }

      // 일괄 조회
      function searchAll() {
        codeGrpMngStore.loadPage(1);
        codeMngStore.loadPage(1);
        $.util.setSearchCondition();
      }

      // 그룹 코드 추가
      function insertCodeGrpMng() {
        var row = Ext.create("codeGrpMngModel", {
          rn: 0,
          grpCd: "",
          grpNm: "",
          useYn: "Y",
          state: "I"
        });
        codeGrpMngStore.insert(0, row);
      }

      // 공통 코드 추가
      function insertCodeMng() {
        var grpCd = $('input[name="grpCd"]').val();
        if (grpCd === "") {
          alert("그룹 코드를 선택하세요.");
          return;
        }
        var row = Ext.create("codeMngModel", {
          rn: 0,
          grpCd: grpCd,
          comCd: "",
          comNm: "",
          ord: 0,
          useYn: "Y",
          state: "I"
        });
        codeMngStore.insert(0, row);
      }

      // 그룹 코드 상세
      function readCodeGrpMng() {
        var title = "그룹 코드 정보";
        var width = 750;
        $.ajax({
          type: "post",
          url: "/sys/codemng/codeGrpMngDetail.do",
          data: {
            grpCd: $('input[name="grpCd"]').val()
          },
          success: function (data, textStatus) {
            $("#popLayout").html(data);
            $.util.openDialog(title, width);
          }
        });
      }

      // 공통 코드 상세
      function readCodeMng() {
        var title = "공통 코드 정보";
        var width = 750;
        $.ajax({
          type: "post",
          url: "/sys/codemng/codeMngDetail.do",
          data: {
            grpCd: $('input[name="grpCd"]').val(),
            comCd: $('input[name="comCd"]').val()
          },
          success: function (data, textStatus) {
            $("#popLayout").html(data);
            $.util.openDialog(title, width);
          }
        });
      }

      // 그룹 코드 수정
      function updateCodeGrpMng() {
        var grpItems = subGrid.store.data.items;
        var grpJson = [];
        $(grpItems).each(function (i) {
          grpJson[i] = grpItems[i].data;
        });

        $.ajax({
          type: "put",
          url: "/sys/codemng/codeGrpMngUpdate.do",
          data: {
            jsonData: JSON.stringify(grpJson)
          },
          success: function (res) {
            alert(res.dataMsg);
            if (res.dataStatus === "SUCCESS_UPDATE") {
              searchAll();
            }
          }
        });
      }

      // 공통 코드 수정
      function updateCodeMng() {
        var codeItems = subGrid2.store.data.items;
        var codeJson = [];
        $(codeItems).each(function (i) {
          codeJson[i] = codeItems[i].data;
        });

        $.ajax({
          type: "put",
          url: "/sys/codemng/codeMngUpdate.do",
          data: {
            jsonData: JSON.stringify(codeJson)
          },
          success: function (res) {
            alert(res.dataMsg);
            if (res.dataStatus === "SUCCESS_UPDATE") {
              searchAll();
            }
          }
        });
      }

      // 그룹 코드 삭제
      function deleteCodeGrpMng() {
        var grpCnt = subGrid.getSelectionModel().getCount();
        var rows = subGrid.getSelectionModel().getSelection();
        var grpCd = "";
        for (var i = 0; i < grpCnt; i++) {
          grpCd += rows[i].data.grpCd + ",";
        }
        if (grpCnt === 0) {
          alert("그룹 코드를 선택하세요.");
        } else {
          if (confirm("선택한 그룹 코드를 삭제 하시겠습니까?")) {
            $.ajax({
              type: "delete",
              url: "/sys/codemng/codeGrpMngDelete.do",
              data: {
                grpCd: grpCd
              },
              success: function (res) {
                alert(res.dataMsg);
                if (res.dataStatus === "SUCCESS_DELETE") {
                  searchAll();
                }
              }
            });
          }
        }
      }

      // 공통 코드 삭제
      function deleteCodeMng() {
        var grpCd = $('input[name="grpCd"]').val();
        var codeCnt = subGrid2.getSelectionModel().getCount();
        var rows = subGrid2.getSelectionModel().getSelection();
        var comCd = "";
        for (var i = 0; i < codeCnt; i++) {
          comCd += rows[i].data.comCd + ",";
        }
        if (grpCd === "") {
          alert("그룹 코드를 선택하세요.");
        } else if (codeCnt === 0) {
          alert("공통 코드를 선택하세요.");
        } else {
          if (confirm("선택한 공통 코드를 삭제 하시겠습니까?")) {
            $.ajax({
              type: "delete",
              url: "/sys/codemng/codeMngDelete.do",
              data: {
                grpCd: $('input[name="grpCd"]').val(),
                comCd: comCd
              },
              success: function (res) {
                alert(res.dataMsg);
                if (res.dataStatus === "SUCCESS_DELETE") {
                  searchAll();
                }
              }
            });
          }
        }
      }

      // 그룹 코드 엑셀
      function excelCodeGrpMng() {
        var form = $('form[name="codeMngSearchForm"]');
        $(form).attr({
          action: "/sys/codemng/codeGrpMngExcel.do"
        }).submit();
      }

      // 공통 코드 엑셀
      function excelCodeMng() {
        var form = $('form[name="codeMngSearchForm"]');
        $(form).attr({
          action: "/sys/codemng/codeMngExcel.do"
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

            <form:form modelAttribute="searchDTO" name="codeMngSearchForm" method="post">
                <div class="contents-box search-area">
                    <div class="row">
                        <div class="col-md-5 col-sm-12 col-xs-12 padding_l25">
                            <div class="col-md-1 col-sm-12 col-xs-12 padding_none">
                                <span class="search-icon">구분</span>
                            </div>
                            <div class="col-md-3 col-sm-12 col-xs-12 padding_none">
                                <form:select path="searchCd" cssClass="form-control input-sm pull-left">
                                    <form:option value="grpCd" label="그룹 코드"/>
                                    <form:option value="grpNm" label="그룹 코드명"/>
                                    <form:option value="comCd" label="공통 코드"/>
                                    <form:option value="comNm" label="공통 코드명"/>
                                </form:select>
                            </div>
                            <div class="col-md-6 col-sm-12 col-xs-12 padding_r0">
                                <form:input path="searchWord" cssClass="form-control input-sm"
                                            onkeydown="if(event.keyCode==13){searchOne(); return false;}"/>
                            </div>
                        </div>
                        <div class="col-md-offset-6 col-md-1 col-sm-12 col-xs-12 askbtn">
                            <div class="search-btn-area text_r">
                                <button type="button" onclick="searchOne();" class="btn btn-gray margin_none">
                                    <i class="fa fa-search"></i>조회
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>

            <form:form modelAttribute="searchDTO" name="codeMngForm" method="post">
                <form:hidden path="grpCd"/>
                <form:hidden path="comCd"/>
                <div class="contents-box grid-area border_none">
                    <div class="grid-box2">
                        <ul class="nav nav-pills">
                            <li class="text-left">
                                <p class="contents-title">
                                    <i class="fa fa-arrow-circle-right"></i>그룹 코드
                                    <span class="sub-title-info"></span>
                                </p>
                            </li>
                            <li class="pull-right">
                                <div class="text-right">
                                    <div class="grid-option-box">
                                        <button type="button" onclick="excelCodeGrpMng();" class="btn btn-green">
                                            <i class="fa fa-file-excel-o" aria-hidden="true"></i><span>EXCEL</span>
                                        </button>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div id="codeGrpMngGrid"></div>
                        <div class="btn-right-area">
                            <button type="button" onclick="insertCodeGrpMng();" class="btn btn-red">
                                <i class="fa fa-pencil-square-o"></i>추가
                            </button>
                            <button type="button" onclick="updateCodeGrpMng();" class="btn btn-red">
                                <i class="fa fa-floppy-o"></i>저장
                            </button>
                            <button type="button" onclick="deleteCodeGrpMng();" class="btn btn-red">
                                <i class="fa fa-trash"></i>삭제
                            </button>
                        </div>
                    </div>

                    <div class="grid-box2">
                        <ul class="nav nav-pills">
                            <li class="text-left">
                                <p class="contents-title">
                                    <i class="fa fa-arrow-circle-right"></i>공통 코드
                                    <span class="sub-title-info"></span>
                                </p>
                            </li>
                            <li class="pull-right">
                                <div class="text-right">
                                    <div class="grid-option-box">
                                        <button type="button" onclick="excelCodeMng();" class="btn btn-green">
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
                                                     onchange="searchAll();">
                                            <c:forEach var="pageVO" items="${pageList}" varStatus="status">
                                                <form:option value="${pageVO.comCd}" label="${pageVO.comNm}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div id="codeMngGrid"></div>
                        <div class="btn-right-area">
                            <button type="button" onclick="insertCodeMng();" class="btn btn-red">
                                <i class="fa fa-pencil-square-o"></i>추가
                            </button>
                            <button type="button" onclick="updateCodeMng();" class="btn btn-red">
                                <i class="fa fa-floppy-o"></i>저장
                            </button>
                            <button type="button" onclick="deleteCodeMng();" class="btn btn-red">
                                <i class="fa fa-trash"></i>삭제
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
