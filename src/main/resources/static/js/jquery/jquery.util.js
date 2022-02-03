/**
 * @Class : jquery.util.js
 * @Author : KDW
 * @Date : 2022-01-21
 * @Description : 유틸리티 JS
 */
// 전역 변수 설정
var mainGrid, subGrid, subGrid2, subGrid3, popGrid, popGrid2, mainTree, subTree,
    mainChart, subChart, subChart2, subChart3;
var comHeight = 475; // 공통 그리드 Height
var comExtHeight = 600; // 공통 그리드 확장 Height
var comPopHeight = 265; // 공통 그리드 팝업 Height
var comTreeHeight = 696; // 공통 트리 Height
var comPopTreeHeight = 239; // 공통 트리 팝업 Height
var defaultPageSize = 20;
var paramArrNum; // 검색조건 파라미터 배열 넘버
var settingSearchParamFunction; // 검색조건 재귀 호출을 위한 function 저장 변수

// Date Format Prototype 지정
Date.prototype.format = function (f) {
  if (!this.valueOf()) {
    return " ";
  }

  var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
  var d = this;

  return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function ($1) {
    switch ($1) {
      case "yyyy":
        return d.getFullYear();
      case "yy":
        return (d.getFullYear() % 1000).zf(2);
      case "MM":
        return (d.getMonth() + 1).zf(2);
      case "dd":
        return d.getDate().zf(2);
      case "E":
        return weekName[d.getDay()];
      case "HH":
        return d.getHours().zf(2);
      case "hh":
        return ((h = d.getHours() % 12) ? h : 12).zf(2);
      case "mm":
        return d.getMinutes().zf(2);
      case "ss":
        return d.getSeconds().zf(2);
      case "a/p":
        return d.getHours() < 12 ? "오전" : "오후";
      default:
        return $1;
    }
  });
};
String.prototype.string = function (len) {
  var s = '', i = 0;
  while (i++ < len) {
    s += this;
  }
  return s;
};
String.prototype.zf = function (len) {
  return "0".string(len - this.length) + this;
};
Number.prototype.zf = function (len) {
  return this.toString().zf(len);
};

// Number Format Prototype 지정
Number.prototype.format = function () {
  if (this === 0) {
    return 0;
  }
  var reg = /(^[+-]?\d+)(\d{3})/;
  var n = (this + "");
  while (reg.test(n)) {
    n = n.replace(reg, "$1" + "," + "$2");
  }
  return n;
};

// String Format Prototype 지정
String.prototype.format = function () {
  var num = parseFloat(this);
  if (isNaN(num)) {
    return "0";
  }
  return num.format();
};

// ajax 공통 설정 : CSRF 적용 + blockUI 적용 + 로그인 여부 확인
(function ($) {
  var header = $('meta[name="_csrf_header"]').attr("content");
  var token = $('meta[name="_csrf"]').attr("content");
  $.ajaxSetup({
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
      xhr.setRequestHeader("AJAX", true);

      $.blockUI({
        css: {
          border: "none",
          padding: "15px",
          backgroundColor: "",
          "-webkit-border-radius": "10px",
          "-moz-border-radius": "10px",
          opacity: 1,
          color: "#fff"
        }
      });
    },
    complete: function () {
      $.unblockUI();
    },
    error: function (xhr) {
      if (xhr.status === 401) {
        alert("인증되지 않은 계정입니다.");
      } else if (xhr.status === 403) {
        if (confirm("세션이 만료되었습니다. 다시 로그인 하시겠습니까?")) {
          var targetUrl = location.pathname + "?menuCd=" + "${comsMenuVO.menuCd}";
          $.util.moveMenuTarget("/main/login/login.do", "A0200", "_csrf", token, targetUrl);
        }
      } else {
        alert("오류가 발생하였습니다.");
      }
    }
  });
})(jQuery);

