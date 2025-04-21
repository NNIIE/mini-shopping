### 회원
```sql
CREATE TABLE user
(
    id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nickname        VARCHAR(20)                 NOT NULL,
    email           VARCHAR(254)                NOT NULL,
    password        VARCHAR(128)                NOT NULL,
    role            VARCHAR(20)                 NOT NULL,
    status          VARCHAR(20)                 NOT NULL,
    created_at      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_nickname (nickname),
    UNIQUE INDEX idx_email (email)
);

-- id: 회원수에 비해 BIGINT 범위는 너무 크다고 판단해 대략 0 ~ 43억개 까지 저장할 수 있는 INT UNSIGNED 선택
-- nickname: 닉네임은 10글자 이하가 요구사항이지만 추후 변경 가능성을 고려해 VARCHAR(20) 선택
-- email: 이메일 주소는 RFC 5322 표준에 따라 최대 총 길이가 254자까지 가능해서 VARCHAR(255) 선택
-- password: SHA-512 해싱 알고리즘이 128자를 생성하므로 VARCHAR(128) 선택
-- role: 의미를 알아보기 쉽게 role enum을 문자열로 저장하기 위해 VARCHAR(20) 선택
-- status: 의미를 알아보기 쉽게 status enum을 문자열로 저장하기 위해 VARCHAR(20) 선택
-- created_at: 2038년 이후 사용될 가능성이 낮고 DATETIME 대비 적은 공간을 차지하고 타임존이 자동처리되는 TIMESTAMP 선택
-- updated_at: 2038년 이후 사용될 가능성이 낮고 DATETIME 대비 적은 공간을 차지하고 타임존이 자동처리되는 TIMESTAMP 선택

-- idx_nickname: 조회에는 사용되지 않지만 nickname의 유일조건을 만족하기 위해 유니크인덱스 설정
-- idx_email: 조회에도 사용되고 email의 유일조건도 만족하기 위해 유니크인덱스 설정
```

<br>

### 브랜드
```sql
CREATE TABLE brand
(
    id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id         INT UNSIGNED                NOT NULL,
    name            VARCHAR(40)                 NOT NULL,
    created_at      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_user (user_id),
    UNIQUE INDEX idx_name (name)
);

-- id: 브랜드 숫자 비해 BIGINT 범위는 너무 크다고 판단해 대략 0 ~ 43억개 까지 저장할 수 있는 INT UNSIGNED 선택
-- user_id: 브랜드를 소유하는 유저의 id
-- name: 브랜드명은 20글자 이하가 요구사항이지만 추후 변경 가능성을 고려해 VARCHAR(40) 선택
-- created_at: 2038년 이후 사용될 가능성이 낮고 DATETIME 대비 적은 공간을 차지하고 타임존이 자동처리되는 TIMESTAMP 선택
-- updated_at: 2038년 이후 사용될 가능성이 낮고 DATETIME 대비 적은 공간을 차지하고 타임존이 자동처리되는 TIMESTAMP 선택

-- idx_user: 상품의 추가/삭제 등이 자신 소유 브랜드로 매핑되기 때문에 검색을 위해 인덱스 설정
-- idx_name: 조회에는 사용되지 않지만 브랜드명의 유일조건을 만족하기 위해 유니크인덱스 설정
```

<br>

### 상품
``` sql
CREATE TABLE product
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id        INT UNSIGNED          NOT NULL,
    category_id     SMALLINT UNSIGNED     NOT NULL,
    name            VARCHAR(40)           NOT NULL,
    price           DECIMAL(10, 2)        NOT NULL,
    created_at      TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_brand (brand_id),
    INDEX idx_category (category_id),
    INDEX idx_price (price)
);

-- id: 상품 수는 매우 많아질 수 있기 때문에 확장성을 고려해 BIGINT 선택
-- brand_id: 상품이 속하는 브랜드의 id
-- name: 상품명은 20글자 이하가 요구사항이지만 추후 변경 가능성을 고려해 VARCHAR(40) 선택
-- category: 상품이 속하는 카테고리의 id
-- price: 정확한 상품가격 계산을 위한 Java BigDecimal에 매핑되는 decimal(10, 2) 선택
-- created_at: 2038년 이후 사용될 가능성이 낮고 DATETIME 대비 적은 공간을 차지하고 타임존이 자동처리되는 TIMESTAMP 선택
-- updated_at: 2038년 이후 사용될 가능성이 낮고 DATETIME 대비 적은 공간을 차지하고 타임존이 자동처리되는 TIMESTAMP 선택

-- idx_brand: 특정 브랜드의 전체 상품 검색등에 쓰이기 위해 인덱스 설정
-- idx_category: 카테고리 별 검색등에 쓰이기 위해 인덱스 설정
-- idx_price: 가격 범위 검색에 쓰이기 위해 인덱스 설정
```

### 카테고리
```sql
CREATE TABLE category
(
    id              SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(20)                      NOT NULL,
    created_at      TIMESTAMP                        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_name (name)
);

-- id: 카테고리 수로 TINYINT와 INT 사이의 65000개면 충분하다고 판단해 SMALLINT 선택
-- name: 카테고리 이름으로 최대 20자면 충분하다고 판단해 VARCHAR(20) 선택
-- created_at: 2038년 이후 사용될 가능성이 낮고 DATETIME 대비 적은 공간을 차지하고 타임존이 자동처리되는 TIMESTAMP 선택
-- updated_at: 2038년 이후 사용될 가능성이 낮고 DATETIME 대비 적은 공간을 차지하고 타임존이 자동처리되는 TIMESTAMP 선택

-- idx_name: 카테고리명의 유일조건을 만족하기위해 유니크인덱스 설정
```

