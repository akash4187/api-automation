package com.api.tests;

import com.api.base.BaseTest;
import com.api.base.RequestManager;
import com.api.constants.ApiEndpoints;
import com.api.constants.HttpStatus;
import com.api.models.request.CreateUserRequest;
import com.api.models.response.CreateUserResponse;
import com.api.utils.ResponseValidator;
import com.api.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for User CRUD operations.
 * Covers GET, POST, PUT, PATCH, DELETE for /users endpoint.
 */
@Epic("User Management")
@Feature("User CRUD Operations")
public class UserTests extends BaseTest {

    private RequestManager requestManager;

    @BeforeClass(alwaysRun = true)
    @Override
    public void setup() {
        super.setup();
        requestManager = new RequestManager(requestSpec);
    }

    // ==================== GET Tests ====================

    @Test(description = "Verify fetching list of users returns 200")
    @Story("Get Users")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validate that GET /users returns a paginated list of users with status 200")
    public void testGetUsersList() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", 1);

        Response response = requestManager.getWithQueryParams(
                ApiEndpoints.USERS, queryParams
        );

        ResponseValidator.validateStatusCode(response, HttpStatus.OK);
        ResponseValidator.validateFieldNotEmpty(response, "data");
        ResponseValidator.validateResponseTime(response, 5000);

        int totalUsers = response.jsonPath().getInt("total");
        assertThat(totalUsers).isGreaterThan(0);
        logger.info("Total users found: {}", totalUsers);
    }

    @Test(description = "Verify fetching a single user by ID returns 200")
    @Story("Get Users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that GET /users/{id} returns user details with status 200")
    public void testGetSingleUser() {
        Response response = requestManager.get(
                ApiEndpoints.USER_BY_ID,
                Collections.singletonMap("id", 2)
        );

        ResponseValidator.validateStatusCode(response, HttpStatus.OK);
        ResponseValidator.validateFieldNotEmpty(response, "data.email");
        ResponseValidator.validateFieldNotEmpty(response, "data.first_name");
        ResponseValidator.validateFieldNotEmpty(response, "data.last_name");
    }

    @Test(description = "Verify fetching non-existent user returns 404")
    @Story("Get Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate that GET /users/{id} with invalid ID returns 404")
    public void testGetNonExistentUser() {
        Response response = requestManager.get(
                ApiEndpoints.USER_BY_ID,
                Collections.singletonMap("id", 9999)
        );

        ResponseValidator.validateStatusCode(response, HttpStatus.NOT_FOUND);
    }

    // ==================== POST Tests ====================

    @Test(description = "Verify creating a new user returns 201")
    @Story("Create User")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validate that POST /users creates a user and returns 201 with user details")
    public void testCreateUser() {
        CreateUserRequest request = TestDataGenerator.generateCreateUserRequest();
        logger.info("Creating user with name: {} and job: {}", request.getName(), request.getJob());

        Response response = requestManager.post(ApiEndpoints.USERS, request);

        ResponseValidator.validateStatusCode(response, HttpStatus.CREATED);
        ResponseValidator.validateFieldNotEmpty(response, "id");
        ResponseValidator.validateFieldNotEmpty(response, "createdAt");

        CreateUserResponse createResponse = response.as(CreateUserResponse.class);
        assertThat(createResponse.getName()).isEqualTo(request.getName());
        assertThat(createResponse.getJob()).isEqualTo(request.getJob());
        assertThat(createResponse.getId()).isNotNull();

        logger.info("User created successfully with ID: {}", createResponse.getId());
    }

    // ==================== PUT Tests ====================

    @Test(description = "Verify updating a user via PUT returns 200")
    @Story("Update User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that PUT /users/{id} updates user and returns 200")
    public void testUpdateUserPut() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Updated Name")
                .job("Updated Job")
                .build();

        Response response = requestManager.put(
                ApiEndpoints.USER_BY_ID,
                request,
                Collections.singletonMap("id", 2)
        );

        ResponseValidator.validateStatusCode(response, HttpStatus.OK);
        ResponseValidator.validateFieldEquals(response, "name", "Updated Name");
        ResponseValidator.validateFieldEquals(response, "job", "Updated Job");
        ResponseValidator.validateFieldNotEmpty(response, "updatedAt");
    }

    // ==================== PATCH Tests ====================

    @Test(description = "Verify partial update via PATCH returns 200")
    @Story("Update User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate that PATCH /users/{id} partially updates user and returns 200")
    public void testUpdateUserPatch() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Patched Name")
                .build();

        Response response = requestManager.patch(
                ApiEndpoints.USER_BY_ID,
                request,
                Collections.singletonMap("id", 2)
        );

        ResponseValidator.validateStatusCode(response, HttpStatus.OK);
        ResponseValidator.validateFieldEquals(response, "name", "Patched Name");
        ResponseValidator.validateFieldNotEmpty(response, "updatedAt");
    }

    // ==================== DELETE Tests ====================

    @Test(description = "Verify deleting a user returns 204")
    @Story("Delete User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that DELETE /users/{id} removes user and returns 204")
    public void testDeleteUser() {
        Response response = requestManager.delete(
                ApiEndpoints.USER_BY_ID,
                Collections.singletonMap("id", 2)
        );

        ResponseValidator.validateStatusCode(response, HttpStatus.NO_CONTENT);
    }
}
