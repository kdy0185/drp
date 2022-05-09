package com.jsplan.drp.domain.sys.codemng.dto;

import com.jsplan.drp.domain.sys.codemng.entity.CodeGrpMng;
import com.jsplan.drp.global.obj.entity.BaseListDTO;
import com.jsplan.drp.global.obj.entity.UseStatus;
import com.jsplan.drp.global.util.FilterUtil;
import com.jsplan.drp.global.util.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.json.JSONArray;

/**
 * @Class : CodeGrpMngRequest
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 그룹 코드 Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class CodeGrpMngRequest extends BaseListDTO {

    private String grpCd; // 그룹 코드
    private String grpNm; // 그룹 코드명
    private String useYn; // 사용 여부
    private String detl; // 비고
    private String detailStatus; // 등록/수정 구분
    private String jsonData; // 저장할 JSON 데이터

    // JSON String 데이터 → JSONArray 형태로 변환
    public JSONArray changeJsonData() {
        jsonData = FilterUtil.filterXssReverse(jsonData);
        jsonData = StringUtil.clean(jsonData);
        return JSONArray.fromObject(jsonData);
    }

    // Test 전용 생성자 : 개별 그룹 코드
    CodeGrpMngRequest(String grpCd, String grpNm, UseStatus useYn, String detl) {
        this.grpCd = grpCd;
        this.grpNm = grpNm;
        this.useYn = useYn.getCode();
        this.detl = detl;
    }

    // Test 전용 생성자 : 전체 그룹 코드
    CodeGrpMngRequest(String jsonData) {
        this.jsonData = jsonData;
    }

    // Request DTO → Entity 변환
    public CodeGrpMng toEntity() {
        return CodeGrpMng.builder()
            .grpCd(grpCd)
            .grpNm(grpNm)
            .useYn(UseStatus.valueOf(useYn))
            .detl(detl)
            .build();
    }
}
