package com.api.base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Centralized request manager that wraps REST Assured calls.
 * Provides a clean API for making HTTP requests with consistent logging and error handling.
 */
public class RequestManager {

    private static final Logger logger = LogManager.getLogger(RequestManager.class);

    private final RequestSpecification requestSpec;

    public RequestManager(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    // ==================== GET ====================

    public Response get(String endpoint) {
        logger.info("GET {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(endpoint);
    }

    public Response get(String endpoint, Map<String, ?> pathParams) {
        logger.info("GET {} with pathParams: {}", endpoint, pathParams);
        return RestAssured.given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .when()
                .get(endpoint);
    }

    public Response getWithQueryParams(String endpoint, Map<String, ?> queryParams) {
        logger.info("GET {} with queryParams: {}", endpoint, queryParams);
        return RestAssured.given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .when()
                .get(endpoint);
    }

    // ==================== POST ====================

    public Response post(String endpoint, Object body) {
        logger.info("POST {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post(endpoint);
    }

    public Response post(String endpoint) {
        logger.info("POST {} (no body)", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .post(endpoint);
    }

    // ==================== PUT ====================

    public Response put(String endpoint, Object body) {
        logger.info("PUT {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .put(endpoint);
    }

    public Response put(String endpoint, Object body, Map<String, ?> pathParams) {
        logger.info("PUT {} with pathParams: {}", endpoint, pathParams);
        return RestAssured.given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .body(body)
                .when()
                .put(endpoint);
    }

    // ==================== PATCH ====================

    public Response patch(String endpoint, Object body) {
        logger.info("PATCH {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .body(body)
                .when()
                .patch(endpoint);
    }

    public Response patch(String endpoint, Object body, Map<String, ?> pathParams) {
        logger.info("PATCH {} with pathParams: {}", endpoint, pathParams);
        return RestAssured.given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .body(body)
                .when()
                .patch(endpoint);
    }

    // ==================== DELETE ====================

    public Response delete(String endpoint) {
        logger.info("DELETE {}", endpoint);
        return RestAssured.given()
                .spec(requestSpec)
                .when()
                .delete(endpoint);
    }

    public Response delete(String endpoint, Map<String, ?> pathParams) {
        logger.info("DELETE {} with pathParams: {}", endpoint, pathParams);
        return RestAssured.given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .when()
                .delete(endpoint);
    }
}
