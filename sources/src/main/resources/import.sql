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
insert into course_user (id, container_ip, user_id, course_id) values('1', '58.142.191.143', 1, 1);
insert into course_user (id, container_ip, user_id, course_id) values('2', '58.142.191.141', 1, 2);
insert into course_user (id, container_ip, user_id, course_id) values('3', '58.142.191.141', 6, 1);
