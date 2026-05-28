package com.api.constants;

/**
 * Centralized endpoint constants.
 * All API paths are defined here to avoid hardcoding in tests.
 */
public final class ApiEndpoints {

    private ApiEndpoints() {
        // Utility class - prevent instantiation
    }

    // User endpoints
    public static final String USERS = "/users";
    public static final String USER_BY_ID = "/users/{id}";

    // Authentication endpoints
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";

    // Resource endpoints
    public static final String RESOURCES = "/unknown";
    public static final String RESOURCE_BY_ID = "/unknown/{id}";
}
