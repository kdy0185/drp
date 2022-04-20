/**
 * @Class : jquery.validate.extend.js
 * @Author : KDW
 * @Date : 2022-01-21
 * @Description : Validation 확장 JS
 */
// Validation Rules 추가
$.validator.addMethod("idDupCheck", function (value, element) {
  var result;
  $.ajax({
    type: "post",
    async: false,
    url: "/sys/usermng/userMngIdDupCheck.do",
    data: {
      userId: value
    },
    success: function (res) {
      result = res.dataStatus;
    }
  });
  return result === "SUCCESS" && /^[A-Za-z0-9_-]{6,20}$/.test(value);
});
$.validator.addMethod("pwCheck", function (value, element) {
  return this.optional(element)
      || /^(?=^.{8,16}$)(?=.*\d)(?=.*[a-zA-Z]).*$/.test(value);
});
$.validator.addMethod("pwCheckReq", function (value, element) {
  return !$.isEmpty(value) && /^(?=^.{8,16}$)(?=.*\d)(?=.*[a-zA-Z]).*$/.test(value);
});
$.validator.addMethod("pwEqualTo", function (value, element) {
  return value === $(element).closest("form").find('input:password:first').val();
});
$.validator.addMethod("digitsReq", function (value, element) {
  return !$.isEmpty(value) && /^\d+(.\d)?$/.test(value);
});
$.validator.addMethod("alphaNumeric", function (value, element) {
  return !$.isEmpty(value) && /^[a-zA-Z0-9]+$/.test(value);
});
$.validator.addMethod("lessThan", function(value, element) {
	var maxValue = $(element).closest("form").find("input[id=" + $(element).attr("name") + "]").val();
	return !$.isEmpty(value) && /^\d+$/.test(value) && Number(value) <= Number(maxValue);
}, function(param, element) {
	var maxName = $(element).closest("form").find("input[id=" + $(element).attr("name") + "]").closest("td").prev().find("span").text();
	return maxName + " 이하의 수를 입력해야 합니다.";
});
$.validator.addMethod("moreThan", function(value, element) {
	var minValue = $(element).closest("form").find("input[id=" + $(element).attr("name") + "]").val();
	return !$.isEmpty(value) && /^\d+$/.test(value) && Number(value) >= Number(minValue);
}, function(param, element) {
	var minName = $(element).closest("form").find("input[id=" + $(element).attr("name") + "]").closest("td").prev().find("span").text();
	return minName + " 이상의 수를 입력해야 합니다.";
});
$.validator.addMethod("date", function (value, element) {
  return this.optional(element)
      || /^(19|20)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$/.test(value);
});
$.validator.addMethod("dateReq", function (value, element) {
  return !$.isEmpty(value)
      && /^(19|20)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$/.test(value);
});
$.validator.addMethod("dateTime", function (value, element) {
  return this.optional(element) || /^([01][0-9]|2[0-3]):([0-5][0-9])$/.test(value);
});
$.validator.addMethod("dateTimeReq", function (value, element) {
  return !$.isEmpty(value) && /^([01][0-9]|2[0-3]):([0-5][0-9])$/.test(value);
});
$.validator.addMethod("mobileNum", function (value, element) {
  return this.optional(element) || /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/.test(value);
});

// Validation Message 지정
$.extend($.validator.messages, {
  idDupCheck: $.msg("js.valid.msg.idDupCheck"),
  required: $.msg("js.valid.msg.required"),
  pwCheck: $.msg("js.valid.msg.pwCheck"),
  pwCheckReq: $.msg("js.valid.msg.pwCheckReq"),
  pwEqualTo: $.msg("js.valid.msg.pwEqualTo"),
  number: $.msg("js.valid.msg.number"),
  digits: $.msg("js.valid.msg.digits"),
  digitsReq: $.msg("js.valid.msg.digitsReq"),
  alphaNumeric: $.msg("js.valid.msg.alphaNumeric"),
  date: $.msg("js.valid.msg.date"),
  dateReq: $.msg("js.valid.msg.dateReq"),
  dateTime: $.msg("js.valid.msg.dateTime"),
  dateTimeReq: $.msg("js.valid.msg.dateTimeReq"),
  mobileNum: $.msg("js.valid.msg.mobileNum"),
  email: $.msg("js.valid.msg.email"),
  url: $.msg("js.valid.msg.url")
});
