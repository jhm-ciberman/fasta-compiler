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

To run the tests:

````
gradlew test
````

To create the dist zip/tar files:

````
gradlew assembleDist
````