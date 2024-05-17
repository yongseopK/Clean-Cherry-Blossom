## 🌸 깨끗한 꽃놀이 (Clean Cherry Blossom)

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
