# CLOUDY

## **프로젝트 소개**

클라우드 자원 사용 현황과 비용을 실시간으로 시각화하고 최적화 방안을 제시하는 FinOps 대시보드 서비스

## 개발 기간

**시작일**: 2024.10.12

**종료일**: 2024.11.19

## 개발환경 및 기술스택

### Frontend

<img src="https://github.com/user-attachments/assets/094fb570-b444-4a4b-ba07-220be032892b" alt="TypeScript" width="50" />
<img src="https://github.com/user-attachments/assets/8b6eaf6f-07d8-4d4b-9544-3826865e6908" alt="React.js" width="50" />
<img src="https://github.com/user-attachments/assets/22f079fb-47e2-497f-b56e-696f8db6da68" alt="Next.js" width="50" />
<img src="https://github.com/user-attachments/assets/5c520917-2dbe-4bc1-a806-ea90efc8aea6" alt="Tanstack" width="50" />
<img src="https://github.com/user-attachments/assets/1eb2c20d-dd3b-4b29-b390-79d17ab9d642" alt="TailwindCSS" width="50" />
<img src="https://github.com/user-attachments/assets/6313e5a7-f2fc-46bc-b15f-de70e084ab35" alt="Yarn" width="50" />

### Backend

<img src="https://github.com/user-attachments/assets/0469144a-7fb0-4dea-a918-ea5d6c2db993" alt="Logstash" width="50" />
<img src="https://github.com/user-attachments/assets/b7d83208-ef64-4729-b964-3c9d54f3c17b" alt="Spring" width="50" />
<img src="https://github.com/user-attachments/assets/f63bf6e9-1639-4d50-bd31-8ff3dd151427" alt="Java" width="50" />
<img src="https://github.com/user-attachments/assets/218b338b-d543-4ccc-8e8c-0ce02c0d92a8" alt="Elasticsearch" width="50" />
<img src="https://github.com/user-attachments/assets/248a66ce-870f-43de-a9b3-71b8a296fa16" alt="MySQL" width="50" />

### Infra

<img src="https://github.com/user-attachments/assets/3d8cc576-8dd2-41a8-ae8c-bf10521c3125" alt="Nginx" width="50" />  
<img src="https://github.com/user-attachments/assets/df442036-371d-489c-84e4-d93e7521858d" alt="Jenkins" width="35" />
<img src="https://github.com/user-attachments/assets/48330965-13cc-45c5-bba7-02e1ee807494" alt="Docker" width="50" />

### Tool

<img src="https://github.com/user-attachments/assets/f2958f55-b4a7-4d38-ac10-f9d431464592" alt="Figma" width="50" />
<img src="https://github.com/user-attachments/assets/1e0e8938-065d-4dc6-8e3d-7c97f54ece2b" alt="GitLab" width="50" />
<img src="https://github.com/user-attachments/assets/4677b90c-215c-4858-b444-31892ca2cfbf" alt="Slack" width="50" />
<img src="https://github.com/user-attachments/assets/4eb16258-2232-4741-ad33-c7dbda41656f" alt="Notion" width="50" />

## 설계

![설계1](https://github.com/user-attachments/assets/1f59dd6c-ad93-4b3a-8dea-0ae3a8097227)
![설계2](https://github.com/user-attachments/assets/3d2cefa1-5bca-476f-94db-fbab5b22b92f)

## 주요기능

![주요기능](https://github.com/user-attachments/assets/ec8034bf-d4ce-41c4-98df-5eb447bbcec3)

## 구현 화면

[cloudy 배포 링크](http://k11a606.p.ssafy.io:3000/signin)

### 대시보드

- 서버 호출 횟수 및 CPU/메모리 사용량을 보여주는 실시간 line 및 guage 차트

- 사용량에 따른 비용 요약과 서버 추천

- 일일 비용을 보여주는 캘린더

![메인](https://github.com/user-attachments/assets/1c95e8b3-45d1-4bd7-b5c3-8af3ff209944)

### 서버 사용량

- 컨테이너별 호출 횟수를 보여주는 실시간 차트와 표

- 컨테이너별 호출 횟수를 비율로 확인할 수 있는 버블 차트

![서버사용량](https://github.com/user-attachments/assets/1be284f1-69a1-4d1c-938f-6465893741d3)

### 컨테이너 사용량

- 컨테이너에 떠있는 각각의 외부 서비스에 대한 사용량

![컨테이너](https://github.com/user-attachments/assets/7393fcae-8460-4e1e-a790-b2f6f37af1c9)

### 비용 캘린더

- 일자별 서버 사용 비용을 확인할 수 있는 캘린더

- 선택된 날자로부터 최근 일주일의 컨테이너별 호출 횟수를 보여주는 표와 bar 차트

![비용캘린더](https://github.com/user-attachments/assets/a5760bd9-3942-42a1-8df4-59cebf8528a0)

### 임계치 알람

- 회원이 설정한 임계치에 도달하면 해당 회사 직원들에게 임계치가 초과했다는 알람 발송

![알람목록 PNG](https://github.com/user-attachments/assets/54c4a009-b2a9-44d2-97ef-0b358568fb12)

### 회원 설정

- 관리자 계정일 경우 부서에 따른 계정 생성 및 삭제를 통해 관리

![회원설정](https://github.com/user-attachments/assets/73823bfc-2165-4538-85f7-2e0284cd072b)

### 임계치 설정

- 서버 사용량에 따른 비용($)를 기준으로 임계치를 설정

![임계치 PNG](https://github.com/user-attachments/assets/9613cb46-030c-487a-bfe0-564caa1397e9)

### 서버 설정

- 클라우드 종류 및 인스턴스 종류, 결제 방식을 선택하여 서버를 등록하고 상세 내용을 조회 및 삭제

![서버설정 PNG](https://github.com/user-attachments/assets/14e0f2ed-0398-4e33-a317-161aa2f6a844)

## 팀 소개

### Frontend

| 역할         | 이름       | 작업 내용                                                                                                                                            | GitHub ID                                            |
| ------------ | ---------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------- |
| **Frontend** | **표다영** | 디자인, 로그인, 대시보드/서버 및 컨테이너 사용량 실시간 line 차트, 서버 설정, SSE 알람                                                               | [@celestedayoung](https://github.com/celestedayoung) |
|              | **김현지** | 디자인, 대시보드/서버 및 컨테이너 사용량 실시간 bubble 차트, 비용 캘린더, 임계치 설정, 일반 회원 설정, 회원가입                                      | [@hyeonzi423](https://github.com/hyeonzi423)         |
| **BackEnd**  | **박성재** | 인프라(프론트, 백엔드 배포 자동화, 컨테이너 관리), logstash를 활용한 로그 데이터 수집, elasticsearch를 활용한 로그 데이터 분석 API 구축, 부하 테스트 | [@qkrtjdwo5662](https://github.com/qkrtjdwo5662)     |
|              | **안재현** | Elasticsearch를 활용한 로그 데이터 분석 및 인스턴스 추천, Processbuilder를 활용한 CPU, 메모리 사용량 API 개발, API 구축                              | [@jhyun9505](https://github.com/jhyun9505)           |
|              | **고동현** | SSE 알람, 인프라 초기세팅, 기본 API 세팅, DB 설계                                                                                                    | [@DongHyun222](https://github.com/DongHyun222)       |

## 아키텍쳐

![아키텍쳐](https://github.com/user-attachments/assets/07656563-4e00-483e-98b5-45a362789f79)
