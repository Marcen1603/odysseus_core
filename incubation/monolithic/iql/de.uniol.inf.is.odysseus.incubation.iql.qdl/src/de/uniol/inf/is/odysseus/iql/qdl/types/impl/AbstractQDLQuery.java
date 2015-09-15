package de.uniol.inf.is.odysseus.iql.qdl.types.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.Operator;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQueryExecutor;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.QDLSubscribableWithPort;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscribable;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscriber;


public abstract class AbstractQDLQuery implements IQDLQuery {
	private final int DEFAULT_SOURCE_OUT_PORT = 0;
	
	private List<IPair<String, Object>> metadata = new ArrayList<>();
	
	private IQDLQueryExecutor executor;

		
	@Override
	public void setExecutor(IQDLQueryExecutor executor) {
		this.executor = executor;
	}
	
	public void create(Operator operator) {
		executor.create(operator);
	}	
	
	protected void create(List<Operator> operators) {
		executor.createWithMultipleSinks(operators);
	}
	
	protected void start(Operator operator) {
		executor.start(operator);
	}
	
	protected void start(List<Operator> operators) {
		executor.startWithMultipleSinks(operators);
	}
	
	public void create(String name,Operator operator) {
		executor.create(name, operator);
	}	
	
	protected void create(String name,List<Operator> operators) {
		executor.createWithMultipleSinks(name, operators);
	}
	
	protected void start(String name,Operator operator) {
		executor.start(name, operator);
	}
	
	protected void start(String name, List<Operator> operators) {
		executor.startWithMultipleSinks(name, operators);
	}
	
	
	@Override
	public List<IPair<String, Object>> getMetadata() {
		return metadata;
	}

	protected void addMetadata(String key, Object value) {
		metadata.add(new Pair<String, Object>(key, value));
	}
		

	protected List<Subscribable> subscribeToSource(Collection<?> sinks, Collection<?> sources) {
		List<Subscribable> result = new ArrayList<>();
		for (Object sink : sinks) {			
			for (Object source : sources) {
				if (sink instanceof Collection && source instanceof Collection) {
					result.addAll(subscribeToSource((Collection<?>)sink, (Collection<?>)source));
				} else if (sink instanceof Collection) {
					result.add(subscribeToSource((Collection<?>)sink, source));
				} else if (source instanceof Collection) {
					result.addAll(subscribeToSource((Subscriber)sink, (Collection<?>)source));
				} else {
					result.add(subscribeToSource(sink, source));
				}
			}
		}
		return result;
	}
	
	
	protected List<Subscribable> subscribeToSource(Subscriber sink, Collection<?> sources) {
		List<Subscribable> result = new ArrayList<>();
		for (Object source : sources) {
			if (source instanceof Collection) {
				result.addAll(subscribeToSource(sink, (Collection<?>)source));
			} else {
				result.add(subscribeToSource(sink, source));
			} 			
		}
		return result;
	}
	
	protected Subscribable subscribeToSource(Collection<?> sinks, Object source) {
		for (Object sink : sinks) {
			if (sink instanceof Collection) {
				subscribeToSource((Collection<?>) sink, source);
			} else  {
				subscribeToSource(sink, source);
			} 			
		}
		if (source instanceof QDLSubscribableWithPort) {
			return ((QDLSubscribableWithPort)source).getSource();
		} else if (source instanceof Subscribable) {
			return ((Subscribable)source);
		} else {
			return null;
		}
	}
	
	protected Subscribable subscribeToSource(Object sink, Object sourceObj) {
		Subscribable source = null;
		int sourceOutPort = DEFAULT_SOURCE_OUT_PORT;

		if (sourceObj instanceof QDLSubscribableWithPort) {
			source = ((QDLSubscribableWithPort)sourceObj).getSource();
			sourceOutPort = ((QDLSubscribableWithPort)sourceObj).getPort();
		} else if (sourceObj instanceof Subscribable) {
			source = ((Subscribable)sourceObj);
		}
		Subscriber subscriber = (Subscriber) sink;
		subscriber.subscribeToSource(source, sourceOutPort, subscriber.getSubscriptionsToSource().size());
		return source;
	}
	
	protected List<Subscriber> subscribeSink(Collection<?> sources, Collection<?> sinks) {
		List<Subscriber> result = new ArrayList<>();
		for (Object source : sources) {			
			for (Object sink : sinks) {
				if (source instanceof Collection && sink instanceof Collection) {
					result.addAll(subscribeSink((Collection<?>)source, (Collection<?>)sink));
				} else if (source instanceof Collection) {
					result.add(subscribeSink((Collection<?>)source, (Subscriber)sink));
				} else if (sink instanceof Collection) {
					result.addAll(subscribeSink(source, (Collection<?>)sink));
				} else {
					result.add(subscribeSink(source,  sink));
				}
			}
		}
		return result;
	}
	
	
	protected Subscriber subscribeSink(Collection<?> sources, Object sink) {
		for (Object obj : sources) {
			if (obj instanceof Collection) {
				subscribeSink((Collection<?>)obj, sink);
			} else {
				subscribeSink(obj, sink);
			} 			
		}
		return (Subscriber) sink;
	}
	
	protected List<Subscriber> subscribeSink(Object source, Collection<?> sinks) {
		List<Subscriber> result = new ArrayList<>();
		for (Object sink : sinks) {
			if (sink instanceof Collection) {
				result.addAll(subscribeSink(source, (Collection<?>) sink));
			} else  {
				result.add(subscribeSink(source, sink));
			} 			
		}
		return result;
	}
	
	protected Subscriber subscribeSink(Object sourceObj, Object sink) {
		Subscribable source = null;
		int sourceOutPort = DEFAULT_SOURCE_OUT_PORT;
		
		if (sourceObj instanceof QDLSubscribableWithPort) {
			source = ((QDLSubscribableWithPort)sourceObj).getSource();
			sourceOutPort = ((QDLSubscribableWithPort)sourceObj).getPort();
		} else if (sourceObj instanceof Subscribable) {
			source = ((Subscribable)sourceObj);
		}
		Subscriber subscriber = (Subscriber) sink;
		source.subscribeSink(subscriber, sourceOutPort, subscriber.getSubscriptionsToSource().size());

		return subscriber;
	}

}
