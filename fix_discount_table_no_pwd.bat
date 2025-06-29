@echo off
echo Fixing Discount Table Permission Issue...
echo.

echo Setting up database connection...
set PGPASSWORD=schoolstuff

echo Running SQL script to create/fix discount table...
psql -h localhost -U squire -d bd_project -f check_discount_table.sql

if %ERRORLEVEL% NEQ 0 (
    echo Failed to run SQL script. Please check your database connection.
    echo Make sure PostgreSQL is running and credentials are correct.
    pause
    exit /b 1
)

echo.
echo Discount table has been created/fixed successfully!
echo You can now test the central admin interface.
echo.

pause 