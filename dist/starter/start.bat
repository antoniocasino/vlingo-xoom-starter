@ECHO OFF
SET CURRENT_DIR=%CD%
SET VLINGO_STARTER_HOME=%CURRENT_DIR%
call java -jar bin\vlingo-xoom-starter-dist.jar
SET VLINGO_STARTER_HOME=