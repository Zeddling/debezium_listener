package com.debezium.config;

import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the embedded debezium
 * */
@Configuration
public class DebeziumConfiguration {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String db_url;

    /**
     * Configure Embedded Debezium
     *
     * @return debezium configuration
     *
     * @see io.debezium.config.Configuration
     * */
    @Bean
    public io.debezium.config.Configuration studentConnector() {
        String[] url_info = decodeDBURL(db_url);

        return io.debezium.config.Configuration.create()
                .with("name", "student-mysql-connector")
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", "/tmp/offsets.dat")
                .with("offset.flush.interval.ms", "60000")
                .with("database.hostname", url_info[0])
                .with("database.port", url_info[1])
                .with("database.user", username)
                .with("database.password", password)
                .with("database.dbname", url_info[2])
                .with("database.include.list", url_info[2])
                .with("include.schema.changes", "false")
                .with("database.server.id", "10181")
                .with("database.server.name", "customer-mysql-db-server")
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", "/tmp/dbhistory.dat")
                .build();
    }

    /**
     * Decodes the provided database url
     *
     * @param url the string db url
     * @return string array containing db host, port and name respectively
     * */
    private String[] decodeDBURL(String url) {
        //  Declare array
        String[] decoded = new String[3];

        //  Split url using /
        //  Array inf the format:
        //  {"jdbc:mariadb","",:localhost:3306","db_name"}
        String[] split_by_slash = url.split("/");

        //  Save db name
        decoded[2] = split_by_slash[3];

        //  Split localhost:3306 and save in decoded
        String[] split_by_colon = split_by_slash[2].split(":");
        decoded[0]= split_by_colon[0];
        decoded[1]= split_by_colon[1];

        return decoded;
    }

}
