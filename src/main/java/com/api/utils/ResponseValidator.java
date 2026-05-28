package com.api.utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utility class for common response validations.
 * Provides reusable assertion methods for consistent validation across tests.
 */
public final class ResponseValidator {

    private static final Logger logger = LogManager.getLogger(ResponseValidator.class);

    private ResponseValidator() {
        // Utility class - prevent instantiation
    }

    /**
     * Validate response status code.
     */
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        logger.debug("Validating status code - Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
        assertThat(actualStatusCode)
                .as("Expected status code %d but got %d", expectedStatusCode, actualStatusCode)
                .isEqualTo(expectedStatusCode);
    }

    /**
     * Validate response time is within acceptable threshold.
     */
    public static void validateResponseTime(Response response, long maxTimeMs) {
        long responseTime = response.getTime();
        logger.debug("Response time: {}ms (threshold: {}ms)", responseTime, maxTimeMs);
        assertThat(responseTime)
                .as("Response time %dms exceeded threshold of %dms", responseTime, maxTimeMs)
                .isLessThan(maxTimeMs);
    }

    /**
     * Validate response contains a specific header.
     */
    public static void validateHeaderExists(Response response, String headerName) {
        String headerValue = response.getHeader(headerName);
        assertThat(headerValue)
                .as("Expected header '%s' to be present", headerName)
                .isNotNull();
    }

    /**
     * Validate response body field is not null or empty.
     */
    public static void validateFieldNotEmpty(Response response, String jsonPath) {
        String value = response.jsonPath().getString(jsonPath);
        assertThat(value)
                .as("Expected field '%s' to be non-empty", jsonPath)
                .isNotNull()
                .isNotEmpty();
    }

    /**
     * Validate response body field equals expected value.
     */
    public static void validateFieldEquals(Response response, String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        assertThat(actualValue)
                .as("Expected field '%s' to equal '%s' but was '%s'", jsonPath, expectedValue, actualValue)
                .isEqualTo(expectedValue);
    }
}
