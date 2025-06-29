@echo off
echo Testing Central Admin Performance Tab Update...
echo.

echo Building the project...
call mvnw clean compile

if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Please check the errors above.
    pause
    exit /b 1
)

echo.
echo Build successful! Running the application...
echo.
echo Instructions for testing:
echo 1. Login as central admin (username: admin_central, password: admin123)
echo 2. Go to "Performa Cabang" tab
echo 3. Verify that performance data is displayed directly in the table
echo 4. Test the "Refresh Data" button
echo 5. Verify the table shows: Cabang, Tingkat Performa, Pendapatan
echo.

call mvnw javafx:run

pause 