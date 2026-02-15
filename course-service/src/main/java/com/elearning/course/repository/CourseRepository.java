package com.elearning.course.repository;

import com.elearning.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(String instructor);
    List<Course> findByTitleContainingIgnoreCase(String title);
}
