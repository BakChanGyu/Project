# 필적감정 AI를 활용한 신원확인 시스템
`2023.09.04` ~ `2023.11.12`

> 필적감정 AI를 활용한 신원확인 시스템
> 

## 1. 프로젝트 개요

- 기존에 존재하는 신원 확인 시스템은 발급이 번거롭고, 분실 또는 변질의 우려가 있습니다. 이런 불편함을 개선하기 위해 시간과 장소의 제한 없이 구하기 쉽고, 개개인을 식별하기에 알맞은 필적을 새로운 신원 확인 방법으로 제시하고자 합니다.

## 2. 프로젝트 멤버

- 한재혁 : UI 설계 및 React를 이용한 View 구현
- 박찬규 : SpringBoot를 이용한 백엔드, AWS 배포
- 김동현 : DB 설계 및 필적 인식 AI모델 개발
- 양준규 : 필적 인식 AI 모델 개발, React를 이용한 View 구현

## 3. 기능 구현 목록

- 회원 관리, 대상 관리
- 파일 업로드 관리
- 이메일 관리
- 필적 이미지 처리
- 필적 감정
- 필적 학습
- 엑셀 명단 데이터 추출

## 4. 기술 스택

**FrontEnd**

- React
- Redux

**Backend**

- Java 11
- Spring Boot 2.7.0
- JDBC
- AWS RDS(MySQL)
- Tensorflow
- Keras
    - VGG-16
- Flask

**DevOps**

- AWS EC2
- AWS RDS

## 5. 시스템 아키텍처

![제목 없는 다이어그램.drawio (1).png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/24b894de-c1f0-461f-a021-1e83fd762421/%EC%A0%9C%EB%AA%A9_%EC%97%86%EB%8A%94_%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.drawio_(1).png)

![제목 없는 다이어그램 drawio (1)](https://github.com/BakChanGyu/Project/assets/103313634/fe23d7c3-7477-4c1b-9035-a43ab7b1beaa)


## 6. 설계 다이어그램

![noname01.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/46e8ef17-5ba5-43a9-bdd7-b6981a2a5ba7/noname01.png)

![noname01.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1f511613-da6b-4b4a-8921-fc339729693a/noname01.png)

![noname01.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/410ac0d9-1c49-4fec-878b-3238584532e6/noname01.png)

![noname01](https://github.com/BakChanGyu/Project/assets/103313634/d46b8a0b-0885-44d4-bb5c-58f52aab184d)
