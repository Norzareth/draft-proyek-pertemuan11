@echo off
echo Testing Central Admin Interface After Fix...
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
echo 2. Verify no "permission denied" errors appear
echo 3. Check that menu data loads properly in "Manajemen Menu" tab
echo 4. Test discount management features
echo 5. Check that performance data loads in "Performa Cabang" tab
echo.

call mvnw javafx:run

pause 