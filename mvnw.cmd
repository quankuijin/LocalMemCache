@echo off
setlocal

set MAVEN_VERSION=3.9.6
set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%
set MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip
set ZIP_FILE=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%.zip

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Downloading Maven %MAVEN_VERSION%...
    if not exist "%USERPROFILE%\.m2\wrapper\dists" mkdir "%USERPROFILE%\.m2\wrapper\dists"
    powershell -Command "Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%ZIP_FILE%'"
    powershell -Command "Expand-Archive -Path '%ZIP_FILE%' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists' -Force"
    del "%ZIP_FILE%"
)

"%MAVEN_HOME%\bin\mvn.cmd" %*
