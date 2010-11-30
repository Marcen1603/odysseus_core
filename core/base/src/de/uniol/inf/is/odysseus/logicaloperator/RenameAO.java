package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * @author Jonas Jacobi
 */
public class RenameAO extends UnaryLogicalOp implements OutputSchemaSettable{

	private static final long serialVersionUID = 4218605858465342011L;
	protected SDFAttributeList outputSchema;
	
	public RenameAO() {
		super();
	}

	public RenameAO(AbstractLogicalOperator po) {
		super(po);
		outputSchema = po.getOutputSchema();
	}
	
	public RenameAO(RenameAO ao){
		super(ao);
		outputSchema = new SDFAttributeList(ao.outputSchema);
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new RenameAO(this);
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
