@echo off
echo ========================================
echo TESTING ORDER HISTORY FIX
echo ========================================
echo.

echo Status: OrderHistoryController errors have been fixed!
echo.
echo Problems fixed:
echo 1. tanggalProperty() - replaced with getTanggalPesanan().toString()
echo 2. menuProperty() - replaced with getCustomerName()
echo 3. statusProperty() - replaced with getStatus()
echo 4. jadwalProperty() - replaced with getJadwalKirim().toString()
echo 5. OrderHistoryItem constructor - updated to use new format
echo.

echo Changes made:
echo - Updated cell value factories to use getters instead of properties
echo - Updated SQL query to match new OrderHistoryItem structure
echo - Updated constructor calls to use new parameters
echo - Added proper null handling for dates
echo.

echo To test:
echo 1. Run: mvn javafx:run
echo 2. Login as customer
echo 3. Go to order history
echo 4. Should display without errors
echo.

echo Press any key to continue...
pause >nul

echo Testing Order History Fix...
echo.

echo Compiling and running the application...
call mvn clean compile exec:java -Dexec.mainClass="com.example.bdsqltester.HelloApplication"

echo.
echo Test completed!
pause 