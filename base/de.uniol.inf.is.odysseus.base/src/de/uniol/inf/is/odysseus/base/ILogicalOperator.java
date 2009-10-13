package de.uniol.inf.is.odysseus.base;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface ILogicalOperator extends IClone, IOwnedOperator, 
	ISubscribable<ILogicalOperator, LogicalSubscription>, ISubscriber<ILogicalOperator,LogicalSubscription>{

	public ILogicalOperator clone();

	public SDFAttributeList getOutputSchema();
	public void setOutputSchema(SDFAttributeList outElements);
	public SDFAttributeList getInputSchema(int pos);
	public void setInputSchema(int pos, SDFAttributeList schema);
	
//	/**
//	 * Use InputSchemas to determine OutputSchema
//	 */
//	public void calcOutputSchema();
	
	public IPredicate getPredicate();
	public void setPredicate(IPredicate predicate);

	public String getName();
	public void setName(String name);
	
	public void setPhysSubscriptionTo(Subscription<ISource<?>> sub);
	public void setPhysSubscriptionTo(ISource<?> op, int sinkPort, int sourcePort);
	public Subscription<ISource<?>> getPhysSubscriptionTo(int port);
	public Collection<Subscription<ISource<?>>> getPhysSubscriptionsTo();
	// Currently needed for Transformation --> we should get rid of this!
	public Collection<ISource<?>> getPhysInputPO();
	public int getNumberOfInputs();

	public Collection<LogicalSubscription> getSubscribtions(ILogicalOperator a);
	public Collection<LogicalSubscription> getSubscribedTo(ILogicalOperator a);
}
