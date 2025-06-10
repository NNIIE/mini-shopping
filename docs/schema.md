## ERD

![스크린샷 2025-04-27 오전 8 03 42](https://github.com/user-attachments/assets/69e717d7-b6b2-40bf-8177-51c502294f31)


<br>

### 어카운트
```sql
CREATE TABLE account
(
    id              INT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    email           VARCHAR(255)                    NOT NULL,
    password        VARCHAR(255)                    NOT NULL,
    role            VARCHAR(20)                     NOT NULL,
    status          VARCHAR(20)                     NOT NULL,
    created_at      TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_email (email)
);
```

<br>

### 일반 회원
```sql
CREATE TABLE basic_user
(
    id              INT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    account_id      INT UNSIGNED                    NOT NULL,
    nickname        VARCHAR(20)                     NOT NULL,
    phone_number    VARCHAR(30)                     NOT NULL,
    created_at      TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_nickname (nickname)
);
```

<br>

### 관리자 회원
```sql
CREATE TABLE admin_user
(
    id                INT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    account_id        INT UNSIGNED                    NOT NULL,
    created_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

<br>

### 브랜드
```sql
CREATE TABLE brand
(
    id                INT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    admin_user_id     INT UNSIGNED                    NOT NULL,
    name              VARCHAR(40)                     NOT NULL,
    business_number   VARCHAR(30)                     NOT NULL,
    created_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_name (name),
    UNIQUE INDEX idx_business_number (business_number)
);
```

<br>

### 카테고리
```sql
CREATE TABLE category
(
    id                INT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    name              VARCHAR(30)                     NOT NULL,
    created_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_name (name)
);
```

<br>

### 상품
``` sql
CREATE TABLE product
(
    id              BIGINT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    brand_id        INT UNSIGNED                       NOT NULL,
    category_id     INT UNSIGNED                       NOT NULL,
    name            VARCHAR(40)                        NOT NULL,
    price           DECIMAL(10, 2)                     NOT NULL,
    stock           INT UNSIGNED                       NOT NULL,
    created_at      TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

<br>

### 주문
```sql
CREATE TABLE orders
(
    id              BIGINT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    basic_user_id   INT UNSIGNED                       NOT NULL,
    status          VARCHAR(20)                        NOT NULL,
    created_at      TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

<br>

### 주문 상품
```sql
CREATE TABLE order_product
(
    id              BIGINT UNSIGNED AUTO_INCREMENT       PRIMARY KEY,
    orders_id       BIGINT UNSIGNED                      NOT NULL,
    product_id      BIGINT UNSIGNED                      NOT NULL,
    quantity        INT UNSIGNED                         NOT NULL,
    created_at      TIMESTAMP                            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```


