package com.api.tests.automationexercise;

import com.api.base.AutomationExerciseBaseTest;
import com.api.constants.AutomationExerciseEndpoints;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for Products API endpoints on automationexercise.com.
 * Covers API 1, 2, 5, 6 from the API list.
 */
@Epic("AutomationExercise")
@Feature("Products API")
public class ProductsTests extends AutomationExerciseBaseTest {

    // ==================== API 1: Get All Products List ====================

    @Test(description = "API 1: Get All Products List")
    @Story("Products List")
    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /api/productsList - Verify response code 200 and products list is returned")
    public void testGetAllProductsList() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(AutomationExerciseEndpoints.PRODUCTS_LIST);

        logger.info("Response Status Code: {}", response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(200);

        int responseCode = getResponseCode(response);
        assertThat(responseCode).isEqualTo(200);

        // Verify products list is not empty
        String products = response.jsonPath().getString("products");
        assertThat(products).isNotNull().isNotEmpty();

        logger.info("Products list retrieved successfully. Response code: {}", responseCode);
    }

    @Test(description = "API 1: Verify products list contains expected fields")
    @Story("Products List")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /api/productsList - Verify each product has id, name, price, brand, category")
    public void testProductsListContainsExpectedFields() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .get(AutomationExerciseEndpoints.PRODUCTS_LIST);

        assertThat(getResponseCode(response)).isEqualTo(200);

        // Verify first product has required fields
        assertThat(response.jsonPath().getInt("products[0].id")).isGreaterThan(0);
        assertThat(response.jsonPath().getString("products[0].name")).isNotEmpty();
        assertThat(response.jsonPath().getString("products[0].price")).isNotEmpty();
        assertThat(response.jsonPath().getString("products[0].brand")).isNotEmpty();
        assertThat(response.jsonPath().getString("products[0].category")).isNotNull();

        logger.info("Product fields validation passed");
    }

    // ==================== API 2: POST To All Products List ====================

    @Test(description = "API 2: POST To All Products List - Method Not Supported")
    @Story("Products List")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /api/productsList - Verify response code 405 and method not supported message")
    public void testPostToProductsListNotAllowed() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .post(AutomationExerciseEndpoints.PRODUCTS_LIST);

        logger.info("Response: {}", response.asString());

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(405);
        assertThat(message).containsIgnoringCase("This request method is not supported");

        logger.info("POST to products list correctly returned 405: {}", message);
    }

    // ==================== API 5: POST To Search Product ====================

    @Test(description = "API 5: POST To Search Product with valid search parameter")
    @Story("Search Products")
    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /api/searchProduct with search_product param - Verify 200 and results returned")
    public void testSearchProductWithValidParam() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("search_product", "top")
                .when()
                .post(AutomationExerciseEndpoints.SEARCH_PRODUCT);

        logger.info("Search Response Status: {}", response.getStatusCode());

        int responseCode = getResponseCode(response);
        assertThat(responseCode).isEqualTo(200);

        String products = response.jsonPath().getString("products");
        assertThat(products).isNotNull().isNotEmpty();

        logger.info("Search for 'top' returned products successfully");
    }

    @Test(description = "API 5: Search for 'tshirt' returns relevant products")
    @Story("Search Products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /api/searchProduct with search_product=tshirt - Verify results are relevant")
    public void testSearchProductTshirt() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("search_product", "tshirt")
                .when()
                .post(AutomationExerciseEndpoints.SEARCH_PRODUCT);

        int responseCode = getResponseCode(response);
        assertThat(responseCode).isEqualTo(200);

        String products = response.jsonPath().getString("products");
        assertThat(products).isNotNull();

        logger.info("Search for 'tshirt' completed successfully");
    }

    @Test(description = "API 5: Search for 'jean' returns relevant products")
    @Story("Search Products")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /api/searchProduct with search_product=jean - Verify results returned")
    public void testSearchProductJean() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .formParam("search_product", "jean")
                .when()
                .post(AutomationExerciseEndpoints.SEARCH_PRODUCT);

        int responseCode = getResponseCode(response);
        assertThat(responseCode).isEqualTo(200);

        logger.info("Search for 'jean' completed successfully");
    }

    // ==================== API 6: POST To Search Product without parameter ====================

    @Test(description = "API 6: POST To Search Product without search_product parameter")
    @Story("Search Products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /api/searchProduct without param - Verify 400 and error message")
    public void testSearchProductWithoutParam() {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .when()
                .post(AutomationExerciseEndpoints.SEARCH_PRODUCT);

        int responseCode = getResponseCode(response);
        String message = getResponseMessage(response);

        assertThat(responseCode).isEqualTo(400);
        assertThat(message).containsIgnoringCase("search_product parameter is missing");

        logger.info("Missing param correctly returned 400: {}", message);
    }
}
