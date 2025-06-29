@echo off
echo ========================================
echo TESTING DATABASE CONNECTION
echo ========================================
echo.

echo Compiling and running database connection test...
echo.

javac -cp ".;postgresql-42.7.1.jar" TestDatabaseConnection.java
if %errorlevel% neq 0 (
    echo ❌ Compilation failed! Please check if PostgreSQL JDBC driver is available.
    pause
    exit /b 1
)

java -cp ".;postgresql-42.7.1.jar" TestDatabaseConnection
if %errorlevel% neq 0 (
    echo ❌ Test failed! Please check database connection settings.
) else (
    echo.
    echo ✅ Database connection test completed!
)

echo.
echo ========================================
echo NEXT STEPS
echo ========================================
echo.
echo If connection test is successful:
echo 1. Run setup_database.bat to setup admin data
echo 2. Run the application with: mvn clean javafx:run
echo 3. Test admin login with the provided credentials
echo.

pause 