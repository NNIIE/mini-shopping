# API

<br>

## 목차
- [상품](#상품)
  - [브랜드 별 상품 목록 조회](#브랜드-별-상품-목록-조회)
  - [상품 등록](#상품-등록)
  - [상품 수정](#상품-수정)
  - [상품 삭제](#상품-삭제)
- [검색](#검색)
  - [카테고리별 최저가 브랜드 상품 조회](#카테고리별-최저가-브랜드-상품-조회)
  - [단일 브랜드로 모든 카테고리 구매시 최저가격 브랜드와 가격, 총액 조회](#단일-브랜드로-모든-카테고리-구매시-최저가격-브랜드와-가격-총액-조회)
  - [카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회](#카테고리-이름으로-최저-최고-가격-브랜드와-상품-가격-조회)

---

<br>

# 상품
## 브랜드 별 상품 목록 조회
#### URL
```
GET <baseEndPoint>/product/brand/{brandId}
```

#### 요청 헤더
```
Accept: application/json
```

#### Request Parameter
| 이름      | 타입      | 설명        | 필수 |
|----------|----------|------------|-----|
| brandId | Long    | 브랜드 ID    | O  |
| page | Integer    | 페이지 번호    | X (기본값: 0)  |
| size | Integer    | 페이지당 항목 수    | X (기본값: 20) |
| sort | Integer    | 정렬 기준     | X (기본값: id, asc) |

#### Response
| 이름      | 타입      | 설명        |
|----------|----------|------------|
| elements | List     | 현재 페이지 상품 목록     |
| elements.id       | Long     | 상품 ID     |
| elements.brandId  | Long     | 브랜드 ID    |
| elements.category | String   | 상품 카테고리  |
| elements.price    | BigDecimal   | 상품 가격     |
| totalPages    | Integer   | 전체 페이지 수     |
| totalElements    | Long   | 전체 항목 수    |
| pageNumber    | Integer   | 현재 페이지 번호     |
| pageSize    | Integer   | 페이지당 항목 수     |

#### 예제
##### 요청
```
GET <baseEndPoint>/product/brand/1?page=0&size=10&sort=price,asc
```

##### 응답
``` json
{
  "elements": [
    {
      "id": 1,
      "brandId": 1,
      "category": "SNEAKERS",
      "price": 120000
    },
    {
      "id": 3,
      "brandId": 1,
      "category": "HAT",
      "price": 35000
    }
  ],
  "totalPages": 3,
  "totalElements": 24,
  "pageNumber": 0,
  "pageSize": 10
}
```

<br>

------------------------------------

<br>

## 상품 등록
#### URL
```
POST <baseEndPoint>/product
```

#### 요청 헤더
```
Accept: application/json
Content-Type: application/json
```

#### Request Parameter
| 이름      | 타입      | 설명        | 필수 |
|----------|----------|------------|-----|
| brandId  | Long     | 브랜드 ID    | O  |
| category | String   | 상품 카테고리  | O  |
| price    | BigDecimal   | 상품 가격     | O  |


#### Response
- 상태코드: 201 Created
- 응답 본문: 없음

#### 예제
##### 요청
```
POST <baseEndPoint>/product

{
  "brandId": 1,
  "category": "SNEAKERS",
  "price": 120000
}
```

<br>

------------------------------------

<br>

## 상품 수정
#### URL
```
PATCH <baseEndPoint>/product/{id}
```

#### 요청 헤더
```
Accept: application/json
Content-Type: application/json
```

#### Request Parameter
| 이름      | 타입      | 설명        | 필수 |
|----------|----------|------------|-----|
| id       | Long     | 상품 ID     | O   |
| price    | BigDecimal   | 상품 가격     | O  |


#### Response
- 상태코드: 200 OK
- 응답 본문: 없음

#### 예제
##### 요청
```
PATCH <baseEndPoint>/product/1

{
  "price": 120000
}
```

<br>

------------------------------------

<br>

## 상품 삭제
#### URL
```
DELETE <baseEndPoint>/product/{id}
```

#### 요청 헤더
```
Accept: application/json
```

#### Request Parameter
| 이름      | 타입      | 설명        | 필수 |
|----------|----------|------------|-----|
| id       | Long     | 상품 ID     | O   |


#### Response
- 상태코드: 204 No Content
- 응답 본문: 없음

#### 예제
##### 요청
```
DELETE <baseEndPoint>/product/1
```

<br>

------------------------------------

<br>

# 검색
## 카테고리별 최저가 브랜드 상품 조회
#### URL
```
GET <baseEndPoint>/search/categories/lowest-prices
```

#### 요청 헤더
```
Accept: application/json
```

#### Request Parameter
없음

#### Response
| 이름      | 타입      | 설명        |
|----------|----------|------------|
| totalPrice    | BigDecimal     | 전체 가격     |
| products  | List     | 상품 목록    |
| products.category | String   | 상품 카테고리  |
| products.brandId    | Long   | 브랜드 ID   |
| products.price    | BigDecimal   | 상품 가격   |

#### 예제
##### 요청
```
GET <baseEndPoint>/search/categories/lowest-prices
```

##### 응답
``` json
{
  "totalPrice": 350000,
  "products": [
    {
      "category": "TOP",
      "brandId": 1,
      "price": 50000
    },
    {
      "category": "PANTS",
      "brandId": 2,
      "price": 70000
    },
    {
      "category": "SNEAKERS",
      "brandId": 3,
      "price": 100000
    }
  ]
}
```

<br>

------------------------------------

<br>

## 단일 브랜드로 모든 카테고리 구매시 최저가격 브랜드와 가격, 총액 조회
#### URL
```
GET <baseEndPoint>/search/brands/best-value
```

#### 요청 헤더
```
Accept: application/json
```

#### Request Parameter
없음

#### Response
| 이름      | 타입      | 설명        |
|----------|----------|------------|
| brandId    | Long     | 브랜드 ID     |
| totalPrice | BigDecimal     | 전체 가격     |
| products  | List     | 상품 목록    |
| products.category | String   | 상품 카테고리  |
| products.price    | BigDecimal   | 상품 가격   |

#### 예제
##### 요청
```
GET <baseEndPoint>/search/brands/best-value
```

##### 응답
``` json
{
  "brandId": 1,
  "products": [
    {
      "category": "TOP",
      "price": 55000
    },
    {
      "category": "PANTS",
      "price": 75000
    },
    {
      "category": "SNEAKERS",
      "price": 120000
    }
  ],
  "totalPrice": 250000
}
```

<br>

------------------------------------

<br>

## 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회
#### URL
```
GET <baseEndPoint>/search/category/{category}/price-range
```

#### 요청 헤더
```
Accept: application/json
```

#### Request Parameter
| 이름      | 타입      | 설명        | 필수 |
|----------|----------|------------|-----|
| category |  String   | 상품 카테고리    | O  |

#### Response
| 이름      | 타입      | 설명        |
|----------|----------|------------|
| category    | String     | 상품 카테고리     |
| lowestProduct | Object     | 최저가격 브랜드의 상품 가격     |
| lowestProduct.brandId  | Long     | 브랜드 ID    |
| lowestProduct.price | BigDecimal   | 상품 가격  |
| highestProduct | Object     | 최고가격 브랜드의 상품 가격     |
| highestProduct.brandId  | Long     | 브랜드 ID    |
| highestProduct.price | BigDecimal   | 상품 가격  |

#### 예제
##### 요청
```
GET <baseEndPoint>/search/category/TOP/price-range
```

##### 응답
``` json
{
  "category": "TOP",
  "lowestProduct": {
    "brandId": 1,
    "price": 90000
  },
  "highestProduct": {
    "brandId": 2,
    "price": 250000
  }
}
```
