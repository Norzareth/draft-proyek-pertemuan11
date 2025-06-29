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
echo 3. Test the discount functionality:
echo    - Select a menu from the table
echo    - Enter new price (lower than original price)
echo    - Enter discount description
echo    - Click "Simpan Diskon"
echo 4. Verify no permission denied errors appear
echo 5. Check that form clears after successful save
echo 6. Test with invalid inputs (higher price, empty fields)
echo 7. Verify data is saved correctly in database
echo.

call mvnw javafx:run

pause 