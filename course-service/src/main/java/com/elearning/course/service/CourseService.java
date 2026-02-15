package com.elearning.course.service;

import com.elearning.common.dto.CourseDTO;
import com.elearning.common.event.ModuleCreatedEvent;
import com.elearning.course.config.RabbitMqConfig;
import com.elearning.course.dto.ModuleDTO;
import com.elearning.course.entity.Course;
import com.elearning.course.entity.Module;
import com.elearning.course.repository.CourseRepository;
import com.elearning.course.repository.ModuleRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final RabbitTemplate rabbitTemplate;

    public CourseService(CourseRepository courseRepository, ModuleRepository moduleRepository, RabbitTemplate rabbitTemplate) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // REST operations
    public CourseDTO createCourse(CourseDTO dto) {
        Course course = Course.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .instructor(dto.getInstructor())
                .totalModules(0)
                .build();
        
        Course saved = courseRepository.save(course);
        return convertToDTO(saved);
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return convertToDTO(course);
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public CourseDTO updateCourse(Long id, CourseDTO dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setInstructor(dto.getInstructor());
        course.setUpdatedAt(LocalDateTime.now());
        
        Course updated = courseRepository.save(course);
        return convertToDTO(updated);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public ModuleDTO createModule(Long courseId, ModuleDTO dto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Module module = Module.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .courseId(courseId)
                .build();

        Module saved = moduleRepository.save(module);

        // Update course module count
        course.setTotalModules(course.getTotalModules() + 1);
        courseRepository.save(course);

        // Publish event to notify other services
        ModuleCreatedEvent event = ModuleCreatedEvent.builder()
                .courseId(courseId)
                .courseName(course.getTitle())
                .moduleName(module.getName())
                .moduleDescription(module.getDescription())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();

        rabbitTemplate.convertAndSend(RabbitMqConfig.COURSE_EXCHANGE, 
                RabbitMqConfig.MODULE_CREATED_ROUTING_KEY, event);

        System.out.println("Module created event published for courseId: " + courseId);

        return ModuleDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .courseId(saved.getCourseId())
                .createdAt(saved.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }

    public List<ModuleDTO> getModulesByCourse(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return moduleRepository.findByCourseId(courseId).stream()
                .map(module -> ModuleDTO.builder()
                        .id(module.getId())
                        .name(module.getName())
                        .description(module.getDescription())
                        .courseId(module.getCourseId())
                        .createdAt(module.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                        .build())
                .toList();
    }

    // Reactive operations
    public Mono<CourseDTO> getCourseMono(Long id) {
        return Mono.fromCallable(() -> courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found")))
                .map(this::convertToDTO);
    }

    public Flux<CourseDTO> getAllCoursesFlux() {
        return Flux.fromIterable(courseRepository.findAll())
                .map(this::convertToDTO);
    }

    private CourseDTO convertToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .instructor(course.getInstructor())
                .totalModules(course.getTotalModules())
                .createdAt(course.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }
}
