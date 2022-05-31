# 데일리 루틴 프로젝트 (Daily Routine Project)


## Description

**자기 주도적인 루틴**을 **수치 중심**으로 기록하는 시스템
  * 일과별 카테고리를 활용하여 루틴을 체계적으로 관리
  * 자동으로 산출되는 점수를 통한 일일 셀프 피드백 진행
  * 데일리리포트 및 각종 일정 통계의 기반으로 활용 예정


## Environment

* Java 11.0.13
* Spring Boot 2.6.3
* Tomcat 9.0.56
* Gradle, War, ExtJS, MSSQL, MyBatis, JPA, QueryDSL, EC2


## Execution

* 실행 파일 : DrpApplication.java
* 기본 주소 : http://localhost:8080/
* 배포 주소 : http://3.34.2.174:3380/	


## Functions

**시스템 관리**
  * 사용자, 메뉴, 권한 등을 관리할 수 있는 관리자 시스템 구현
  * 공통 코드 관리 기능 구현

**일과 관리**
  * 일과별 달성률, 몰입도 등을 활용한 점수 산출
  * 그리드 & 차트를 활용한 결산 정보 및 통계 조회

**공통 컴포넌트**
  * QueryDSL 공통 Support 클래스 제작
  * 그리드, 차트 커스터마이징 (ExtJS)
    * 일반 그리드, 트리 그리드, 멀티 그리드, 팝업 그리드, 동적 그리드 등등
    * 꺾은선, 막대, 원형 차트 등
  * 파일 업/다운로드 컴포넌트 구현 + 커스터마이징
  * Spring Security 커스터마이징
    * 자동 로그인, 동시 세션 처리
    * 권한 별, URL 별 접근 제어
    * SSL, OAuth 2.0 적용 등

**구조 변경 및 레거시 시스템 개선**
  * Spring MVC → Spring Boot 변경
  * Spring 버전 업그레이드 (3.x → 4.x → 5.x)
  * 설정 파일 변경 (xml → java 기반)
  * Dependency 변경 (Maven → Gradle)
  * 기존 xml 쿼리의 JPA 전환
  * MyBatis, JPA, QueryDSL을 병행하여 사용할 수 있도록 설정
  * ~~View Template 변경 (JSP → ThymeLeaf)~~

**TDD 방법론 적용**
  * 유형별 테스트 케이스 작성 및 테스트 주도 개발
  * 단위 테스트, 통합 테스트 진행


## Author

* D.W Kang (kdy0185@gmail.com)
* https://github.com/kdy0185
