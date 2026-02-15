package com.elearning.enrollment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.elearning.enrollment", "com.elearning.common"})
public class EnrollmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrollmentServiceApplication.class, args);
    }

}
