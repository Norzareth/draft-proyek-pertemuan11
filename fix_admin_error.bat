@echo off
echo ========================================
echo FIXING ADMIN CONTROLLER ERROR
echo ========================================
echo.

echo Step 1: Cleaning previous build...
mvn clean

echo.
echo Step 2: Compiling with fixed module-info.java...
mvn compile

if %errorlevel% equ 0 (
    echo ✅ Compilation successful!
    echo.
    echo Step 3: Running application...
    echo.
    echo ========================================
    echo READY TO TEST ADMIN FEATURES
    echo ========================================
    echo.
    echo Test Admin Cabang:
    echo 1. Login: admin_jak / admin1
    echo 2. Role: Admin Cabang
    echo 3. Should now load without error
    echo.
    echo Test Admin Pusat:
    echo 1. Login: super_adm / super1
    echo 2. Role: Admin Pusat
    echo 3. Should now load without error
    echo.
    echo ========================================
    echo RUNNING APPLICATION...
    echo ========================================
    echo.
    mvn javafx:run
) else (
    echo ❌ Compilation failed!
    echo.
    echo Please check the error messages above.
    echo Common issues:
    echo 1. Java version (need Java 17+)
    echo 2. Maven installation
    echo 3. Missing dependencies
    echo.
    pause
) 