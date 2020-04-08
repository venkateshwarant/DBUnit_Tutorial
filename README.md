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

# Project details

## DB Properties
* The details about the database should be written in the DB.properties, change the url if you run mysql in different port, if not specially configured 3306 is the mysql's default port.
* give your corresponding root username and password, this is required as in test we would be required to connect to the database.

```
jdbc.initialSize=5
jdbc.maxActive=10 
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/hellodb?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
jdbc.username=root
jdbc.password=12345678
```

## Database Helper class

* This class reads the database properties file and creates BasicDataSource object with which we can connect to the database and execute queries in it.

## Test cases

i) **setupTests**
In this method we check whether we can connect to our database, if not we not proceed to the test case and we will exit saying that the before test condition is not met.

ii) **loadData**
In this method we write the data which we have in testdata.xml to our database. The contents of testdata.xml is in the following format
```
<?xml version="1.0" encoding="UTF-8"?>
<dataset>
    <table name="TEST">
        <column>ID</column>
        <column>NAME</column>
        <column>VAL</column>

        <row>
            <value>999</value>
            <value>HELLO</value>
            <value>WORLD</value>
        </row>
      
    </table>
</dataset>
```
After writing data, we assert whether the data is there in the database.

iii) **checkForData**
In this test method we access the TEST table and print all of its contents.
