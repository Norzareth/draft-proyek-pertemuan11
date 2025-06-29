@echo off
echo === LOGIN TEST BATCH FILE ===
echo.

echo Testing database connection and login credentials...
echo.

REM Compile the test class
javac -cp "target/classes;target/dependency/*" src/main/java/com/example/bdsqltester/TestLogin.java

REM Run the test
java -cp "target/classes;target/dependency/*" com.example.bdsqltester.TestLogin

echo.
echo Test completed!
pause 