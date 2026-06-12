package com.school.enrollmentservice.client;

import com.school.enrollmentservice.dto.CourseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CourseClient {
    private final WebClient webClient;

    public CourseClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://COURSE-SERVICE").build();
    }

    public Mono<CourseResponse> getCourseById(Integer id) {
        log.info("Calling Course Service for courseId: {}", id);
        return webClient.get()
                .uri("/courses/{id}", id)
                .retrieve()
                .bodyToMono(CourseResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    log.warn("Course service returned 404 for courseId: {}", id);
                    return Mono.empty();
                })
                .onErrorResume(ex -> {
                    log.error("Error calling Course Service for courseId {}: {}", id, ex.getMessage());
                    return Mono.empty();
                });
    }
}
