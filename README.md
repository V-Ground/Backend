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

**클래스 기능**

#### 학생 초대 및 컨테이너 자원 생성

학생이 클래스에 초대될 때 서버에서는 `aws-cli` 를 이용하여 ecs cluster 에 Fargate 기반 컨테이너를 할당받고 상호작용 모듈과 통신을 위한 IP를 저장합니다.

#### 가상머신(컨테이너) 접속

컨테이너 내부에는 웹에서 컨테이너 GUI를 위한 VNC가 포함되어있고 학생들은 VNC를 통해 컨테이너 내부에서 실습을 진행합니다.

#### OX 퀴즈

강사는 실습의 속도 및 학생들의 상황을 파악하기 위해 OX 퀴즈를 출제할 수 있습니다.
학생은 OX를 통해 강사와 상호작용합니다.

#### 과제 생성 및 문제 출제

강사는 수업 도중, 수업이 끝나고 난 후에 과제를 생성하고 실습과 관련된 단답식, 주관식 문제를 출제할 수 있습니다.

- **학생 상호작용 기능**

#### 명령어 기록 확인

강사는 학생들의 실습 상황 및 활동을 확인하기 위해 학생들이 터미널에서 사용한 명령어들을 확인할 수 있습니다.
해당 API가 호출되면 컨테이너 내부에 위치한 `bash_history` 를 확인할 수 있습니다.

#### 원격 명령어 실행

강사는 컨테이너 내부에서 실행 가능한 bash command 를 입력할 수 있습니다.
해당 API가 호출되면 클래스에 소속된 모든 컨테이너로 원격 명령을 수행합니다.

#### 원격 스크립트 실행

원격 명령이 길거나 다양한 명령을 수행하기 위해 shell script 및 compilabe code 를 업로드할 수 있습니다.
해당 API가 호출되면 해당 코드의 실행 결과를 확인할 수 있습니다.

#### 파일 삽입

강사는 실습에 필요한 파일을 수업 도중 생성하고 학생들의 컨테이너 내부로 주입할 수 있습니다.

#### 설치 패키지 확인

강사가 실습에 필요한 패키지를 설치하라고 한 경우 설치 여부에 대한 확인을 이 API를 통해 수행할 수 있습니다.
package 를 이용한 설치나 source code 설치 모두 확인할 수 있습니다.

#### 파일 내용 확인 및 생성 여부 확인

강사는 컨테이너 내부에 위치한 모든 파일들을 확인할 수 있습니다.
해당 API가 호출되면 컨테이너 내부로 파일 존재 여부를 확인하고 필요한 경우 파일을 강사에게 보여줍니다.

# 👩🏻‍💻 What is Challenging

### 📌 awc-cli를 이용한 클라우드 자원 관리

학생 가상머신 (컨테이너) 생성 자동화를 위해 awc-cli 가 사용되었습니다.

클래스에 학생을 초대할 때 awc-cli 의 ecs 명령어로 task 를 생성합니다.

### 📌 Open Feign 을 이용한 API 통신

상호작용 모듈은 한 번의 호출에 클래스에 소속된 모든 컨테이너의 상호작용 웹서버 통신을 수행해야 합니다.
이를 위해 Spring Cloud 에서 사용되는 HTTP Client 모듈인 OpenFeign 이 사용되었습니다.

### 📌 실습을 위한 가상머신 선정에 대한 고민

실습 머신으로 다양한 후보군이 존재하였습니다.

- VM
- Container
- Firecracker

# ⛳️ 프로젝트 소개

### 📌 사용 기술

### 📌 아키텍쳐

![deploy](https://github.com/V-Ground/Backend/blob/master/assets/infra.png)

### 📌 DB 설계

##### [자세히](https://github.com/V-Ground/Backend/blob/master/assets/db.png)

![deploy](https://github.com/V-Ground/Backend/blob/master/assets/db.png)

### 📌 Contents List

### 📌 UI Prototype

![UI](https://github.com/dhslrl321/Bless-Music-Studio/blob/master/images/xd.png)
