package de.uniol.inf.is.odysseus.storing.logicaloperator;

import java.sql.Connection;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DatabaseAccessAO extends AbstractDatabaseAO implements OutputSchemaSettable {
	

	private static final long serialVersionUID = 2509860582432192501L;
	
	private boolean timesensitiv = false;		
	protected SDFSource source = null;

	private SDFAttributeList outputSchema;

	public DatabaseAccessAO(SDFSource source, Connection connection, String table, boolean timesensitiv) {
		super(connection, table);
		this.timesensitiv = timesensitiv;		
		this.source = source;
	}

	public DatabaseAccessAO(DatabaseAccessAO original) {
		super(original.getConnection(), original.getTable());
		this.timesensitiv = original.timesensitiv;		
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
	
	
	public SDFElement getSource(){
		return this.source;
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
		if(port==0){
			setOutputSchema(outputSchema);
		}else{
			throw new IllegalArgumentException("no such port: " + port);
		}
		
	}
	
}
