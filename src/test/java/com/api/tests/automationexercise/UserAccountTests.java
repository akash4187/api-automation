package com.api.tests.automationexercise;

import com.api.base.AutomationExerciseBaseTest;
import com.api.constants.AutomationExerciseEndpoints;
import com.api.models.request.CreateAccountRequest;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for User Account CRUD operations on automationexercise.com.
 * Covers API 11, 12, 13, 14 from the API list.
 *
 * Test execution order: Create → Get → Update → Delete
 */
@Epic("AutomationExercise")
@Feature("User Account API")
public class UserAccountTests extends AutomationExerciseBaseTest {

    private static final Faker faker = new Faker();
    private CreateAccountRequest testUser;

    @BeforeClass(alwaysRun = true)
    @Override
    public void setup() {
        super.setup();
        // Generate unique test user data
        testUser = CreateAccountRequest.builder()
                .name(faker.name().fullName())
                .email("testapi_" + System.currentTimeMillis() + "@example.com")
                .password("SecurePass@123")
                .title("Mr")
                .birth_date("15")
                .birth_month("6")
                .birth_year("1990")
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .company(faker.company().name())
                .address1(faker.address().streetAddress())
                .address2(faker.address().secondaryAddress())
                .country("United States")
                .zipcode(faker.address().zipCode())
                .state(faker.address().state())
                .city(faker.address().city())
                .mobile_number(faker.phoneNumber().cellPhone())
                .build();

        logger.info("Test user generated with email: {}", testUser.getEmail());
    }

    // ==================== API 11: POST To Create/Register User Account ====================

    @Test(description = "API 11: POST To Create/Register User Account", priority = 1)
    @Story("Create Account")
    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /api/createAccount with all required params - Verify 201 and 'User created!'")
    public void testCreateUserAccount() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("name", testUser.getName())
                .formParam("email", testUser.getEmail())
                .formParam("password", testUser.getPassword())
                .formParam("title", testUser.getTitle())
                .formParam("birth_date", testUser.getBirth_date())
                .formParam("birth_month", testUser.getBirth_month())
                .formParam("birth_year", testUser.getBirth_year())
                .formParam("firstname", testUser.getFirstname())
                .formParam("lastname", testUser.getLastname())
                .formParam("company", testUser.getCompany())
                .formParam("address1", testUser.getAddress1())
                .formParam("address2", testUser.getAddress2())
                .formParam("country", testUser.getCountry())
                .formParam("zipcode", testUser.getZipcode())
                .formParam("state", testUser.getState())
                .formParam("city", testUser.getCity())
                .formParam("mobile_number", testUser.getMobile_number())
                .when()
                .post(AutomationExerciseEndpoints.CREATE_ACCOUNT);

        logger.info("Create Account Response: {}", response.asString());

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(201);
        assertThat(message).containsIgnoringCase("User created");