(function ($) {
  /* prototype util - 전역 함수 추가 : S */
  // 빈 값 체크 ([], {} 포함)
  $.isEmpty = function (value) {
    return (value === "" || value == null || false || (typeof value
        == "object" && !Object.keys(value).length));
  };

  // Null 대체 함수
  $.nvl = function (value, replaceValue) {
    return !this.isEmpty(value) ? value : replaceValue;
  };

  // 다국어 처리 (msglib.jsp + message.properties)
  $.msg = function (key) {
    return globalMsg[key];
  };

  // IE 체크
  $.isIE = function () {
    var isIE = false;
    var agent = navigator.userAgent.toLowerCase();
    if ((navigator.userAgent.search('Trident') !== -1) || (agent.indexOf("msie") !== -1)) {
      isIE = true;
    }
    return isIE;
  };

  // 모바일 여부 체크
  $.isMobile = function () {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent);
  };

  // lpad 함수
  $.lpad = function (str, padLength, padStr) {
    while (str.length < padLength) {
      str = padStr + str;
    }
    return str;
  };

  // rpad 함수
  $.rpad = function (str, padLength, padStr) {
    while (str.length < padLength) {
      str += padStr;
    }
    return str;
  };

  // 중복 여부 확인
  $.isDup = function (data, list) {
    var isDup = false;
    var keyword = list.split(",");
    if ($.inArray(data, keyword) !== -1) {
      isDup = true;
    }
    return isDup;
  };

  // jsonObject Grouping
  $.groupingJson = function (xs, key) {
    var groupingJson = xs.reduce(function (rv, x) {
      (rv[x[key]] = rv[x[key]] || []).push(x);
      return rv;
    }, {});
    return Object.keys(groupingJson);
  };

  // Comma 생성
  $.commaNum = function (value) {
    value = value.toString();
    var minusSign = (value.substring(0, 1) === "-") ? "-" : ""; // 입력값의 음수, 양수 판별
    var absVal = value.replace(/[^\d]+/g, ''); // 숫자를 제외한 모든 기호 제거
    var commaVal = absVal.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,'); // 절대값 콤마 찍기
    return minusSign + commaVal;
  };

  // 입력 Comma 제거
  $.removeCommaNum = function (value) {
    value = value.toString();
    var minusSign = (value.substring(0, 1) === "-") ? "-" : ""; // 입력값의 음수, 양수 판별
    var absVal = value.replace(/[^\d]+/g, ''); // 숫자를 제외한 모든 기호 제거
    return minusSign + absVal;
  };
  /* prototype util - 전역 함수 추가 : E */

  /* prototype util - 객체 메소드 추가 : S */
  // 클래스 상호 교환
  $.fn.swapClass = function (cls1, cls2) {
    return this.each(function () {
      var $el = $(this);
      if ($el.hasClass(cls1)) {
        $el.removeClass(cls1).addClass(cls2);
      }
      if ($el.hasClass(cls2)) {
        $el.removeClass(cls2).addClass(cls1);
      }
    });
  };

  // 유효성 검사
  $.fn.validateForm = function () {
    return this.each(function () {
      var $form = $(this);
      var checkArea = $form.find("div.val-check-area");
      if (checkArea.length > 0) {
        // form tag name의 validation 유형별 배열화 ("name1 name2 name3 …")
        var arrIdDupCheckTag = [];
        var arrRequiredTag = [];
        var arrPwCheckTag = [];
        var arrPwCheckReqTag = [];
        var arrPwEqualToTag = [];
        var arrNumberTag = [];
        var arrDigitsTag = [];
        var arrDigitsReqTag = [];
        var arrAlphaNumericTag = [];
        var arrLessThanTag = [];
        var arrMoreThanTag = [];
        var arrDateTag = [];
        var arrDateReqTag = [];
        var arrDateTimeTag = [];
        var arrDateTimeReqTag = [];
        var arrMobileNumTag = [];
        var arrEmailTag = [];
        var arrUrlTag = [];
        $.each($($form.serializeArray()), function (i, field) {
          var type = $form.find("[name=" + field.name + "]").attr("type");
          var cls = $form.find("[name=" + field.name + "]").attr("class");
          if (type !== "hidden" && !$.isEmpty(cls)) {
            if (cls.indexOf("idDupCheck") > -1) {
              arrIdDupCheckTag.push(field.name);
            }
            if (cls.indexOf("required") > -1) {
              arrRequiredTag.push(field.name);
            }
            if (cls.indexOf("pwCheck") > -1) {
              arrPwCheckTag.push(field.name);
            }
            if (cls.indexOf("pwCheckReq") > -1) {
              arrPwCheckReqTag.push(field.name);
            }
            if (cls.indexOf("pwEqualTo") > -1) {
              arrPwEqualToTag.push(field.name);
            }
            if (cls.indexOf("number") > -1) {
              arrNumberTag.push(field.name);
            }
            if (cls.indexOf("digits") > -1) {
              arrDigitsTag.push(field.name);
            }
            if (cls.indexOf("digitsReq") > -1) {
              arrDigitsReqTag.push(field.name);
            }
            if (cls.indexOf("alphaNumeric") > -1) {
              arrAlphaNumericTag.push(field.name);
            }
            if (cls.indexOf("lessThan") > -1) {
              arrLessThanTag.push(field.name);
            }
            if (cls.indexOf("moreThan") > -1) {
              arrMoreThanTag.push(field.name);
            }
            if (cls.indexOf("date") > -1) {
              arrDateTag.push(field.name);
            }
            if (cls.indexOf("dateReq") > -1) {
              arrDateReqTag.push(field.name);
            }
            if (cls.indexOf("dateTime") > -1) {
              arrDateTimeTag.push(field.name);
            }
            if (cls.indexOf("dateTimeReq") > -1) {
              arrDateTimeReqTag.push(field.name);
            }
            if (cls.indexOf("mobileNum") > -1) {
              arrMobileNumTag.push(field.name);
            }
            if (cls.indexOf("email") > -1) {
              arrEmailTag.push(field.name);
            }
            if (cls.indexOf("url") > -1) {
              arrUrlTag.push(field.name);
            }
          }
        });

        // groups 설정 시 상단 input tag의 validation 유형부터, error의 범위가 좁은 것부터 설정해야 함
        $form.validate({
          debug: false,
          onfocusout: false,
          groups: {
            idDupCheckGroup: arrIdDupCheckTag.join(" "),
            requiredGroup: arrRequiredTag.join(" "),
            pwCheckGroup: arrPwCheckTag.join(" "),
            pwCheckReqGroup: arrPwCheckReqTag.join(" "),
            pwEqualToGroup: arrPwEqualToTag.join(" "),
            numberGroup: arrNumberTag.join(" "),
            digitsGroup: arrDigitsTag.join(" "),
            digitsReqGroup: arrDigitsReqTag.join(" "),
            alphaNumericGroup: arrAlphaNumericTag.join(" "),
            lessThanGroup: arrLessThanTag.join(" "),
            moreThanGroup: arrMoreThanTag.join(" "),
            dateGroup: arrDateTag.join(" "),
            dateReqGroup: arrDateReqTag.join(" "),
            dateTimeGroup: arrDateTimeTag.join(" "),
            dateTimeReqGroup: arrDateTimeReqTag.join(" "),
            mobileNumGroup: arrMobileNumTag.join(" "),
            emailGroup: arrEmailTag.join(" "),
            urlGroup: arrUrlTag.join(" ")
          },
          errorElement: "div",
          errorPlacement: function (error, element) {
            $form.find("div.val-check-area").append(error);
          },
          invalidHandler: function (form, validator) {
            var errors = validator.numberOfInvalids();
            if (errors) {
              validator.errorList[0].element.focus();
            }
          }
        });
      }
    });
  };
  /* prototype util - 객체 메소드 추가 : E */

  /* 확장 util - 전역 함수 추가 : S */
  $.util = {
    // 메뉴 이동
    moveMenu: function (url, menuCd, csrfParam, csrfToken) {
      this.moveMenuTarget(url, menuCd, csrfParam, csrfToken, "");
    },

    // 메뉴 이동
    moveMenuTarget: function (url, menuCd, csrfParam, csrfToken, targetUrl) {
      var form = $("<form></form>");
      $(form).attr("method", "post");
      $(form).attr("action", url);
      $(form).append(
          "<input type='hidden' name='menuCd' value='" + menuCd + "' />");
      if (targetUrl !== "") {
        $(form).append(
            "<input type='hidden' name='loginUrl' value='" + targetUrl + "' />");
      }
      $(form).append(
          "<input type='hidden' name='" + csrfParam + "' value='" + csrfToken + "' />");
      $(form).appendTo("body");
      $(form).submit();
    },

    // 메뉴 슬라이드
    menuSlide: function () {
      var flag = $(".contents-area").hasClass("slide-menu-ext");
      if (flag === true) {
        $(".contents-area").removeClass("slide-menu-ext");
      }

      // Left 메뉴 설정
      $(".width-slider").toggle();
      $("div.left-side").stop().fadeToggle("fast", function () {
        if (flag === false) {
          $(".contents-area").addClass("slide-menu-ext");
        }
        $(".main-table").find("div.contents-box").toggleClass("slide-menu-ext");

        // Grid Width 적용
        if (mainGrid != null) {
          mainGrid.setWidth(false);
        }
        if (subGrid != null) {
          subGrid.setWidth(false);
          subGrid2.setWidth(false);
        }
        if (subGrid3 != null) {
          subGrid3.setWidth(false);
        }
        if (mainTree != null) {
          mainTree.setWidth(false);
        }
        if (subTree != null) {
          subTree.setWidth(false);
        }
      });
    },

    // 화면 슬라이드
    screenSlide: function () {
      // Grid Height 설정
      var height = 0;
      if (mainGrid != null) {
        height = mainGrid.getHeight() === comHeight ? comExtHeight : comHeight;
      }
      if (subGrid != null) {
        height = subGrid.getHeight() === comHeight ? comExtHeight : comHeight;
      }

      $(".height-slider").toggle();
      $("div.search-area").slideToggle(function () {
        // Grid Height 적용
        if (mainGrid != null) {
          mainGrid.setHeight(height);
        }
        if (subGrid != null) {
          subGrid.setHeight(height);
          subGrid2.setHeight(height);
        }
        if (subGrid3 != null) {
          subGrid3.setHeight(height);
        }
      });
    },

    // jQuery Scroll Plugin 적용
    setScrollbar: function () {
      $(".scrollbar").mCustomScrollbar({
        theme: "rounded-dots",
        scrollInertia: 400
      });
    },

    // jQuery tooltip Plugin 적용
    setTooltip: function (obj) {
      var el = obj || $('[title!=""]');
      $(el).qtip({
        content: {
          title: function (event, api) {
            if (!$.isEmpty($(this).attr("title-header"))) {
              return $(this).attr("title-header");
            } else {
              api.set("content.title", null);
            }
          },
          text: $(this).attr("title")
        },
        position: {
          target: "mouse", // 마우스 포인터를 따라다니도록 설정
          viewport: $(window), // 전체 화면 기준을 브라우저 창 전체로 설정
          adjust: {
            x: 12, // 마우스 포인터와 툴팁 가로 축 사이 여백
            y: 5, // 마우스 포인터와 툴팁 세로 축 사이 여백
            method: "flipinvert none" // 툴팁이 윈도우 끝에 닿으면 x축 반전
          }
        },
        show: {
          effect: function (api) {
            $(this).fadeTo(500, 1);
          }
        },
        hide: {
          effect: function (api) {
            $(this).fadeTo(500, 0);
          }
        },
        style: {
          classes: "qtip-dark qtip-shadow qtip-rounded"
        }
      });
    },

    // 중앙 팝업 창 열기
    openPop: function (url, target, width, height) {
      var left = (screen.width - width) / 2;
      var top = (screen.height - height) / 2;
      var option = "width=" + width + ", height=" + height;
      option += ", top=" + top + ", left=" + left;
      option += ", scrollbars=yes, resizable=yes";
      window.open(url, target, option);
    },

    // dialog 팝업창 열기
    openDialog: function (title, width) {
      $("#popLayout").dialog({
        resizable: false,
        width: width,
        modal: true,
        draggable: true,
        istitle: false,
        title: title,
        closeOnEscape: true,
        open: function (event, ui) {
          $(".ui-dialog .ui-dialog-title").css("color", "#ffffff");
          $(".ui-widget-header").css("background", "#767c89");
          $(".ui-dialog-titlebar-close").addClass(
              "ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only ui-dialog-titlebar-close ui-icon ui-icon-closethick");
          $(this).find("input").keypress(function (e) {
            if ((e.which && e.which === 13) || (e.keyCode && e.keyCode === 13)) {
              $(this).parent().parent().parent().parent().find(
                  ".ui-dialog-buttonpane").find("button:first").click();
              return false;
            }
          });
        },
        close: function () {
          $(this).dialog("destroy").empty();
        }
      }).parent().position({
        my: "center",
        at: "center"
      });
    },

    // dialog 팝업창 닫기
    closeDialog: function () {
      $("#popLayout").dialog("close");
    },

    // 서브 dialog 팝업창 열기
    openSubDialog: function (title, width) {
      $("#subPopLayout").dialog({
        resizable: false,
        width: width,
        modal: true,
        draggable: true,
        istitle: false,
        title: title,
        closeOnEscape: true,
        open: function (event, ui) {
          $(".ui-dialog .ui-dialog-title").css("color", "#ffffff");
          $(".ui-widget-header").css("background", "#767c89");
          $(".ui-dialog-titlebar-close").addClass(
              "ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only ui-dialog-titlebar-close ui-icon ui-icon-closethick");
          $(this).find("input").keypress(function (e) {
            if ((e.which && e.which === 13) || (e.keyCode && e.keyCode === 13)) {
              $(this).parent().parent().parent().parent().find(
                  ".ui-dialog-buttonpane").find("button:first").click();
              return false;
            }
          });
        },
        close: function () {
          $(this).dialog("destroy").empty();
        }
      }).parent().position({
        my: "center",
        at: "center"
      });
    },

    // 서브 dialog 팝업창 닫기
    closeSubDialog: function () {
      if ($("#subPopLayout").parent().css("display") !== "none") {
        $("#subPopLayout").dialog("close");
      } else {
        this.closeDialog();
      }
    },

    // 조회 조건 스타일 설정
    setSearchStyle: function (searchArea) {
      this.removeSearchStyle(searchArea);

      var elements = $(searchArea).closest("form").serializeArray();
      var values = {};
      $.each($(elements), function (i, field) {
        values[field.name] = field.value;
      });
      $(searchArea).find("div.row").each(function () {
        $(this).children("div").each(function () {
          var span = $(this).find("span.search-icon");
          var input = $(this).find("input, select, radio, checkbox").filter(':not([name^="_"])');
          var isEmptyWord = true;
          $(input).each(function (i) {
            var name = $(this).attr("name");
            if (values[name] !== "" && values[name] != null) {
              isEmptyWord = false;
            }
          });
          if (!isEmptyWord) {
            $(span).addClass("search");
          } else {
            $(span).removeClass("search");
          }
        });
      });
    },

    // 조회 조건 스타일 제거
    removeSearchStyle: function (searchArea) {
      $(searchArea).find("span.search-icon").on("click", function () {
        var div = $(this).parent().parent();
        $(div).find("input:text, input:hidden").val("").trigger("change");
        $(div).find("select").each(function () {
          $(this).find("option").eq(0).prop("selected", true);
        });
        $(div).find("input:radio:first").prop("checked", true);
        $(div).find("input:checkbox").prop("checked", false);
        $(this).removeClass("search");
      });
    },

    // 우편번호 검색 (daum 우편번호 서비스)
    openPostPop: function () {
      var width = 500;
      var height = 500;
      new daum.Postcode({
        width: width,
        height: height,
        oncomplete: function (data) {
          var fullAddr = "";
          var extraAddr = "";

          if (data.userSelectedType === "R") { // 사용자가 도로명 주소를 선택했을 경우
            fullAddr = data.roadAddress;
          } else { // 사용자가 지번 주소를 선택했을 경우(J)
            fullAddr = data.jibunAddress;
          }

          // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
          if (data.userSelectedType === "R") {
            // 법정동명이 있을 경우 추가한다.
            if (data.bname !== "") {
              extraAddr += data.bname;
            }
            // 건물명이 있을 경우 추가한다.
            if (data.buildingName !== "") {
              extraAddr += (extraAddr !== "" ? ", " + data.buildingName
                  : data.buildingName);
            }
            // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
            fullAddr += (extraAddr !== "" ? " (" + extraAddr + ")" : "");
          }

          // 우편번호와 주소 정보를 해당 필드에 넣는다.
          document.getElementById("postNum").value = data.zonecode; // 5자리 새우편번호 사용
          document.getElementById("baseAddr").value = fullAddr;

          // 커서를 상세주소 필드로 이동한다.
          document.getElementById("detlAddr").focus();
        }
      }).open({
        left: (window.screen.width / 2) - (width / 2),
        top: (window.screen.height / 2) - (height / 2),
        autoClose: true // 기본값 true
      });
    },

    // localStorage 를 통한 검색 조건 유지 세팅
    setSearchCondition: function () {
      var form = $("form[name$='SearchForm']");
      var formNm = $(form).attr('name');
      // setItem
      var paramJson = [];
      $.each($($(form).serializeArray()), function (i, field) {
        var paramInfo = {};
        var tagName = $(form).find("[name=" + field.name + "]").prop("tagName");
        var type = $(form).find("[name=" + field.name + "]").prop("type");
        paramInfo.tagName = tagName;
        paramInfo.type = type;
        paramInfo.name = field.name;
        paramInfo.value = field.value;
        paramJson.push(paramInfo);
      });
      localStorage.setItem(formNm, JSON.stringify(paramJson));
    },

    // localStorage 를 통한 검색 조건 유지 목록 조회
    getSearchCondition: function () {
      var form = $("form[name$='SearchForm']");
      paramArrNum = 0;
      var formNm = $(form).attr('name');
      var jsonArr = JSON.parse(localStorage.getItem(formNm));
      if (jsonArr) {
        settingSearchParamFunction = this.setSearchConditionForm(
            jsonArr);
      }
    },

    // localStorage 조회 목록 세팅
    setSearchConditionForm: function (jsonArr) {
      if (jsonArr[paramArrNum]) { // 파라미터 객체가 있으면
        var json = jsonArr[paramArrNum];
        if (json.value) {
          $.when(settingSearchParamFunction).done(function () {
            if (json.tagName === "SELECT") {
              $(json.tagName + "[name='" + json.name + "']").val(
                  json.value).prop("selected", true).triggerHandler("change");
            } else {
              if (json.type === "radio"|| json.type === "checkbox") {
                $(json.tagName + "[name='" + json.name + "']:" + json.type
                    + "[value='" + json.value + "']").prop("checked",
                    true).triggerHandler("click");
              } else {
                $(json.tagName + "[name='" + json.name + "']").val(
                    json.value).triggerHandler("blur");
                $(json.tagName + "[name='" + json.name + "']").triggerHandler(
                    "change");
              }
            }
          });
        }
        paramArrNum++;
        return settingSearchParamFunction = this.setSearchConditionForm(jsonArr);
      } else { // 파라미터 객체가 없다면
        return "Success";
      }
    }
  }
  /* 확장 util - 전역 함수 추가 : E */
}(jQuery));

