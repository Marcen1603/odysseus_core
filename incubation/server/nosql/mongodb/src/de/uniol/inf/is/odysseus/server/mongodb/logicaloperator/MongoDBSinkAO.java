package de.uniol.inf.is.odysseus.server.mongodb.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * Erstellt von RoBeaT
 * Date: 01.12.14
 */
@LogicalOperator(name = "MONGODBSINK", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can write data to a MongoDB.", category = {
        LogicalOperatorCategory.SINK, LogicalOperatorCategory.DATABASE })
public class MongoDBSinkAO extends UnaryLogicalOp {

    /**
	 * 
	 */
	private static final long serialVersionUID = 48527005198082974L;
	private String mongoDBName;
    private String collectionName;

    public MongoDBSinkAO(){
        super();
    }

    public MongoDBSinkAO(MongoDBSinkAO old) {
        super(old);
        mongoDBName = old.mongoDBName;
        collectionName = old.collectionName;
    }

    @Override
    public AbstractLogicalOperator clone() {
        return new MongoDBSinkAO(this);
    }

    @Parameter(name = "MONGODBNAME", type = StringParameter.class, optional = false, doc = "Name of store mongo database")
    public void setMongoDBName(String mongoDBName) {
        this.mongoDBName = mongoDBName;
    }

    @Parameter(name = "COLLECTIONNAME", type = StringParameter.class, optional = false, doc = "Name of store mongo collection")
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getMongoDBName() {
        return mongoDBName;
    }

    public String getCollectionName() {
        return collectionName;
    }
}
