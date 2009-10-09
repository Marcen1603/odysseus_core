package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public abstract class BinaryLogicalOp extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1558576598140153762L;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	public BinaryLogicalOp(AbstractLogicalOperator po) {
		super(po);
	}

	public BinaryLogicalOp() {
		super();
	}
	
	public ILogicalOperator getLeftInput(){
		return getSubscribedTo(LEFT).getTarget();
	}
	
	public ILogicalOperator getRightInput(){
		return getSubscribedTo(RIGHT).getTarget();
	}

	public void setLeftInput(ILogicalOperator source) {
		subscribeTo(source, LEFT, 0);
	}

	public void setRightInput(ILogicalOperator source) {
		subscribeTo(source, RIGHT, 0);
	}
	
	public void setLeftInputSchema(SDFAttributeList schema) {
		setInputSchema(LEFT, schema);
	}
	
	public void setRightInputSchema(SDFAttributeList schema) {
		setInputSchema(RIGHT, schema);
	}
	
	public ISource<?> getLeftPhysInput(){
		return getPhysSubscriptionTo(LEFT)==null?null:getPhysSubscriptionTo(LEFT).getTarget();
	}

	public ISource<?> getRightPhysInput(){
		return getPhysSubscriptionTo(RIGHT)==null?null:getPhysSubscriptionTo(RIGHT).getTarget();
	}

}
