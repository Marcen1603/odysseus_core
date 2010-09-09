package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;


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
		return getSubscribedToSource(LEFT).getTarget();
	}
	
	public ILogicalOperator getRightInput(){
		return getSubscribedToSource(RIGHT).getTarget();
	}

//	public void setLeftInput(ILogicalOperator source, SDFAttributeList inputSchema) {
//		subscribeTo(source, LEFT, 0, inputSchema);
//	}
//
//	public void setRightInput(ILogicalOperator source, SDFAttributeList inputSchema) {
//		subscribeTo(source, RIGHT, 0, inputSchema);
//	}	
	
//	private ISource<?> getLeftPhysInput(){
//		return getPhysSubscriptionTo(LEFT)==null?null:getPhysSubscriptionTo(LEFT).getTarget();
//	}
//
//	private ISource<?> getRightPhysInput(){
//		return getPhysSubscriptionTo(RIGHT)==null?null:getPhysSubscriptionTo(RIGHT).getTarget();
//	}
}
