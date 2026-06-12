package com.school.enrollmentservice.client;

import com.school.enrollmentservice.dto.StudentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class StudentClient {
    private final WebClient webClient;

    public StudentClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://STUDENT-SERVICE").build();
    }

    public Mono<StudentResponse> getStudentById(Integer id) {
        log.info("Calling Student Service for studentId: {}", id);
        return webClient.get()
                .uri("/students/{id}", id)
                .retrieve()
                .bodyToMono(StudentResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.warn("Student service returned 404 for studentId: {}", id);
                    return Mono.empty();
                })
                .onErrorResume(ex -> {
                    log.error("Error calling Student Service for studentId {}: {}", id, ex.getMessage());
                    return Mono.empty();
                });
    }
}
