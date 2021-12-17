/* 비밀번호는 email 계정명 */
insert into user (id, email, password, username) values('1', 'student1@vground.com', '$2a$10$EyidPRI4WuvHqpZfjVVRmurBEJXeFOSKE6n0tvzPWT4NQ2ogm29gq', '강지민');
insert into user (id, email, password, username) values('2', 'student2@vground.com', '$2a$10$iadnCLJkh4oZsuWB8lYRLODvMHRfhjuYFCZwr.DjOIQR/SOb/5l9O', '고현수');
insert into user (id, email, password, username) values('3', 'student3@vground.com', '$2a$10$5lD.defa9U3dzwwYK33c0OtwFaccqkcdOh/zkcpQldXpnHTGQCVqG', '김경태');
insert into user (id, email, password, username) values('4', 'student4@vground.com', '$2a$10$Gyk/mLgD2N2y5rHgEC8aVugHJ94.upfS/2gK1tZ7GpaNkzVL3RHx2', '임창현');
insert into user (id, email, password, username) values('5', 'student5@vground.com', '$2a$10$Xx5/an7H13SLrNrKy9A8nO4KHJww2yr1qP46aW29Puv5k/YcrJxhG', '장원익');

/* 강사 추가 */
insert into user (id, email, password, username) values('6', 'teacher1@vground.com', '$2a$10$uB.x1R9EzvTF9vCocT2DkOUcnjfE9yvwPAopj36NpMVQ0NWebbgQ2', '정승기');
insert into user (id, email, password, username) values('7', 'teacher2@vground.com', '$2a$10$G4/8JGT6wP4FxOkVx5zP.um/K0/HsGoaSbD0zaLAl5n/pzS0jN5gq', '강대명');
insert into user (id, email, password, username) values('8', 'teacher3@vground.com', '$2a$10$kpLtgRpj43mLb9VcZe3i5.TMbwJbmXpyI0VCZ.75DOzhD7n.krJXu', '홍성진');

/* 학생 권한 추가 */
insert into role (id, role_type, user_id) values('1', 'STUDENT', '1');
insert into role (id, role_type, user_id) values('2', 'STUDENT', '2');
insert into role (id, role_type, user_id) values('3', 'STUDENT', '3');
insert into role (id, role_type, user_id) values('4', 'STUDENT', '4');
insert into role (id, role_type, user_id) values('5', 'STUDENT', '5');
insert into role (id, role_type, user_id) values('6', 'STUDENT', '6');
insert into role (id, role_type, user_id) values('7', 'STUDENT', '7');
insert into role (id, role_type, user_id) values('8', 'STUDENT', '8');

/* 강사 권한 추가 */
insert into role (id, role_type, user_id) values('9', 'TEACHER', '6');
insert into role (id, role_type, user_id) values('10', 'TEACHER', '7');
insert into role (id, role_type, user_id) values('11', 'TEACHER', '8');

/* 클래스 추가 */
insert into course (id, title, description, cpu, memory, os, visibility, teacher_id) values('1', '클라우드 기초 과정', 'AWS 와 NCP 를 이용해서 클라우드 컴퓨팅의 기초에 대해서 학습힙니다.', '1', '1', 'UBUNTU', true, '6');

