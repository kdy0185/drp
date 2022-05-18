<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : planSettleDayDetail.jsp
     * @Author : KDW
     * @Date : 2022-01-28
     * @Description : 일일 결산 상세
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">
  // Ext Chart Store 정의 - 분류별 할당 시간
  var planSettleDayTimeStore = Ext.create("Ext.data.JsonStore", {
    fields: ["rtneCtgNm", "rtneAssignCnt"],
    proxy: {
      type: "ajax",
      url: "/pl/settle/planSettleDayTimeSearch.do",
      reader: {
        type: "json",
        root: "timeList"
      }
    }
  });

  // Ext Chart Store 정의 - 일과별 달성률
  var planSettleDayAchvRateStore = Ext.create("Ext.data.JsonStore", {
    fields: ["achvRate", "rtneCnt"],
    proxy: {
      type: "ajax",
      url: "/pl/settle/planSettleDayAchvRateSearch.do",
      reader: {
        type: "json",
        root: "achvRateList"
      }
    }
  });

  // Ext Chart Store 정의 - 일과별 몰입도
  var planSettleDayConcRateStore = Ext.create("Ext.data.JsonStore", {
    fields: ["concRate", "rtneCnt"],
    proxy: {
      type: "ajax",
      url: "/pl/settle/planSettleDayConcRateSearch.do",
      reader: {
        type: "json",
        root: "concRateList"
      }
    }
  });

  // 페이지 이동 시 파라미터 재생성 - 분류별 할당 시간
  planSettleDayTimeStore.on("beforeload", function (store, operation) {
    var form = $('form[name="planSettleDayForm"]');
    var rtneDate = $(form).find('input[name="rtneDate"]').val();
    var planUser = $(form).find('input[name="planUser"]').val();
    operation.params = {
      rtneDate: rtneDate,
      planUser: planUser
    };
  }, subChart);

  // 페이지 이동 시 파라미터 재생성 - 일과별 달성률
  planSettleDayAchvRateStore.on("beforeload", function (store, operation) {
    var form = $('form[name="planSettleDayForm"]');
    var rtneDate = $(form).find('input[name="rtneDate"]').val();
    var planUser = $(form).find('input[name="planUser"]').val();
    operation.params = {
      rtneDate: rtneDate,
      planUser: planUser
    };
  }, subChart2);

  // 페이지 이동 시 파라미터 재생성 - 일과별 몰입도
  planSettleDayConcRateStore.on("beforeload", function (store, operation) {
    var form = $('form[name="planSettleDayForm"]');
    var rtneDate = $(form).find('input[name="rtneDate"]').val();
    var planUser = $(form).find('input[name="planUser"]').val();
    operation.params = {
      rtneDate: rtneDate,
      planUser: planUser
    };
  }, subChart3);

  Ext.onReady(function () {
    Ext.QuickTips.init();

    // Ext Chart 정의 - 분류별 할당 시간
    var planSettleDayTimeChart = Ext.create("Ext.chart.Chart", {
      animate: true,
      shadow: true,
      width: 360,
      height: comPopHeight,
      theme: "Base:gradients",
      insetPadding: 10,
      store: planSettleDayTimeStore,
      legend: {
        field: "rtneCtgNm",
        position: "right",
        boxStrokeWidth: 0,
        labelFont: "11px nanumGothic"
      },
      series: [{
        type: "pie",
        angleField: "rtneAssignCnt",
        donut: 40,
        showInLegend: true,
        label: {
          field: "rtneCtgNm",
          display: "none",
          calloutLine: true
        },
        highlight: {
          fill: "#60697b",
          "stroke-width": 0.6,
          stroke: "#ccc"
        },
        tips: {
          trackMouse: true,
          width: 150,
          height: 20,
          style: "background: #FFF",
          renderer: function (storeItem, item) {
            var total = 0;
            for (var i = 0; i < planSettleDayTimeStore.getCount(); i++) {
              total += Number(planSettleDayTimeStore.getAt(i).get("rtneAssignCnt"));
            }
            this.setTitle(
                storeItem.get("rtneCtgNm") + " : " + (storeItem.get("rtneAssignCnt") / total
                    * 100).toFixed(2) + "%");
          }
        }
      }],
      renderTo: "planSettleDayTimeChart"
    });
    subChart = planSettleDayTimeChart;

    // Ext Chart 정의 - 일과별 달성률
    var planSettleDayAchvRateChart = Ext.create("Ext.chart.Chart", {
      animate: true,
      shadow: true,
      width: 360,
      height: comPopHeight,
      theme: "Base:gradients",
      insetPadding: 10,
      store: planSettleDayAchvRateStore,
      legend: {
        field: "achvRate",
        position: "right",
        boxStrokeWidth: 0,
        labelFont: "11px nanumGothic"
      },
      series: [{
        type: "pie",
        angleField: "rtneCnt",
        donut: 40,
        showInLegend: true,
        label: {
          field: "achvRate",
          display: "none",
          calloutLine: true
        },
        highlight: {
          fill: "#60697b",
          "stroke-width": 0.6,
          stroke: "#ccc"
        },
        tips: {
          trackMouse: true,
          width: 150,
          height: 20,
          style: "background: #FFF",
          renderer: function (storeItem, item) {
            var total = 0;
            for (var i = 0; i < planSettleDayAchvRateStore.getCount(); i++) {
              total += Number(planSettleDayAchvRateStore.getAt(i).get("rtneCnt"));
            }
            this.setTitle(
                storeItem.get("achvRate") + " : " + (storeItem.get("rtneCnt") / total
                    * 100).toFixed(2) + "%");
          }
        }
      }],
      renderTo: "planSettleDayAchvRateChart"
    });
    subChart2 = planSettleDayAchvRateChart;

    // Ext Chart 정의 - 일과별 몰입도
    var planSettleDayConcRateChart = Ext.create("Ext.chart.Chart", {
      animate: true,
      shadow: true,
      width: 360,
      height: comPopHeight,
      theme: "Base:gradients",
      insetPadding: 10,
      store: planSettleDayConcRateStore,
      legend: {
        field: "concRate",
        position: "right",
        boxStrokeWidth: 0,
        labelFont: "11px nanumGothic"
      },
      series: [{
        type: "pie",
        angleField: "rtneCnt",
        donut: 40,
        showInLegend: true,
        label: {
          field: "concRate",
          display: "none",
          calloutLine: true
        },
        highlight: {
          fill: "#60697b",
          "stroke-width": 0.6,
          stroke: "#ccc"
        },
        tips: {
          trackMouse: true,
          width: 150,
          height: 20,
          style: "background: #FFF",
          renderer: function (storeItem, item) {
            var total = 0;
            for (var i = 0; i < planSettleDayConcRateStore.getCount(); i++) {
              total += Number(planSettleDayConcRateStore.getAt(i).get("rtneCnt"));
            }
            this.setTitle(
                storeItem.get("concRate") + " : " + (storeItem.get("rtneCnt") / total
                    * 100).toFixed(2) + "%");
          }
        }
      }],
      renderTo: "planSettleDayConcRateChart"
    });
    subChart3 = planSettleDayConcRateChart;
  });

  $(function () {
    $.util.setScrollbar();
    selectPlanSettleDay();
  });

  // 조회 항목 선택
  function selectPlanSettleDay() {
    var form = $('form[name="planSettleDayDetailForm"]');
    var settleType = $(form).find('select[name="settleType"] option:selected').val();

    $('div[id$="Area"]').hide();
    $('div[id="' + settleType + '"]').show();

    // 차트 로드
    if (settleType === "timeArea") planSettleDayTimeStore.load();
    if (settleType === "achvRateArea") planSettleDayAchvRateStore.load();
    if (settleType === "concRateArea") planSettleDayConcRateStore.load();
  }
