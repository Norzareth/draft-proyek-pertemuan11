@echo off
echo Fixing Discount Table Permissions with Correct Structure...
echo.

echo Setting up database connection as postgres...
set PGPASSWORD=postgres

echo Running permission fix script...
psql -h localhost -U postgres -d bd_project -f fix_discount_permissions_correct.sql

if %ERRORLEVEL% NEQ 0 (
    echo Failed to fix permissions. Please check:
    echo 1. PostgreSQL is running
    echo 2. You have access to postgres user
    echo 3. Database bd_project exists
    pause
    exit /b 1
)

echo.
echo Discount table permissions have been fixed successfully!
echo The table now has the correct structure with harga_baru and deskripsi_diskon columns.
echo You can now test the discount functionality.
echo.

pause 