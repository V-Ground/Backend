# V-Ground-Lab, Back-End

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


# Backend

- 용어 정리
- API Entrypoints

# 용어 정리

### 클래스

- 클라우드 자원을 이용해서 강사와 학생이 서로 실습을 진행할 수 있는 공간
- fields : **Course**

### 테스트

- 클래우드 자원을 이용해서 참여자가 문제를 해결하고 점수를 획득하는 공간
- fields : **Evaluation**

### 퀴즈

- 사용자가 해결해야 하는 문제로 클래스와 테스트에서 사용될 수 있다.
- fileds : **Quiz**

# API Entrypoints

- 회원 관리
- 클래스
- 테스트
- 퀴즈
- 과제
- 스냅샷

## 회원 관리

- 로그인

#### **사용자 계정**

```json
{
  "학생": {
    "학생 1": {
      "id": "student1@vground.com",
      "password": "student1",
      "이름": "강지민"
    },
    "학생 2": {
      "id": "student2@vground.com",
      "password": "student2",
      "이름": "고현수"
    },
    "학생 3": {
      "id": "student3@vground.com",
      "password": "student3",
      "이름": "김경태"
    },
    "학생 4": {
      "id": "student4@vground.com",
      "password": "student4",
      "이름": "임창현"
    },
    "학생 5": {
      "id": "student5@vground.com",
      "password": "student5",
      "이름": "장원익"
    }
  },
  "강사": {
    "강사 1": {
      "id": "teacher1@vground.com",
      "password": "teacher1",
      "이름": "정승기"
    },
    "강사 2": {
      "id": "teacher2@vground.com",
      "password": "teacher2",
      "이름": "강대명"
    },
    "강사 3": {
      "id": "teacher3@vground.com",
      "password": "teacher3",
      "이름": "홍성진"
    }
  }
}
```

## 회원

- **로그인** : POST
- **내가 소속된 모든 코스 조회** : GET : `/api/v1/users/{userId}/participating`
- **테스트 퀴즈 풀기** : POST `/api/v1/users/{userId}/evaluations/{evaluationId}/quizzes/{quizId}`
- **특정 학셍이 제출한 퀴즈 정답 확인** : GET `/api/v1/users/${teacherId}/evaluations/{evaluationId}/users/{userId}/quizzes`
- **과제 주관식 풀기** : POST `/api/v1/users/{userId}/course/{courseId}/assignments/{assignments}/questions/{questionId}`
- **특정 학생이 제출한 과제 정답 확인** : POST `/api/v1/users/{teacherId}/courses/{courseId}/assignments/{assignmentId}/questions/{questionId}`

## 강사

- **특정 학셍이 제출한 퀴즈 정답 확인** : GET `/api/v1/teachers/${teacherId}/evaluations/{evaluationId}/users/{userId}/quizzes/{quizId}`
- **특정 학셍이 제출한 퀴즈 정답 확인** : GET `/api/v1/teachers/${teacherId}/evaluations/{evaluationId}/assignments/{assignmentId}/users/{userId}/quizzes/{quizId}`
- **퀴즈 채점** : PATCH `/api/v1/teachers/${teacherId}/evaluations/{evaluationId}/quizzes/scoring/{quizId}`
- **과제 채점** : PATCH `/api/v1/users/{teacherId}/courses/{courseId}/assignments/{assignmentId}/scoring/{questionId}`

## 클래스

- **클래스 생성** : POST `/api/v1/courses`
- **클래스 전체 조회** : GET `/api/v1/courses`
- **클래스 비활성화** : PATCH `/api/v1/courses/{courseId}`
- **클래스 삭제** : DELETE `/api/v1/courses/{courseId}`

## 테스트

- **테스트 생성** : POST `/api/v1/evaluation`
- **테스트 전체 조회** : GET `/api/v1/evaluation`
- **테스트 비활성화** : PATCH `/api/v1/evaluation/{evaluationId}`
- **테스트 삭제** : DELETE `/api/v1/evaluation/{evaluationId}`

## 퀴즈

퀴즈의 수는 많지 않을 것으로 예상되므로 Client Side Paging 할 것
퀴즈의 (삭제, 사용자가 제출한 정답 수정)는 중요도가 낮아 보여서 개발하지 않았음

- **퀴즈 생성** : POST `/api/v1/evaluations/{evaluationId}/quizzes`
- **테스트에 존재하는 모든 퀴즈 조회** : GET `/api/v1/evaluations/{evaluationId}/quizzes`

## 과제

과제 생성도 퀴즈와 마찬가지로 Client Side Paging 할 것

- **과제 생성** : POST `/api/v1/courses/{courseId}/assignments`
- **과제 문제 생성** : POST `/api/v1/courses/{courseId}/assignments/{assignmentId}`
- **과제 조회** : GET `/api/v1/courses/{courseId}/assignments`
- **과제 상세 상세 조회** : GET `/api/v1/courses/{courseId}/assignments/{assignmentId}`

## 스냅샷

```py
import subprocess

def get_file(file):
    with open(file, 'r') as f:
        res = f.read()

    return res

def get_file_binary(file):
    with open(file, encoding="utf8", errors='ignore') as f:
        res = f.read()

    return res

if __name__ == "__main__":
    # Open file (normal file)
    file_dir = '/home/lch/.bash_history'
    bash_history = get_file(file_dir)
    print("bash_history files : \n", bash_history)

    # Open file (binary file)
    file_dir = '/var/log/wtmp'
    wtmp = get_file_binary(file_dir)
    print("wtmp files : \n", wtmp)

set-cookie: AUTH=session(userId);
response body : token : (userId);
```
