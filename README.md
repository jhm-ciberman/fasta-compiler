# fasta-compiler

Toy compiler for the Compilers' assignment in FASTA University. 

A design document for the lexer can be found here: (Spanish)
[Google Docs](https://docs.google.com/document/d/1oZinqdoqda2kB6562U3Gv60rz3Eov6dKzmH6Rmdxd44/edit?usp=sharing)

The default input file is located under `src/main/resources/test.fasta` 


## Build and run

This project uses Gradle. To build and run use:

````
gradlew run
````

To run the unit tests:

````
gradlew test
````

To create the dist zip/tar files:

````
gradlew assembleDist
````

The YACC grammar file for the parser is located at `src/main/java/com/ciberman/fastacompiler/parser/grammar.y`. 
To automatically run [BYACC/J](http://byaccj.sourceforge.net/) and regenerate the Parser java file, 
run the following graddle task (Windows only): 

````
gradlew parser
````

