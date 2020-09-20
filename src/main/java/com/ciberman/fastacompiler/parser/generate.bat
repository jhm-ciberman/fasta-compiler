@echo off

IF EXIST yacc.err del yacc.err
IF EXIST y.output del y.output

yacc.exe -v -J^
    -Jpackage=com.ciberman.fastacompiler.parser ^
    -Jthrows="IOException, LexicalException, SyntaxException" ^
    -Jnoconstruct ^
    -Jnorun ^
    -Jnodebug ^
    grammar.y 2> yacc.err

set yaccexitcode=%ERRORLEVEL%

if %yaccexitcode% EQU 0 goto checkstderr
else goto Error


:checkstderr
IF EXIST yacc.err goto checkfilesize
else goto error

:checkfilesize
call :filesize yacc.err FileSize
if "%FileSize%" == "" (
    goto success
) else (
    if "%FileSize%" == "0" (
        goto success
    ) else (
        goto error
    )
)

:success
echo Success
del yacc.err
exit 0

:error
echo Failure Exit Code is %yaccexitcode% >&2
if exist yacc.err type yacc.err >&2
del yacc.err
exit 1

:filesize
SET %~2=%~z1

GOTO :EOF

