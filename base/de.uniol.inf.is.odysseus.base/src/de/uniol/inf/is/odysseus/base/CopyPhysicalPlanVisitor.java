package de.uniol.inf.is.odysseus.base;

import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * 
 * @author Tobias Witt
 *
 */
public class CopyPhysicalPlanVisitor implements 
		//INodeVisitor<ISubscriber<ISource<? extends T>, PhysicalSubscription<ISource<? extends T>>>, String> {
		INodeVisitor<ISubscriber<IPhysicalOperator, PhysicalSubscription<IPhysicalOperator>>, String> {

	@Override
	public void ascend(
			ISubscriber<IPhysicalOperator, PhysicalSubscription<IPhysicalOperator>> to) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void descend(
			ISubscriber<IPhysicalOperator, PhysicalSubscription<IPhysicalOperator>> to) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void node(
			ISubscriber<IPhysicalOperator, PhysicalSubscription<IPhysicalOperator>> op) {
		// TODO Auto-generated method stub
		
	}

}
