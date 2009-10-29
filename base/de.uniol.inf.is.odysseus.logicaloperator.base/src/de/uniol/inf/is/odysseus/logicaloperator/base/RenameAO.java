package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * @author Jonas Jacobi
 */
public class RenameAO extends UnaryLogicalOp implements OutputSchemaSettable{

	private static final long serialVersionUID = 4218605858465342011L;
	private SDFAttributeList outputSchema;
	
	public RenameAO() {
		super();
	}

	public RenameAO(AbstractLogicalOperator po) {
		super(po);
		outputSchema = po.getOutputSchema();
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}
	

}
