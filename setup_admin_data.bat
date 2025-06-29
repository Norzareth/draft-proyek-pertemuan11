@echo off
echo Setting up Admin Data...
echo.

echo Step 1: Checking existing data...
psql -U squire -d bd_project -f check_admin_data.sql

echo.
echo Step 2: Setting up admin data...
psql -U squire -d bd_project -f setup_admin_data.sql

echo.
echo Step 3: Testing application...
call mvn clean compile exec:java -Dexec.mainClass="com.example.bdsqltester.HelloApplication"

echo.
echo Setup completed!
pause 