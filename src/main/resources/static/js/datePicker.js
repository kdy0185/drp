/**
 * @Class : datePicker.js
 * @Author : KDW
 * @Date : 2022-01-21
 * @Description : 달력 위젯
 */
$(function () {
  $.datepicker.regional["ko"] = {
    monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월",
      "11월", "12월"],
    monthNamesShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월",
      "10월", "11월", "12월"],
    dayNames: ["일", "월", "화", "수", "목", "금", "토"],
    dayNamesShort: ["일", "월", "화", "수", "목", "금", "토"],
    dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
    firstDay: 0,
    isRTL: false,
    showMonthAfterYear: true
  };
  $.datepicker.setDefaults($.datepicker.regional["ko"]);
});

// 일별 달력 표시
function setDatePicker(obj) {
  var startId = $(obj).closest("div").find('input[name$="Date"]:first').attr("id");
  var endId = $(obj).closest("div").find('input[name$="Date"]:last').attr("id");
  var dates = $("#" + startId + ", #" + endId).datepicker({
    prevText: "이전 달",
    nextText: "다음 달",
    dateFormat: "yy-mm-dd",
    showAnim: "fadeIn",
    maxDate: "+10y",
    minDate: "-70y",
    yearRange: "-70:+10",
    changeYear: true,
    showOtherMonths: true,
    changeMonth: true,
    autoSize: true,
    fixFocusIE: false,
    onSelect: function (selectedDate) {
      var option = this.id === startId ? "minDate" : "maxDate",
          instance = $(this).data("datepicker"),
          date = $.datepicker.parseDate(
              instance.settings.dateFormat || $.datepicker._defaults.dateFormat,
              selectedDate, instance.settings);
      dates.not(this).datepicker("option", option, date);
      this.fixFocusIE = true;
      $(this).blur().change().focus();
    },
    onClose: function (dateText) {
      this.fixFocusIE = true;
      this.focus();
    },
    beforeShow: function (input) {
      var result = $.isIE() ? !this.fixFocusIE : true;
      this.fixFocusIE = false;
      return result;
    }
  });
  if ($(obj).attr("id") === startId) {
    $("#" + startId).datepicker("show");
  }
  if ($(obj).attr("id") === endId) {
    $("#" + endId).datepicker("show");
  }
}

// 시간 선택 영역 표시
function setTimePicker(obj) {
  var id = $(obj).attr("id");
  $("#" + id).timepicker({
    timeFormat: "HH:mm",
    interval: 10,
    dynamic: false,
    dropdown: true,
    scrollbar: true
  });
  $("#" + id).timepicker("open");
}
