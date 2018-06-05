package de.uniol.inf.is.odysseus.server.mongodb.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;

@LogicalOperator(name = "MONGODBSINK", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can write data to a MongoDB.", category = {
		LogicalOperatorCategory.SINK, LogicalOperatorCategory.DATABASE })
public class MongoDBSinkAO extends AbstractNoSQLSinkAO {

	private static final long serialVersionUID = 48527005198082974L;
	private String collectionName;
	private boolean deleteBeforeInsert;
	private String deleteEqualAttribute;

	public MongoDBSinkAO() {
		super();
		this.deleteBeforeInsert = false;
		this.deleteEqualAttribute = "";
	}

	public MongoDBSinkAO(MongoDBSinkAO old) {
		super(old);
		this.collectionName = old.collectionName;
		this.deleteBeforeInsert = old.isDeleteBeforeInsert();
		this.deleteEqualAttribute = old.getDeleteEqualAttribute();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MongoDBSinkAO(this);
	}

	/**
	 * @param host
	 *            Name of the associated host
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

	@Parameter(name = "deleteBeforeInsert", type = BooleanParameter.class, optional = true, doc = "If you set this to true, the operator will remove the data from the collection before inserting the new data. Default is false.")
	public void setDeleteBeforeInsert(boolean deleteBeforeInsert) {
		this.deleteBeforeInsert = deleteBeforeInsert;
	}
	
	@Parameter(name = "deleteEqualAttribute", type = StringParameter.class, optional = true, doc = "If you use deleteBeforeInsert and only want to delete tuples, which are equal in the given attribute. E.g. an uuid.")
	public void setDeleteEqualAttribute(String deleteEqualAttribute) {
		this.deleteEqualAttribute = deleteEqualAttribute;
	}

	public boolean isDeleteBeforeInsert() {
		return deleteBeforeInsert;
	}

	public String getDeleteEqualAttribute() {
		return deleteEqualAttribute;
	}
	
	public String getCollectionName() {
		return collectionName;
	}

	@Override
	public boolean isSourceOperator() {
		return false;
	}
}
