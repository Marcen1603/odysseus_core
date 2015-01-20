package de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;

import java.util.List;

/**
 * Erstellt von RoBeaT
 * Date: 15.12.2014
 */
public abstract class AbstractNoSQLJsonSinkPO extends AbstractNoSQLSinkPO {

    protected String[] attributeNames;
    protected static Gson gson = new Gson();

    public AbstractNoSQLJsonSinkPO(AbstractNoSQLSinkAO abstractNoSQLSinkAO) {
        super(abstractNoSQLSinkAO);
    }

    @Override
    protected final void process_open() throws OpenFailedException {

        List<SDFAttribute> attributes = getOutputSchema().getAttributes();

        attributeNames = new String[attributes.size()];

        for (int i = 0, attributesSize = attributes.size(); i < attributesSize; i++) {
            SDFAttribute attribute = attributes.get(i);
            attributeNames[i] = attribute.getAttributeName();

            System.out.println(attribute.getDatatype().getQualName());
            System.out.println(attribute.getDatatype().getURI());
            System.out.println(attribute.getDatatype().getURIWithoutQualName());

            System.out.println("----------------------------------------------------------");
        }

        process_open_connection();
    }

    protected JsonObject toJsonObject(Tuple<ITimeInterval> tuple){

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

        return jsonObject;
    }

    protected String toJsonString(Tuple<ITimeInterval> tuple){
        JsonObject jsonObject = toJsonObject(tuple);
        return gson.toJson(jsonObject);
    }
}
