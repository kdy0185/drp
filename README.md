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
* Gradle 7.4
* ExtJS 4.2.1


## Execution

* 실행 파일 : DrpApplication.java
* 기본 주소 : http://localhost:8080/
* 배포 주소 : http://3.34.2.174:3380/	


## Functions

**관리 시스템 패키지 적용**
  * 사용자, 메뉴, 권한 등을 관리할 수 있는 관리자 시스템 구현

**일과 관리**
  * 일과별 달성률, 몰입도 등을 활용한 점수 산출
  * 그리드 & 차트를 활용한 결산 정보 및 통계 조회

**구조 변경 및 레거시 시스템 개선**
  * Maven → Gradle 변경
  * Spring MVC → Spring Boot 변경
  * MyBatis → JPA + QueryDSL 변경
  * ~~JSP → ThymeLeaf 변경~~


## Author

* D.W Kang (kdy0185@gmail.com)
* https://github.com/kdy0185
