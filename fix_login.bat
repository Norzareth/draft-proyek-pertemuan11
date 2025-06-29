@echo off
echo Fixing Login Issue...
echo.

echo Step 1: Setting up database with squire user...
psql -U squire -d bd_project -f setup_login_data.sql

echo.
echo Step 2: Verifying login data...
psql -U squire -d bd_project -f verify_login_data.sql

echo.
echo Step 3: Testing application...
call mvn clean compile exec:java -Dexec.mainClass="com.example.bdsqltester.HelloApplication"

echo.
echo Login fix completed!
pause 