package com.jsplan.drp.cmmn.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : DateUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 날짜 처리
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * <p>날짜에 해당하는 포맷 구하기</p>
     *
     * <pre>
     * DateUtil.getFormat(null)                  = null
     * DateUtil.getFormat("")                    = null
     * DateUtil.getFormat("  ")                  = null
     * DateUtil.getFormat("1978122")             = null
     * DateUtil.getFormat("19781220")            = "yyyyMMdd"
     * DateUtil.getFormat("1978 12 20")          = "yyyy MM dd"
     * DateUtil.getFormat("1978-12-20")          = "yyyy-MM-dd"
     * DateUtil.getFormat("1978.12.20")          = "yyyy.MM.dd"
     * DateUtil.getFormat("1978/12/20")          = "yyyy/MM/dd"
     * DateUtil.getFormat("1978-12-20 13")       = "yyyy-MM-dd HH"
     * DateUtil.getFormat("1978-12-20 13:11")    = "yyyy-MM-dd HH:mm"
     * DateUtil.getFormat("1978-12-20 13:11:04") = "yyyy-MM-dd HH:mm:ss"
     * </pre>
     *
     * @param strDate (날짜형식 문자)
     * @return String (포맷)
     */
    public static String getFormat(String strDate) {
        if (StringUtil.isBlank(strDate)) {
            return null;
        }
        strDate = StringUtil.clean(strDate);
        String format = null;
        if (NumberUtil.isDigits(strDate) && strDate.length() == 8) {
            format = "yyyyMMdd";
        } else {
            if (StringUtil.isContain(strDate, "-")) {
                format = "yyyy-MM-dd";
            } else if (StringUtil.isContain(strDate, ".")) {
                format = "yyyy.MM.dd";
            } else if (StringUtil.isContain(strDate, "/")) {
                format = "yyyy/MM/dd";
            } else {
                format = "yyyy MM dd";
            }
            if (strDate.length() > 10) {
                if (StringUtil.countMatches(strDate, ":") == 2) {
                    format += " HH:mm:ss";
                } else if (StringUtil.countMatches(strDate, ":") == 1) {
                    format += " HH:mm";
                } else {
                    format += " HH";
                }
            }
        }
        return format;
    }

    /**
     * <p>날짜의 Date 객체 변경</p>
     *
     * @param strDate (날짜형식 문자)
     * @return Date (Date 객체)
     * @throws ParseException throws ParseException
     */
    public static Date parseDate(String strDate) throws ParseException {
        if (StringUtil.isBlank(strDate)) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat(getFormat(strDate));
        return sdf.parse(strDate);
    }

    /**
     * <p>지역설정을 반영한 날짜의 Date 객체 변경</p>
     *
     * @param strDate (날짜형식 문자)
     * @param locale  (지역설정 값)
     * @return Date (Date 객체)
     * @throws ParseException throws ParseException
     */
    public static Date parseDate(String strDate, Locale locale) throws ParseException {
        if (StringUtil.isBlank(strDate)) {
            return null;
        }
        if (locale == null) {
            locale = Locale.KOREA;
        }
        DateFormat sdf = new SimpleDateFormat(getFormat(strDate), locale);
        return sdf.parse(strDate);
    }

    /**
     * <p>Date 객체 포맷 변경</p>
     *
     * @param date   (Date 객체)
     * @param format (변경할 포맷)
     * @return String (변경 후 날짜)
     */
    public static String toDateFormat(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (StringUtil.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * <p>지역설정을 반영한 Date 객체 포맷 변경</p>
     *
     * @param date   (Date 객체)
     * @param format (변경할 포맷)
     * @param locale (지역설정 값)
     * @return String (변경 후 날짜)
     */
    public static String toDateFormat(Date date, String format, Locale locale) {
        if (date == null) {
            return "";
        }
        if (StringUtil.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if (locale == null) {
            locale = Locale.KOREA;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }

    /**
     * <p>지역설정을 반영한 날짜 포맷 변경</p>
     *
     * @param strDate (날짜형식 문자)
     * @param format  (변경할 포맷)
     * @param locale  (지역설정 값)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toDateFormat(String strDate, String format,
        Locale locale) throws ParseException {
        if (StringUtil.isBlank(strDate)) {
            return "";
        }
        if (StringUtil.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if (locale == null) {
            locale = Locale.KOREA;
        }
        Date date = parseDate(strDate, locale);
        return toDateFormat(date, format, locale);
    }

    /**
     * <p>날짜 형식 변경 (Korea, 'yyyy-MM-dd')</p>
     *
     * @param strDate (날짜형식 문자)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toKoreaDate1(String strDate) throws ParseException {
        return toDateFormat(strDate, "yyyy-MM-dd", Locale.KOREA);
    }

    /**
     * <p>날짜 형식 변경 (Korea, 'yyyy.MM.dd')</p>
     *
     * @param strDate (날짜형식 문자)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toKoreaDate2(String strDate) throws ParseException {
        return toDateFormat(strDate, "yyyy.MM.dd", Locale.KOREA);
    }

    /**
     * <p>날짜 형식 변경 (Korea, 'MM.dd(EEEE)')</p>
     *
     * @param strDate (날짜형식 문자)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toKoreaDate3(String strDate) throws ParseException {
        return toKoreaDate3(strDate, true);
    }

    /**
     * <p>날짜 형식 변경 (Korea, 'MM.dd(EEEE), MM.dd(EE)')</p>
     *
     * @param strDate (날짜형식 문자)
     * @param isLong  (요일의 스타일 false = 월, true = 월요일)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toKoreaDate3(String strDate, boolean isLong)
        throws ParseException {
        String format = isLong ? "MM.dd (EEEE)" : "MM.dd (EE)";
        return toDateFormat(strDate, format, Locale.KOREA);
    }

    /**
     * <p>날짜 형식 변경 (Korea, 'yyyy-MM-dd HH:mm')</p>
     *
     * @param strDate (날짜형식 문자)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toKoreaDate4(String strDate) throws ParseException {
        return toDateFormat(strDate, "yyyy-MM-dd HH:mm", Locale.KOREA);
    }

    /**
     * <p>날짜 형식 변경 (English, 'MMMM d, yyyy')</p>
     *
     * @param strDate (날짜형식 문자)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toEnglishDate(String strDate) throws ParseException {
        return toEnglishDate(strDate, true);
    }

    /**
     * <p>날짜 형식 변경 (English, 'MMMM d, yyyy, MMM dd, yyyy')</p>
     *
     * @param strDate (날짜형식 문자)
     * @param isLong  (월의 스타일 false = Mar, true = March)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toEnglishDate(String strDate, boolean isLong)
        throws ParseException {
        String format = isLong ? "MMMM d, yyyy" : "MMM dd, yyyy";
        return toDateFormat(strDate, format, Locale.ENGLISH);
    }

    /**
     * <p>날짜 형식 변경 (English, 'MM-dd-yyyy')</p>
     *
     * @param strDate (날짜형식 문자)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toEnglishDate1(String strDate) throws ParseException {
        return toDateFormat(strDate, "MM-dd-yyyy", Locale.ENGLISH);
    }

    /**
     * <p>날짜 형식 변경 (English, 'MM.dd.yyyy')</p>
     *
     * @param strDate (날짜형식 문자)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toEnglishDate2(String strDate) throws ParseException {
        return toDateFormat(strDate, "MM.dd.yyyy", Locale.ENGLISH);
    }

    /**
     * <p>날짜 형식 변경 (English, 'MM.dd (EEEE)')</p>
     *
     * @param strDate (날짜형식 문자)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toEnglishDate3(String strDate) throws ParseException {
        return toEnglishDate3(strDate, true);
    }

    /**
     * <p>날짜 형식 변경 (English, 'MM.dd (EEEE), MM.dd (EE)')</p>
     *
     * @param strDate (날짜형식 문자)
     * @param isLong  (요일의 스타일 false = Mon, true = Monday)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toEnglishDate3(String strDate, boolean isLong)
        throws ParseException {
        String format = isLong ? "MM.dd (EEEE)" : "MM.dd (EE)";
        return toDateFormat(strDate, format, Locale.ENGLISH);
    }

    /**
     * <p>날짜 형식 변경 (English, 'yyyy-MM-dd HH:mm')</p>
     *
     * @param strDate (날짜형식 문자)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toEnglishDate4(String strDate) throws ParseException {
        return toDateFormat(strDate, "yyyy-MM-dd HH:mm", Locale.ENGLISH);
    }

    /**
     * <p>날짜 형식 변경 (RSS, 'EE, dd MMM yyyy HH:mm:ss +0000')</p>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @return String (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static String toRssDate(Object object) {
        try {
            if (StringUtil.isBlank(object)) {
                return null;
            }
            if (object instanceof Date) {
                Date date = (Date) object;
                return toDateFormat(date, "EE, dd MMM yyyy HH:mm:ss +0900", Locale.ENGLISH);
            } else {
                String strDate = StringUtil.clean(object);
                if (strDate.length() == 10) {
                    strDate += " 00:00:00.0";
                }
                return toDateFormat(strDate, "EE, dd MMM yyyy HH:mm:ss +0900", Locale.ENGLISH);
            }
        } catch (Exception e) {
            logger.error(e.toString(), e.fillInStackTrace());
            return null;
        }
    }

    /**
     * <p>날짜 형식 변경 (XML, 'yyyy-MM-ddT00:00:00')</p>
     *
     * <pre>
     * DateUtil.toXmlDate("20191127") = "2019-11-27T00:00:00"
     * </pre>
     *
     * @param strDate (날짜형식 문자)
     * @return XMLGregorianCalendar (변경 후 날짜)
     * @throws ParseException throws ParseException
     */
    public static XMLGregorianCalendar toXmlDate(String strDate)
        throws ParseException, DatatypeConfigurationException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Calendar calendar = Calendar.getInstance();
        Date date = dateFormat.parse(strDate + "000000");
        calendar.setTime(date);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return DatatypeFactory.newInstance()
            .newXMLGregorianCalendar(gregorianCalendar.get(Calendar.YEAR),
                gregorianCalendar.get(Calendar.MONTH) + 1,
                gregorianCalendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED).normalize();
    }

    /**
     * <p>현재 날짜 구하기 ('yyyy-MM-dd')</p>
     *
     * <pre>
     * DateUtil.getNow() = &quot;2018-04-27&quot;
     * </pre>
     *
     * @return String (현재 날짜)
     */
    public static String getNow() {
        return toDateFormat(new Date(), "yyyy-MM-dd");
    }

    /**
     * <p>현재 날짜 구하기 ('yyyy-MM-dd HH:mm:ss')</p>
     *
     * <pre>
     * DateUtil.getNowAll() = &quot;2018-04-27 16:03:15&quot;
     * </pre>
     *
     * @return String (현재 날짜와 시간)
     */
    public static String getNowAll() {
        return toDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * <p>날짜에서 년도 구하기</p>
     *
     * <pre>
     * DateUtil.getYear(null)         = null
     * DateUtil.getYear("2010-12-20") = "2010"
     * DateUtil.getYear(Date)         = "2010"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @return String (년도)
     * @throws ParseException throws ParseException
     */
    public static String getYear(Object object) throws ParseException {
        if (object instanceof String) {
            return toDateFormat((String) object, "yyyy", Locale.KOREA);
        } else if (object instanceof Date) {
            return toDateFormat((Date) object, "yyyy", Locale.KOREA);
        }
        return null;
    }

    /**
     * <p>날짜에서 월 구하기</p>
     *
     * <pre>
     * DateUtil.getMonth(null)         = null
     * DateUtil.getMonth("2010-06-05") = "06"
     * DateUtil.getMonth("2010.06.05") = "06"
     * DateUtil.getMonth("2010-6-5")   = "06"
     * DateUtil.getMonth("20100605")   = "06"
     * DateUtil.getMonth(Date)         = "06"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @return String (월)
     * @throws ParseException throws ParseException
     */
    public static String getMonth(Object object) throws ParseException {
        return getMonth(object, true);
    }

    /**
     * <p>날짜에서 월 구하기 (형식 추가)</p>
     *
     * <pre>
     * DateUtil.getMonth(null, *)             = null
     * DateUtil.getMonth("2010-06-05", true)  = "06"
     * DateUtil.getMonth("2010-06-05", false) = "6"
     * DateUtil.getMonth("2010.06.05", true)  = "06"
     * DateUtil.getMonth("2010-6-5", true)    = "06"
     * DateUtil.getMonth("20100605", false)   = "6"
     * DateUtil.getMonth(Date, false)         = "6"
     * </pre>
     *
     * @param object    (날짜형식 문자 또는 Date 객체)
     * @param isAddZero (1 ~ 9월 앞에 0을 붙일지 여부)
     * @return String (월)
     * @throws ParseException throws ParseException
     */
    public static String getMonth(Object object, boolean isAddZero)
        throws ParseException {
        if (object instanceof String) {
            return toDateFormat((String) object, (isAddZero ? "MM" : "M"), Locale.KOREA);
        } else if (object instanceof Date) {
            return toDateFormat((Date) object, (isAddZero ? "MM" : "M"), Locale.KOREA);
        }
        return null;
    }

    /**
     * <p>날짜에서 일 구하기</p>
     *
     * <pre>
     * DateUtil.getDay(null)         = null
     * DateUtil.getDay("2010-11-04") = "04"
     * DateUtil.getDay("2010.11.04") = "04"
     * DateUtil.getDay("2010-11-4")  = "04"
     * DateUtil.getDay("20101104")   = "04"
     * DateUtil.getDay(Date)         = "04"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @return String (일)
     * @throws ParseException throws ParseException
     */
    public static String getDay(Object object) throws ParseException {
        return getDay(object, true);
    }

    /**
     * <p>날짜에서 일 구하기 (형식 추가)</p>
     *
     * <pre>
     * DateUtil.getDay(null, *)             = null
     * DateUtil.getDay("2010-11-04", true)  = "04"
     * DateUtil.getDay("2010-11-04", false) = "4"
     * DateUtil.getDay("2010.11.04", true)  = "04"
     * DateUtil.getDay("2010-11-4", true)   = "04"
     * DateUtil.getDay("20101104", false)   = "4"
     * DateUtil.getDay(Date, false)         = "4"
     * </pre>
     *
     * @param object    (날짜형식 문자 또는 Date 객체)
     * @param isAddZero (1 ~ 9일 앞에 0을 붙일지 여부)
     * @return String (일)
     * @throws ParseException throws ParseException
     */
    public static String getDay(Object object, boolean isAddZero)
        throws ParseException {
        if (object instanceof String) {
            return toDateFormat((String) object, (isAddZero ? "dd" : "d"), Locale.KOREA);
        } else if (object instanceof Date) {
            return toDateFormat((Date) object, (isAddZero ? "dd" : "d"), Locale.KOREA);
        }
        return null;
    }

    /**
     * <p>날짜의 주 수 구하기 (년/월 기준)</p>
     *
     * <pre>
     * DateUtil.getWeek(null, *)             = 0
     * DateUtil.getWeek("2011-08-03", true)  = 1
     * DateUtil.getWeek("2011-08-03", false) = 32
     * DateUtil.getWeek("2011.08.03", true)  = 1
     * DateUtil.getWeek("2011-08-3", false)  = 32
     * DateUtil.getWeek("20110803", false)   = 32
     * DateUtil.getWeek(Date, true)          = 1
     * </pre>
     *
     * @param object        (날짜형식 문자 또는 Date 객체)
     * @param isWeekOfMonth (true : 월 기준, false : 년 기준)
     * @return int (주)
     * @throws ParseException throws ParseException
     */
    public static int getWeek(Object object, boolean isWeekOfMonth)
        throws ParseException {
        Date date = null;
        if (object instanceof String) {
            date = parseDate((String) object);
        } else if (object instanceof Date) {
            date = (Date) object;
        } else {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (isWeekOfMonth) {
            return calendar.get(Calendar.WEEK_OF_MONTH);
        } else {
            return calendar.get(Calendar.WEEK_OF_YEAR);
        }
    }

    /**
     * <p>날짜의 주 수 구하기 (월 기준)</p>
     *
     * <pre>
     * DateUtil.getWeekOfMonth(null)         = 0
     * DateUtil.getWeekOfMonth("2011-08-01") = 1
     * DateUtil.getWeekOfMonth("2011-08-08") = 2
     * DateUtil.getWeekOfMonth("2011.08.01") = 1
     * DateUtil.getWeekOfMonth("20110815")   = 3
     * DateUtil.getWeekOfMonth(Date, true)   = 1
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @return int (주)
     * @throws ParseException throws ParseException
     */
    public static int getWeekOfMonth(Object object) throws ParseException {
        return getWeek(object, true);
    }

    /**
     * <p>날짜의 주 수 구하기 (년도 기준)</p>
     *
     * <pre>
     * DateUtil.getWeekOfYear(null)         = 0
     * DateUtil.getWeekOfYear("2011-08-01") = 32
     * DateUtil.getWeekOfYear("2011-08-08") = 33
     * DateUtil.getWeekOfYear("2011.08.01") = 32
     * DateUtil.getWeekOfYear("20110815")   = 34
     * DateUtil.getWeekOfYear(Date, true)   = 32
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @return int (주)
     * @throws ParseException throws ParseException
     */
    public static int getWeekOfYear(Object object) throws ParseException {
        return getWeek(object, false);
    }

    /**
     * <p>현재 날짜의 TimeStamp 구하기 ('yyyyMMddHHmmssSSS')</p>
     *
     * <pre>
     * DateUtil.getTimeStamp() = &quot;20101119131734344&quot;
     * </pre>
     *
     * @return String (TimeStamp 값)
     * @throws ParseException throws ParseException

     */
    public static String getTimeStamp() throws ParseException {
        return getTimeStamp(new Date(), null, null);
    }

    /**
     * <p>Date 객체의 TimeStamp 구하기 ('yyyyMMddHHmmssSSS')</p>
     *
     * <pre>
     * DateUtil.getTimeStamp(null, "*", "*") = null
     * DateUtil.getTimeStamp("  ", "*", "*") = null
     * DateUtil.getTimeStamp(Date, "*", "*") = "20101119131734344"
     * </pre>
     *
     * @param date   (날짜)
     * @param format (변경할 포맷)
     * @param locale (지역설정 값)
     * @return String (TimeStamp 값)
     * @throws ParseException throws ParseException

     */
    public static String getTimeStamp(Date date, String format, Locale locale)
        throws ParseException {
        if (date == null) {
            return null;
        }
        if (StringUtil.isBlank(format)) {
            format = "yyyyMMddHHmmssSSS";
        }
        if (locale == null) {
            locale = Locale.KOREA;
        }
        return toDateFormat(date, format, locale);
    }

    /**
     * <p>날짜 계산 및 포맷 적용</p>
     *
     * <pre>
     * DateUtil.addField(null, *, *, *)                      = ""
     * DateUtil.addField("", *, *, *)                        = ""
     * DateUtil.addField("  ", *, *, *)                      = ""
     * DateUtil.addField("2010-12-20", 1, 3, null)           = "2013-12-20"
     * DateUtil.addField("2010/12/20", 1, 3, null)           = "2013/12/20"
     * DateUtil.addField("2010-12-20", 1, -3, null)          = "2007-12-20"
     * DateUtil.addField("2010-12-20", 1, -3, "yyyy.MM.dd")  = "2007.12.20"
     * DateUtil.addField("2010-12-20", 2, 3, null)           = "2011-03-20"
     * DateUtil.addField("2010-12-20 23:59:59", 2, 3, null)  = "2011-03-20 23:59:59"
     * DateUtil.addField("2010-12-20 23:59:59", 5, 12, null) = "2011-01-01 23:59:59"
     * DateUtil.addField("2010-12-20 23:59:59", 13, 2, null) = "2010-12-21 00:00:01"
     * DateUtil.addField("2010-12-20", 13, -1, null)         = "2010-12-19"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @param field  (계산할 필드 - year=1, month=2, day=5, hour=10, minute=12, second=13)
     * @param amount (계산할 값)
     * @param format (계산 후 날짜 포맷)
     * @return String (계산 후 날짜)
     * @throws ParseException throws ParseException

     */
    public static String addField(Object object, int field, int amount,
        String format) throws ParseException {
        if (StringUtil.isBlank(object)) {
            return "";
        }

        Date date = null;
        if (object instanceof String) {
            String strDate = StringUtil.clean(object);
            if (StringUtil.isBlank(format)) {
                format = getFormat(strDate);
            }
            date = parseDate(strDate, Locale.KOREA);
        } else if (object instanceof Date) {
            if (StringUtil.isBlank(format)) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            date = (Date) object;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);

        return toDateFormat(calendar.getTime(), format, Locale.KOREA);
    }

    /**
     * <p>날짜 계산 적용 (년도)</p>
     *
     * <pre>
     * DateUtil.addYear(null, *)                   = ""
     * DateUtil.addYear("", *)                     = ""
     * DateUtil.addYear("  ", *)                   = ""
     * DateUtil.addYear("2010-12-20", 3)           = "2013-12-20"
     * DateUtil.addYear("2010/12/20", 3)           = "2013/12/20"
     * DateUtil.addYear("2010-12-20", -3)          = "2007-12-20"
     * DateUtil.addYear("2010-12-20 23:59:59", -3) = "2007-12-20"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @param amount (계산할 값)
     * @return String (계산 후 날짜)
     * @throws ParseException throws ParseException

     */
    public static String addYear(Object object, int amount)
        throws ParseException {
        return addField(object, Calendar.YEAR, amount, null).substring(0, 10);
    }

    /**
     * <p>날짜 계산 적용 (월)</p>
     *
     * <pre>
     * DateUtil.addMonth(null, *)                   = ""
     * DateUtil.addMonth("", *)                     = ""
     * DateUtil.addMonth("  ", *)                   = ""
     * DateUtil.addMonth("2010-12-20", 3)           = "2011-03-20"
     * DateUtil.addMonth("2010/12/20", 3)           = "2011/03/20"
     * DateUtil.addMonth("2010-12-20", -3)          = "2010-09-20"
     * DateUtil.addMonth("2010-12-20 23:59:59", -3) = "2010-09-20"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @param amount (계산할 값)
     * @return String (계산 후 날짜)
     * @throws ParseException throws ParseException

     */
    public static String addMonth(Object object, int amount)
        throws ParseException {
        return addField(object, Calendar.MONTH, amount, null).substring(0, 10);
    }

    /**
     * <p>날짜 계산 적용 (일)</p>
     *
     * <pre>
     * DateUtil.addDay(null, *)                    = ""
     * DateUtil.addDay("", *)                      = ""
     * DateUtil.addDay("  ", *)                    = ""
     * DateUtil.addDay("2010-12-20", 13)           = "2011-01-02"
     * DateUtil.addDay("2010/12/20", 13)           = "2011/01/02"
     * DateUtil.addDay("2010-12-20", -13)          = "2010-12-07"
     * DateUtil.addDay("2010-12-20 23:59:59", -13) = "2010-12-07"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @param amount (계산할 값)
     * @return String (계산 후 날짜)
     * @throws ParseException throws ParseException

     */
    public static String addDay(Object object, int amount)
        throws ParseException {
        return addField(object, Calendar.DATE, amount, null).substring(0, 10);
    }

    /**
     * <p>날짜 계산 적용 (시간)</p>
     *
     * <pre>
     * DateUtil.addHour(null, *)                    = ""
     * DateUtil.addHour("", *)                      = ""
     * DateUtil.addHour("  ", *)                    = ""
     * DateUtil.addHour("2010-12-20", 24)           = "2010-12-21"
     * DateUtil.addHour("2010/12/20", 23)           = "2010/12/20"
     * DateUtil.addHour("2010-12-20", -24)          = "2010-12-19"
     * DateUtil.addHour("2010-12-20 23:59:59", -13) = "2010-12-20 10:59:59"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @param amount (계산할 값)
     * @return String (계산 후 날짜)
     * @throws ParseException throws ParseException

     */
    public static String addHour(Object object, int amount)
        throws ParseException {
        return addField(object, Calendar.HOUR, amount, null);
    }

    /**
     * <p>날짜 계산 적용 (분)</p>
     *
     * <pre>
     * DateUtil.addMinute(null, *)                    = ""
     * DateUtil.addMinute("", *)                      = ""
     * DateUtil.addMinute("  ", *)                    = ""
     * DateUtil.addMinute("2010-12-20", 60*24)        = "2010-12-21"
     * DateUtil.addMinute("2010/12/20", 37)           = "2010/12/20"
     * DateUtil.addMinute("2010-12-20", -60*24)       = "2010-12-19"
     * DateUtil.addMinute("2010-12-20 23:59:59", -70) = "2010-12-20 22:49:59"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @param amount (계산할 값)
     * @return String (계산 후 날짜)
     * @throws ParseException throws ParseException

     */
    public static String addMinute(Object object, int amount)
        throws ParseException {
        return addField(object, Calendar.MINUTE, amount, null);
    }

    /**
     * <p>날짜 계산 적용 (초)</p>
     *
     * <pre>
     * DateUtil.addSecond(null, *)                    = ""
     * DateUtil.addSecond("", *)                      = ""
     * DateUtil.addSecond("  ", *)                    = ""
     * DateUtil.addSecond("2010-12-20", 60*60*24)     = "2010-12-21"
     * DateUtil.addSecond("2010/12/20", 37)           = "2010/12/20"
     * DateUtil.addSecond("2010-12-20", -60*60*24)    = "2010-12-19"
     * DateUtil.addSecond("2010-12-20 23:59:59", -70) = "2010-12-20 23:58:49"
     * </pre>
     *
     * @param object (날짜형식 문자 또는 Date 객체)
     * @param amount (계산할 값)
     * @return String (계산 후 날짜)
     * @throws ParseException throws ParseException

     */
    public static String addSecond(Object object, int amount)
        throws ParseException {
        return addField(object, Calendar.SECOND, amount, null);
    }

    /**
     * <p>날짜 간 간격 계산</p>
     *
     * @param interval (구하고자 하는 간격)
     * @param date1    (계산에 사용할 날짜)
     * @param date2    (계산에 사용할 날짜)
     * @return long (해당 간격)
     * @throws ParseException throws ParseException

     */
    public static long getDateDiff(char interval, String date1, String date2)
        throws ParseException {
        long result = 0;
        long millisecond = 1000;
        long second = 60;
        long minute = 60;
        long day = 24;
        long month = 30;
        long year = 12;
        long dateDiff = parseDate(date1).getTime() - parseDate(date2).getTime();
        switch (interval) {
            case 's':
                result = dateDiff / (millisecond);
                break;
            case 'm':
                result = dateDiff / (millisecond * second);
                break;
            case 'h':
                result = dateDiff / (millisecond * second * minute);
                break;
            case 'd':
                result = dateDiff / (millisecond * second * minute * day);
                break;
            case 'M':
                result = dateDiff / (millisecond * second * minute * day * month);
                break;
            case 'y':
                result = dateDiff / (millisecond * second * minute * day * month * year);
                break;
            default:
                result = dateDiff / (1000 * 60 * 60 * 24);
        }
        return result;
    }
}
