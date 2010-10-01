package de.uniol.inf.is.odysseus.parser.pql.test;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MuhOperator extends AbstractLogicalOperator {

	private static final long serialVersionUID = 7770543328602025880L;
	
	SDFAttributeList outputSchema = null;
	
	public MuhOperator() {
		
	}
	
	public MuhOperator(MuhOperator muhOperator) {
		this.outputSchema = muhOperator.outputSchema.clone();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		// The Sum of all InputSchema
		if (outputSchema == null || recalcOutputSchemata){
			outputSchema = new SDFAttributeList();
			for (LogicalSubscription l:getSubscribedToSource()){
				outputSchema.addAttributes(l.getSchema());
			}
			recalcOutputSchemata = false;
		}
		return outputSchema;
	}
	
	@Override
	public boolean isAllPhysicalInputSet() {
		boolean set = true;
		for (int i=0;i<getNumberOfInputs() && set;i++){
			set = set && (getPhysSubscriptionTo(i) != null);
		}
		return set;
	}
	
	@Override
	public MuhOperator clone() {
		return new MuhOperator(this);
	}
	
}
