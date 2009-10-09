package de.uniol.inf.is.odysseus.transformation.drools;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.Subscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

public class Utils {
	
	public static Collection<ILogicalOperator> replace(
			ILogicalOperator logical, IPipe physical){
		Collection<ILogicalOperator> ret = replace(logical, (ISink) physical);
		ret.addAll(replace(logical, (ISource)physical));
		System.out.println("Collection replace "+ret);
		return ret;
		
	}
	
	public static Collection<ILogicalOperator> replace(
			ILogicalOperator logical, ISink physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (Subscription<ISource<?>> psub : logical.getPhysSubscriptionsTo()) {
			physical.subscribeTo((ISource) psub.getTarget(),
					psub.getSinkPort(), psub.getSourcePort());
		}
		ret.add(logical);
		return ret;
	}

	public static Collection<ILogicalOperator> replace(
			ILogicalOperator logical, ISource physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (LogicalSubscription l : logical.getSubscribtions()) {
			l.getTarget().setPhysSubscriptionTo(physical, l.getSinkPort(),
					l.getSourcePort());
			ret.add(l.getTarget());
		}
		return ret;
	}

}
