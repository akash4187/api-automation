package com.api.base;

import com.api.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.util.concurrent.TimeUnit;

/**
 * Base test class that all test classes should extend.
 * Provides common setup for REST Assured configuration,
 * request/response specifications, and shared utilities.
 */
public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected static ConfigReader config;
    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        config = ConfigReader.getInstance();
        logger.info("=== API Automation Framework Initialized ===");
        logger.info("Environment: {}", config.getEnvironment());
        logger.info("Base URL: {}", config.getBaseUrl());

        // Global REST Assured configuration
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeClass(alwaysRun = true)
    public void setup() {
        requestSpec = buildRequestSpec();
        responseSpec = buildResponseSpec();
    }

    /**
     * Build default request specification.
     * Override in subclasses for custom specs (e.g., authenticated requests).
     */
    protected RequestSpecification buildRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        if (config.getBooleanProperty("log.request", true)) {
            builder.log(LogDetail.ALL);
        }

        return builder.build();
    }

    /**
     * Build default response specification.
     * Override in subclasses for custom response validation.
     */
    protected ResponseSpecification buildResponseSpec() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON);

        if (config.getBooleanProperty("log.response", true)) {
            builder.log(LogDetail.ALL);
        }

        return builder.build();
    }

    /**
     * Build an authenticated request specification with Bearer token.
     */
    protected RequestSpecification getAuthenticatedRequestSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
}
