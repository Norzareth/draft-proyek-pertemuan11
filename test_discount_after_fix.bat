@echo off
echo Testing Discount Functionality After Permission Fix...
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
echo 2. Go to "Manajemen Menu" tab
echo 3. Test adding a discount:
echo    - Select a menu from dropdown
echo    - Enter new price (lower than original)
echo    - Enter discount description
echo    - Click "Simpan Diskon"
echo 4. Test editing existing discount
echo 5. Test deleting discount
echo 6. Verify no permission errors appear
echo.

call mvnw javafx:run

pause 