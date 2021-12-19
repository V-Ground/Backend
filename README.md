# **클라우드 기반 샌드박스 보안 실습 강의 플랫폼**

<div align="center"> 
  <img style="margin: 15px" src="https://img.shields.io/badge/react.js-17.0.2-9cf.svg" alt="badge-react" />
  <img style="margin: 15px" src="https://img.shields.io/badge/Next.js-11.1.2-inactive.svg" alt="badge-react"/>
  <img style="margin: 15px" src="https://img.shields.io/badge/Redux-4.1.1-red.svg" alt="badge-react" />
  <img style="margin: 15px" src="https://img.shields.io/badge/SpringBoot-2.5.4-green.svg" alt="badge-react" />
  <img style="margin: 15px" src="https://img.shields.io/badge/QueryDsl-4.4.0-success.svg" alt="badge-react"/>
  <img style="margin: 15px" src="https://img.shields.io/badge/NHNCloud-Instance-blue.svg" alt="badge-react"/>
  <img style="margin: 15px" src="https://img.shields.io/badge/AWS-S3-orange.svg" alt="badge-react"/>
  <img style="margin: 15px" src="https://img.shields.io/badge/Nginx-2.5.4-green.svg" alt="badge-react" />
</div>

# 📝목차

1. ### [팀원 소개](#-팀원-소개)
2. ### [기획 배경 및 기능 명세](#-기획-배경-및-필요성)

- - 2-1 [기회 의도](#-기획-의도)
- - 2-2 [해결 방안](#-서비스-목적)
- - 2-2 [제공 기능](#-제공-기능)

3. ### [What is Challenging](#-What-is-Challenging)

- - 3-1 [awc-cli를 이용한 클라우드 자원 관리](#-SEO-검색-엔진-최적화)
- - 3-2 [Open Feign 을 이용한 API 통신](#-Nginx-와-무중단-배포)
- - 3-3 [실습을 위한 가상머신 선정에 대한 고민](#-Nginx-와-무중단-배포)

4. ### [프로젝트 소개](#-프로젝트-소개)

- - 4-1 [사용 기술](#-사용-기술)
- - 4-2 [아키텍처](#-CI-CD-Pipeline)
- - 4-3 [DB 설계](#-배포-구조)

5. ### [User Interfaces](#-User-Interface)

- - 5-1 [Contents List](#-Contents-List)
- - 5-2 [UI Prototype By Adobe XD](#-UI-Prototype)

# 👨‍👩‍👧‍👦 팀원 소개

|    👨‍👨‍👧    |    🧑‍💼 김경태    | 🧑‍💻 고현수 | 🧑‍🎨 장원익 |          🥷 임창현           |               👩🏻‍⚕️ 강지민                |
| :------: | :----------------: | :----------: | :----------: | :-------------------------: | :------------------------------------: |
| **역할** | PM, 평가 모듈 개발 |  Front-End   |   Back-End   | 평가 모듈(Log Capture) 개발 | 평가 모듈(Network Packet Capture) 게발 |

# 🔖 기획 배경 및 필요성

### 📌 기획 의도

Best Of the Best 멘토님들을 대상으로 한 **온라인 실습시 불편한점 및 개선사항** 설문조사에서 압도적으로 **학생들의 실습 진행 상황 파악이 어려움**이 문제로 지적되었습니다.

이를 위해 웹에서 강사와 학생들에게 **동일한 실습 환경을 제공**하고 학생들의 **실습 진행상황을 확인**할 수 있는 플랫폼을 제공하고자 합니다.

### 📌 제공 기능

- **클래스 기능**
  - `학생 초대 및 컨테이너 자원 생성`
  - `가상머신(컨테이너) 접속`
  - `OX 퀴즈`
  - `과제 생성 및 문제 출제`
- **학생 상호작용 기능**
  - `명령어 기록 확인`
  - `원격 명령어 실행`
  - `원격 스크립트 실행`
  - `파일 삽입`
  - `설치 패키지 확인`
  - `파일 내용 확인 및 생성 여부 확인`

##### [> 자세히](https://github.com/V-Ground/Backend/wiki/기능-명세)

# 👩🏻‍💻 What is Challenging

### 📌 awc-cli를 이용한 클라우드 자원 관리

학생 가상머신 (컨테이너) 생성 자동화를 위해 `awc-cli` 가 사용되었습니다.

클래스에 학생을 초대할 때 `awc-cli` 의 `ecs` 명령어로 task 를 생성합니다.

### 📌 Open Feign 을 이용한 API 통신

상호작용 모듈은 한 번의 호출에 클래스에 소속된 모든 컨테이너의 상호작용 웹서버 통신을 수행해야 합니다.
이를 위해 Spring Cloud 에서 사용되는 HTTP Client 모듈인 `OpenFeign` 이 사용되었습니다.

### 📌 실습을 위한 가상머신 선정에 대한 고민

실습 머신으로 다양한 후보군이 존재하였습니다.

- `VM`
- `Container`
- `Firecracker`

# ⛳️ 프로젝트 소개

### 📌 사용 기술

![tech](https://github.com/V-Ground/Backend/blob/master/assets/tech.png)

- **Front-End**
  - `next.js`
  - `react.js`
  - `axios`
  - `mui`
  - `NoVnc`
- **Back-End**
  - `spring-boot`
  - `sprint security`
  - `spring data jpa`
  - `query dsl`
  - `spring-cloud-open-feign`
- **Infrastructure**
  - `aws-cli`
  - `aws-ec2`
  - `aws-ecs`
  - `aws-fargate`
- **Etc**
  - `postman`
  - `git`
  - `github`
  - `adobe xd`

### 📌 아키텍쳐

![infra](https://github.com/V-Ground/Backend/blob/master/assets/infra.png)

### 📌 DB 설계

![deploy](https://github.com/V-Ground/Backend/blob/master/assets/db.png)

##### [> 자세히](https://github.com/V-Ground/Backend/blob/master/ass0ets/db.png)

### 📌 UI Prototype

![UI](https://github.com/V-Ground/Backend/blob/master/assets/ui.png)
