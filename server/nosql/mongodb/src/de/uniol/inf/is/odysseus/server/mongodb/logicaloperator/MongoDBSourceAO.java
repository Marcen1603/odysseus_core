package de.uniol.inf.is.odysseus.server.mongodb.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSourceAO;

@LogicalOperator(name = "MONGODBSOURCE", minInputPorts = 0, maxInputPorts = 0, doc = "This operator can read data from a MongoDB.", category = {
        LogicalOperatorCategory.SOURCE, LogicalOperatorCategory.DATABASE })
public class MongoDBSourceAO extends AbstractNoSQLSourceAO {

	private static final long serialVersionUID = 7290475028394007187L;
	private String collectionName;
    private String referenceObject;

    public MongoDBSourceAO(){
        super();
    }

    public MongoDBSourceAO(MongoDBSourceAO old) {
        super(old);
        this.collectionName = old.collectionName;
        this.referenceObject = old.referenceObject;
    }

    /**
     * @param host Name of the associated host
     */
    @Override
	@Parameter(name = "Database", type = StringParameter.class, optional = false, doc = "The name of the target database")
    public void setDatabase(String database) {
        super.setDatabase(database);
    }

    
    @Parameter(name = "COLLECTIONNAME", type = StringParameter.class, optional = false, doc = "Name of store mongo collection")
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Parameter(name = "REFERENCE_OBJECT", type = StringParameter.class, optional = true, doc = "Reference object as Json String")
    public void setReferenceObject(String referenceObject) {
        this.referenceObject = referenceObject;
    }

    public String getReferenceObject() {
        return referenceObject;
    }

    public String getCollectionName() {
        return collectionName;
    }

    @Override
    public MongoDBSourceAO clone() {
        return new MongoDBSourceAO(this);
    }
}
