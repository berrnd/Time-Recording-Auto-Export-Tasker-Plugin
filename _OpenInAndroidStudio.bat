set projectPath=%~dp0
set androidStudioBin=A:\AndroidStudio\bin


rem Android Studio needs the project path without a trailing backslash
if %projectPath:~-1%==\ set projectPath=%projectPath:~0,-1%

start "" /D "%androidStudioBin%" "studio.exe" "%projectPath%"