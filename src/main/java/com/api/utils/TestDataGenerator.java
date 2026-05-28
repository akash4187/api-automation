package com.api.utils;

import com.api.models.request.CreateUserRequest;
import com.api.models.request.LoginRequest;
import com.github.javafaker.Faker;

/**
 * Generates randomized test data using Java Faker.
 * Ensures tests are not dependent on hardcoded values.
 */
public final class TestDataGenerator {

    private static final Faker faker = new Faker();

    private TestDataGenerator() {
        // Utility class - prevent instantiation
    }

    public static CreateUserRequest generateCreateUserRequest() {
        return CreateUserRequest.builder()
                .name(faker.name().fullName())
                .job(faker.job().title())
                .build();
    }

    public static LoginRequest generateLoginRequest(String email, String password) {
        return LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static String getRandomName() {
        return faker.name().fullName();
    }

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String getRandomJob() {
        return faker.job().title();
    }
}
