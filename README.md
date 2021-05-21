# oralhistory-workflow-cms

Spring Boot/Spring Data/Spring Security/Hibernate/MySQL/REST

Oral history workflow content management system, a CMS is a piece of tool that helps you create and manage content related to oral History interview records. Systems enables to have multiple user types. System allows to add interview records (individual or bulk) , manage depending categories, modification (individual or bulk) and exporting.

There are four types of roles such as student, user, staff, and administrator.

## Things to run the application.

__Clone the repository__
```
git clone https://github.com/okstate-library/oralhistory-workflow-cms.git
```

__Go the folder__
```
src/main/resources/sql, combine all definition.x.x.x.sql and master_data.x.x.x.sql files  run in MySql DBMS.
```

The administrator user name and password are "admin". After setup the system user can change his/her password.

__Set Your MySQL user name & password in application.properties__

Change the server, user name and password on [application.properties](../../blob/master/src/main/resources/application.properties)

__Run the application__
```
mvn clean spring-boot:run
```

## Build application and run on a live environment.

###### Build application
```
mvn clean build
```

###### Run application
```
java -jar target/oralhistory-workflow-cms-2.0.1.jar
```

Check the URL "http://localhost:8080".

###### OKState Libraries would also appreciate recognition if you use this code.

If need any help(development feature, hosting,..) contact our developers

