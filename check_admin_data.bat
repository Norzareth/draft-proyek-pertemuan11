@echo off
echo Checking Admin Data in Database...
echo.

echo Running database check...
psql -U squire -d bd_project -f check_admin_data.sql

echo.
echo Check completed!
pause 