package com.jsplan.drp.cmmn.util;

/**
 * @Class : PagingUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 페이징 처리
 */
public class PagingUtil {

	private int pageNo; // 현재 페이지 번호 (필수)
	private int pageSize; // 페이지당 데이터 출력 수 (필수)
	private int firstPageNo; // 첫 번째 페이지 번호
	private int lastPageNo; // 마지막 페이지 번호
	private int prevPageNo; // 이전 페이지 번호
	private int nextPageNo; // 다음 페이지 번호
	private int pageBlock; // 한 블록당 페이지 수 (필수)
	private int startPageNo; // 현재 블록의 시작 페이지
	private int endPageNo; // 현재 블록의 끝 페이지
	private int startIndex; // 현재 페이지의 시작 인덱스
	private int endIndex; // 현재 페이지의 끝 인덱스
	private int totalCnt; // 전체 데이터 수 (필수)

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getFirstPageNo() {
		return firstPageNo;
	}

	public void setFirstPageNo(int firstPageNo) {
		this.firstPageNo = firstPageNo;
	}

	public int getLastPageNo() {
		return lastPageNo;
	}

	public void setLastPageNo(int lastPageNo) {
		this.lastPageNo = lastPageNo;
	}

	public int getPrevPageNo() {
		return prevPageNo;
	}

	public void setPrevPageNo(int prevPageNo) {
		this.prevPageNo = prevPageNo;
	}

	public int getNextPageNo() {
		return nextPageNo;
	}

	public void setNextPageNo(int nextPageNo) {
		this.nextPageNo = nextPageNo;
	}

	public int getPageBlock() {
		return pageBlock;
	}

	public void setPageBlock(int pageBlock) {
		this.pageBlock = pageBlock;
	}

	public int getStartPageNo() {
		return startPageNo;
	}

	public void setStartPageNo(int startPageNo) {
		this.startPageNo = startPageNo;
	}

	public int getEndPageNo() {
		return endPageNo;
	}

	public void setEndPageNo(int endPageNo) {
		this.endPageNo = endPageNo;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	/**
	 * <p>페이징 처리</p>
	 * 
	 * @param 
	 * @return 
	 */
	public void setPaging() {
		// 데이터가 없는 경우
		if (this.totalCnt == 0) {
			this.pageNo = 0;
			return;
		} 

		// 기본 값 설정
		if (this.pageNo == 0) this.setPageNo(1); 
		if (this.pageSize == 0) this.setPageSize(10);
		if (this.pageBlock == 0) this.setPageBlock(10);

		// 마지막 페이지 + 현재 페이지 설정
		int lastPage = (totalCnt + (pageSize - 1)) / pageSize;
		if (this.pageNo > lastPage) this.setPageNo(lastPage);
		if (this.pageNo < 0 || this.pageNo > lastPage) this.pageNo = 1;

		// 블록 내 시작 페이지 + 끝 페이지 설정
		int startPage = ((pageNo - 1) / pageBlock) * pageBlock + 1;
		int endPage = startPage + pageBlock - 1;
		if (endPage > lastPage) endPage = lastPage;
		this.setStartPageNo(startPage);
		this.setEndPageNo(endPage);

		// 이전 페이지 + 다음 페이지 설정
		boolean isFirst = pageNo == 1 ? true : false;
		boolean isLast = pageNo == lastPage ? true : false;
		if (isFirst) {
			this.setPrevPageNo(1);
		} else {
			this.setPrevPageNo(((pageNo - 1) < 1 ? 1 : (pageNo - 1)));
		}
		if (isLast) {
			this.setNextPageNo(lastPage);
		} else {
			this.setNextPageNo(((pageNo + 1) > lastPage ? lastPage : (pageNo + 1)));
		}

		// 첫 페이지 + 마지막 페이지 설정
		this.setFirstPageNo(1);
		this.setLastPageNo(lastPage);

		// 페이지 내 인덱스 설정
		int startIndex = (pageNo - 1) * pageSize + 1;
		int endIndex = startIndex + pageSize - 1;
		this.setStartIndex(startIndex);
		this.setEndIndex(endIndex);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
