@echo off
echo ========================================
echo TEST NEW FEATURES - BD PROJECT
echo ========================================
echo.

echo Testing Admin Cabang Features...
echo.

echo 1. Testing Stock Management...
echo - Check if stok_harian table exists
psql -d bd_project -c "SELECT COUNT(*) FROM stok_harian;" 2>nul
if %errorlevel% equ 0 (
    echo ✅ Stock table exists
) else (
    echo ❌ Stock table missing - run add_promotion_tables.sql
)

echo.
echo 2. Testing Promotion Management...
echo - Check if promosi table exists
psql -d bd_project -c "SELECT COUNT(*) FROM promosi;" 2>nul
if %errorlevel% equ 0 (
    echo ✅ Promotion table exists
) else (
    echo ❌ Promotion table missing - run add_promotion_tables.sql
)

echo.
echo 3. Testing Delivery Schedule...
echo - Check if jadwal_pengiriman table exists
psql -d bd_project -c "SELECT COUNT(*) FROM jadwal_pengiriman;" 2>nul
if %errorlevel% equ 0 (
    echo ✅ Delivery schedule table exists
) else (
    echo ❌ Delivery schedule table missing - run add_promotion_tables.sql
)

echo.
echo 4. Testing Admin Login...
echo - Check admin users exist
psql -d bd_project -c "SELECT username, role FROM users WHERE role IN ('AdminCab', 'AdminPus');" 2>nul
if %errorlevel% equ 0 (
    echo ✅ Admin users exist
) else (
    echo ❌ Admin users missing - run setup_admin_data.sql
)

echo.
echo 5. Testing Sample Data...
echo - Check sample promotions
psql -d bd_project -c "SELECT nama_promosi, jenis_promosi, persentase_diskon FROM promosi LIMIT 3;" 2>nul
if %errorlevel% equ 0 (
    echo ✅ Sample promotions exist
) else (
    echo ❌ Sample promotions missing - run add_promotion_tables.sql
)

echo.
echo 6. Testing Stock Data...
echo - Check sample stock data
psql -d bd_project -c "SELECT COUNT(*) FROM stok_harian WHERE tanggal = CURRENT_DATE;" 2>nul
if %errorlevel% equ 0 (
    echo ✅ Sample stock data exists
) else (
    echo ❌ Sample stock data missing - run add_promotion_tables.sql
)

echo.
echo ========================================
echo FEATURE TEST SUMMARY
echo ========================================
echo.

echo ADMIN CABANG FEATURES TO TEST:
echo 1. Login with admin_jak / admin1
echo 2. Select "Admin Cabang" role
echo 3. Test "Manajemen Menu & Stok" tab:
echo    - View menu with stock information
echo    - Click "Set Stok" to update stock
echo    - Check stock status (Tersedia/Terbatas/Habis)
echo 4. Test "Pesanan & Pengiriman" tab:
echo    - Filter orders by status and date
echo    - Click "Proses Pesanan" to update status
echo    - Click "Jadwalkan Pengiriman" to schedule delivery
echo 5. Test "Laporan" tab:
echo    - Generate reports for different periods
echo.

echo ADMIN PUSAT FEATURES TO TEST:
echo 1. Login with super_adm / super1
echo 2. Select "Admin Pusat" role
echo 3. Test "Performa Cabang" tab:
echo    - Set date range for performance analysis
echo    - Click "Analisis Performa" to view branch performance
echo    - Check performance metrics and scores
echo 4. Test "Manajemen Promosi" tab:
echo    - Click "Tambah Promosi" to add new promotion
echo    - View existing promotions
echo    - Test "Aktifkan/Nonaktifkan" feature
echo 5. Test "Manajemen Cabang" tab:
echo    - View branch information
echo    - Check branch performance and staff count
echo 6. Test "Laporan Keseluruhan" tab:
echo    - Generate different types of reports
echo.

echo ========================================
echo MANUAL TESTING INSTRUCTIONS
echo ========================================
echo.

echo To test the new features manually:
echo.
echo 1. Start the application:
echo    run_application.bat
echo.
echo 2. Test Admin Cabang:
echo    - Login: admin_jak / admin1
echo    - Role: Admin Cabang
echo    - Test stock management and order processing
echo.
echo 3. Test Admin Pusat:
echo    - Login: super_adm / super1
echo    - Role: Admin Pusat
echo    - Test performance analysis and promotion management
echo.

echo ========================================
echo EXPECTED BEHAVIOR
echo ========================================
echo.

echo ADMIN CABANG:
echo - Should see menu with stock information
echo - Should be able to update stock quantities
echo - Should see order status and delivery scheduling
echo - Should generate branch reports
echo.

echo ADMIN PUSAT:
echo - Should see performance metrics for all branches
echo - Should be able to create and manage promotions
echo - Should see comprehensive reports
echo - Should manage branches and staff
echo.

echo ========================================
echo TROUBLESHOOTING
echo ========================================
echo.

echo If features don't work:
echo 1. Check database connection
echo 2. Verify all SQL scripts executed
echo 3. Check console for Java errors
echo 4. Ensure correct login credentials
echo 5. Check role selection in login
echo.

pause 