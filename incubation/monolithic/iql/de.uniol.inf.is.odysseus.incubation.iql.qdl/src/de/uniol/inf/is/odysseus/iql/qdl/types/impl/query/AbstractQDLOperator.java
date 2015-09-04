package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.iql.qdl.types.IQLSubscription;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;

public abstract class AbstractQDLOperator implements IQDLOperator {
	private String name;
	private Map<String, Object> parameters = new HashMap<String, Object>();
	private Set<IQLSubscription> subscriptions = new HashSet<>();
	private Set<IQLSubscription> subscriptionsToSource = new HashSet<>();

	public AbstractQDLOperator(String name) {
		this.name = name;
	}	

	public AbstractQDLOperator(String name, IQDLOperator source) {
		this.name = name;
		source.subscribeSink(this);		
	}
	
	public AbstractQDLOperator(String name, IQDLOperator source1, IQDLOperator source2) {
		this.name = name;
		source1.subscribeSink(this);	
		source2.subscribeSink(this);
	}
	
	@Override
	public Set<IQLSubscription> getSubscriptions() {
		return subscriptions;
	}
	
	@Override
	public Set<IQLSubscription> getSubscriptionsToSource() {
		return subscriptionsToSource;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setParameter(String name, Object parameter) {
		parameters.put(name.toLowerCase(),  parameter);
	}
	
	public Object getParameter(String name) {
		return parameters.get(name.toLowerCase());
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	@Override
	public void subscribeSink(IQDLOperator sink, int sourceOutPort) {
		IQLSubscription subs = new IQLSubscription(this, sink, sourceOutPort, sink.getSubscriptionsToSource().size());
		subscriptions.add(subs);
		sink.getSubscriptionsToSource().add(subs);
	}

	@Override
	public void subscribeToSource(IQDLOperator source, int sourceOutPort) {
		source.subscribeSink(this, sourceOutPort);
	}
	
	@Override
	public void subscribeSink(IQDLOperator sink) {
		subscribeSink(sink, 0);
	}

	@Override
	public void subscribeToSource(IQDLOperator source) {
		source.subscribeSink(this);
	}
	
	@Override
	public boolean isSource() {
		return false;
	}
	
}
