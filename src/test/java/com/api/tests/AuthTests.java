package com.api.tests;

import com.api.base.BaseTest;
import com.api.base.RequestManager;
import com.api.constants.ApiEndpoints;
import com.api.constants.HttpStatus;
import com.api.models.request.LoginRequest;
import com.api.models.response.LoginResponse;
import com.api.utils.ResponseValidator;
import com.api.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for Authentication endpoints.
 * Covers login and register scenarios including positive and negative cases.
 */
@Epic("Authentication")
@Feature("Login & Registration")
public class AuthTests extends BaseTest {

    private RequestManager requestManager;

    @BeforeClass(alwaysRun = true)
    @Override
    public void setup() {
        super.setup();
        requestManager = new RequestManager(requestSpec);
    }

    // ==================== Login Tests ====================

    @Test(description = "Verify successful login returns token")
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validate that POST /login with valid credentials returns a token")
    public void testSuccessfulLogin() {
        LoginRequest request = TestDataGenerator.generateLoginRequest(
                "eve.holt@reqres.in", "cityslicka"
        );

        Response response = requestManager.post(ApiEndpoints.LOGIN, request);

        ResponseValidator.validateStatusCode(response, HttpStatus.OK);
        ResponseValidator.validateFieldNotEmpty(response, "token");

        LoginResponse loginResponse = response.as(LoginResponse.class);
        assertThat(loginResponse.getToken()).isNotEmpty();
        logger.info("Login successful. Token received.");
    }

    @Test(description = "Verify login without password returns 400")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that POST /login without password returns 400 with error message")
    public void testLoginWithoutPassword() {
        LoginRequest request = LoginRequest.builder()
                .email("eve.holt@reqres.in")
                .build();

        Response response = requestManager.post(ApiEndpoints.LOGIN, request);

        ResponseValidator.validateStatusCode(response, HttpStatus.BAD_REQUEST);

        LoginResponse loginResponse = response.as(LoginResponse.class);
        assertThat(loginResponse.getError()).isNotEmpty();
        logger.info("Expected error received: {}", loginResponse.getError());
    }

    @Test(description = "Verify login without email returns 400")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that POST /login without email returns 400 with error message")
    public void testLoginWithoutEmail() {
        LoginRequest request = LoginRequest.builder()
                .password("cityslicka")
                .build();

        Response response = requestManager.post(ApiEndpoints.LOGIN, request);

        ResponseValidator.validateStatusCode(response, HttpStatus.BAD_REQUEST);

        LoginResponse loginResponse = response.as(LoginResponse.class);
        assertThat(loginResponse.getError()).isNotEmpty();
    }

    // ==================== Register Tests ====================

    @Test(description = "Verify successful registration returns id and token")
    @Story("Registration")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validate that POST /register with valid data returns id and token")
    public void testSuccessfulRegistration() {
        LoginRequest request = TestDataGenerator.generateLoginRequest(
                "eve.holt@reqres.in", "pistol"
        );

        Response response = requestManager.post(ApiEndpoints.REGISTER, request);

        ResponseValidator.validateStatusCode(response, HttpStatus.OK);
        ResponseValidator.validateFieldNotEmpty(response, "id");
        ResponseValidator.validateFieldNotEmpty(response, "token");
    }

    @Test(description = "Verify registration without password returns 400")
    @Story("Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that POST /register without password returns 400 with error")
    public void testRegistrationWithoutPassword() {
        LoginRequest request = LoginRequest.builder()
                .email("eve.holt@reqres.in")
                .build();

        Response response = requestManager.post(ApiEndpoints.REGISTER, request);

        ResponseValidator.validateStatusCode(response, HttpStatus.BAD_REQUEST);
        ResponseValidator.validateFieldNotEmpty(response, "error");
    }
}
