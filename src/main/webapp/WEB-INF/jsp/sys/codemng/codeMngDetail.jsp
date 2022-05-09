<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * @FileName : codeMngDetail.jsp
     * @Author : KDW
     * @Date : 2022-01-26
     * @Description : 공통 코드 정보 팝업
     */
%>
<%@ include file="/WEB-INF/jsp/cmmn/include/subTaglib.jsp" %>

<script type="text/javascript">

</script>
<form:form modelAttribute="detailDTO" name="codeMngDetailForm" method="post">
    <table class="table blue-base-table">
        <colgroup>
            <col style="width: 15%"/>
            <col style="width: 35%"/>
            <col style="width: 15%"/>
            <col style="width: 35%"/>
        </colgroup>
        <tbody>
        <tr>
            <th class="top-line"><span class="star-mark">그룹 코드</span></th>
            <td class="top-line">
                <form:input path="grpCd" cssClass="form-control input-sm width_42" readonly="true"/>
            </td>
            <th class="top-line"><span class="star-mark">그룹 코드명</span></th>
            <td class="top-line">
                <form:input path="grpNm" cssClass="form-control input-sm width_95" readonly="true"/>
            </td>
        </tr>
        <tr>
            <th><span class="star-mark">공통 코드</span></th>
            <td><form:input path="comCd" cssClass="form-control input-sm width_42" readonly="true"/></td>
            <th><span class="star-mark">공통 코드명</span></th>
            <td><form:input path="comNm" cssClass="form-control input-sm width_95" readonly="true"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">사용 여부</span></th>
            <td>
                <div class="radio-box">
                    <form:radiobutton path="useYn" cssClass="required" value="Y" label="사용"/>
                    <form:radiobutton path="useYn" cssClass="required" value="N" label="미사용"/>
                </div>
            </td>
            <th><span class="star-mark">정렬 순서</span></th>
            <td><form:input path="ord" cssClass="form-control input-sm width_42" readonly="true"/></td>
        </tr>
        <tr>
            <th><span class="star-mark">등록자</span></th>
            <td><form:input path="regUser" cssClass="form-control input-sm width_66" readonly="true"/></td>
            <th><span class="star-mark">등록 일시</span></th>
            <td><form:input path="regDate" cssClass="form-control input-sm width_66" readonly="true"/></td>
        </tr>
        <tr>
            <th><span class="star-unmark">수정자</span></th>
            <td><form:input path="modUser" cssClass="form-control input-sm width_66" readonly="true"/></td>
            <th><span class="star-unmark">수정 일시</span></th>
            <td><form:input path="modDate" cssClass="form-control input-sm width_66" readonly="true"/></td>
        </tr>
        <tr>
            <th class="bottom-line"><span class="star-unmark">비고</span></th>
            <td class="bottom-line" colspan="3">
                <form:textarea path="detl" rows="5" cols="40"
                               cssClass="form-control input-sm width_981" readonly="true"/>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="btn-center-area">
        <button type="button" onclick="$.util.closeDialog();" class="btn btn-gray">
            <i class="fa fa-check"></i>확인
        </button>
    </div>
</form:form>
