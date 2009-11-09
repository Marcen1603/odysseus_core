package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.Subscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 */

public abstract class UnaryLogicalOp extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;
	private static final int PORTNUMBER = 0;

	public UnaryLogicalOp(AbstractLogicalOperator po) {
		super(po);
	}

	public UnaryLogicalOp() {
		super();
	}

	public SDFAttributeList getInputSchema() {
		return getInputSchema(PORTNUMBER);
	}

//	public void setInputSchema(SDFAttributeList schema) {
//		setInputSchema(PORTNUMBER, schema);
//	}

	public ILogicalOperator getInputAO(){
		return getSubscribedTo(PORTNUMBER)==null?null:getSubscribedTo(PORTNUMBER).getTarget();
	}
	
	public Subscription<ISource<?>> getPhysSubscriptionTo() {
		return getPhysSubscriptionTo(PORTNUMBER);
	}
	
	public void subscribeTo(ILogicalOperator source, SDFAttributeList inputSchema){
		subscribeTo(source, 0, 0, inputSchema);
	}
	
}
