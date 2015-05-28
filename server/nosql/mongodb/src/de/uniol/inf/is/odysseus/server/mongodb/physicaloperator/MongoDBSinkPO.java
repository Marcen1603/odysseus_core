package de.uniol.inf.is.odysseus.server.mongodb.physicaloperator;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import de.uniol.inf.is.odysseus.server.mongodb.connectionwrapper.MongoDBConnectionWrapper;
import de.uniol.inf.is.odysseus.server.mongodb.logicaloperator.MongoDBSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLJsonSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Default Port: 27017
 */
public class MongoDBSinkPO extends AbstractNoSQLJsonSinkPO {

    private String mongoDBName;
    private String collectionName;

    private DBCollection mongoDBCollection;

    public MongoDBSinkPO(MongoDBSinkAO mongoDBSinkAO) {
        super(mongoDBSinkAO);
        this.mongoDBName = mongoDBSinkAO.getMongoDBName();
        this.collectionName = mongoDBSinkAO.getCollectionName();
    }

    @Override
    public Class<? extends NoSQLConnectionWrapper<?>> getNoSQLConnectionWrapperClass() {
        return MongoDBConnectionWrapper.class;
    }

    @Override
    public void setupConnection(Object connection) {
    	
    	// TODO?
    	// MongoCredential credential = MongoCredential.createCredential(userName, database, password);
    	// MongoClient mongoClient = new MongoClient(new ServerAddress(), Arrays.asList(credential));
    	
        MongoClient mongoClient = (MongoClient) connection;
        mongoDBCollection = mongoClient.getDB(mongoDBName).getCollection(collectionName);
    }

    @Override
    protected void process_next_json_to_write(List<String> jsonToWrite) {
        List<DBObject> dbObjects = new ArrayList<>();
        for (String json : jsonToWrite) {
            DBObject dbObject = (DBObject) JSON.parse(json);
            dbObjects.add(dbObject);
        }
        mongoDBCollection.insert(dbObjects);
    }
}
