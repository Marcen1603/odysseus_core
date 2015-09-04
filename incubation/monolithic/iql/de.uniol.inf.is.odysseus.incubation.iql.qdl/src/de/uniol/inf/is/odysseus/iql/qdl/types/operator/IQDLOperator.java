package de.uniol.inf.is.odysseus.iql.qdl.types.operator;

import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.iql.qdl.types.IQLSubscription;


public interface IQDLOperator {

	public String getName();
	
	public void subscribeSink(IQDLOperator sink, int sourceOutPort);
	public void subscribeToSource(IQDLOperator source, int sourceOutPort);
		
	public void subscribeSink(IQDLOperator sink);
	public void subscribeToSource(IQDLOperator source);

	Map<String, Object> getParameters();
	
	public boolean isSource();

	Set<IQLSubscription> getSubscriptions();
	Set<IQLSubscription> getSubscriptionsToSource();

}
