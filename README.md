# DBUnit Tutorial

Getting started with DBUnit testing framework


## General overview

This tutorial shows how to run test cases agains information stored in a database. The database to be used all along this tutorial is the one of the [Web-based Hello World case study](https://github.com/acapozucca/helloworld). 

This tutorial comes with test cases that show how to insert new data into the DB, how to retrieve data from the DB, and how to assert whether our database contains certain particular data.



## Assumptions/Pre-requisites

### Hardware
Laptop with at least 8 Gb memory (recommended 16 Gb, ideally 32 Gb)

### Software

1. The Web-based Hello World case study
* Instructions to install here: https://github.com/acapozucca/helloworld/blob/master/product.helloworld/README.md
* You need to complete (at least) until the step 3 (included) of the section 
"Check local testing environment setup", and
(at least) until step 3 (included) of the section
"Run the application"

2. **Maven** (v 3.6.2, or higher)
* Instructions to install here: https://maven.apache.org/download.cgi
* Check installation with the command `mvn --version`

3. **Git** (version 2.21.1)
* Instructions to install here: https://help.ubuntu.com/lts/serverguide/git.html
* Check installation with the command `git --version`






## Set local working environment


### Create DB user 

1- Clone this repo

2- Get to the directory

```
cd ~/<git_root_folder>/helloworld/product.helloworld
```

**Note**: this directory must exist as it is required to have installed already the Web-based Hello World case study.


3- Jump into the virtual environment (refered to also as *guest*) : 

```
 vagrant ssh
```

4- Connect to DB
```
mysql -u root -p 
```

**Note**:  root's password is “12345678”


5- Create new DB user named 'ac'
```
CREATE USER ‘ac’@‘%' IDENTIFIED BY ‘ac';
```

6- Grant access to user 'ac'
```
grant all privileges on hellodb.* TO 'ac'@'%';
```

**Note**: the user can be removed doing:
```
DROP USER 'ac'@'%';
```



### Open access to mysql 

It is required to make possible to reach mysql (running into the guest) from the host via port 3306

1- Edit this file:
```
/etc/mysql/mysql.conf.d/mysqld.cnf
```

2- Find the following line:
```
bind-address = 127.0.0.1
```

3- Change the bind-address to 0.0.0.0:
```
bind-address = 0.0.0.0
```

4- Save this file and then run the following command as root:
```
sudo service mysql restart
```


5- Test the connection works
Use Java program named 'CheckConnectivity' to check access to the database from the host.

Run the Java program from the console using maven.

To run it using maven you have to execute the following commands:
```
cd ~/<git_root_folder>/DBUnit_Tutorial/product.hellworld.testing.dbunit
mvn clean
mvn test
mvn exec:java  -Dexec.mainClass=database.CheckConnectivity
```


The expected outcome is:
```
[INFO] Scanning for projects...
[INFO] 
[INFO] --< product.hellworld.testing.dbunit:product.hellworld.testing.dbunit >--
[INFO] Building product.hellworld.testing.dbunit 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ product.hellworld.testing.dbunit ---
Start connectivity test ...
Connection set starts ...
Connection set done!
```




## Running the test cases using Maven

1- Navigate to the directory that contains the .pom file

```
cd ~/<git_root_folder>/DBUnit_Tutorial/product.hellworld.testing.dbunit
```

2- Run the comand:

```
mvn clean test
```

Running this command should shown (almost at the end of the output):

```
Results :

Tests run: 6, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------

```

**Note**: the project is ready to be imported into the Eclipse IDE as an existen Maven project.


## Project details

### DB Properties
* The details about the database should be written in the file `DB.properties`. Next, the content of this file is shown:


```
# local datasource
jdbc.initialSize=5
jdbc.maxActive=10 

jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://192.168.33.14:3306/hellodb?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
jdbc.username=ac
jdbc.password=ac
```



### Database Helper class

* This class reads the database properties file and creates BasicDataSource object with which we can connect to the database and execute queries in it.

### Test cases in `TestDB`

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



### Test cases in `TestDB2`

This class shows how to write test cases by extending from the super class `DBTestCase`.
By extending from this class you can override some methods to ensure that the database is in a known state. This is appropriate for tests that require the database to only contain a specific set of data.

Test cases the are written as you normally would do with JUnit. 

Your database is now initialised before and cleaned-up after each test methods according to what you did in previous steps.

For more details about how to write test cases by extending from DBTestCase class, look at: http://dbunit.sourceforge.net/howto.html





## Final remarks

These guidelines explain how to use DBUnit:

- to feed the DB with data required to fulfil the initial conditions of a test case,
- to run test cases to assert whether the information retrieved from the DB satisfies certain condition, 
- to track the data to be stored into the DB for running the test cases, and
- to track the assertions used for one (or more) test cases. 


