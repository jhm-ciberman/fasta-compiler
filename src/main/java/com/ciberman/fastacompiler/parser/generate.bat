@echo off > NUL

yacc.exe -J ^
    -Jpackage=com.ciberman.fastacompiler.parser ^
    -Jthrows="IOException, LexicalException, SyntaxException" ^
    -Jnoconstruct ^
    -Jnorun ^
    grammar.y

echo Generated Successfully