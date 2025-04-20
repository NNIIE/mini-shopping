# 실행
- 

<br>

# 테스트 방법
#### 테스트 유형별 실행
- 모든 단위 테스트: `./gradlew unitTest`
- 모든 통합 테스트: `./gradlew integrationTest`
- 프로젝트 전체 테스트: `./gradlew test`

#### API 별 전체 테스트
- 회원 관련 모든 테스트: `./gradlew userAllTest`
- 브랜드 관련 모든 테스트: `./gradlew brandAllTest`
- 상품 관련 모든 테스트: `./gradlew productAllTest`
- 분석 관련 모든 테스트: `./gradlew analysisAllTest`

#### 회원 가입
- 단위 테스트: `./gradlew userSignupUnitTest`
- 통합 테스트: `./gradlew userSignupIntegrationTest`
- 모든 테스트: `./gradlew userSignupAllTest`

#### 회원 정보 변경
- 단위 테스트: `./gradlew userUpdateUnitTest`
- 통합 테스트: `./gradlew userUpdateIntegrationTest`
- 모든 테스트: `./gradlew userUpdateAllTest`

#### 회원 탈퇴
- 단위 테스트: `./gradlew userSignOffUnitTest`
- 통합 테스트: `./gradlew userSignOffIntegrationTest`
- 모든 테스트: `./gradlew userSignOffAllTest`

#### 브랜드 등록
- 단위 테스트: `./gradlew brandCreateUnitTest`
- 통합 테스트: `./gradlew brandCreateIntegrationTest`
- 모든 테스트: `./gradlew brandCreateAllTest`

#### 브랜드 삭제
- 단위 테스트: `./gradlew brandDeleteUnitTest`
- 통합 테스트: `./gradlew brandDeleteIntegrationTest`
- 모든 테스트: `./gradlew brandDeleteAllTest`

#### 상품 등록
- 단위 테스트: `./gradlew productCreateUnitTest`
- 통합 테스트: `./gradlew productCreateIntegrationTest`
- 모든 테스트: `./gradlew productCreateAllTest`

#### 상품 변경
- 단위 테스트: `./gradlew productUpdateUnitTest`
- 통합 테스트: `./gradlew productUpdateIntegrationTest`
- 모든 테스트: `./gradlew productUpdateAllTest`

#### 상품 삭제
- 단위 테스트: `./gradlew productDeleteUnitTest`
- 통합 테스트: `./gradlew productDeleteIntegrationTest`
- 모든 테스트: `./gradlew productDeleteAllTest`

#### 상품 조회
- 단위 테스트: `./gradlew productQueryUnitTest`
- 통합 테스트: `./gradlew productQueryIntegrationTest`
- 모든 테스트: `./gradlew productQueryAllTest`

#### 카테고리별 최저가 브랜드/가격/총액 조회
- 단위 테스트: `./gradlew categoryLowestPriceUnitTest`
- 통합 테스트: `./gradlew categoryLowestPriceIntegrationTest`
- 모든 테스트: `./gradlew categoryLowestPriceAllTest`

#### 전체 상품 구매 시 최저가격 브랜드/총액 조회
- 단위 테스트: `./gradlew totalLowestPriceUnitTest`
- 통합 테스트: `./gradlew totalLowestPriceIntegrationTest`
- 모든 테스트: `./gradlew totalLowestPriceAllTest`

#### 카테고리별 최저/최고 브랜드와 상품가격 조회
- 단위 테스트: `./gradlew categoryPriceRangeUnitTest`
- 통합 테스트: `./gradlew categoryPriceRangeIntegrationTest`
- 모든 테스트: `./gradlew categoryPriceRangeAllTest`

<br>

# 테스트 상세
## 회원 가입
### Unit Test
- 이메일 형식, 중복
- 닉네임 형식, 중복
- 비밀번호 형식, 암호화
- 회원가입 성공
- role 설정

### Integration Test
- 유효한 요청 - 200
- 잘못된 이메일, 닉네임, 비밀번호 형식 - 400
- 중복 이메일, 닉네임 - 409

<br>

## 회원 정보 변경
### Unit Test
- 변경 닉네임 형식
- 변경 비밀번호 형식
- 닉네임 중복
- 비밀번호 암호화

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 잘못된 닉네임, 비밀번호 - 400
- 중복 닉네임 - 409

<br>

## 회원 탈퇴
### Unit Test
- 비밀번호 검증
- 회원상태 변경
- 로그인 상태

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 잘못된 비밀번호 - 400
- 탈퇴 후 로그인 확인

<br>

## 브랜드 등록
### Unit Test
- 한글, 영어 길이 제한
- 중복 브랜드 명
- 관리자 역활 확인

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 일반 사용자 요청 - 403
- 잘못된 브랜드 이름 형식 - 400
- 중복 브랜드 요청 - 409

<br>

## 브랜드 삭제
### Unit Test
- 자신의 브랜드인지 확인
- 관리자 역활 확인

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 일반 사용자 요청 - 403
- 타인 브랜드 요청 - 403
- 없는 브랜드 요청 - 404

<br>

## 상품 등록
### Unit Test
- 상품이름 형식
- 가격 음수 검증
- 자신의 브랜드 확인 로직

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 일반 사용자 요청 - 403
- 타인 브랜드 요청 - 403
- 없는 브랜드 요청 - 404
- 잘못된 이름, 가격 형식 - 400

<br>

## 상품 변경
### Unit Test
- 자신의 상품인지 여부
- 상품명, 가격 유효성

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 일반 사용자 요청 - 403
- 타인 상품 요청 - 403
- 없는 상품 요청 - 404
- 잘못된 이름, 가격 형식 - 400

<br>

## 상품 삭제
### Unit Test
- 자신의 상품인지 여부

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 일반 사용자 요청 - 403
- 타인 상품 요청 - 403
- 없는 상품 요청 - 404

<br>

## 상품 조회
### Unit Test
- 모든 상품 조회
- 브랜드. 카테고리, 가격 범위 필터링

### Integration Test
- 유효한 요청 - 200
  - 전체 조회 / 브랜드, 카테고리, 가격범위 필터링
- 로그인 안된 상태 요청 - 401
- 없는 브랜드 요청 - 200 (빈 리스트 응답)
- 페이징 기능

<br>

## 카테고리별 최저가 브랜드/가격/총액 조회
### Unit Test
- 카테고리 별 최저가 브랜드 계산 로직
- 카테고리별 최저 가격 계산 로직
- 카테고리별 최저가 합산 로직

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 최저가 및 총액 계산 정확성
- 상품 추가/삭제 후 결과 확인

<br>

## 전체 상품 구매 시 최저가격 브랜드/총액 조회
### Unit Test
- 브랜드 별 전체 카테고리 상품 가격 합산 로직
- 최저 총액 브랜드 선정 로직

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 최저 총액 계산 확인
- 상품 추가/삭제 후 결과 확인

<br>

## 카테고리별 최저/최고 브랜드와 상품가격 조회
### Unit Test
- 지정 카테고리 최저가/최고가 브랜드 계산 로직

### Integration Test
- 유효한 요청 - 200
- 로그인 안된 상태 요청 - 401
- 없는 카테고리 요청 - 400
- 최저가/최고가 계산 정확성 확인
- 상품 추가/삭제 후 결과 확인



















