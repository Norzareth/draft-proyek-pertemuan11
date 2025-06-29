@echo off
echo ========================================
echo RUNNING BD PROJECT APPLICATION
echo ========================================
echo.

echo Step 1: Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ❌ Java not found! Please install Java 17 or later.
    echo Download from: https://adoptium.net/
    pause
    exit /b 1
)

echo.
echo Step 2: Checking Maven installation...
mvn -version
if %errorlevel% neq 0 (
    echo ⚠️  Maven not found, trying Maven wrapper...
    if exist "mvnw.cmd" (
        echo Using Maven wrapper...
        set MAVEN_CMD=mvnw.cmd
    ) else (
        echo ❌ Neither Maven nor Maven wrapper found!
        echo Please install Maven or ensure mvnw.cmd exists.
        pause
        exit /b 1
    )
) else (
    set MAVEN_CMD=mvn
)

echo.
echo Step 3: Compiling application...
%MAVEN_CMD% clean compile
if %errorlevel% neq 0 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 4: Running application...
echo.
echo ========================================
echo ADMIN LOGIN CREDENTIALS
echo ========================================
echo.
echo ADMIN CABANG:
echo - Username: admin_jak, Password: admin1, Role: Admin Cabang
echo - Username: admin_ban, Password: admin1, Role: Admin Cabang  
echo - Username: admin_sur, Password: admin1, Role: Admin Cabang
echo.
echo ADMIN PUSAT:
echo - Username: super_adm, Password: super1, Role: Admin Pusat
echo.
echo ========================================
echo.

%MAVEN_CMD% javafx:run

echo.
echo Application closed.
pause 