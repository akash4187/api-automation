@echo off
REM ============================================================
REM Jenkins Freestyle Project - Build Step Script
REM Usage: jenkins-run-tests.bat [SUITE_FILE] [ENVIRONMENT]
REM Example: jenkins-run-tests.bat testng-automationexercise.xml dev
REM ============================================================

SET SUITE_FILE=%1
SET ENVIRONMENT=%2

IF "%SUITE_FILE%"=="" SET SUITE_FILE=testng-automationexercise.xml
IF "%ENVIRONMENT%"=="" SET ENVIRONMENT=dev

echo ========================================
echo  API Automation - Test Execution
echo  Suite: %SUITE_FILE%
echo  Environment: %ENVIRONMENT%
echo ========================================

mvn clean test -Dsurefire.suiteXmlFiles=%SUITE_FILE% -Denvironment=%ENVIRONMENT%

echo ========================================
echo  Test execution completed.
echo  Report: target\extent-reports\
echo ========================================
