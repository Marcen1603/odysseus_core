package de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSourceAO;

/**
 *  The AbstractNoSQLJsonSourcePO transforms the JSON strings which are read from the NoSQL database into KeyValue objects.
 *
 *  For NoSQL databases which save their data in JSON Format it is recommended to use the
 *  AbstractNoSQLJsonSourcePO instead of AbstractNoSQLSourcePO.
 */
public abstract class AbstractNoSQLJsonSourcePO<M extends IMetaAttribute> extends AbstractNoSQLSourcePO<M, KeyValueObject<M>> {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractNoSQLJsonSourcePO.class);

    protected static Gson gson = new Gson();

    public AbstractNoSQLJsonSourcePO(AbstractNoSQLSourceAO abstractNoSQLSourceAO) {
        super(abstractNoSQLSourceAO);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    protected final List<KeyValueObject<M>> process_transfer_tuples(int maxElementCount) {

        List<String> strings = process_transfer_jsons(maxElementCount);

        List<KeyValueObject<M>> keyValueObjects = new ArrayList<>();

        //noinspection MismatchedQueryAndUpdateOfCollection
        Map<String, Object> map = new HashMap<>();

        for (String string : strings) {

            //noinspection unchecked    Map<String, Object> will be returned
			Map<String, Object> jsonMap = gson.fromJson(string, map.getClass());

            KeyValueObject keyValueObject = KeyValueObject.createInstance(jsonMap);
    		M meta = null;
    		try {
    			meta = getMetadataInstance();
    		} catch (InstantiationException | IllegalAccessException e1) {
    			LOG.error("Error creating meta data",e1);
                // BAD Bad bad idea, this line kills your screen with popups!!!
    			// sendError("Error creating meta data",e1);
    		}
    		keyValueObject.setMetadata(meta);
    		updateMetadata(keyValueObject);
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
