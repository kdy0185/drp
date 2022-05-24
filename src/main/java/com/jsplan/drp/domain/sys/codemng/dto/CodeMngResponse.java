package com.jsplan.drp.domain.sys.codemng.dto;

import com.jsplan.drp.domain.sys.codemng.vo.CodeMngDataStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : CodeMngResponse
 * @Author : KDW
 * @Date : 2022-05-02
 * @Description : 코드 관리 Response DTO
 */
@Getter
@RequiredArgsConstructor
public class CodeMngResponse {

    private final int dataCnt; // 변경된 row 수
    private final CodeMngDataStatus dataStatus; // 응답 상태
    private final String dataMsg; // 응답 내용
}
