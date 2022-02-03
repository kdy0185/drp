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
  }
</script>

<form:form modelAttribute="planSettleVO" name="planSettleDayDetailForm" method="post">
    <ul class="nav nav-pills">
        <li class="text-left">
            <p class="contents-title">
                <i class="fa fa-arrow-circle-right"></i>일일 결산 상세 (${planSettleVO.rtneDate})
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
    </div>
    <!-- 일과별 몰입도 : E -->
</form:form>
