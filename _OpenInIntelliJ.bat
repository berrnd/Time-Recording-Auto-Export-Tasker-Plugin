set projectPath=%~dp0
set intellij_bin=A:\IntelliJIDEA\bin


rem IntelliJ needs the project path without a trailing backslash
if %projectPath:~-1%==\ set projectPath=%projectPath:~0,-1%

start "" /D "%intellij_bin%" "idea.exe" "%projectPath%"