        logger.info("User account created successfully: {}", testUser.getEmail());
    }

    @Test(description = "API 11: Verify creating account with existing email returns 400", priority = 2,
            dependsOnMethods = "testCreateUserAccount")
    @Story("Create Account")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /api/createAccount with existing email - Verify 400 and 'Email already exists!'")
    public void testCreateAccountWithExistingEmail() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("name", testUser.getName())
                .formParam("email", testUser.getEmail())
                .formParam("password", testUser.getPassword())
                .formParam("title", testUser.getTitle())
                .formParam("birth_date", testUser.getBirth_date())
                .formParam("birth_month", testUser.getBirth_month())
                .formParam("birth_year", testUser.getBirth_year())
                .formParam("firstname", testUser.getFirstname())
                .formParam("lastname", testUser.getLastname())
                .formParam("company", testUser.getCompany())
                .formParam("address1", testUser.getAddress1())
                .formParam("address2", testUser.getAddress2())
                .formParam("country", testUser.getCountry())
                .formParam("zipcode", testUser.getZipcode())
                .formParam("state", testUser.getState())
                .formParam("city", testUser.getCity())
                .formParam("mobile_number", testUser.getMobile_number())
                .when()
                .post(AutomationExerciseEndpoints.CREATE_ACCOUNT);

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(400);
        assertThat(message).containsIgnoringCase("Email already exist");

        logger.info("Duplicate email correctly returned 400: {}", message);
    }

    // ==================== API 14: GET user account detail by email ====================

    @Test(description = "API 14: GET user account detail by email", priority = 3,
            dependsOnMethods = "testCreateUserAccount")
    @Story("Get User Detail")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /api/getUserDetailByEmail with valid email - Verify 200 and user details returned")
    public void testGetUserDetailByEmail() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .queryParam("email", testUser.getEmail())
                .when()
                .get(AutomationExerciseEndpoints.GET_USER_DETAIL);

        logger.info("Get User Detail Response: {}", response.asString());

        int responseCode = getResponseCode(response);
        assertThat(responseCode).isEqualTo(200);

        // Verify user details match what we created
        String returnedName = response.jsonPath().getString("user.name");
        assertThat(returnedName).isEqualTo(testUser.getName());

        logger.info("User details retrieved successfully for: {}", testUser.getEmail());
    }

    @Test(description = "API 14: GET user detail with non-existent email", priority = 3)
    @Story("Get User Detail")
    @Severity(SeverityLevel.NORMAL)
    @Description("GET /api/getUserDetailByEmail with invalid email - Verify 404")
    public void testGetUserDetailWithInvalidEmail() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .queryParam("email", "nonexistent_" + System.currentTimeMillis() + "@fake.com")
                .when()
                .get(AutomationExerciseEndpoints.GET_USER_DETAIL);

        int responseCode = getResponseCode(response);
        assertThat(responseCode).isEqualTo(404);

        logger.info("Non-existent email correctly returned 404");
    }

    // ==================== API 13: PUT METHOD To Update User Account ====================

    @Test(description = "API 13: PUT METHOD To Update User Account", priority = 4,
            dependsOnMethods = "testCreateUserAccount")
    @Story("Update Account")
    @Severity(SeverityLevel.CRITICAL)
    @Description("PUT /api/updateAccount with updated params - Verify 200 and 'User updated!'")
    public void testUpdateUserAccount() {
        String updatedName = "Updated " + faker.name().fullName();
        String updatedCompany = faker.company().name();

        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("name", updatedName)
                .formParam("email", testUser.getEmail())
                .formParam("password", testUser.getPassword())
                .formParam("title", "Mrs")
                .formParam("birth_date", "20")
                .formParam("birth_month", "12")
                .formParam("birth_year", "1985")
                .formParam("firstname", testUser.getFirstname())
                .formParam("lastname", testUser.getLastname())
                .formParam("company", updatedCompany)
                .formParam("address1", testUser.getAddress1())
                .formParam("address2", testUser.getAddress2())
                .formParam("country", "Canada")
                .formParam("zipcode", testUser.getZipcode())
                .formParam("state", testUser.getState())
                .formParam("city", testUser.getCity())
                .formParam("mobile_number", testUser.getMobile_number())
                .when()
                .put(AutomationExerciseEndpoints.UPDATE_ACCOUNT);

        logger.info("Update Account Response: {}", response.asString());

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(200);
        assertThat(message).containsIgnoringCase("User updated");

        logger.info("User account updated successfully");
    }

    // ==================== API 12: DELETE METHOD To Delete User Account ====================

    @Test(description = "API 12: DELETE METHOD To Delete User Account", priority = 5,
            dependsOnMethods = "testCreateUserAccount")
    @Story("Delete Account")
    @Severity(SeverityLevel.BLOCKER)
    @Description("DELETE /api/deleteAccount with email and password - Verify 200 and 'Account deleted!'")
    public void testDeleteUserAccount() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("email", testUser.getEmail())
                .formParam("password", testUser.getPassword())
                .when()
                .delete(AutomationExerciseEndpoints.DELETE_ACCOUNT);

        logger.info("Delete Account Response: {}", response.asString());

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(200);
        assertThat(message).containsIgnoringCase("Account deleted");

        logger.info("User account deleted successfully: {}", testUser.getEmail());
    }

    @Test(description = "API 12: Verify deleting non-existent account returns 404", priority = 6)
    @Story("Delete Account")
    @Severity(SeverityLevel.NORMAL)
    @Description("DELETE /api/deleteAccount with non-existent email - Verify 404")
    public void testDeleteNonExistentAccount() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("email", "nonexistent_" + System.currentTimeMillis() + "@fake.com")
                .formParam("password", "randompass")
                .when()
                .delete(AutomationExerciseEndpoints.DELETE_ACCOUNT);

        int responseCode = getResponseCode(response);
        assertThat(responseCode).isEqualTo(404);

        logger.info("Delete non-existent account correctly returned 404");
    }
}
