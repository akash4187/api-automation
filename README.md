# API Automation Framework

Industry-level REST API automation framework built with Java, REST Assured, TestNG, and Allure Reporting.

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 17 |
| Build Tool | Maven |
| HTTP Client | REST Assured 5.4.0 |
| Test Runner | TestNG 7.9.0 |
| Reporting | Allure 2.25.0 |
| Serialization | Jackson 2.17.0 |
| Logging | Log4j2 |
| Assertions | AssertJ |
| Test Data | Java Faker |

## Project Structure

```
api-automation/
├── pom.xml
├── testng.xml
├── src/
│   ├── main/
│   │   ├── java/com/api/
│   │   │   ├── base/           # Base test class, Request Manager
│   │   │   ├── constants/      # Endpoints, HTTP status codes
│   │   │   ├── models/
│   │   │   │   ├── request/    # Request POJOs
│   │   │   │   └── response/   # Response POJOs
│   │   │   └── utils/          # Config reader, JSON utils, validators
│   │   └── resources/
│   │       ├── config.properties
│   │       └── log4j2.xml
│   └── test/
│       └── java/com/api/
│           ├── tests/          # Test classes (UserTests, AuthTests)
│           └── listeners/      # TestNG listeners (retry, logging)
└── README.md
```

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
mvn clean test -Dtest=UserTests
```

### Run by Environment
```bash
mvn clean test -Denvironment=staging
```

### Generate Allure Report
```bash
mvn allure:serve
```

## Configuration

Edit `src/main/resources/config.properties` to configure:
- Base URLs per environment
- Authentication credentials
- Timeouts and retry settings
- Logging preferences

Environment variables override properties file values. Convention:
- Property `base.url.dev` → Env var `BASE_URL_DEV`

## Key Design Decisions

- **Singleton ConfigReader** with env var override support for CI/CD flexibility
- **RequestManager** abstracts HTTP methods for clean, reusable test code
- **Builder pattern** on request models for readable test data construction
- **Allure annotations** for rich, navigable test reports
- **RetryAnalyzer** handles transient failures gracefully
- **Parallel execution** via TestNG for faster feedback loops

## Adding New Tests

1. Create request/response models in `com.api.models`
2. Add endpoint constants in `ApiEndpoints.java`
3. Create test class extending `BaseTest`
4. Use `RequestManager` for HTTP calls
5. Use `ResponseValidator` for common assertions
6. Add class to `testng.xml`
