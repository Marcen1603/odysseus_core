package de.uniol.inf.is.odysseus.server.mongodb.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.server.mongodb.connectionwrapper.MongoDBConnectionWrapper;
import de.uniol.inf.is.odysseus.server.mongodb.logicaloperator.MongoDBSourceAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLJsonSourcePO;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;


public class MongoDBSourcePO<M extends IMetaAttribute> extends AbstractNoSQLJsonSourcePO<M> {

    private String mongoDBName;
    private String collectionName;
    private String referenceObject;

    private DBCollection mongoDBCollection;

    public MongoDBSourcePO(MongoDBSourceAO mongoDBSourceAO) {
        super(mongoDBSourceAO);
        this.mongoDBName = mongoDBSourceAO.getDatabase();
        this.collectionName = mongoDBSourceAO.getCollectionName();
        this.referenceObject = mongoDBSourceAO.getReferenceObject();
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
    protected List<String> process_transfer_jsons(int maxTupleCount) {

        DBCursor dbCursor;

        if(referenceObject == null){
            dbCursor  = mongoDBCollection.find();
        }else {
            DBObject parsed = (DBObject) JSON.parse(referenceObject);
            dbCursor = mongoDBCollection.find(parsed);
        }

        List<String> jsons = new ArrayList<>();

        for (DBObject dbObject : dbCursor) {
            jsons.add(dbObject.toString());
        }
        return jsons;
    }
}
