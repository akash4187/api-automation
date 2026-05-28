package com.api.base;

import com.api.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

/**
 * Base test class specifically for automationexercise.com APIs.
 * This API uses form parameters (not JSON bodies) for POST/PUT/DELETE requests.
 * Responses are JSON wrapped in HTML for some endpoints.
 */
public abstract class AutomationExerciseBaseTest {

    protected static final Logger logger = LogManager.getLogger(AutomationExerciseBaseTest.class);
    protected static final String BASE_URL = "https://automationexercise.com";
    protected static ConfigReader config;
    protected RequestSpecification requestSpec;

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        config = ConfigReader.getInstance();
        logger.info("=== AutomationExercise API Tests Initialized ===");
        logger.info("Base URL: {}", BASE_URL);
    }

    @BeforeClass(alwaysRun = true)
    public void setup() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Helper to extract responseCode from the API response body.
     * automationexercise.com returns JSON like: {"responseCode": 200, "message": "..."}
     */
    protected int getResponseCode(io.restassured.response.Response response) {
        return response.jsonPath().getInt("responseCode");
    }

    /**
     * Helper to extract message from the API response body.
     */
    protected String getResponseMessage(io.restassured.response.Response response) {
        return response.jsonPath().getString("message");
    }
}
