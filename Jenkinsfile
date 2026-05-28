pipeline {
    agent any

    tools {
        maven 'Maven'   // Name must match your Jenkins Global Tool Configuration
        jdk 'JDK8'      // Name must match your Jenkins JDK configuration
    }

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['dev', 'staging', 'prod'],
            description: 'Target environment for API tests'
        )
        choice(
            name: 'TEST_SUITE',
            choices: ['testng-automationexercise.xml', 'testng.xml'],
            description: 'TestNG suite file to execute'
        )
    }

    environment {
        TIMESTAMP = new Date().format('yyyy-MM-dd_HH-mm-ss')
    }

    options {
        timeout(time: 10, unit: 'MINUTES')
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Checking out source code..."
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Compiling project..."
                bat 'mvn clean compile -q'
            }
        }

        stage('Run API Tests') {
            steps {
                echo "Running API tests against ${params.ENVIRONMENT} environment..."
                bat "mvn test -Dsurefire.suiteXmlFiles=${params.TEST_SUITE} -Denvironment=${params.ENVIRONMENT}"
            }
            post {
                always {
                    // Archive TestNG results
                    testNG(
                        reportFilenamePattern: '**/testng-results.xml',
                        unstableOnFailure: true,
                        failedFails: true
                    )
                }
            }
        }

        stage('Publish Reports') {
            steps {
                echo "Publishing ExtentReport..."
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/extent-reports',
                    reportFiles: '*.html',
                    reportName: 'API Test Report',
                    reportTitles: 'ExtentReport'
                ])
            }
        }
    }

    post {
        success {
            echo "ALL TESTS PASSED - Build #${env.BUILD_NUMBER}"
        }
        failure {
            echo "TESTS FAILED - Build #${env.BUILD_NUMBER}"
            // Uncomment below to send email notifications
            // mail to: 'team@example.com',
            //      subject: "API Tests FAILED - Build #${env.BUILD_NUMBER}",
            //      body: "Check: ${env.BUILD_URL}"
        }
        always {
            echo "Archiving test artifacts..."
            archiveArtifacts artifacts: 'target/extent-reports/**/*.html', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
        }
    }
}
