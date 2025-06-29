@echo off
echo ========================================
echo TESTING MENU MANAGEMENT FEATURE
echo ========================================
echo.

echo 1. Testing database connection...
java -cp "target/classes;target/dependency/*" com.example.bdsqltester.testPostgresConn
if %errorlevel% neq 0 (
    echo ERROR: Database connection failed!
    pause
    exit /b 1
)
echo Database connection successful!
echo.

echo 2. Running the application...
echo Please test the following features:
echo - Login as admin cabang (admin_jak / admin1)
echo - View menu list in the table
echo - Filter menu by category
echo - Add new menu
echo - Edit existing menu
echo - Delete menu
echo.
echo Press any key to start the application...
pause >nul

mvn javafx:run 