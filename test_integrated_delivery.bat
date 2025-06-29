@echo off
echo Testing Integrated Delivery Scheduling Feature...
echo.

echo Compiling and running the application...
call mvn clean compile exec:java -Dexec.mainClass="com.example.bdsqltester.HelloApplication"

echo.
echo Test completed!
pause 