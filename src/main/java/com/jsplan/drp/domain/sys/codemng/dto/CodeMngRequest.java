package com.jsplan.drp.domain.sys.codemng.dto;

import com.jsplan.drp.domain.sys.codemng.entity.CodeMng;
import com.jsplan.drp.domain.sys.codemng.entity.CodeMngId;
import com.jsplan.drp.global.obj.entity.BaseListDTO;
import com.jsplan.drp.global.obj.entity.UseStatus;
import com.jsplan.drp.global.util.FilterUtil;
import com.jsplan.drp.global.util.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.json.JSONArray;

/**
 * @Class : CodeMngRequest
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 공통 코드 Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class CodeMngRequest extends BaseListDTO {

    private String grpCd; // 그룹 코드
    private String comCd; // 공통 코드
    private String comNm; // 공통 코드명
    private String useYn; // 사용 여부
    private String detl; // 비고
    private Integer ord; // 정렬 순서
    private String state; // 등록/수정 (등록 : I, 수정 : U)
    private String jsonData; // 저장할 JSON 데이터

    // JSON String 데이터 → JSONArray 형태로 변환
    public JSONArray changeJsonData() {
        jsonData = FilterUtil.filterXssReverse(jsonData);
        jsonData = StringUtil.clean(jsonData);
        return JSONArray.fromObject(jsonData);
    }

    // Test 전용 생성자 : 개별 공통 코드
    CodeMngRequest(String grpCd, String comCd, String comNm, UseStatus useYn, String detl,
        Integer ord) {
        this.grpCd = grpCd;
        this.comCd = comCd;
        this.comNm = comNm;
        this.useYn = useYn.getCode();
        this.detl = detl;
        this.ord = ord;
    }

    // Test 전용 생성자 : 전체 공통 코드
    CodeMngRequest(String jsonData) {
        this.jsonData = jsonData;
    }

    // Request DTO → Entity 변환
    public CodeMng toEntity() {
        return CodeMng.builder()
            .codeMngId(CodeMngId.createCodeMngId(grpCd, comCd))
            .comNm(comNm)
            .useYn(UseStatus.valueOf(useYn))
            .detl(detl)
            .ord(ord)
            .build();
    }
}
