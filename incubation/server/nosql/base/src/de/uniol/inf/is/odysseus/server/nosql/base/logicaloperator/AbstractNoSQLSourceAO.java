package de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 *  The AbstractNoSQLSourceAO is the superclass for all logical operators in the NoSQL context for sources.
 *  The parameters maxTupleCount, delayBetweenTuple, reloadData and reloadDataInterval will be set here and
 *  are needed in all logical operators in the NoSQL context for sources.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AbstractNoSQLSourceAO extends AbstractNoSQLAO {

	private static final long serialVersionUID = 1361416706500398607L;
	private int maxTupleCount;
    private int delayBetweenTuple;
    private boolean reloadData;
    private int reloadDataInterval;

    public AbstractNoSQLSourceAO(){
        super();
    }

    public AbstractNoSQLSourceAO(AbstractNoSQLSourceAO old) {
        super(old);

        this.maxTupleCount = old.maxTupleCount;
        this.delayBetweenTuple = old.delayBetweenTuple;
        this.reloadData = old.reloadData;
        this.reloadDataInterval = old.reloadDataInterval;
    }

    /**
     * @param maxTupleCount The max count of tuples which will be read from the NoSQL database
     */
    @Parameter(name = "MAX_TUPLE_COUNT", type = IntegerParameter.class, optional = true,
               doc = "The max count of tuples which will be read from the NoSQL database")
    public void setMaxTupleCount(int maxTupleCount) {
        this.maxTupleCount = maxTupleCount;
    }

    /**
     * @param delayBetweenTuple The delay between each tuple which will be transferred in ms
     *                          Default is 0
     */
    @Parameter(name = "DELAY_BETWEEN_TUPLE", type = IntegerParameter.class, optional = true,
               doc = "The delay between each tuple which will be transferred in ms. Default is 0")
    public void setDelayBetweenTuple(int delayBetweenTuple) {
        this.delayBetweenTuple = delayBetweenTuple;
    }

    /**
     * @param reloadData true:  the query to the NoSQL database will be called again after "reloadDataInterval" ms
     *                   false: the query will be called just one time
     *
     *                   Default ist false
     */
    @Parameter(name = "RELOAD_DATA", type = BooleanParameter.class, optional = true,
               doc = "If true, the query to the NoSQL database will be called again after \"RELOAD_DATA_INTERVAL\" ms. " +
                     "Default is false")
    public void setReloadData(boolean reloadData) {
        this.reloadData = reloadData;
    }

    /**
     * @param reloadDataInterval The delay between each query to the NoSQL database in ms
     *                           Default is 60.000 (10 minutes)
     */
    @Parameter(name = "RELOAD_DATA_INTERVAL", type = IntegerParameter.class, optional = true,
               doc = "The delay between each query to the NoSQL database in ms. Default is 60.000 (10 minutes)" +
                     "RELOAD_DATA_INTERVAL will just be used, if RELOAD_DATA is true")
    public void setReloadDataInterval(int reloadDataInterval) {
        this.reloadDataInterval = reloadDataInterval;
    }

    public boolean isReloadData() {
        return reloadData;
    }

    public int getReloadDataInterval() {
        return reloadDataInterval;
    }

    public int getMaxTupleCount() {
        return maxTupleCount;
    }

    public int getDelayBetweenTuple() {
        return delayBetweenTuple;
    }
    
    @Override
    protected SDFSchema getOutputSchemaIntern(int pos) {
    	
    	@SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema(getName(), (Class<? extends IStreamObject<?>>) KeyValueObject.class, new ArrayList<SDFAttribute>());
    	    	
    	return schema;
    }
}
