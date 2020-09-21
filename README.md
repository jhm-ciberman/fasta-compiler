# fasta-compiler

Toy compiler for the Compilers' assignment in FASTA University. 

A design document for the lexer can be found here: (Spanish)
[Google Docs](https://docs.google.com/document/d/1oZinqdoqda2kB6562U3Gv60rz3Eov6dKzmH6Rmdxd44/edit?usp=sharing)

The default input file is located under `src/main/resources/test.fasta` 

## Usage
Dowload the latest release from the [Releases section](https://github.com/jhm-ciberman/fasta-compiler/releases/)
then unzip the zip/tar file and run:

Windows:
```
./bin/fasta-compiler.bat <filename>
```
Linux/MacOS:
```
./bin/fasta-compiler <filename>
```
You can pass any text file as an argument.

Additionally you can pass the following arguments: 

```
usage: fasta-compiler [options] inputfile
 -d,--demo <arg>    Loads a built-in demo file. Values: "99bottles", "basic",
                    "factorial", "if", "loop", "syntax", "vars".
 -l,--lexer <arg>   Select the lexer implementation. Values: "functional",
                    "matrix". (Default is "matrix")
```



## Build and run from source

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
run the following graddle task (Windows only). 
Note that this task will run automatically when you run or build the project.

````
gradlew parser
````

