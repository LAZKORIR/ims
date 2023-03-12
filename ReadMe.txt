How to run the microservices

Pre-requisites :
 1. Set up RbabbitMQ server and update the credentials on the application.properties for 3 microservices (Api gateway, loan service, repay loan service)
 2. Set up mysql, create database named lending_platform and on it create 3 tables (tbl_users,tbl_products,tbl_loans) 
 as given with the attached table structure guide and shemas accompanied. Update the database credentials in the application.properties as well.
 3. Set up java 11
- After the above setup, Navigate to the directory where the microservices are stored.
- Run this command to start on each of the service 'mvn spring-boot:run'
- once the server starts , you can send requests from the collection attached.

You can as well follow this documentation: https://documenter.getpostman.com/view/7495088/2s93JtQij2