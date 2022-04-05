package com.jsplan.drp.global.util;

/**
 * @Class : RowNumUtil
 * @Author : KDW
 * @Date : 2022-04-01
 * @Description : RowNum 처리
 */
public class RowNumUtil {

    private long totalCnt = 0; // 전체 데이터 수
    private long pageNo = 0; // 현재 페이지 번호
    private int pageSize = 0; // 페이지당 데이터 출력 수
    private long rn = 0; // 순번

    public long getRn() {
        return rn++;
    }

    public void setRn(long rn) {
        this.rn = rn;
    }

    public RowNumUtil(long totalCnt, long pageNo, int pageSize) {
        this.totalCnt = totalCnt;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        pageCalc();
    }

    private void pageCalc() {
        if (totalCnt == 0) pageNo = 1;
        rn = (pageNo - 1) * pageSize + 1;
    }
}
