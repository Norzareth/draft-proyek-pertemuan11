@echo off
echo ========================================
echo COMPLETE SETUP - BD PROJECT
echo ========================================
echo.

echo Step 1: Setting up database structure...
echo Please run the following SQL commands in your PostgreSQL database:
echo.
echo 1. Create tables:
echo    psql -d bd_project -f sqlnya.sql
echo.
echo 2. Insert sample data:
echo    psql -d bd_project -f complete_sample_data.sql
echo.
echo 3. Setup admin data:
echo    psql -d bd_project -f setup_admin_data.sql
echo.
echo 4. Add new feature tables:
echo    psql -d bd_project -f add_promotion_tables.sql
echo.

echo Step 2: Verify database connection...
echo Database URL: jdbc:postgresql://localhost:5432/bd_project
echo Username: squire
echo Password: schoolstuff
echo.

echo Step 3: Test database connection...
echo Running database connection test...
javac -cp ".;postgresql-42.7.1.jar" TestDatabaseConnection.java
if %errorlevel% neq 0 (
    echo ⚠️  Database connection test failed. Please check PostgreSQL installation.
) else (
    java -cp ".;postgresql-42.7.1.jar" TestDatabaseConnection
)

echo.
echo Step 4: Available login credentials...
echo.
echo CUSTOMER:
echo - john_doe / pass1
echo - jane_smith / pass2
echo - bob_wilson / pass3
echo.
echo ADMIN CABANG:
echo - admin_jak / admin1 (Jakarta)
echo - admin_ban / admin1 (Bandung)
echo - admin_sur / admin1 (Surabaya)
echo.
echo ADMIN PUSAT:
echo - super_adm / super1 (Super Admin)
echo.

echo Step 5: New Features Available...
echo.
echo ADMIN CABANG FEATURES:
echo - Manajemen Menu & Stok Harian
echo - Pesanan & Pengiriman
echo - Laporan Cabang
echo.
echo ADMIN PUSAT FEATURES:
echo - Performa Cabang
echo - Manajemen Promosi
echo - Manajemen Cabang & Staff
echo - Laporan Keseluruhan
echo.

echo Step 6: Run the application...
echo.
echo ========================================
echo READY TO RUN APPLICATION
echo ========================================
echo.
echo To start the application, run:
echo run_application.bat
echo.
echo Or manually:
echo mvn clean javafx:run
echo.

echo ========================================
echo TROUBLESHOOTING
echo ========================================
echo.
echo If you encounter issues:
echo 1. Check PostgreSQL is running on port 5432
echo 2. Verify database 'bd_project' exists
echo 3. Check user 'squire' with password 'schoolstuff'
echo 4. Ensure all SQL scripts have been executed
echo 5. Check Java 17+ and Maven are installed
echo.

echo ========================================
echo DOCUMENTATION
echo ========================================
echo.
echo - README.md - Main documentation
echo - QUICK_START.md - Quick start guide
echo - ADMIN_LOGIN_INSTRUCTIONS.md - Admin login guide
echo - NEW_FEATURES.md - New features documentation
echo.

pause 