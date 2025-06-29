@echo off
echo Testing Database Connection...
echo.

echo Step 1: Testing connection with squire/schoolstuff...
psql -U squire -d bd_project -c "SELECT 'Connection successful' as status;"

echo.
echo Step 2: Testing Java connection...
javac -cp ".;postgresql-42.7.1.jar" test_database_connection.java
if %errorlevel% equ 0 (
    java -cp ".;postgresql-42.7.1.jar" test_database_connection
) else (
    echo Failed to compile test file
)

echo.
echo Step 3: Testing application...
call mvn clean compile exec:java -Dexec.mainClass="com.example.bdsqltester.HelloApplication"

echo.
echo Test completed!
pause 