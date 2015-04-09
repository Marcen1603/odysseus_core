package de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator;

import com.google.gson.Gson;
import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSourceAO;

import java.util.*;

/**
 *  The AbstractNoSQLJsonSourcePO transforms the JSON strings which are read from the NoSQL database into KeyValue objects.
 *
 *  For NoSQL databases which save their data in JSON Format it is recommended to use the
 *  AbstractNoSQLJsonSourcePO instead of AbstractNoSQLSourcePO.
 */
public abstract class AbstractNoSQLJsonSourcePO extends AbstractNoSQLSourcePO {

    protected static Gson gson = new Gson();

    public AbstractNoSQLJsonSourcePO(AbstractNoSQLSourceAO abstractNoSQLSourceAO) {
        super(abstractNoSQLSourceAO);
    }

    @Override
    protected final List<KeyValueObject> process_transfer_tuples(int maxTupleCount) {

        List<String> strings = process_transfer_jsons(maxTupleCount);

        List<KeyValueObject> keyValueObjects = new ArrayList<>();

        //noinspection MismatchedQueryAndUpdateOfCollection
        Map<String, Object> map = new HashMap<>();

        for (String string : strings) {

            //noinspection unchecked    Map<String, Object> will be returned
            Map<String, Object> jsonMap = gson.fromJson(string, map.getClass());

            KeyValueObject keyValueObject = new KeyValueObject(jsonMap);
            keyValueObjects.add(keyValueObject);
        }

        return keyValueObjects;
    }

    /**
     *  process_transfer_jsons will be implemented in the concrete NoSQLSourcePO.
     *  In this method will the concrete NoSQLSourcePO read data from the NoSQL database.
     *
     * @param maxTupleCount the max count of elements. The concrete class should respect the max count
     * @return a list of JSON strings
     */
    abstract protected List<String> process_transfer_jsons(int maxTupleCount);
}
