package com.project.expensetracker.controller;

import com.project.expensetracker.AbstractIntegrationTest;
import com.project.expensetracker.dto.auth.AuthResponse;
import com.project.expensetracker.dto.auth.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createCategory_shouldReturn201_whenAuthenticatedAndValid() {
        RegisterRequest registerRequest = new RegisterRequest(
                "integration-" + UUID.randomUUID() + "@test.com",
                "motdepasse123",
                "Test Integration");

        ResponseEntity<AuthResponse> authResponse = restTemplate.postForEntity(
                "/api/auth/register", registerRequest, AuthResponse.class);

        assertThat(authResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(authResponse.getBody()).isNotNull();

        String token = authResponse.getBody().token();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("{\"name\": \"Loisirs\"}", headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/categories", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Loisirs");
    }

    @Test
    void getAllCategories_shouldReturn401_whenNotAuthenticated() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/categories", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
