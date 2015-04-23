package de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator;

import com.google.gson.Gson;
import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  The AbstractNoSQLJsonSinkPO transforms the elements which should be saved in the NoSQL into JSON strings.
 *
 *  For NoSQL databases which save their data in JSON Format it is recommended to use the
 *  AbstractNoSQLJsonSinkPO instead of AbstractNoSQLSinkPO.
 */
public abstract class AbstractNoSQLJsonSinkPO extends AbstractNoSQLSinkPO<KeyValueObject<?>> {

    protected static Gson gson = new Gson();

    public AbstractNoSQLJsonSinkPO(AbstractNoSQLSinkAO abstractNoSQLSinkAO) {
        super(abstractNoSQLSinkAO);
    }

    @Override
    protected final synchronized void process_next_tuple_to_write(List<KeyValueObject<?>> elementsToWrite) {

        ArrayList<String> jsons = new ArrayList<>();

        for (KeyValueObject<?> tuple : elementsToWrite) {

            String json = toJsonString(tuple);
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
}
