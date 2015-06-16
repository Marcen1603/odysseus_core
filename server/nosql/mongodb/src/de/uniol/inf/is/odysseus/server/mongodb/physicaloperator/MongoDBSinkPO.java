package de.uniol.inf.is.odysseus.server.mongodb.physicaloperator;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.server.mongodb.connectionwrapper.MongoDBConnectionWrapper;
import de.uniol.inf.is.odysseus.server.mongodb.logicaloperator.MongoDBSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLJsonSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Default Port: 27017
 */
public class MongoDBSinkPO<T extends IStreamObject<?>> extends AbstractNoSQLJsonSinkPO<T> {

	private String mongoDBName;
	private String collectionName;

	private DBCollection mongoDBCollection;

	private boolean deleteBeforeInsert;
	private String deleteEqualAttribute;

	public MongoDBSinkPO(MongoDBSinkAO mongoDBSinkAO) {
		super(mongoDBSinkAO);
		this.mongoDBName = mongoDBSinkAO.getDatabase();
		this.collectionName = mongoDBSinkAO.getCollectionName();
		this.deleteBeforeInsert = mongoDBSinkAO.isDeleteBeforeInsert();
		this.deleteEqualAttribute = mongoDBSinkAO.getDeleteEqualAttribute();
	}

	@Override
	public Class<? extends NoSQLConnectionWrapper<?>> getNoSQLConnectionWrapperClass() {
		return MongoDBConnectionWrapper.class;
	}

	@Override
	public void setupConnection(Object connection) {
		MongoClient mongoClient = (MongoClient) connection;
		mongoDBCollection = mongoClient.getDB(mongoDBName).getCollection(collectionName);
	}

	@Override
	protected void process_next_json_to_write(List<String> jsonToWrite) {

		List<DBObject> dbObjects = new ArrayList<>();
		for (String json : jsonToWrite) {
			DBObject dbObject = (DBObject) JSON.parse(json);
			dbObjects.add(dbObject);

			// In case, that the user want to delete some tuples (maybe equal
			// tuples) before inserting the new version of them
			if (deleteBeforeInsert) {
				// If no attribute is given, just delete everything in the
				// collection
				String deleteString = "{}";
				if (!this.deleteEqualAttribute.isEmpty()) {
					if (dbObject.containsField(deleteEqualAttribute)) {
						deleteString = "{\"" + deleteEqualAttribute + "\":\"" + dbObject.get(deleteEqualAttribute)
								+ "\"}";
					}
				}
				DBObject deleteObject = (DBObject) JSON.parse(deleteString);
				mongoDBCollection.remove(deleteObject);
			}
		}
		mongoDBCollection.insert(dbObjects);
	}
}
