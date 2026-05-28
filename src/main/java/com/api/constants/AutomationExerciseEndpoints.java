package com.api.constants;

/**
 * API Endpoints for automationexercise.com
 * Reference: https://www.automationexercise.com/api_list
 */
public final class AutomationExerciseEndpoints {

    private AutomationExerciseEndpoints() {
        // Utility class - prevent instantiation
    }

    // Products
    public static final String PRODUCTS_LIST = "/api/productsList";
    public static final String SEARCH_PRODUCT = "/api/searchProduct";

    // Brands
    public static final String BRANDS_LIST = "/api/brandsList";

    // Authentication
    public static final String VERIFY_LOGIN = "/api/verifyLogin";

    // User Account
    public static final String CREATE_ACCOUNT = "/api/createAccount";
    public static final String UPDATE_ACCOUNT = "/api/updateAccount";
    public static final String DELETE_ACCOUNT = "/api/deleteAccount";
    public static final String GET_USER_DETAIL = "/api/getUserDetailByEmail";
}
