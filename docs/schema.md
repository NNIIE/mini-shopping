### 회원
```sql
CREATE TABLE user
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname        VARCHAR(20)           NOT NULL,
    email           VARCHAR(100)          NOT NULL,
    password        VARCHAR(100)          NOT NULL,
    role            TINYINT UNSIGNED      NOT NULL DEFAULT 1 COMMENT '1: 회원, 2: 판매자, 3: 관리자',
    status          TINYINT UNSIGNED      NOT NULL DEFAULT 1 COMMENT '1: 활성, 2: 탈퇴, 3: 정지',
    created_at      TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_nickname (nickname),
    UNIQUE INDEX idx_email (email)
);
```

<br>

### 브랜드
```sql
CREATE TABLE brand
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT                NOT NULL,
    name            VARCHAR(20)           NOT NULL,
    created_at      TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_user (user_id),
    UNIQUE INDEX idx_name (name)
);
```

<br>

### 상품
``` sql
CREATE TABLE product
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id        BIGINT                NOT NULL,
    name            VARCHAR(20)           NOT NULL,
    category        TINYINT UNSIGNED      NOT NULL,
    price           DECIMAL(10, 2)        NOT NULL,
    created_at      TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_brand (brand_id),
    INDEX idx_category (category),
    INDEX idx_price (price)
);
```
