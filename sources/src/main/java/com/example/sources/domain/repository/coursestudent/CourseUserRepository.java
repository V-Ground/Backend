package com.example.sources.domain.repository.coursestudent;

import com.example.sources.domain.entity.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseUserRepository extends JpaRepository<CourseUser, Long>, CourseUserQuery {

}
