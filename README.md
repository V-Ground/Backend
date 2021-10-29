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

## 클래스

- **클래스 생성** : POST `/api/v1/courses`
- **클래스 전체 조회** : GET `/api/v1/courses`
- **클래스 비활성화** : PATCH `/api/v1/courses/`
- **클래스 삭제** : DELETE `/api/v1/courses/`

## 테스트

- **테스트 생성** : POST `/api/v1/evaluation/`
- **테스트 전체 조회** : GET `/api/v1/evaluation/`
- **테스트 비활성화** : PATCH `/api/v1/evaluation/`
- **테스트 삭제** : DELETE `/api/v1/evaluation/`

## 퀴즈

- **퀴즈 생성** : POST `/api/v1/quizzes`
- **테스트에 존재하는 모든 퀴즈 조회** : GET `/api/v1/quizzes`
- **퀴즈 풀기** : `/api/v1/quizzes`
