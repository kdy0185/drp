<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : main.jsp
     * @Author : KDW
     * @Date : 2022-01-21
     * @Description : 메인 페이지
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

    <script type="text/javascript" src="/js/moment.js"></script>
    <script type="text/javascript" src="/js/fullcalendar.js"></script>
    <script type="text/javascript" src="/js/locale-ko.js"></script>
    <link rel="stylesheet" href="/css/fullcalendar.css">

    <script type="text/javascript">
      $(function () {
        $.util.setScrollbar();
        setCalendar();
        setFrames();
      });

      // 일정
      function setCalendar() {
        var calendar = $("#calendar").fullCalendar({
          locale: "ko",
          header: {
            left: "prev,next today",
            center: "title",
            right: "month listMonth"
          },
          selectable: true,
          timeFormat: "HH:mm",
          buttonText: {
            month: "달력",
            listMonth: "일정"
          },
          eventLimit: true,
          eventRender: function (event, element) {
            element.qtip({
              content: event.title,
              position: {
                target: "mouse", // 마우스 포인터를 따라다니도록 설정
                adjust: {x: 12, y: 5} // 마우스 포인터와 툴팁 사이 여백
              },
              show: {
                effect: function () {
                  $(this).fadeTo(500, 1);
                }
              },
              hide: {
                effect: function () {
                  $(this).fadeTo(500, 0);
                }
              },
              style: {
                classes: "qtip-dark qtip-shadow qtip-rounded"
              }
            });
          },
          events: JSON.parse('${mainSkedList}')
        });
      }

      // 권한별 표시 영역 조정
      function setFrames() {
        // 루틴 권한이 없을 경우 루틴 표시 영역 삭제
        <%--if ("${mainVO.authRtne}" === "N") {--%>
        <%--  $("ol.carousel-indicators").children('li[id$="rtne"]').remove();--%>
        <%--  $("div.carousel-inner").children('div[id$="rtne"]').remove();--%>
        <%--}--%>
        // 거래 권한이 없을 경우 거래 표시 영역 삭제
        <%--if ("${mainVO.authAccnt}" === "N") {--%>
        <%--  $("ol.carousel-indicators").children('li[id$="accnt"]').remove();--%>
        <%--  $("div.carousel-inner").children('div[id$="accnt"]').remove();--%>
        <%--}--%>
        // 영역 조정 후 세팅
        $("ol.carousel-indicators").children("li").each(function (i) {
          $(this).attr("data-slide-to", i);
          if (i === 0) {
            $(this).addClass("active");
            var id = $(this).attr("id").substr(7);
            $("div.carousel-inner").children("div[id$=" + id + "]").addClass("active");
          }
        });
      }
    </script>
