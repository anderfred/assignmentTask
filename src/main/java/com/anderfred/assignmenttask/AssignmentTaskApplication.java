package com.anderfred.assignmenttask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/***
 *      Application entrypoint
 *      Setting separate package for jpa repos
 */

@SpringBootApplication
@EnableJpaRepositories("com.anderfred.assignmenttask.repository")
public class AssignmentTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssignmentTaskApplication.class, args);
    }
}
