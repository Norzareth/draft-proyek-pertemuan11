@echo off
echo ========================================
echo TESTING DELIVERY COLUMN FIX
echo ========================================
echo.

echo Status: Delivery column error has been fixed!
echo.
echo Problem: Duplicate fx:id="deliveryStatusColumn" in FXML
echo.
echo Solution: 
echo - Changed delivery status column to "deliveryScheduleStatusColumn"
echo - Updated controller to use correct column name
echo - Updated cell value factory reference
echo.

echo Changes made:
echo 1. FXML: deliveryStatusColumn -> deliveryScheduleStatusColumn
echo 2. Controller: Updated @FXML annotation
echo 3. Controller: Updated cell value factory
echo.

echo To test:
echo 1. Run: mvn javafx:run
echo 2. Login as admin cabang: admin_jak / admin1
echo 3. Go to "Jadwal Pengiriman" tab
echo 4. Should display without errors
echo.

echo Press any key to continue...
pause >nul 