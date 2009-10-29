package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

//TODO: Ist dies ein sinnvoller AlgebraAO?
public class CollectorAO extends AbstractLogicalOperator {
	
	private static final long serialVersionUID = 529611110861321094L;
	
	SDFAttributeList outputSchema = null;

	public CollectorAO(AbstractLogicalOperator po) {
		super(po);
		setName("CollectorAO");
	}

	public CollectorAO() {
		super();
		setName("CollectorAO");
	}

	public @Override
	CollectorAO clone() {
		return new CollectorAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		// The Sum of all InputSchema
		if (outputSchema == null || recalcOutputSchemata){
			outputSchema = new SDFAttributeList();
			for (LogicalSubscription l:getSubscribedTo()){
				outputSchema.addAttributes(l.getInputSchema());
			}
			recalcOutputSchemata = false;
		}
		return outputSchema;
	}
	

}
