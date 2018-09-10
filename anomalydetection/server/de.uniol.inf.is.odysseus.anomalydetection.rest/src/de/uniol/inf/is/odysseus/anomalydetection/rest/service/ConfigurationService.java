package de.uniol.inf.is.odysseus.anomalydetection.rest.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import de.uniol.inf.is.odysseus.anomalydetection.rest.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.Configuration;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.QueryInformation;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.ServerConfiguration;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This class can load configurations from a MongoDB and can store
 * configurations within a MongoDB. These configurations can be executed by this
 * service.
 * 
 * @author Tobias Brandt
 *
 */
public class ConfigurationService {

	public static List<Configuration> getAvailableConfigurations(String username) {
		List<Configuration> configs = new ArrayList<>();
		try {
			MongoClient mongoClient = new MongoClient("127.0.0.1:27017");
			DBCollection mongoDBCollection = mongoClient.getDB("odysseus").getCollection("conditionConfigurations");

			// Read from this collection
			DBCursor dbCursor;
			dbCursor = mongoDBCollection.find();
			List<String> jsons = new ArrayList<>();

			for (DBObject dbObj : dbCursor) {
				jsons.add(dbObj.toString());
			}

			for (String json : jsons) {
				Gson gson = new Gson();
				Configuration config = gson.fromJson(json, Configuration.class);
				if (config.getUsers().contains(username)) {
					// Only add the configurations which are for this user
					configs.add(config);
				}
			}
			return configs;

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Configuration getConfiguration(UUID identifier) {
		try {
			MongoClient mongoClient = new MongoClient("127.0.0.1:27017");
			DBCollection mongoDBCollection = mongoClient.getDB("odysseus").getCollection("conditionConfigurations");

			// Read from this collection
			DBCursor dbCursor;
			dbCursor = mongoDBCollection.find();
			List<String> jsons = new ArrayList<>();

			for (DBObject dbObj : dbCursor) {
				jsons.add(dbObj.toString());
			}

			for (String json : jsons) {
				Gson gson = new Gson();
				Configuration config = gson.fromJson(json, Configuration.class);
				if (config.getIdentifier().equals(identifier)) {
					return config;
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void runConfiguration(Configuration config, ISession session) {
		runServerConfiguration(config.getServerConfiguration(), session);
	}

	private static void runServerConfiguration(ServerConfiguration serverConfig, ISession session) {
		try {
			// First the sources from files
			for (QueryInformation sourcePath : serverConfig.getSourcePaths()) {
				String content = new String(Files.readAllBytes(Paths.get(sourcePath.getContent())));
				try {
					ExecutorServiceBinding.getExecutor().addQuery(content, sourcePath.getParser(), session,
							new Context());
				} catch (QueryParseException e) {
					e.printStackTrace();
				} catch (PlanManagementException e) {
					// Probably, the query with this name is already installed
					e.printStackTrace();
				}
			}

			// Then sources from strings
			for (QueryInformation source : serverConfig.getSources()) {
				try {
					ExecutorServiceBinding.getExecutor().addQuery(source.getContent(), source.getParser(), session,
							new Context());
				} catch (QueryParseException e) {
					e.printStackTrace();
				} catch (PlanManagementException e) {
					// Probably, the query with this name is already installed
					e.printStackTrace();
				}
			}

			// After the sources we can start the queries
			for (QueryInformation query : serverConfig.getQueryPaths()) {
				String content = new String(Files.readAllBytes(Paths.get(query.getContent())));
				try {
					ExecutorServiceBinding.getExecutor().addQuery(content, query.getParser(), session, new Context());
				} catch (QueryParseException e) {
					e.printStackTrace();
				} catch (PlanManagementException e) {
					// Probably, the query with this name is already installed
					e.printStackTrace();
				}
			}

			// Last but not least the queries from strings
			for (QueryInformation query : serverConfig.getQueries()) {
				try {
					ExecutorServiceBinding.getExecutor().addQuery(query.getContent(), query.getParser(), session,
							new Context());
				} catch (QueryParseException e) {
					e.printStackTrace();
				} catch (PlanManagementException e) {
					// Probably, the query with this name is already installed
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (QueryParseException e) {
			e.printStackTrace();
		}

	}

}
