## 🌸 깨끗한 꽃놀이 (Clean Cherry Blossom)

### 서울시 벚꽃 개화시기 및 올바른 쓰레기 버리기 문화를 위한 쓰레기통 위치를 알려주는 웹서비스

- 개요
  - 벚꽃 개화시기가 도래함에 따라 많은 관광지가 활성화될 것으로 예상됨 이에 서울시 내의 벚꽃 명소들의 벚꽃 개화시기를 화면상에 표시하고, 관광지에 사람이 몰려들수록 무분별하게 버려지는 쓰레기가 많아진다는 자료를 참고해 서울시에 존재하는 쓰레기통의 위치를 알려줄 수 있는 웹 사이트를 제작하려고 함. 쓰레기통의 위치는 초기 제작자가 데이터를 넣어두지만, 사용자의 제보에 의해 삭제/추가될 수 있음. 추후 지역 및 장소는 추가 예정
- 필요기능

  - 회원기능
    - 로그인
    - 회원가입
    - 임시 비밀번호 발급
    - SNS(구글, 네이버) 로그인 구현
  - 지도
    - 쓰레기통의 위치를 마커로 표시
      - 공공데이터포털의 지역구별 “쓰레기통설치현황” 정보를 기반으로 위치정보 획득
    - 지역별 섹터를 나누어 꽃의 예상 개화시기를 알려줌
      - 인터넷 검색을 통한 정보 취합 후 신뢰성 높은 정보를 채택
  - 관리자 페이지
    - 회원관리
    - 쓰레기통, 꽃 개화시기 제보 수렴

- 계획 (총 40일)
  - 회원기능 (5일)
  - 클라이언트 페이지 제작(14일)
  - 백엔드 엔드포인트 및 비즈니스 로직 제작 (14일)
  - 테스트 및 디버그, 배포 (7일)

## Stacks 📚

#### Environment

<span>
    <img src="https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=IntelliJIDEA&logoColor=white">
    <img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white">
    <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white">
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=black">
</span>

#### Package Manager

<img src="https://img.shields.io/badge/Npm-CB3837?style=for-the-badge&logo=Npm&logoColor=white">

#### Backend

<span>
    <img src="https://img.shields.io/badge/Spring Boot-6DB340?style=for-the-badge&logo=SpringBoot&logoColor=white">
    <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white">
    <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white">
    <img src="https://img.shields.io/badge/Spring Security-6DB340?style=for-the-badge&logo=SpringSecurity&logoColor=white">
    <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white">
</span>

#### Frontend

<span>
    <img src="https://img.shields.io/badge/React-61DBFB?style=for-the-badge&logo=React&logoColor=black">
    <img src="https://img.shields.io/badge/Javascript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=black">
    <img src="https://img.shields.io/badge/Mui-007FFF?style=for-the-badge&logo=Mui&logoColor=white">
    <img src="https://img.shields.io/badge/Sass-CC6699?style=for-the-badge&logo=Sass&logoColor=white">
    <img src="https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=Vite&logoColor=white">
</span>

## API Endpoint 📢

### 👨‍🏫 Member

| Method | URI                                | Description         | Parameter                      |
| ------ | ---------------------------------- | ------------------- | ------------------------------ |
| PUT    | /api/members/modify                | 회원정보 수정       | 변경할 비밀번호, 이름, JWT토큰 |
| POST   | /api/members                       | 회원가입            | 이메일, 비밀번호, 유저이름     |
| POST   | /api/members/withdrawal            | 회원 탈퇴 요청      | JWT Token                      |
| POST   | /api/members/withdrawal/cancel     | 회원 탈퇴 취소      | 이메일, 비밀번호               |
| POST   | /api/members/token/validate        | 토큰 유효성 검사    | JWT Token                      |
| POST   | /api/members/naver                 | 네이버 로그인       | Naver Access Token             |
| POST   | /api/members/login                 | 로그인              | 이메일, 비밀번호               |
| POST   | /api/members/google                | 구글 로그인         | Google Credential Token        |
| POST   | /api/members/forget-password       | 임시 비밀번호 발급  | 이메일                         |
| PATCH  | /api/members/admin/{email}         | 회원 강제 탈퇴/취소 | JWT Token                      |
| PATCH  | /api/members/admin/promote/{email} | 유저 권한 변경      | JWT Token                      |
| GET    | /api/members/my-info               | 내 정보 조회        | JWT Token                      |
| GET    | /api/members/check                 | 이메일 중복 확인    | 이메일                         |
| GET    | /api/members/admin                 | 전체 회원 조회      | JWT Token                      |

### 📋 Report

