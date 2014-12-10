package de.uniol.inf.is.odysseus.server.mongodb.physicaloperator;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

import java.net.UnknownHostException;
import java.util.List;

/**
 * C:\Users\RoBeaT\EclipseWorkspace\Odysseus\server\database\database\lib\mongo-java-driver-2.12.4.jar
 * Erstellt von RoBeaT
 * Date: 01.12.14
 */
public class MongoDBSinkPO extends AbstractSink<Tuple<ITimeInterval>> {

    private String[] attributeNames;
    private MongoClient mongoClient;

    private String mongoDBName;
    private String collectionName;

    private DBCollection mongoDBCollection;

    private static Gson gson = new Gson();

    public MongoDBSinkPO(String mongoDBName, String collectionName) {
        this.mongoDBName = mongoDBName;
        this.collectionName = collectionName;
    }

    @Override
    protected void process_open() throws OpenFailedException {

        try {
            mongoClient = new MongoClient( "134.106.56.17" , 27017 );
        } catch (UnknownHostException e) {
            throw new OpenFailedException(e);
        }

        mongoDBCollection = mongoClient.getDB(mongoDBName).getCollection(collectionName);

        List<SDFAttribute> attributes = getOutputSchema().getAttributes();

        attributeNames = new String[attributes.size()];

        for (int i = 0, attributesSize = attributes.size(); i < attributesSize; i++) {
            SDFAttribute attribute = attributes.get(i);
            attributeNames[i] = attribute.getAttributeName();
        }
    }

    @Override
    protected void process_next(Tuple<ITimeInterval> tuple, int port) {

        JsonObject jsonObject = new JsonObject();

        for (int i = 0, attributeNamesLength = attributeNames.length; i < attributeNamesLength; i++) {
            String attributeName = attributeNames[i];
            Object attribute = tuple.getAttribute(i);

            //necessary casting for serialization     schema known -> optimization possible
            if(attribute instanceof Number){
                jsonObject.addProperty(attributeName, (Number) attribute);
            }    else
            if(attribute instanceof String){
                jsonObject.addProperty(attributeName, (String) attribute);
            }    else
            if(attribute instanceof Boolean){
                jsonObject.addProperty(attributeName, (Boolean) attribute);
            }    else
            if(attribute instanceof Character){
                jsonObject.addProperty(attributeName, (Character) attribute);
//            }    else {
//                jsonObject.add(attributeName, JsonNull);
            }
        }

        transferToMongoDB(jsonObject);
    }

    @Override
    protected void process_close() {
        mongoClient.close();
    }

    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {

    }

    private void transferToMongoDB(JsonObject jsonObject) {

        String json = gson.toJson(jsonObject);

        DBObject dbObject = (DBObject) JSON.parse(json);

        mongoDBCollection.insert(dbObject);
    }

    @Override
    public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
        return false;   
    }
}