/* 클래스 소속 회원 추가 */
-- insert into course_user (id, container_ip, user_id, course_id) values('1', '3.35.135.147', 1, 1);
insert into course_user (id, container_ip, task_arn, user_id, course_id) values('1', '13.124.32.214', 'arn:aws:ecs:ap-northeast-2:075534638877:task/vground/2d221947c63a4b41ba9440745d47ff3c', 1, 1);
insert into course_user (id, container_ip, task_arn, user_id, course_id) values('2', '3.35.25.15', 'arn:aws:ecs:ap-northeast-2:075534638877:task/vground/6a3db9fa11d2444a81aa9a5d4e6332cf', 2, 1);
insert into course_user (id, container_ip, task_arn, user_id, course_id) values('3', '52.78.150.204', 'arn:aws:ecs:ap-northeast-2:075534638877:task/vground/7a6e0a456d294871a7865114e2c5f895', 3, 1);
insert into course_user (id, container_ip, task_arn, user_id, course_id) values('4', '3.34.187.158', 'arn:aws:ecs:ap-northeast-2:075534638877:task/vground/a474a28dd7b14656a79596036da65548', 4, 1);
insert into course_user (id, container_ip, task_arn, user_id, course_id) values('5', '54.180.132.232', 'arn:aws:ecs:ap-northeast-2:075534638877:task/vground/c0ef9393ad124e53b6205278e3c1d23b', 5, 1);
insert into course_user (id, container_ip, task_arn, user_id, course_id) values('6', '54.180.162.98', 'arn:aws:ecs:ap-northeast-2:075534638877:task/vground/336ccb23bbf84e908eadad0101c375d6', 6, 1);

/* 클래스 인터렉션 추가 */
insert into interaction (id, created_at, interaction_type, title, course_id) values('1', '2021-12-16 23:32:13.543845', 'OX', 'nginx 설치', 1);
insert into interaction (id, created_at, interaction_type, title, course_id) values('2', '2021-12-16 23:32:13.543845', 'OX', 'flask 설치', 1);
insert into interaction (id, created_at, interaction_type, title, course_id) values('3', '2021-12-16 23:32:13.543845', 'OX', '설정 변경', 1);

insert into interaction_submit (id, answer, yes_no, interaction_id, student_id) values('1', '', TRUE, 1, 1);
insert into interaction_submit (id, answer, yes_no, interaction_id, student_id) values('2', '', FALSE, 1, 2);
insert into interaction_submit (id, answer, yes_no, interaction_id, student_id) values('3', '', TRUE, 1, 3)

/* 클래스 과제 추가 */
insert into assignment (id, description, ended_at, started_at, title, course_id) values (1, '클라우드 상식 퀴즈', '2021-12-31 12:00:00', '2021-11-09 12:00:00', '클라우드 네트워크', 1);
insert into assignment (id, description, ended_at, started_at, title, course_id) values (2, '클라우드 고급 퀴즈', '2021-12-31 12:00:00', '2021-11-09 12:00:00', '클라우드 네트워크', 1);

/* 클래스 과제 주관식 */
insert into course_question (id, answer, description, question, score, assignment_id) values (1, 'Floating IP', '', '클라우드의 유동 IP', 1, 1);
insert into course_question (id, answer, description, question, score, assignment_id) values (2, 'VPC', '', '클라우드 가상 네트워크', 1, 1);
insert into course_question (id, answer, description, question, score, assignment_id) values (3, 'CIDR', '', '네트워크 할당 방식', 1, 1);

insert into course_question (id, answer, description, question, score, assignment_id) values (4, 'AWS', '', '가장 대표적인 클라우드는?', 1, 2);

/* 주관식 해결 */
insert into question_submit (id, answer, scored, question_id, user_id) values (1, 'Floating IP', 1, 1, 1);
insert into question_submit (id, answer, scored, question_id, user_id) values (2, 'Floating IP', 1, 1, 2);
insert into question_submit (id, answer, scored, question_id, user_id) values (3, 'Private IP', 0, 1, 3);
insert into question_submit (id, answer, scored, question_id, user_id) values (4, 'Floating IP', 1, 1, 4);
insert into question_submit (id, answer, scored, question_id, user_id) values (5, 'Public IP', 0, 1, 5);

insert into question_submit (id, answer, scored, question_id, user_id) values (6, 'VPC', 1, 2, 1);
insert into question_submit (id, answer, scored, question_id, user_id) values (7, 'ICPC', 0, 2, 2);

insert into question_submit (id, answer, scored, question_id, user_id) values (8, 'CIDR', 1, 3, 1);

insert into question_submit (id, answer, scored, question_id, user_id) values (9, 'GCP', 0, 4, 1);
insert into question_submit (id, answer, scored, question_id, user_id) values (10, 'AWS', 1, 4, 2);