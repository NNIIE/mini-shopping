DROP TABLE IF EXISTS order_product;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS brand;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS token;

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

CREATE TABLE user
(
    id              INT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    account_id      INT UNSIGNED                    NOT NULL,
    nickname        VARCHAR(20)                     NOT NULL,
    phone_number    VARCHAR(30)                     NOT NULL,
    refresh_token   VARCHAR(600)                    NULL,
    created_at      TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_nickname (nickname)
);

CREATE TABLE admin
(
    id                INT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    account_id        INT UNSIGNED                    NOT NULL,
    created_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE brand
(
    id                INT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    admin_id          INT UNSIGNED                    NOT NULL,
    name              VARCHAR(40)                     NOT NULL,
    business_number   VARCHAR(30)                     NOT NULL,
    created_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_name (name),
    UNIQUE INDEX idx_business_number (business_number)
);

CREATE TABLE category
(
    id                INT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    name              VARCHAR(30)                     NOT NULL,
    created_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX idx_name (name)
);

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

CREATE TABLE orders
(
    id              BIGINT UNSIGNED AUTO_INCREMENT     PRIMARY KEY,
    user_id         INT UNSIGNED                       NOT NULL,
    status          VARCHAR(20)                        NOT NULL,
    created_at      TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE order_product
(
    id              BIGINT UNSIGNED AUTO_INCREMENT       PRIMARY KEY,
    order_id        BIGINT UNSIGNED                      NOT NULL,
    product_id      BIGINT UNSIGNED                      NOT NULL,
    quantity        INT UNSIGNED                         NOT NULL,
    created_at      TIMESTAMP                            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);