package de.uniol.inf.is.odysseus.server.mongodb.physicaloperator;

import com.google.gson.JsonObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.server.mongodb.logicaloperator.MongoDBSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLJsonSinkPO;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Erstellt von RoBeaT
 * Date: 01.12.14
 */
public class MongoDBSinkPO extends AbstractNoSQLJsonSinkPO {

    private String mongoDBName;
    private String collectionName;

    private MongoClient mongoClient;
    private DBCollection mongoDBCollection;

    public MongoDBSinkPO(MongoDBSinkAO mongoDBSinkAO) {
        super(mongoDBSinkAO);
        this.mongoDBName = mongoDBSinkAO.getMongoDBName();
        this.collectionName = mongoDBSinkAO.getCollectionName();
    }

    @Override
    protected void process_open_connection() throws OpenFailedException {

        try {
            mongoClient = new MongoClient( "134.106.56.17" , 27017 );
        } catch (UnknownHostException e) {
            throw new OpenFailedException(e);
        }

        mongoDBCollection = mongoClient.getDB(mongoDBName).getCollection(collectionName);
    }

    @Override
    protected void process_next_tuple_to_write(List<Tuple<ITimeInterval>> tupleToWrite) {

        for (Tuple<ITimeInterval> tuple : tupleToWrite) {

            String json = toJsonString(tuple);
            transferToMongoDB(json);
        }
    }

    @Override
    protected void process_close() {
        mongoClient.close();
    }

    private void transferToMongoDB(String json) {

        DBObject dbObject = (DBObject) JSON.parse(json);

        mongoDBCollection.insert(dbObject);
    }
}
