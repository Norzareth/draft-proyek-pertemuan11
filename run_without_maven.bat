@echo off
echo ========================================
echo RUNNING APPLICATION WITHOUT MAVEN
echo ========================================
echo.

echo Step 1: Check if Java is available...
java -version
if %errorlevel% neq 0 (
    echo ❌ Java not found! Please install Java 17+ and set JAVA_HOME
    pause
    exit /b 1
)

echo.
echo Step 2: Check if target folder exists...
if not exist "target\classes" (
    echo ❌ Compiled classes not found!
    echo Please run: mvn clean compile
    echo Or use an IDE to compile the project
    pause
    exit /b 1
)

echo.
echo Step 3: Running application...
echo.

set CLASSPATH=target\classes;lib\*

java --module-path "C:\Program Files\Java\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -cp %CLASSPATH% com.example.bdsqltester.HelloApplication

if %errorlevel% neq 0 (
    echo.
    echo ❌ Failed to run application!
    echo.
    echo Possible solutions:
    echo 1. Install JavaFX SDK and update the path above
    echo 2. Use Maven: mvn javafx:run
    echo 3. Use IDE to run the application
    echo.
    pause
) else (
    echo.
    echo ✅ Application started successfully!
) 