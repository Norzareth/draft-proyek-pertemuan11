@echo off
echo ========================================
echo TESTING MENU COLUMN FIX
echo ========================================
echo.

echo Status: Column error has been fixed!
echo.
echo Problem: "the column name id_penjual was not found in this resultset"
echo.
echo Solution: Added dm.id_penjual to the SELECT statement
echo.
echo Before:
echo SELECT dm.id_menu, dm.nama_menu, dm.jenis, dm.harga, dm.cabang_id,
echo        ps.nama_penjual, c.lokasi as nama_cabang
echo.
echo After:
echo SELECT dm.id_menu, dm.nama_menu, dm.jenis, dm.id_penjual, dm.harga, dm.cabang_id,
echo        ps.nama_penjual, c.lokasi as nama_cabang
echo.

echo To test:
echo 1. Run: mvn javafx:run
echo 2. Login as admin cabang: admin_jak / admin1
echo 3. Menu data should load without errors
echo.

echo Press any key to continue...
pause >nul 