| Method | URI                          | Description       | Parameter            |
| ------ | ---------------------------- | ----------------- | -------------------- |
| GET    | /api/report                  | 내 제보 내역 조회 | JWT Token            |
| POST   | /api/report                  | 제보 등록         | JWT Token            |
| PATCH  | /api/report/admin/{reportId} | 제보 처리         | 제보 번호, JWT Token |
| GET    | /api/report/admin            | 전체 제보 조회    | JWT Token            |

### 📍 Map

| Method | URI                   | Description        | Parameter    |
| ------ | --------------------- | ------------------ | ------------ |
| GET    | /api/maps/garbage-can | 쓰레기통 위치 조회 | District(구) |

## Project Structure 🌲

```
cherry-blossom-clean
├─ .gitignore
├─ README.md
├─ gradle
│  └─ wrapper
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ echomap
   │  │        └─ cherryblossomclean
   │  │           ├─ CherryBlossomCleanApplication.java
   │  │           ├─ auth
   │  │           │  ├─ TokenProvider.java
   │  │           │  └─ TokenUserInfo.java
   │  │           ├─ config
   │  │           │  ├─ AppConfig.java
   │  │           │  ├─ CorsConfig.java
   │  │           │  └─ WebSecurityConfig.java
   │  │           ├─ exception
   │  │           │  ├─ DuplicateOAuthEmailException.java
   │  │           │  ├─ ForcedWithdrawalException.java
   │  │           │  ├─ TokenExpiredException.java
   │  │           │  ├─ TokenInvalidException.java
   │  │           │  └─ ValidateEmailException.java
   │  │           ├─ filter
   │  │           │  └─ JwtAuthFilter.java
   │  │           ├─ map
   │  │           │  ├─ controller
   │  │           │  │  ├─ GarbageCanController.java
   │  │           │  │  └─ MapController.java
   │  │           │  ├─ entity
   │  │           │  │  └─ GarbageCan.java
   │  │           │  ├─ repository
   │  │           │  │  └─ GarbageCanRepository.java
   │  │           │  └─ service
   │  │           │     ├─ GarbageCanService.java
   │  │           │     └─ MapService.java
   │  │           ├─ member
   │  │           │  ├─ controller
   │  │           │  │  └─ MemberController.java
   │  │           │  ├─ dto
   │  │           │  │  ├─ request
   │  │           │  │  │  ├─ MemberModifyRequestDTO.java
   │  │           │  │  │  ├─ MemberSignInRequestDTO.java
   │  │           │  │  │  └─ MemberSignUpRequsetDTO.java
   │  │           │  │  └─ response
   │  │           │  │     ├─ MemberInfoResponseDTO.java
   │  │           │  │     ├─ MemberListResponseDTO.java
   │  │           │  │     ├─ MemberModifyResponseDTO.java
   │  │           │  │     ├─ MemberSignInResponseDTO.java
   │  │           │  │     └─ MemberSignUpResponseDTO.java
   │  │           │  ├─ entity
   │  │           │  │  └─ Member.java
   │  │           │  ├─ repository
   │  │           │  │  └─ MemberRepository.java
   │  │           │  ├─ scheduler
   │  │           │  │  └─ MemberDeleteScheduler.java
   │  │           │  └─ service
   │  │           │     ├─ AuthService.java
   │  │           │     ├─ MemberService.java
   │  │           │     └─ SocialLoginSevice.java
   │  │           ├─ report
   │  │           │  ├─ controller
   │  │           │  │  └─ ReportController.java
   │  │           │  ├─ dto
   │  │           │  │  ├─ ReportCreateRequestDTO.java
   │  │           │  │  ├─ ReportDetailResponseDTO.java
   │  │           │  │  └─ ReportListResponseDTO.java
   │  │           │  ├─ entity
   │  │           │  │  ├─ Report.java
   │  │           │  │  └─ ReportCategory.java
   │  │           │  ├─ repository
   │  │           │  │  └─ ReportRepository.java
   │  │           │  ├─ scheduler
   │  │           │  │  └─ ReportCleanupScheduler.java
   │  │           │  └─ service
   │  │           │     └─ ReportService.java
   │  │           └─ swagger
   │  │              └─ SwaggerConfig.java
   │  └─ resources
   │     ├─ static
   │     └─ templates
   └─ test
      └─ java
         └─ com
            └─ echomap
               └─ cherryblossomclean
                  ├─ CherryBlossomCleanApplicationTests.java
                  ├─ auth
                  │  └─ TokenProviderTest.java
                  ├─ map
                  │  └─ service
                  │     └─ GarbageCanServiceTest.java
                  └─ member
                     ├─ controller
                     └─ service
                        └─ MemberServiceTest.java

```
