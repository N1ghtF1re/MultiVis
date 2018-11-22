<h1 align="center">MultiVis</h1>
<p align="center"><img width=200 src="https://png2.kisspng.com/20180404/zxe/kisspng-computer-icons-web-hosting-service-web-content-cloud-computing-5ac4e289564a65.8544772215228524893535.png?raw=true" style="width: 150px;"></p>

<p align="center">
<a href="https://github.com/N1ghtF1re/MultiVis/stargazers"><img src="https://img.shields.io/github/stars/N1ghtF1re/MultiVis.svg" alt="Stars"></a>
<a href="https://github.com/N1ghtF1re/MultiVis/releases"><img src="https://img.shields.io/badge/downloads-5-brightgreen.svg" alt="Total Downloads"></a>
<a href="https://github.com/N1ghtF1re/MultiVis/releases"><img src="https://img.shields.io/github/tag/N1ghtF1re/MultiVis.svg" alt="Latest Stable Version"></a>
<a href="https://github.com/N1ghtF1re/MultiVis/blob/master/LICENSE"><img src="https://img.shields.io/github/license/N1ghtF1re/MultiVis.svg" alt="License"></a>
</p>
 

## About
At the moment, there are services that solve individual tasks of visualizing data. (Example: the criminal map of Minsk, a map of cellular coverage, etc.) However, there is no available ready-made solution that each developer could use to visualize the necessary data in a few clicks.

Developed solution allows to visualize multidimensional information effectively.It has an user-friendly interface. Code is easy to modify for any sphere of usage. Application of color mixing enhances perception and analyzation of information.

## How install 
For correct operation of this software, a server of the following minimum configuration is required:

+ OC: Linux / macOS 10.8.3 and Above / Windows 7 and Above
+ Processor: Pentium® III 800 MHz or AMD Athlon;
+ RAM: 1 GB;
+ HDD: 1 GB of free space.

In addition: the presence in the operating system of the installed MySQL DBMS, the JRE virtual machine and the tool for building maven.
Before the server is “deployed”, it is necessary to configure the database. To do this, you need to load a “dump” of an empty database located in database / dump.sql. You can do this with the following command:
```
mysql -u USERNAME -p -h SERVER_NAME DB_NAME < database/dump.sql
```

After that, you need to fill in the main table with data statistics, after which you need to specify the data from the database user in the application src / main / resources / application.properties configuration file by setting the following properties:
+ `spring.datasource.username`
+ `spring.datasource.password`


Optionally, you can configure the port on which the server will work by adding the `server.port = PORT property`.
After configuration settings, just run the mvn build command in the project directory and the maven tool will collect the jar with the built-in tomcat server in the target folder. The last step is to run this file with the command.
```
java –jar target / FILE_NAME.jar
```

