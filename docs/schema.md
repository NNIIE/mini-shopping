## ERD

![스크린샷 2025-04-22 오전 7 25 27](https://github.com/user-attachments/assets/9b991abb-842c-44a9-a825-9ec50583c4e4)

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
    employee_number   INT                             NOT NULL,
    created_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_employee_number (employee_number)
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

### 상품
``` sql
CREATE TABLE product
(
    id              BIGINT AUTO_INCREMENT     PRIMARY KEY,
    brand_id        INT UNSIGNED              NOT NULL,
    category        VARCHAR(30)               NOT NULL,
    name            VARCHAR(40)               NOT NULL,
    price           DECIMAL(10, 2)            NOT NULL,
    created_at      TIMESTAMP                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                 NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
);
```

<br>

### 주문
```sql
CREATE TABLE order
(
    id              BIGINT AUTO_INCREMENT     PRIMARY KEY,
    basic_user_id   INT UNSIGNED              NOT NULL,
    status          VARCHAR(20)               NOT NULL,
    created_at      TIMESTAMP                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                 NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
);
```

<br>

### 주문 상품
```sql
CREATE TABLE order_product
(
    id              BIGINT AUTO_INCREMENT       PRIMARY KEY,
    order_id        BIGINT                      NOT NULL,
    product_id      BIGINT                      NOT NULL,
    quantity        INT                         NOT NULL,
    created_at      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
);
```

