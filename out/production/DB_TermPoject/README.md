# DB_TermPoject

### 1.    프로젝트 주제 (Project topic)
배달 서비스 데이터베이스 관리 시스템

이번에 진행할 프로젝트의 주제는 배달의 민족, 요기요과 같은 배달 서비스 플랫폼에서 사용할 DB를 구현한 프로젝트 입니다. 

또 구현하려는 시스템의 기능에 대해 서술해보면

1.    사용자 관리 
=> 사용자의 회원 가입, 로그인, 개인 정보 관리, 주문 이력 조회, 즐겨찾기 설정, 리뷰 작성 등의 기능을 제공한다. 

2.    음식점 관리
=> 음식점 등록, 메뉴 관리, 영업 시간 설정, 판매 이력 조회 등의 기능을 제공한다. 또 인기 메뉴, 평점, 리뷰, 실시간 매출 등의 기능도 제공함.

3.    메뉴 관리
=> 메뉴 등록, 가격 설정, 재고 관리, 판매량 조회 등의 기능을 제공한다. 또 메뉴별 데이터를 이용하여 인기 메뉴, 판매량, 매출 등의 기능도 제공함.

4.    주문 관리
=> 사용자의 주문을 처리하고, 배달 상태를 업데이트한다. 또 주문 데이터를 이용하여 음식점의 매출, 인기 시간대, 인기 지역 등의 기능도 제공함.

5.    배달 관리
=> 배달원 등록, 배달 상태 업데이트, 배달 이력 조회 등을 관리한다. 또 배달 데이터를 이용하여 배달 시간, 배달 지역, 배달원별 배달 횟수 등의 기능도 제공함. 

### 2.    사용자 (역할) (Users / Roles)
각 사용자의 역할에 대해 서술해보면

1.    고객
=> 고객은 음식점을 검색하고, 메뉴를 선택하여 주문을 할 수 있다. 또, 주문한 음식의 배달 상태를 확인하고, 음식과 배달 서비스에 대한 리뷰를 작성할 수 있음. 

2.    음식점 주인
=> 음식점 주인은 자신의 음식점을 등록하고, 메뉴와 가격, 영업 시간 등을 등록/업데이트 할 수 있다. 또, 주문이 들어오면 주문을 처리하고, 주문 이력을 조회하여 인기 메뉴나 평점, 리뷰 등을 확인할 수 있음.

3.    배달원 
=> 배달원은 주문을 수령하고, 주문한 고객에게 음식을 배달한다. 이때 배달 상태를 업데이트하고, 배달 이력을 조회할 수 있음. 

4.    플랫폼 서비스 제공자
=> 서비스 제공자는 사용자 계정 관리, 데이터를 가공하거나 관리, 웹과 어플의 형태로 서비스 제공 등의 역할을 한다.

### 3.    기능 (Functions)
<고객>
1.    음식점 검색
=> 고객은 음식점 이름, 위치, 음식 종류 등의 다양한 조건을 기반으로 음식점을 검색한다. 구체적으로 SELECT 쿼리를 사용하여 음식점 테이블에서 조건에 맞는 음식점을 조회함.

2.    메뉴 선택 및 주문
=> 고객은 검색한 음식점의 메뉴 중에서 원하는 메뉴를 선택하고 주문한다. 주문은 트랜잭션으로 처리되며, 주문에 성공하면 해당 메뉴의 주문 수를 증가시킴.

3.    배달 상태 확인 
=> 고객은 주문한 음식의 배달 상태를 확인한다. 구체적으로 SELECT 쿼리를 사용하여 주문 테이블에서 배달 상태를 조회함.

4.    리뷰 작성
=> 고객은 주문한 음식과 배달 서비스에 대한 리뷰를 작성한다. 구체적으로 DML을 사용하여 리뷰 테이블에 새로운 레코드를 추가함.

<음식점 주인>
1.    음식점 정보 등록 및 업데이트
=> 음식점 주인은 자신의 음식점 정보를 ‘등록’, ‘수정’, ‘삭제’ 한다. 구체적으로 DML을 사용하여 음식점 테이블에 새로운 레코드를 추가하거나 기존 레코드를 업데이트함.

