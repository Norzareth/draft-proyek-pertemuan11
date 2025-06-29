@echo off
echo ========================================
echo SETUP DATABASE FOR ADMIN LOGIN
echo ========================================
echo.

echo Step 1: Setting up database structure...
echo Please run the following SQL commands in your PostgreSQL database:
echo.
echo 1. Create tables (if not exists):
echo    psql -d bd_project -f sqlnya.sql
echo.
echo 2. Insert sample data:
echo    psql -d bd_project -f complete_sample_data.sql
echo.
echo 3. Setup admin data:
echo    psql -d bd_project -f setup_admin_data.sql
echo.

echo Step 2: Verify database connection...
echo Database URL: jdbc:postgresql://localhost:5432/bd_project
echo Username: squire
echo Password: schoolstuff
echo.

echo Step 3: Test admin login credentials...
echo.
echo ADMIN CABANG:
echo - Username: admin_jak, Password: admin1, Role: Admin Cabang
echo - Username: admin_ban, Password: admin1, Role: Admin Cabang
echo - Username: admin_sur, Password: admin1, Role: Admin Cabang
echo.
echo ADMIN PUSAT:
echo - Username: super_adm, Password: super1, Role: Admin Pusat
echo.

echo Step 4: Run the application...
echo mvn clean javafx:run
echo.

echo ========================================
echo TROUBLESHOOTING
echo ========================================
echo.
echo If login still fails:
echo 1. Check database connection in DataSourceManager.java
echo 2. Verify data exists in database using verify_admin_data.sql
echo 3. Check console output for error messages
echo 4. Ensure PostgreSQL is running on port 5432
echo.

pause 