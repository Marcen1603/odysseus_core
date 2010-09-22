package de.uniol.inf.is.odysseus.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface ILogicalOperator extends IOwnedOperator, 
	ISubscribable<ILogicalOperator, LogicalSubscription>, ISubscriber<ILogicalOperator,LogicalSubscription>, IClone, Serializable{

	public ILogicalOperator clone();
	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> replaced);
	public SDFAttributeList getOutputSchema();
	public SDFAttributeList getInputSchema(int pos);	
	
	@SuppressWarnings("unchecked")
	public IPredicate getPredicate();
	@SuppressWarnings("unchecked")
	public void setPredicate(IPredicate predicate);

	public String getName();
	public void setName(String name);
	
	boolean isAllPhysicalInputSet();
	public void setPhysSubscriptionTo(Subscription<ISource<?>> sub);
	public void setPhysSubscriptionTo(ISource<?> op, int sinkInPort, int sourceOutPort, SDFAttributeList schema);
	public void clearPhysicalSubscriptions();
	public Subscription<ISource<?>> getPhysSubscriptionTo(int port);
	public Collection<Subscription<ISource<?>>> getPhysSubscriptionsTo();
	// Currently needed for Transformation --> we should get rid of this!
	public Collection<ISource<?>> getPhysInputPOs();
	public int getNumberOfInputs();

//	public Collection<LogicalSubscription> getSubscriptions(ILogicalOperator a);
	public Collection<LogicalSubscription> getSubscribedToSource(ILogicalOperator a);
}
