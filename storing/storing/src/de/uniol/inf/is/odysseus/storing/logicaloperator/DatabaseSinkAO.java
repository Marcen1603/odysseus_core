package de.uniol.inf.is.odysseus.storing.logicaloperator;

import java.sql.Connection;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DatabaseSinkAO extends AbstractDatabaseAO {

	private static final long serialVersionUID = -7905452360184301303L;
	private boolean savemetadata;
	private boolean create;
	private boolean ifnotexists;
	private boolean truncate;
	
	public DatabaseSinkAO(Connection connection, String tableName, boolean savemetadata, boolean create, boolean truncate, boolean ifnotexists) {
		super(connection, tableName);		
		this.savemetadata = savemetadata;
		this.create = create;
		this.ifnotexists = ifnotexists;
		this.truncate = truncate;
	}

	public DatabaseSinkAO(DatabaseSinkAO databaseSinkAO) {
		super(databaseSinkAO.getConnection(), databaseSinkAO.getTable());
		this.savemetadata = databaseSinkAO.savemetadata;
		this.create = databaseSinkAO.create;
		this.ifnotexists = databaseSinkAO.ifnotexists;
		this.truncate = databaseSinkAO.truncate;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return super.getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {		
		return new DatabaseSinkAO(this);
	}

	public boolean isSaveMetaData() {		
		return this.savemetadata;
	}

	public boolean isCreate() {
		return create;
	}

	public boolean isIfnotexists() {
		return ifnotexists;
	}

	public boolean isTruncate() {
		return truncate;
	}
	
	

}
