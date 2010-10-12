package de.uniol.inf.is.odysseus.storing.logicaloperator;

import java.sql.Connection;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DatabaseAccessAO extends AbstractLogicalOperator implements OutputSchemaSettable {
	

	private static final long serialVersionUID = 2509860582432192501L;
	
	private boolean timesensitiv = false;
	private Connection connection;
	private String table = "";
	
	protected SDFSource source = null;

	private SDFAttributeList outputSchema;

	public DatabaseAccessAO(SDFSource source, Connection connection, String table, boolean timesensitiv) {
		this.timesensitiv = timesensitiv;
		this.connection = connection;
		this.table = table;
		this.source = source;
	}

	public DatabaseAccessAO(DatabaseAccessAO original) {
		this.timesensitiv = original.timesensitiv;
		this.connection = original.connection;
		this.table = original.table;
		this.source = original.source;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DatabaseAccessAO(this);
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();		
	}

	public boolean isTimesensitiv() {
		return timesensitiv;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getTable() {
		return table;
	}
	
	public SDFElement getSource(){
		return this.source;
	}
	
	

}
