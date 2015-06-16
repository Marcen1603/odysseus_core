package de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;

/**
 *  The AbstractNoSQLJsonSinkPO transforms the elements which should be saved in the NoSQL into JSON strings.
 *
 *  For NoSQL databases which save their data in JSON Format it is recommended to use the
 *  AbstractNoSQLJsonSinkPO instead of AbstractNoSQLSinkPO.
 */
public abstract class AbstractNoSQLJsonSinkPO<T extends IStreamObject<?>> extends AbstractNoSQLSinkPO<T> {

    final protected static Gson gson = new Gson();
    final private boolean tupleMode;
    final private List<String> attributes;
    
    public AbstractNoSQLJsonSinkPO(AbstractNoSQLSinkAO abstractNoSQLSinkAO) {
        super(abstractNoSQLSinkAO);
        SDFSchema inputSchema = abstractNoSQLSinkAO.getInputSchema(0);
        if (inputSchema != null && inputSchema.getType() == Tuple.class){
        	tupleMode = true;
        	attributes = new ArrayList<>(inputSchema.getAttributes().size());
        	for (SDFAttribute a: inputSchema.getAttributes()){
        		attributes.add(a.getAttributeName());
        	}
        }else{
        	tupleMode = false;
        	attributes = null;
        }
    }

    @Override
    protected final synchronized void process_next_tuple_to_write(List<T> elementsToWrite) {

        ArrayList<String> jsons = new ArrayList<>();

        for (T elem : elementsToWrite) {
        	final String json;
        	if (tupleMode){
        		json = toJsonString((Tuple<?>) elem);
        	}else{
        		json = toJsonString((KeyValueObject<?>)elem);
        	}
            jsons.add(json);
        }

        process_next_json_to_write(jsons);
    }

    /**
     *  process_next_json_to_write will be implemented in the concrete NoSQLSinkPO.
     *  In this method all elements of jsonToWrite will be written into the NoSQL database.
     *
     * @param jsonToWrite list with JSON strings
     */
    protected abstract void process_next_json_to_write(List<String> jsonToWrite);

    private String toJsonString(KeyValueObject<?> tuple){
        Map<String,Object> attributes = tuple.getAttributes();
        return gson.toJson(attributes);
    }
    
    private String toJsonString(Tuple<?> tuple){
    	Map<String,Object> e = new HashMap<String, Object>();
    	for (int i=0;i<attributes.size();i++){
    		e.put(attributes.get(i), tuple.getAttribute(i));
    	}
        return gson.toJson(e);
    }
    
}
