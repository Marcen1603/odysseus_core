package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * Converts SDFAttributeList to SDFAttributeListExtended
 * 
 * @author André Bolles
 */
public class SchemaConvertAO extends RenameAO{

	private static final long serialVersionUID = 4218605858465342011L;
	
	public SchemaConvertAO() {
		super();
	}

	public SchemaConvertAO(AbstractLogicalOperator po) {
		super(po);
		outputSchema = new SDFAttributeListExtended(po.getOutputSchema());
	}
	
	public SchemaConvertAO(SchemaConvertAO ao){
		super(ao);
		outputSchema = new SDFAttributeListExtended(ao.outputSchema);
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = new SDFAttributeListExtended(outputSchema.clone()); // clone() is necessary
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		if(this.outputSchema == null){
			this.outputSchema = new SDFAttributeListExtended(this.getSubscribedToSource(0).getSchema().clone());
		}
		return outputSchema;
	}
	
	public AbstractLogicalOperator clone() {
		return new SchemaConvertAO(this);
	}
	

}
