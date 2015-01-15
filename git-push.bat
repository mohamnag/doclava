@echo off
git status
git add -A .
echo.
set /p msg="Write some comments:"
git commit -m "%msg%"
git push
pause
