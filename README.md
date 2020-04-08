# DBUnit Tutorial

## General overview

In this project we will test our database which we created in the helloworld product. We will try to insert new data in it and assert whether our database contains that data.

## Assumptions/Pre-requisites
1. **Maven** (v 3.6.2, or higher)
* Instructions to install here: https://maven.apache.org/download.cgi
* Check installation with the command `mvn --version`

2. **Git** (version 2.21.1)
* Instructions to install here: https://help.ubuntu.com/lts/serverguide/git.html
* Check installation with the command `git --version`

3. **Helloworld product**
* Instruction to setup: https://github.com/acapozucca/helloworld/tree/master/product.helloworld
* This project needs this product to be deployed beforehand running any vagrant test.

## Set local working environment

1- Clone this repo

2- Import the given project into your favourite IDE

3- Build the project using Maven. Either you do it from within the IDE, or from a terminal. From the terminal you must:

3.1-  Get to the directory

```
cd ~/<git_root_folder>/DBUnit_Tutorial
```

3.2- Run the comand.

```
mvn clean install
```

Running this command should shown (almost at the end of the output):

```
[INFO] BUILD SUCCESS
```


**Note**

* The project is ready to be imported on the Eclipse IDE as an existen Maven project.

