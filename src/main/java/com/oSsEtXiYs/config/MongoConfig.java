package com.oSsEtXiYs.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
@PropertySource({"classpath:application.properties"})
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongo.db.connection}")
    private String connection;
    @Value("${mongo.db.database}")
    private String database;

    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(connection));
    }

    protected String getDatabaseName() {
        return database;
    }

}
