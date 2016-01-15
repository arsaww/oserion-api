package com.oserion.framework.api.business.impl.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.oserion.framework.api.business.IDBConnection;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

public class MongoDBConnection implements IDBConnection {

	private static MongoDBConnection instance;

	private static final String PROPERTY_DB_HOST = "database.host";
	private static final String PROPERTY_DB_PORT = "database.port";
	private static final String PROPERTY_DB_SCHEMA = "database.schema";

	private MongoDatabase database;
	private MongoDbFactory factory;
	private MongoOperations operations;

	private MongoDBConnection() {

		String dbHost = System.getProperty(PROPERTY_DB_HOST);
		int dbPort = Integer.parseInt(System.getProperty(PROPERTY_DB_PORT));
		String dbSchema = System.getProperty(PROPERTY_DB_SCHEMA);

		MongoClient mongoClient = new MongoClient(dbHost, dbPort);

		this.database = mongoClient.getDatabase(dbSchema);
		this.factory = new SimpleMongoDbFactory(mongoClient,dbSchema);
		this.operations = new MongoTemplate(this.factory);

	}

	public static MongoDBConnection getInstance() {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (MongoDBConnection.class) {
				if (instance == null) {
					instance = new MongoDBConnection();
				}
			}
		}
		return instance;
	}

	@JsonIgnore
	public MongoOperations getOperations(){
		return operations;
	}
}