</script>

<form:form modelAttribute="detailDTO" name="planSettleDayDetailForm" method="post">
    <ul class="nav nav-pills">
        <li class="text-left">
            <p class="contents-title">
                <i class="fa fa-arrow-circle-right"></i>일일 결산 상세 (${detailDTO.rtneDate})
            </p>
        </li>
        <li class="pull-right">
            <div class="text-right">
                <div class="grid-option-box">
                    <form:select path="settleType" cssClass="form-control input-sm pull-right" onchange="selectPlanSettleDay();">
                        <form:option value="timeArea" label="분류별 할당 시간"/>
                        <form:option value="achvRateArea" label="일과별 달성률"/>
                        <form:option value="concRateArea" label="일과별 몰입도"/>
                    </form:select>
                </div>
            </div>
        </li>
    </ul>

    <!-- 분류별 할당 시간 : S -->
    <div id="timeArea">
        <table class="table table-mini margin_b0">
            <colgroup>
                <col style="width: 25%"/>
                <col style="width: 25%"/>
                <col style="width: 25%"/>
                <col style="width: 25%"/>
            </colgroup>
            <thead>
            <tr>
                <th>분류</th>
                <th>할당 시간</th>
                <th>할당 비율</th>
                <th>일과 수</th>
            </tr>
            </thead>
        </table>
        <div class="table_over scrollbar" data-mcs-theme="rounded-dots">
            <table class="table table-mini">
                <colgroup>
                    <col style="width: 25%"/>
                    <col style="width: 25%"/>
                    <col style="width: 25%"/>
                    <col style="width: 25%"/>
                </colgroup>
                <tbody>
                <c:choose>
                    <c:when test="${not empty timeList}">
                        <c:forEach var="timeVO" items="${timeList}" varStatus="status">
                            <tr>
                                <td>${timeVO.rtneCtgNm}</td>
                                <td>${timeVO.rtneAssignTime}</td>
                                <td>${timeVO.rtneAssignPer}</td>
                                <td>${timeVO.rtneCnt}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4">분류별 할당 시간 내역이 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
        <div class="text_c" id="planSettleDayTimeChart"></div>
    </div>
    <!-- 분류별 할당 시간 : E -->

    <!-- 일과별 달성률 : S -->
    <div id="achvRateArea">
        <table class="table table-mini margin_b0">
            <colgroup>
                <col style="width: 50%"/>
                <col style="width: 50%"/>
            </colgroup>
            <thead>
            <tr>
                <th>달성률</th>
                <th>일과 수</th>
            </tr>
            </thead>
        </table>
        <div class="table_over scrollbar" data-mcs-theme="rounded-dots">
            <table class="table table-mini">
                <colgroup>
                    <col style="width: 50%"/>
                    <col style="width: 50%"/>
                </colgroup>
                <tbody>
                <c:choose>
                    <c:when test="${not empty achvRateList}">
                        <c:forEach var="achvRateVO" items="${achvRateList}" varStatus="status">
                            <tr>
                                <td>${achvRateVO.achvRate}</td>
                                <td>${achvRateVO.rtneCnt}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="2">일과별 달성률 내역이 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
        <div class="text_c" id="planSettleDayAchvRateChart"></div>
    </div>
    <!-- 일과별 달성률 : E -->

    <!-- 일과별 몰입도 : S -->
    <div id="concRateArea">
        <table class="table table-mini margin_b0">
            <colgroup>
                <col style="width: 50%"/>
                <col style="width: 50%"/>
            </colgroup>
            <thead>
            <tr>
                <th>몰입도</th>
                <th>일과 수</th>
            </tr>
            </thead>
        </table>
        <div class="table_over scrollbar" data-mcs-theme="rounded-dots">
            <table class="table table-mini">
                <colgroup>
                    <col style="width: 50%"/>
                    <col style="width: 50%"/>
                </colgroup>
                <tbody>
                <c:choose>
                    <c:when test="${not empty concRateList}">
                        <c:forEach var="concRateVO" items="${concRateList}" varStatus="status">
                            <tr>
                                <td>${concRateVO.concRate}</td>
                                <td>${concRateVO.rtneCnt}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="2">일과별 몰입도 내역이 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
        <div class="text_c" id="planSettleDayConcRateChart"></div>
    </div>
    <!-- 일과별 몰입도 : E -->
</form:form>
