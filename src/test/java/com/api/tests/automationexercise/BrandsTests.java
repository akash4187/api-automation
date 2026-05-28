package com.api.tests.automationexercise;

import com.api.base.AutomationExerciseBaseTest;
import com.api.constants.AutomationExerciseEndpoints;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for Brands API endpoints on automationexercise.com.
 * Covers API 3, 4 from the API list.
 */
@Epic("AutomationExercise")
@Feature("Brands API")
public class BrandsTests extends AutomationExerciseBaseTest {

    // ==================== API 3: Get All Brands List ====================

    @Test(description = "API 3: Get All Brands List")
    @Story("Brands List")
    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /api/brandsList - Verify response code 200 and brands list is returned")
    public void testGetAllBrandsList() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(AutomationExerciseEndpoints.BRANDS_LIST);

        logger.info("Response Status Code: {}", response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(200);

        int responseCode = getResponseCode(response);
        assertThat(responseCode).isEqualTo(200);

        String brands = response.jsonPath().getString("brands");
        assertThat(brands).isNotNull().isNotEmpty();

        logger.info("Brands list retrieved successfully");
    }

    @Test(description = "API 3: Verify brands list contains expected fields")
    @Story("Brands List")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /api/brandsList - Verify each brand has id and brand name")
    public void testBrandsListContainsExpectedFields() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(AutomationExerciseEndpoints.BRANDS_LIST);

        assertThat(getResponseCode(response)).isEqualTo(200);

        // Verify first brand has required fields
        assertThat(response.jsonPath().getInt("brands[0].id")).isGreaterThan(0);
        assertThat(response.jsonPath().getString("brands[0].brand")).isNotEmpty();

        logger.info("Brand fields validation passed");
    }

    // ==================== API 4: PUT To All Brands List ====================

    @Test(description = "API 4: PUT To All Brands List - Method Not Supported")
    @Story("Brands List")
    @Severity(SeverityLevel.NORMAL)
    @Description("PUT /api/brandsList - Verify response code 405 and method not supported message")
    public void testPutToBrandsListNotAllowed() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .put(AutomationExerciseEndpoints.BRANDS_LIST);

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(405);
        assertThat(message).containsIgnoringCase("This request method is not supported");

        logger.info("PUT to brands list correctly returned 405: {}", message);
    }
}
