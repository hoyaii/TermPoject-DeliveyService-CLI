# [PNU의 민족 - DB Term project](https://kc29be941feb6a.user-app.krampoline.com/)

<p align='center'>
<img width="309" alt="스크린샷 2024-02-17 오후 3 28 37" src="https://github.com/hoyaii/TermProject-DeliveryService-NodeJS/assets/131665728/0f48f479-391e-4788-bec3-9358cd5c9003">

</p>

<p align='center'>
    <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white">
    <img src="https://img.shields.io/badge/JDBC-339933?style=for-the-badge&logo=Jdbc&logoColor=white">
</p>

<br/>


# 목차
1. [프로젝트 소개](#프로젝트-소개)
2. [사용자 역할](#사용자-역할)
3. [서비스 기능](#서비스-기능)
4. [ERD Diagram](#ERD-Diagram)
5. [작동 화면](#작동-화면)

<br/>
<br/>

# 프로젝트 소개
이번에 진행할 프로젝트의 주제는 배달의 민족, 요기요과 같은 배달 서비스에서 사용할 DB를 구현하는 것 입니다! <br/><br/>
핵심 목표는 서비스의 핵심 기능 구현을 위한 데이터 베이스를 설계해보는 것이고, JAVA와 JDBC를 이용하여 CLI로 구현 했습니다.<br/>
배달의 민족의 경우 테이블이 100개가 넘어가는 것으로 아는데.. 이를 모두 구현해보는 것은 힘들기 때문에 간략하게 설계 헀습니다.<br/>
(JDBC를 써보니 왜 JPA가 등장하게 됬는지 뼈저리게 느꼈습니다 ㅎㅎ..)
<br/><br/>
그리고 DB 설계가 목표인 프로젝트이니, 설계를 위한 사용자 역할 설정과 기능을 자세히 서술하였습니다.
<br/>
<br/>
<br/>

# 사용자 역할

### 고객
- 음식점 검색 및 메뉴 선택을 통한 주문
- 주문한 음식의 배달 상태 확인
- 음식과 배달 서비스에 대한 리뷰 작성

### 음식점 주인
- 음식점 정보의 등록 및 업데이트
- 메뉴 관리 (추가, 수정, 삭제)
- 주문 처리 및 주문 이력 조회

### 배달원
- 배달 상태 업데이트
- 배달 이력 조회

### 플랫폼 서비스 제공자
- 사용자 계정 관리

</br>
<br/>

# 서비스 기능

### 고객 기능
- **음식점 검색**: 이름, 위치, 음식 종류 등으로 검색
- **메뉴 선택 및 주문**: 선택한 메뉴로 주문
- **배달 상태 확인**: 주문한 음식의 배달 상태 조회
- **리뷰 작성**: 음식과 서비스에 대한 평가

### 음식점 주인 기능
- **음식점 정보 등록 및 업데이트**: 정보 관리
- **메뉴 관리**: 메뉴의 추가, 수정, 삭제
- **주문 처리**: 들어온 주문 확인 및 처리
- **주문 이력 조회**: 주문 데이터 분석

### 배달원 기능
- **배달 상태 업데이트**: 배달 과정 관리
- **배달 이력 조회**: 배달 활동 기록 확인

### 플랫폼 서비스 제공자 기능
- **사용자 계정 관리**: 계정의 생성, 수정, 삭제
<br/>
<br/>

# ERD Diagram
![PNU의 민족](https://github.com/hoyaii/TermProject-DeliveryService-NodeJS/assets/131665728/84458ae2-c96a-4c92-922a-f1abbb72ee10)
<br/>
<br/>
<br/>
<br/>

# 작동 화면

서비스 이용을 위한 첫 단계부터 배달 완료 후의 리뷰 작성까지, 각 단계에서의 사용자 경험을 단계별로 설명합니다.

## 1. 회원가입
서비스 이용을 위해 필요한 첫 단계입니다. 유효성 검사와 이메일 중복 검사를 통해 사용자 입력을 검증합니다. DeliveryPerson은 서비스 지역을 추가로 입력해야 합니다.

### Customer 회원 가입 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/817efdb6-7590-411a-91b9-0219de0cc7b7" width="600" alt="그림1" />

### DeliveryPerson 회원 가입 화면 – 서비스 지역 추가 입력
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/381cd0bf-d559-48bf-90b7-f38217007c5d" width="600" alt="그림2" />

## 2. 로그인
회원가입을 마치고 나면 로그인을 통해 서비스를 이용할 수 있습니다. 로그인 시 유저의 역할에 따라 메인 페이지가 다르게 구현되어 있습니다.

### Customer의 메인 페이지
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/deed8fd5-ff8c-4479-9e24-bfcad8f3e8a9" width="600" alt="그림3" />

### RestaurantOwner의 메인 페이지
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/de3f1948-bd2a-49ef-b410-38b60c54f9ee" width="600" alt="그림4" />

## 3. 음식점 등록
식당 운영자는 서비스를 통해 자신의 음식점을 등록할 수 있습니다. 유효성 검사를 통해 입력을 검증합니다.

### 음식점 정보 등록 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/1a3f5cd9-4eb3-48a3-b074-0406f106880d" width="600" alt="그림5" />

## 4. 음식점 업데이트
음식점 정보의 변경이 필요할 때 업데이트할 수 있습니다.

### 음식점 업데이트 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/fd7e78b7-b9a1-459d-a69a-6571f8a1ae2f" width="600" alt="그림6" />

## 5. 메뉴 정보 입력
음식점 정보를 입력한 후, 다음으로 메뉴 정보를 입력해야 합니다. 메뉴 관리 기능에서는 메뉴를 등록, 수정, 삭제를 할 수 있습니다.

### 메뉴 추가 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/89fdf0fb-f495-45f7-a195-5910bf323841" width="600" alt="그림7" />

### 메뉴 수정 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/88b062e2-cd0b-4d0f-8999-97b7cdfdee22" width="600" alt="그림8" />

## 6. 음식 주문
식당과 메뉴가 등록되어 있으면, 고객은 음식을 주문할 수 있습니다.

### 음식점 검색과 주문 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/9043bc7c-dd55-4113-8305-c2a21dc86c3c" width="600" alt="그림9" />

## 7. 배달 상태 확인
고객은 ‘배달 상태 확인’ 기능을 통해 배달 매칭 전, 매칭 완료, 조리 완료, 배달 완료 등의 상태를 지속적으로 확인할 수 있습니다.

### 배달 상태 조회 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/26f95da0-e0e8-419f-b41a-7f357a64627d" width="600" alt="그림10" />

## 8. 배달 요청 확인과 수락
배달원은 들어온 배달 요청 중 하나를 선택할 수 있습니다. 요청을 수락하면 고객과 식당 운영자가 이를 확인할 수 있고, 식당 운영자는 조리를 시작합니다.

### 배달 요청 확인과 수락 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/5cff2c4d-cea3-45da-80b2-ae1f1e3085d4" width="600" alt="그림11" />

### 고객이 배달이 매칭됨을 확인하는 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/0515a1c7-dd6c-447c-830a-f908a9101127" width="600" alt="그림12" />

### 식당 운영자가 배달이 매칭됨을 확인하는 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/10bd8fb9-12e9-4707-bc59-2fc06cc63d7e" width="600" alt="그림13" />

## 9. 조리 완료 처리
조리가 완료되면 식당 운영자는 ‘조리 완료 처리’를 하게 되고, 배달원이 조리 완료 처리를 보고 배달을 시작하게 됩니다.

### 조리 완료 처리 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/fa16250c-b0b7-4e75-b3ea-4b11478bb694" width="600" alt="그림14" />

### 고객이 조리가 완료됨을 확인하는 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/be247524-f0a0-40b4-8e35-b5fdb8805f87" width="600" alt="그림15" />

## 10. 배달 완료 처리
배달이 완료되면 ‘배달 완료’ 처리를 통해 배달 과정이 완료됩니다. 배달 완료 처리가 되면, 정산 처리가 되어 식당의 매출과 판매량에 반영되며, 고객은 리뷰를 작성할 수 있습니다.

### 배달 완료 처리 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/32b6e682-d8c7-4fa8-bbf3-ae3b5e133957" width="600" alt="그림16" />

## 11. 배달 이력 확인
완료된 배달의 경우 ‘배달 이력 확인’을 통해 이력을 확인할 수 있습니다. 배달 횟수와 완료된 배달의 정보를 출력합니다.

### 완료한 배달 이력 확인 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/b4142090-7600-408c-afc7-884d19fc4ce1" width="600" alt="그림17" />

### 고객이 배달이 완료됨을 확인하는 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/2d57e307-b96c-4b7e-810f-7358207df9b4" width="600" alt="그림18" />

## 12. 주문 요청/내역 조회
식당 운영자는 ‘주문 요청/내역 조회’를 통해 들어온 주문의 상태를 확인할 수 있습니다. 배달이 완료되었기 때문에 상태가 'finished'로 출력됩니다.

### 주문 내역 조회 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/9a23472e-a806-4699-b95c-d6bad7c8020f" width="600" alt="그림19" />

## 13. 리뷰 작성
배달이 완료되면 고객은 리뷰를 작성할 수 있습니다. 유효성 검사와 예외 처리를 통해 입력을 검증합니다. 리뷰 작성 후에는 주문한 메뉴를 즐겨찾기에 등록할 수도 있습니다.

### 리뷰 작성 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/95359693-301a-4283-b4a7-ee65eacf321a" width="600" alt="그림20" />

## 14. 즐겨찾기 조회
고객은 등록한 ‘즐겨찾기를 조회’할 수 있습니다.

### 즐겨찾기 조회 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/f3b6b350-fa95-44bd-aeb2-fd032fae2ba8" width="600" alt="그림21" />

## 15. 리뷰 확인
식당 운영자는 고객이 등록한 ‘리뷰를 확인’할 수 있습니다.

### 리뷰 확인 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/add8a9f1-3f1b-4f1d-8a61-add12c8d922a" width="600" alt="그림22" />

## 16. 매출과 판매량 확인
정산 처리된 주문에 대해 식당 운영자는 ‘매출과 판매량을 확인’할 수 있습니다.

### 매출/판매량 확인 화면
<img src="https://github.com/hoyaii/TermPoject-DeliveyService-CLI/assets/131665728/a2ce4d6b-c7b3-47fa-8e8c-80f14fbca412" width="600" alt="그림23" />



