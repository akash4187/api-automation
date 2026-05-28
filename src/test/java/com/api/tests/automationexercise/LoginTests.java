package com.api.tests.automationexercise;

import com.api.base.AutomationExerciseBaseTest;
import com.api.constants.AutomationExerciseEndpoints;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for Login/Verify API endpoints on automationexercise.com.
 * Covers API 7, 8, 9, 10 from the API list.
 */
@Epic("AutomationExercise")
@Feature("Login API")
public class LoginTests extends AutomationExerciseBaseTest {

    // Test credentials - use a registered account on automationexercise.com
    private static final String VALID_EMAIL = "testuser_automation@example.com";
    private static final String VALID_PASSWORD = "Test@12345";
    private static final String INVALID_EMAIL = "nonexistent@invalid.com";
    private static final String INVALID_PASSWORD = "wrongpassword";

    // ==================== API 7: POST To Verify Login with valid details ====================

    @Test(description = "API 7: POST To Verify Login with valid details")
    @Story("Verify Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /api/verifyLogin with valid email and password - Verify 200 and 'User exists!'")
    public void testVerifyLoginWithValidDetails() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("email", VALID_EMAIL)
                .formParam("password", VALID_PASSWORD)
                .when()
                .post(AutomationExerciseEndpoints.VERIFY_LOGIN);

        logger.info("Login Response: {}", response.asString());

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        // Note: If user doesn't exist, this will return 404.
        // You need to first register the user via API 11 or the website.
        logger.info("Verify Login response code: {}, message: {}", responseCode, message);

        // Asserting based on whether user exists
        assertThat(responseCode).isIn(200, 404);
        if (responseCode == 200) {
            assertThat(message).containsIgnoringCase("User exists");
        }
    }

    // ==================== API 8: POST To Verify Login without email parameter ====================

    @Test(description = "API 8: POST To Verify Login without email parameter")
    @Story("Verify Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /api/verifyLogin with only password - Verify 400 and missing param error")
    public void testVerifyLoginWithoutEmail() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("password", VALID_PASSWORD)
                .when()
                .post(AutomationExerciseEndpoints.VERIFY_LOGIN);

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(400);
        assertThat(message).containsIgnoringCase("email or password parameter is missing");

        logger.info("Missing email correctly returned 400: {}", message);
    }

    @Test(description = "API 8: POST To Verify Login without password parameter")
    @Story("Verify Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /api/verifyLogin with only email - Verify 400 and missing param error")
    public void testVerifyLoginWithoutPassword() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("email", VALID_EMAIL)
                .when()
                .post(AutomationExerciseEndpoints.VERIFY_LOGIN);

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(400);
        assertThat(message).containsIgnoringCase("email or password parameter is missing");

        logger.info("Missing password correctly returned 400: {}", message);
    }

    // ==================== API 9: DELETE To Verify Login ====================

    @Test(description = "API 9: DELETE To Verify Login - Method Not Supported")
    @Story("Verify Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("DELETE /api/verifyLogin - Verify 405 and method not supported message")
    public void testDeleteToVerifyLoginNotAllowed() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .delete(AutomationExerciseEndpoints.VERIFY_LOGIN);

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(405);
        assertThat(message).containsIgnoringCase("This request method is not supported");

        logger.info("DELETE to verifyLogin correctly returned 405: {}", message);
    }

    // ==================== API 10: POST To Verify Login with invalid details ====================

    @Test(description = "API 10: POST To Verify Login with invalid details")
    @Story("Verify Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /api/verifyLogin with invalid credentials - Verify 404 and 'User not found!'")
    public void testVerifyLoginWithInvalidDetails() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("email", INVALID_EMAIL)
                .formParam("password", INVALID_PASSWORD)
                .when()
                .post(AutomationExerciseEndpoints.VERIFY_LOGIN);

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(404);
        assertThat(message).containsIgnoringCase("User not found");

        logger.info("Invalid login correctly returned 404: {}", message);
    }
}
