package com.jsplan.drp.global.obj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : ComsDTO
 * @Author : KDW
 * @Date : 2022-05-20
 * @Description : 공통 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class ComsDTO {

    /* 공통 */
    private long rn; // 순번
    private String searchCd; // 검색 조건
    private String searchWord; // 검색어
    private String selectFormNm; // 팝업 호출 전 form
    private String selectInputId1; // 팝업 호출 전 input id1
    private String selectInputId2; // 팝업 호출 전 input id2
    private String selectInputId3; // 팝업 호출 전 input id3
    private String grpCd; // 그룹 코드
    private String grpNm; // 그룹명
    private String comCd; // 공통 코드
    private String comNm; // 공통 코드명
    private String userId; // 사용자 아이디
    private String userNm; // 성명


    /* fineUploader */
    private String uuid; // 고유값 확장자 포함(fineupload 필수)
    private String name; // 원본 파일명 확장자 포함 (fineupload 필수)
    private String path; // 파일이 저장된 경로
}
