# Spring Cloud Config Server DataBase

- Db Environment Repository using JDBC

# Dependencies

- java: 1.8
- spring-boot: 1.5.2.RELEASE
- spring-cloud: Camden.SR5
- spring-jdbc


# Usage

1. Add the dependency:

<dependency>
	<groupId>org.rodrigovelaz</groupId>
	<artifactId>spring-cloud-config-server-db</artifactId>
	<version>1.0.0</version>
</dependency>

2. Set the active profile as "db" (Similar to "native" profile):

spring.profiles.active=db

3. Configure the datasource (Just like any spring-boot application)

spring.datasource.url=jdbc:oracle:thin:@:/
spring.datasource.username=username
spring.datasource.password=password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle10gDialect

4. Create a table for each application and environment that you need

4.1. Table name must be: <application>_<environment> (All in underscore)  

4.2. Fields must be: long id, string property, string value

Oracle example:

application: x-service
environment: dev

CREATE TABLE x_service_dev 
(	
	id NUMBER(38,0), 
	property VARCHAR2(256 CHAR), 
	value VARCHAR2(500 CHAR)
);

MySqlExample

CREATE TABLE x_service_dev 
(
  id bigint(20) NOT NULL,
  property varchar(45) DEFAULT NULL,
  value varchar(45) DEFAULT NULL,
  PRIMARY KEY (id)
);