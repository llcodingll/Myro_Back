# Myro
### 🛠️ Managing schedule Web Application
#### Skills
- Java21, Spring Boot
- Caching: Redis
- AI: Windsurf, ChatGPT, Gemini 2.0
#### Duration
- 2M, 1D per 1H

---

# **📝 MVP 기능 명세서**

### **1. 회원 관리 (User Management)**

> ✅ 핵심 기능: 사용자가 로그인하여 자신의 일정을 관리할 수 있도록 기본적인 인증/인가 기능을 제공
>
>
> ✅ 추가 고려 사항: **이메일 중복 체크, 패스워드 암호화, 유저 세션 관리**
>
- [ ]  **회원가입 (Sign Up)**
    - 이메일, 비밀번호, 닉네임 입력
    - 이메일 중복 체크 API
    - 패스워드 해싱 처리 (BCrypt 적용)
- [ ]  **로그인 (Sign In)**
    - JWT 기반 인증
    - 로그인 시 토큰 발급 및 만료 시간 설정
    - Redis를 활용한 세션 관리 (선택 사항)
- [ ]  **로그아웃 (Sign Out)**
    - JWT 토큰 무효화 처리 (Blacklist 사용 여부 고려)
- [ ]  **비밀번호 재설정**
    - 이메일 인증을 통한 패스워드 재설정 기능
    - 비밀번호 변경 시 기존 패스워드 검증

---

### **2. 일정 관리 (Schedule Management)**

> ✅ 핵심 기능: 일정 CRUD(생성, 조회, 수정, 삭제) 기능 제공
>
>
> ✅ 추가 고려 사항: **중복 일정 방지, 태그 관리, 반복 일정, 공유 기능 고려**
>
- [x]  **일정 생성 (Create Schedule)**
    - 제목, 날짜, 시간, 설명(메모), 태그 입력
    - 일정 충돌 검증 (같은 시간대에 중복 일정 생성 방지)
- [x]  **일정 조회 (Get Schedule)**
    - **단건 조회:** 일정 ID를 기반으로 상세 일정 조회
    - **리스트 조회:** 기간별 조회 (오늘, 이번 주, 특정 날짜 범위)
    - 필터 기능: 태그, 우선순위, 일정 상태(완료/미완료)
    - 정렬 기능: 시간순, 중요도 순
- [x]  **일정 수정 (Update Schedule)**
    - 기존 일정 정보 수정 (제목, 시간, 태그, 메모 등)
- [x]  **일정 삭제 (Delete Schedule)**
    - 일정 삭제 요청 후 논리 삭제(Soft Delete) 처리
- [ ]  **반복 일정 (Recurring Schedule) [Option]**
    - 매일, 매주, 매월 반복되는 일정 관리
    - 특정 기간 동안만 반복 설정 가능

---

### **3. AI 추천 및 분석 (AI-powered Recommendations & Analysis)**

> ✅ 핵심 기능: AI API를 활용한 일정 추천 및 분석
>
>
> ✅ 추가 고려 사항: **유저 데이터 기반 일정 최적화, 추천 알고리즘 개선 고려**
>
- [ ]  **ChatGPT를 활용한 일정 추천 (AI-based Scheduling)**
    - 사용자가 입력한 키워드 기반으로 일정 추천
    - 예: `"내일 오전에 운동할 시간 추천해줘"` → 추천 일정 생성
- [ ]  **Windsurf API를 활용한 일정 패턴 분석**
    - 사용자의 일정 패턴을 분석하여 최적의 일정 추천
    - 예: `"일정이 너무 몰려 있습니다. 오전과 오후에 적절히 분배하는 것이 좋겠습니다."`

---

### **4. 데이터 최적화 및 성능 개선 (Performance Optimization & Caching)**

> ✅ 핵심 기능: 일정 조회 속도 개선 및 트래픽 최적화
>
>
> ✅ 추가 고려 사항: **Redis 캐싱 전략**
>
- [ ]  **Redis를 활용한 일정 캐싱**
    - 최근 조회한 일정 데이터를 Redis에 캐싱하여 빠른 응답 제공
    - 일정 변경 시 캐시 무효화 처리

---

### **5. 로컬 환경 개발 및 테스트 (Local Development & Testing)**

> ✅ 핵심 기능: 개발 환경에서의 효율적인 테스트 및 디버깅
>
>
> ✅ 추가 고려 사항: **로컬에서의 Redis 설정, 단위 테스트, 통합 테스트**
>
- [ ]  **로컬 환경 구성**
    - Docker Compose를 활용하여 Kafka, Redis 로컬 실행
    - Spring Profile (`dev`, `prod`) 설정
- [ ]  **단위 테스트 (Unit Testing)**
    - JUnit & Mockito를 활용한 서비스 레이어 테스트
- [ ]  **통합 테스트 (Integration Testing)**
    - REST API 테스트 (RestAssured 활용)
    - 데이터베이스 연동 테스트 (Testcontainers 활용 가능)
