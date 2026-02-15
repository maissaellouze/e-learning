package com.elearning.course.controller;

import com.elearning.common.dto.CourseDTO;
import com.elearning.course.dto.ModuleDTO;
import com.elearning.course.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin("*")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseDTO dto) {
        return ResponseEntity.ok(courseService.updateCourse(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // Module endpoints
    @PostMapping("/{courseId}/modules")
    public ResponseEntity<ModuleDTO> createModule(@PathVariable Long courseId, @RequestBody ModuleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createModule(courseId, dto));
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<List<ModuleDTO>> getModulesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getModulesByCourse(courseId));
    }

    // Reactive endpoints
    @GetMapping("/reactive/{id}")
    public Mono<ResponseEntity<CourseDTO>> getCourseByIdReactive(@PathVariable Long id) {
        return courseService.getCourseMono(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/reactive/all")
    public Flux<CourseDTO> getAllCoursesReactive() {
        return courseService.getAllCoursesFlux();
    }
}
