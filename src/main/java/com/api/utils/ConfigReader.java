package com.api.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration reader.
 * Loads properties from config.properties and provides typed access methods.
 * Supports environment variable overrides for CI/CD pipelines.
 */
public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        loadProperties();
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(input);
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration", e);
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    /**
     * Get property value with environment variable override support.
     * Environment variables take precedence over properties file values.
     * Convention: property "base.url.dev" maps to env var "BASE_URL_DEV"
     */
    public String getProperty(String key) {
        String envKey = key.replace(".", "_").toUpperCase();
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            logger.debug("Using environment variable override for: {}", key);
            return envValue;
        }

        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isEmpty()) {
            logger.debug("Using system property override for: {}", key);
            return systemProp;
        }

        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key '{}': {}. Using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public String getEnvironment() {
        return getProperty("environment", "dev");
    }

    public String getBaseUrl() {
        String env = getEnvironment();
        return getProperty("base.url." + env);
    }
}
