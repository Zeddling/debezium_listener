# Debezium Listener
Uses an embedded debezium engine to monitor the provided database and pushes updates to the web client using
server side event emitters.

![alt text](architecture.png "System Architecture")

## Requirements
1. mariadb - latest
2. Attach .env file. Format is in the .env.example file.

Run with ./mvnw spring-boot:run.<br>
Access page http://localhost:8055