(function ($) {
  /* fineUpload util - 전역 함수 추가 : S */
  var uploader = [];
  var opts;

  $.fine = {
    // FineUploader 컴포넌트 생성
    init: function (i, elemId, options) {
      opts = $.extend({}, $.fine.defaults, options);

      uploader[i] = new qq.FineUploader({
        debug: true,
        autoUpload: false,
        multiple: opts.fileCnt === 1 ? false : true,
        maxConnections: opts.fileCnt,
        element: document.getElementById(elemId),
        request: { // 파일 업로드 요청
          endpoint: opts.fileUrl + "/insert" + opts.fileType + "Data.do",
          params: opts.params,
          customHeaders: {
            'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
          }
        },
        deleteFile: { // 파일 삭제 요청
          enabled: true,
          method: "post",
          endpoint: opts.fileUrl + "/delete" + opts.fileType + "Data.do",
          forceConfirm: true,
          confirmMessage: $.msg("js.fine.msg.confirm.delete"),
          params: opts.params,
          customHeaders: {
            'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
          }
        },
        messages: {
          typeError: $.msg("js.fine.msg.typeError"),
          sizeError: $.msg("js.fine.msg.sizeError"),
          defaultResponseError: $.msg("js.fine.msg.resError"),
          tooManyFilesError: $.msg("js.fine.msg.singleFile"),
          unsupportedBrowser: $.msg("js.fine.msg.notBrowser"),
          tooManyItemsError: $.msg("js.fine.msg.manyItems"),
          onLeave: $.msg("js.fine.msg.onLeave")
        },
        callbacks: {
          onSubmitted: function (id, name) {
            $("#" + elemId).find("li[qq-file-id='" + id + "']").children(
                'button[name="qq-down-btn"]').addClass("qq-hide");
          },
          onUpload: function (id, name) {
            if (opts.fileCnt < 10) {
              $.blockUI({
                css: {
                  border: "none",
                  padding: "15px",
                  backgroundColor: "",
                  "-webkit-border-radius": "10px",
                  "-moz-border-radius": "10px",
                  opacity: 1,
                  color: "#fff"
                }
              });
            }
            if (!opts.isUuid) {
              uploader[i].setUuid(id, uploader[i].getName(id));
            }
          },
          onComplete: function (id, name, responseJSON, xhr) {
            if (!opts.isSave) {
              uploader[i].clearStoredFiles();
            }

            if (responseJSON.success) {
              $("li[qq-file-id='" + id + "']").children(
                  'button[name="qq-down-btn"]').removeClass("qq-hide");
              $("li[qq-file-id='" + id + "']").children(
                  'input[name="path"]').val(responseJSON.path);
            }
            if (opts.fileCnt < 10) {
              $.unblockUI();
            }

            if (opts.postUpload) {
              var queuedCnt = uploader[i].getUploads(
                  {status: qq.status.QUEUED}).length; // 연결 대기 중
              var uploadingCnt = uploader[i].getUploads(
                  {status: qq.status.UPLOADING}).length; // 업로드 중
              if (queuedCnt + uploadingCnt === 0) {
                var successCnt = uploader[i].getUploads(
                    {status: qq.status.UPLOAD_SUCCESSFUL}).length; // 업로드 성공
                var failCnt = uploader[i].getUploads(
                    {status: qq.status.UPLOAD_FAILED}).length; // 업로드 실패
                fineUploadComplete(i, successCnt, failCnt);
              }
            }
          },
          onSubmitDelete: function (id) {
            var qqli = $("li[qq-file-id='" + id + "']");
            var newParams = {
              uuid: uploader[i].getUuid(id),
              name: uploader[i].getName(id),
              path: $("li[qq-file-id='" + id + "']").children(
                  'input[name="path"]').val()
            };
            var deleteParams = uploader[i]._deleteFileParamsStore.get(id);
            qq.extend(newParams, deleteParams);
            uploader[i].setDeleteFileParams(newParams);
          },
          onDeleteComplete: function (id, xhr, isError) {
            if (isError) {
              alert($.msg("js.fine.msg.error"));
            } else {
              opts.postDelete && fineDeleteComplete();
            }
          }
        },
        validation: {
          allowedExtensions: opts.fileExt,
          sizeLimit: opts.fileSize,
          itemLimit: 1000
        }
      });
    },

    // 업로드 템플릿 설정
    tpl: function (id) {
      document.getElementById(id).innerHTML =
          '<script type="text/template" id="qq-template">' +
          '    <div class="qq-uploader-selector qq-uploader" qq-drop-area-text="파일을 드래그 하여 추가하세요.">' +
          '    <div class="qq-upload-drop-area-selector qq-upload-drop-area" qq-hide-dropzone>' +
          '        <span class="qq-upload-drop-area-text-selector"></span>' +
          '    </div>' +
          '    <div id="addFileBtn_fineUpload" class="allbox">' +
          '    <span class="fine_span">숨김 안보임</span>' +
          '    <p class="fine_span_ani">전체 파일 진행률 <span>0</span>%</>' +
          '    <div class="buttons fine_plus">' +
          '        <div class="qq-upload-button-selector btn btn-green">' +
          '            <div>' + '<i class="fa fa-file" aria-hidden="true"></i>' + $.msg("js.fine.button.addFile") + '</div>' +
          '        </div>' +
          '    </div>' +
          '    <div class="qq-total-progress-bar-container-selector qq-total-progress-bar-container " style="display:block;">' +
          '    	<div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-total-progress-bar-selector qq-progress-bar qq-total-progress-bar posi_ap" id="ani_valu"></div>' +
          '    </div>' +
          '    </div>' +
          '    <span class="qq-drop-processing-selector qq-drop-processing">' +
          '        <span>Processing dropped files...</span>' +
          '        <span class="qq-drop-processing-spinner-selector qq-drop-processing-spinner"></span>' +
          '    </span>' +
          '    <ul class="qq-upload-list-selector qq-upload-list" aria-live="polite" aria-relevant="additions removals">' +
          '        <li>' +
          '            <div class="qq-progress-bar-container-selector">' +
          '                <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-progress-bar-selector qq-progress-bar"></div>' +
          '            </div>' +
          '            <span class="qq-upload-spinner-selector qq-upload-spinner"></span>' +
          '            <span class="qq-upload-file-selector qq-upload-file"></span>' +
          '            <span class="qq-edit-filename-icon-selector qq-edit-filename-icon" aria-label="Edit filename"></span>' +
          '            <input class="qq-edit-filename-selector qq-edit-filename" name="fileNm" tabindex="0" type="text">' +
          '            <span class="qq-upload-size-selector qq-upload-size" name="qq-fileSize"></span>' +
          '            <input type="hidden" name="path">' +
          '            <button type="button" class="qq-btn qq-upload-cancel-selector qq-upload-cancel float_r"title="Cancel"><i class="fa fa-times" aria-hidden="true"></i></button>' +
          '            <button type="button" class="qq-btn qq-upload-retry-selector qq-upload-retry float_r" title="Retry"><i class="fa fa-undo" aria-hidden="true"></i></button>' +
          '            <button type="button" name="qq-del-btn" class="qq-btn qq-upload-delete-selector qq-upload-delete float_r" title="Delete"><i class="fa fa-trash-o" aria-hidden="true"></i></button>' +
          '            <button type="button" name="qq-down-btn" class="qq-btn qq-upload-down-selector qq-upload-download float_r"  title="Download" onclick="$.fine.download(this);"><i class="fa fa-download" aria-hidden="true"></i></button>' +
          '            <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>' +
          '        </li>' +
          '    </ul>' +
          '    <dialog class="qq-alert-dialog-selector">' +
          '        <div class="qq-dialog-message-selector"></div>' +
          '        <div class="qq-dialog-buttons">' +
          '            <button type="button" class="qq-cancel-button-selector">Close</button>' +
          '        </div>' +
          '    </dialog>' +
          '    <dialog class="qq-confirm-dialog-selector">' +
          '        <div class="qq-dialog-message-selector"></div>' +
          '        <div class="qq-dialog-buttons">' +
          '            <button type="button" class="qq-ok-button-selector">' + $.msg("js.fine.button.confirm") + '</button>' +
          '            <button type="button" class="qq-cancel-button-selector">' + $.msg("js.fine.button.cancel") + '</button>' +
          '        </div>' +
          '    </dialog>' +
          '    <dialog class="qq-prompt-dialog-selector">' +
          '        <div class="qq-dialog-message-selector"></div>' +
          '        <input type="text">' +
          '        <div class="qq-dialog-buttons">' +
          '            <button type="button" class="qq-ok-button-selector">' + $.msg("js.fine.button.confirm") + '</button>' +
          '            <button type="button" class="qq-cancel-button-selector">' + $.msg("js.fine.button.cancel") + '</button>' +
          '        </div>' +
          '    </dialog>' +
          '</script>'
    },

    // 기존 파일 설정
    setFile: function (fileList) {
      fileList = fileList.replace(/\r\n/g, "_");
      fileList = fileList.replace(/\\/gi, "/");
      var jsonArr = JSON.parse(fileList);
      for (var i in jsonArr) {
        if (jsonArr[i].length > 0) {
          if (jsonArr[i][0].name !== "") {
            uploader[i].addInitialFiles(jsonArr[i]);
            var uploadCnt = uploader[i].getUploads(
                {status: qq.status.UPLOAD_SUCCESSFUL}).length;
            if (uploadCnt > 0) {
              for (var j = 0; j < jsonArr[i].length; j++) {
                var jsonObject = jsonArr[i][j];
                if (jsonObject.size === "" || jsonObject.size == null) {
                  $("li[qq-file-id='" + j + "']").children('span[name="qq-fileSize"]').addClass("qq-hide");
                  $("li[qq-file-id='" + j + "']").children('input[name="path"]').val(jsonObject.path);
                  if (!opts.isBtn) {
                    $("div#addFileBtn_fineUpload").addClass("qq-hide");
                    $("li[qq-file-id='" + j + "']").children(
                        'button[name="qq-del-btn"]').addClass("qq-hide");
                  }
                }
              }
            }
          }
        }
      }
    },

    // 파라미터 설정
    setParam: function (i, obj) {
      uploader[i].setParams(obj);
      uploader[i].setDeleteFileParams(obj);
    },

    // 유효성 검사
    validate: function () {
      var isValid = true;
      $(uploader).each(function (i) {
        var uploadCnt = uploader[i].getUploads(
            {status: qq.status.SUBMITTED}).length;
        var successCnt = uploader[i].getUploads(
            {status: qq.status.UPLOAD_SUCCESSFUL}).length;

        if (opts.fileCnt === 1) { // 파일 설정이 1개일 때 제출할 파일 수와 제출 완료된 파일 수가 모두 0일때 Validation 처리
          if (uploadCnt === 0 && successCnt === 0) {
            isValid = false;
          }
        } else {
          if (uploadCnt === 0) {
            isValid = false;
          }
        }
      });
      if (!isValid) {
        $("div.val-check-area").html(
            "<div id='fileValidation-error' class='error'>" + $.msg("js.fine.msg.notExist") + "</div>");
        throw "upload validation stopped.";
      } else {
        $("div.val-check-area").html("");
      }
    },

    // 파일 업로드
    upload: function (i) {
      var fileCnt = uploader[i].getUploads(
          {status: qq.status.SUBMITTED}).length;
      if (fileCnt > 0) {
        uploader[i].uploadStoredFiles();
      }
      return fileCnt;
    },

    // 파일 다운로드
    download: function (obj) {
      var id = $(obj).closest('div[id^="fine-uploader"]').attr("id");
      var i = Number(id.substr(13) - 1);
      var uuid = uploader[i].getUuid($(obj).closest("li").attr("qq-file-id"));
      var name = uploader[i].getName($(obj).closest("li").attr("qq-file-id"));
      var path = $(obj).closest("li").children('input[name="path"]').val();
      path = path.replace(/\\\\/gi, '/'); // 역슬래쉬 두개를 슬래쉬 한개로 변환
      if (!opts.isUuid) {
        uuid = name;
      }
      location.href = "/coms/comsFileDownLoad.do?uuid=" + uuid + "&name=" + name + "&path=" + path;
    }
  }

  // fineUpload 초기 옵션 설정
  $.fine.defaults = {
    isUuid: true, // 고유 시퀀스(uuid) 사용 여부
    isSave: true, // 파일 저장 후 다운로드 가능 여부
    isBtn: true, // 추가/삭제 버튼 사용 여부
    postUpload: true, // 파일 업로드 후 callback 함수 사용 여부
    postDelete: true, // 파일 삭제 후 callback 함수 사용 여부
    fileUrl: "", // 실제 파일 관련 작업을 처리할 URL
    fileType: "File", // 업로드 방식 (파일 : File, 엑셀 : Excel)
    fileSize: "100000000", // 최대 용량 (1e+8 Byte = 100MB)
    fileCnt: 1, // 최대 업로드 파일 수 (기본 : 1)
    fileExt: [], // 허용 파일 확장자 (기본 : 제한 없음)
    params: [] // 추가 파라미터 (기본 : 없음)
  }
  /* fineUpload util - 전역 함수 추가 : E */
}(jQuery));