2.    메뉴 관리
=> 음식점 주인은 자신의 음식점의 메뉴를 ‘추가’, ‘수정’, ‘삭제’ 한다. 구체적으로 DML을 사용하여 메뉴 테이블을 업데이트함.

3.    주문 처리 
=> 음식점 주인은 고객의 주문을 확인하고 처리한다. 구체적으로 DML을 사용하여 주문 테이블의 주문 상태를 업데이트함.

4.    주문 이력 조회
=> 음식점 주인은 자신의 음식점에 대한 주문 이력을 조회한다. 구체적으로 SELECT 쿼리를 사용하여 주문 테이블에서 주문 이력을 조회함. 또한, 주문량, 매출 등의 기준으로 결과를 정렬하거나, 특정 기간, 메뉴 등의 조건으로 결과를 필터링할 수 있음.

<배달원>
1.    배달 상태 업데이트
=> 배달원은 배달 상태를 업데이트한다. 구체적으로 DML을 사용하여 주문 테이블의 배달 상태를 업데이트함.

2.    배달 이력 조회
=> 배달원은 자신의 배달 이력을 조회한다. 구체적으로 SELECT 쿼리를 사용하여 주문 테이블에서 배달 이력을 조회함. 또한, 배달 시간, 배달 거리 등의 기준으로 결과를 정렬하거나, 특정 기간, 지역 등의 조건으로 결과를 필터링할 수 있음.

<플랫폼 서비스 제공자>
1.    사용자 계정 관리
=> 서비스 제공자는 사용자 계정을 ‘등록’, ‘수정’, ‘삭제’ 한다. 구체적으로 DML을 사용하여 사용자 테이블을 업데이트함.

### 4.    데이터베이스 스키마 (Database schema)
Customer (customer_id, name, address, phone_number, email)
-    Primary key: customer_id
-    Constraint: email은 UNIQUE 해야 한다. 즉, 각 고객의 이메일 주소는 중복될 수 없음.
-    Authorization: 고객 본인과 관리자만이 정보를 수정할 수 있다.

RestaurantOwner (owner_id, name, phone_number, email, restaurant_id)
-    Primary key: owner_id
-    Foreign key: restaurant_id -> Restaurant(restaurant_id)
-    Constraint: email은 UNIQUE 해야 한다. 즉, 각 음식점 사장의 이메일 주소는 중복될 수 없음.
-    Authorization: 음식점 사장 본인과 관리자만이 정보를 수정할 수 있다.

Restaurant (restaurant_id, name, address, cuisine_type)
-    Primary key: restaurant_id
-    Authorization: 해당 음식점의 사장과 관리자만이 정보를 수정할 수 있다.

DeliveryPerson (delivery_person_id, name, phone_number)
-    Primary key: delivery_person_id
-    Authorization: 배달원 본인과 관리자만이 정보를 수정할 수 있다.

Menu (menu_id, name, price, restaurant_id)
-    Primary key: menu_id
-    Foreign key: restaurant_id -> Restaurant(restaurant_id)
-    Authorization: 해당 메뉴의 음식점 사장과 관리자만이 정보를 수정할 수 있다.

Order (order_id, customer_id, menu_id, delivery_person_id, order_status, order_time)
-    Primary key: order_id
-    Foreign key: customer_id -> Customer(customer_id)
-    Foreign key: menu_id -> Menu(menu_id)
-    Foreign key: delivery_person_id -> DeliveryPerson(delivery_person_id)
-    Authorization: 주문한 고객, 해당 주문의 배달원, 해당 메뉴의 음식점 사장, 관리자만이 정보를 수정할 수 있다.

Review (review_id, rating, comment, customer_id, restaurant_id)
-    Primary key: review_id
-    Foreign key: customer_id -> Customer(customer_id)
-    Foreign key: restaurant_id -> Restaurant(restaurant_id)
-    Authorization: 리뷰를 작성한 고객과 관리자만이 정보를 수정할 수 있다.