</head>
<body>
<%@ include file="/WEB-INF/jsp/cmmn/layout/header.jsp" %>
<div id="contents" class="container-fluid">
    <div class="row bgdark">
        <div class="contents-area col-md-12 col-sm-10 col-xs-12">
            <div class="col-md-4 col-sm-4 col-xs-4 padding_l0">
                <div class="main_box2">
                    <span class="main_title"><i class="fa fa-calendar" aria-hidden="true"></i> 전체 일정</span>
                    <div class="calender_box">
                        <div id="calendar"></div>
                    </div>
                </div>
                <div class="main_box2">
						<span class="main_title">
							<i class="fa fa-bullseye" aria-hidden="true"></i> 최신글 목록
						</span>
                    <div class="main_tbox news_box">
                        <table class="table table-mini margin_b0">
                            <colgroup>
                                <col style="width: 20%"/>
                                <col style="width: 40%"/>
                                <col style="width: 20%"/>
                                <col style="width: 20%"/>
                            </colgroup>
                            <thead>
                            <tr>
                                <th>구분</th>
                                <th>제목</th>
                                <th>등록자</th>
                                <th>등록일</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td colspan="4">최신글 목록이 없습니다.</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-8 col-sm-8 col-xs-8 padding_none">
                <div class="main_slider">
                    <div id="main-content" class="carousel slide" data-ride="carousel">
                        <ol class="carousel-indicators">
                            <li data-target="#main-content" data-slide-to="0" id="target-rtne" class=""></li>
                            <li data-target="#main-content" data-slide-to="1" id="target-accnt" class=""></li>
                        </ol>

                        <div class="carousel-inner">

                            <!-- 루틴 정보 : S -->
                            <div class="item" id="roll-rtne">
                                <div class="main_rolling_table_box">
                                    <div class="main_rolling_table2">
											<span class="main_title">
												<i class="fa fa-hand-o-right" aria-hidden="true"></i> 루틴 현황
												<span class="float_r">
													<a href="javascript:$.util.moveMenu('/pl/rtne/rtneList.do', 'P0100', '${_csrf.parameterName }', '${_csrf.token}');"><i class="fa fa-plus-square-o" aria-hidden="true"></i>더보기</a>
												</span>
											</span>
                                        <div class="main_tbox">
                                            <div class="col-md-4 col-sm-4 col-xs-4 padding_l0">
                                                <table class="table table-mini margin_b0">
                                                    <colgroup>
                                                        <col style="width: 50%"/>
                                                        <col style="width: 50%"/>
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th>구분</th>
                                                        <th>루틴명</th>
                                                    </tr>
                                                    </thead>
                                                </table>
                                                <div class="table_over">
                                                    <table class="table table-mini">
                                                        <colgroup>
                                                            <col style="width: 50%"/>
                                                            <col style="width: 50%"/>
                                                        </colgroup>
                                                        <tbody>
                                                        <tr>
                                                            <td colspan="2">루틴 정보가 없습니다.</td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="col-md-4 col-sm-4 col-xs-4 padding_l0">
                                                <table class="table table-mini margin_b0">
                                                    <colgroup>
                                                        <col style="width: 50%"/>
                                                        <col style="width: 50%"/>
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th>상세 구분</th>
                                                        <th>루틴명</th>
                                                    </tr>
                                                    </thead>
                                                </table>
                                                <div class="table_over scrollbar"
                                                     data-mcs-theme="rounded-dots">
                                                    <table class="table table-mini" id="rtneTable">
                                                        <colgroup>
                                                            <col style="width: 50%"/>
                                                            <col style="width: 50%"/>
                                                        </colgroup>
                                                        <tbody>
                                                        <tr>
                                                            <td colspan="2">상세 루틴 정보가 없습니다.</td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="col-md-4 col-sm-4 col-xs-4 padding_none">
                                                <table class="table table-mini margin_b0">
                                                    <colgroup>
                                                        <col style="width: 50%"/>
                                                        <col style="width: 50%"/>
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th>구분</th>
                                                        <th>루틴명</th>
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
                                                        <tr>
                                                            <td colspan="2">구분별 루틴 정보가 없습니다.</td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="main-chart">
                                        <div id="rtneChart"></div>
                                        <div id="subRtneChart"></div>
                                        <div id="rtneTypeChart"></div>
                                    </div>
                                </div>
                            </div>
                            <!-- 루틴 정보 : E -->

                            <!-- 거래 정보 : S -->
                            <div class="item" id="roll-accnt">
                                <div class="main_rolling_table_box">
                                    <div class="main_rolling_table1">
											<span class="main_title">
												<i class="fa fa-hand-o-right" aria-hidden="true"></i> 거래 현황
												<span class="float_r">
													<a href="javascript:$.util.moveMenu('/ac/accnt/accntList.do', 'M0100', '${_csrf.parameterName }', '${_csrf.token }');"><i class="fa fa-plus-square-o" aria-hidden="true"></i>더보기</a>
												</span>
											</span>
                                        <div class="main_tbox">
                                            <table class="table table-mini margin_b0">
                                                <colgroup>
                                                    <col style="width: 10%"/>
                                                    <col style="width: 30%"/>
                                                    <col style="width: 20%"/>
                                                    <col style="width: 40%"/>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th>순번</th>
                                                    <th>제품명</th>
                                                    <th>매출</th>
                                                    <th>기간</th>
                                                </tr>
                                                </thead>
                                            </table>
                                            <div class="table_over scrollbar" data-mcs-theme="rounded-dots">
                                                <table class="table table-mini">
                                                    <colgroup>
                                                        <col style="width: 10%"/>
                                                        <col style="width: 30%"/>
                                                        <col style="width: 20%"/>
                                                        <col style="width: 40%"/>
                                                    </colgroup>
                                                    <tbody>
                                                    <tr>
                                                        <td colspan="4">소재별 거래 정보가 없습니다.</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="main_rolling_table1">
											<span class="main_title">
												<i class="fa fa-hand-o-right" aria-hidden="true"></i> 일자별 거래 현황
												<span class="float_r">
													<a href="javascript:$.util.moveMenu('/ac/accnt/accntList.do', 'M0200', '${_csrf.parameterName }', '${_csrf.token }');"><i class="fa fa-plus-square-o" aria-hidden="true"></i>더보기</a>
												</span>
											</span>
                                        <div class="main_tbox">
                                            <table class="table table-mini margin_b0">
                                                <colgroup>
                                                    <col style="width: 25%"/>
                                                    <col style="width: 40%"/>
                                                    <col style="width: 20%"/>
                                                    <col style="width: 15%"/>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th>순번</th>
                                                    <th>제품명</th>
                                                    <th>매출</th>
                                                    <th>기간</th>
                                                </tr>
                                                </thead>
                                            </table>
                                            <div class="table_over scrollbar" data-mcs-theme="rounded-dots">
                                                <table class="table table-mini">
                                                    <colgroup>
                                                        <col style="width: 25%"/>
                                                        <col style="width: 40%"/>
                                                        <col style="width: 20%"/>
                                                        <col style="width: 15%"/>
                                                    </colgroup>
                                                    <tbody>
                                                    <tr>
                                                        <td colspan="4">일자별 거래 정보가 없습니다.</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="main-chart">
                                        <div id="accntMtrlChart"></div>
                                        <div id="accntDayChart"></div>
                                    </div>
                                </div>
                            </div>
                            <!-- 거래 정보 : E -->

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/cmmn/layout/footer.jsp" %>
</body>
</html>
