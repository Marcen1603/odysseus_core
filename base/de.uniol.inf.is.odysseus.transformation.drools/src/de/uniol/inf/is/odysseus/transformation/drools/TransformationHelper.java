package de.uniol.inf.is.odysseus.transformation.drools;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.ISubscription;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.Subscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
@SuppressWarnings("unchecked")
public class TransformationHelper {
	
	public static Collection<ILogicalOperator> replace(
			ILogicalOperator logical, IPipe physical) {
		Collection<ILogicalOperator> ret = replace(logical, (ISink) physical);
		ret.addAll(replace(logical, (ISource) physical));
		return ret;
	}

	public static Collection<ILogicalOperator> replace(
			ILogicalOperator logical, ISink physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (Subscription<ISource<?>> psub : logical.getPhysSubscriptionsTo()) {
			physical.subscribeTo((ISource) psub.getTarget(),
					psub.getSinkPort(), psub.getSourcePort());
		}
		for (LogicalSubscription l : logical.getSubscriptions()) {
			ILogicalOperator target = l.getTarget();
			if (target instanceof TopAO) {
				((TopAO) target).setPhysicalInputPO(physical);
			}
		}
		ret.add(logical);
		return ret;
	}

	public static Collection<ILogicalOperator> replace(
			ILogicalOperator logical, ISource physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (LogicalSubscription l : logical.getSubscriptions()) {
			l.getTarget().setPhysSubscriptionTo(physical, l.getSinkPort(),
					l.getSourcePort());
			ret.add(l.getTarget());
		}
		return ret;
	}
	
	/**
	 * Inserts a new operator into a completely transformed physical query plan.
	 * 
	 * @param oldFather The old lower operator (from the dataflow point of view)
	 * @param children The following operators of oldFather (from the dataflow point of view)
	 * @param newFather The new lower operator. oldFather becomes the father of newFather
	 * @return the modified children must be returned to update the drools working memory
	 */
	public static Collection<ISink> insertNewFather(ISource oldFather, Collection<ISubscription<ISink>> children, IPipe newFather){
		Collection<ISink> modifiedChildren = new ArrayList<ISink>();
		
		// for every child, remove the connection between
		// its old father and add it to its new father.
		for(ISubscription<ISink> subscription: children){
			ISink child = subscription.getTarget();
			int sinkPort = subscription.getSinkPort();
			int sourcePort = subscription.getSourcePort();
			
			oldFather.unsubscribe(subscription);
			newFather.subscribe(child, sinkPort, sourcePort);
			modifiedChildren.add(child);
		}
		
		// then add the new father as child to the old father
		oldFather.subscribe(newFather, 0, 0);
		
		// return the modified children to update the drools working memory
		return modifiedChildren;
	}

}
