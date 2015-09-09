package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQueryExecutor;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.QDLSubscribableWithPort;


public abstract class AbstractQDLQuery implements IQDLQuery {
	private final int DEFAULT_SOURCE_OUT_PORT = 0;
	
	private List<IPair<String, Object>> metadata = new ArrayList<>();
	
	private IQDLQueryExecutor executor;

		
	@Override
	public void setExecutor(IQDLQueryExecutor executor) {
		this.executor = executor;
	}
	
	public void create(IQDLOperator operator) {
		executor.create(operator);
	}	
	
	protected void create(List<IQDLOperator> operators) {
		executor.createWithMultipleSinks(operators);
	}
	
	protected void start(IQDLOperator operator) {
		executor.start(operator);
	}
	
	protected void start(List<IQDLOperator> operators) {
		executor.startWithMultipleSinks(operators);
	}
	
	public void create(String name,IQDLOperator operator) {
		executor.create(name, operator);
	}	
	
	protected void create(String name,List<IQDLOperator> operators) {
		executor.createWithMultipleSinks(name, operators);
	}
	
	protected void start(String name,IQDLOperator operator) {
		executor.start(name, operator);
	}
	
	protected void start(String name, List<IQDLOperator> operators) {
		executor.startWithMultipleSinks(name, operators);
	}
	
	
	@Override
	public List<IPair<String, Object>> getMetadata() {
		return metadata;
	}

	protected void addMetadata(String key, Object value) {
		metadata.add(new Pair<String, Object>(key, value));
	}
		

	protected List<IQDLOperator> subscribeToSource(Collection<?> sinks, Collection<?> sources) {
		List<IQDLOperator> result = new ArrayList<>();
		for (Object sink : sinks) {			
			for (Object source : sources) {
				if (sink instanceof Collection && source instanceof Collection) {
					result.addAll(subscribeToSource((Collection<?>)sink, (Collection<?>)source));
				} else if (sink instanceof Collection) {
					result.add(subscribeToSource((Collection<?>)sink, source));
				} else if (source instanceof Collection) {
					result.addAll(subscribeToSource((IQDLOperator)sink, (Collection<?>)source));
				} else {
					result.add(subscribeToSource((IQDLOperator)sink, source));
				}
			}
		}
		return result;
	}
	
	
	protected List<IQDLOperator> subscribeToSource(IQDLOperator sink, Collection<?> sources) {
		List<IQDLOperator> result = new ArrayList<>();
		for (Object source : sources) {
			if (source instanceof Collection) {
				result.addAll(subscribeToSource(sink, (Collection<?>)source));
			} else {
				result.add(subscribeToSource(sink, source));
			} 			
		}
		return result;
	}
	
	protected IQDLOperator subscribeToSource(Collection<?> sinks, Object source) {
		for (Object sink : sinks) {
			if (sink instanceof Collection) {
				subscribeToSource((Collection<?>) sink, source);
			} else  {
				subscribeToSource((IQDLOperator)sink, source);
			} 			
		}
		if (source instanceof QDLSubscribableWithPort) {
			return ((QDLSubscribableWithPort)source).getOp();
		} else if (source instanceof IQDLOperator) {
			return ((IQDLOperator)source);
		} else {
			return null;
		}
	}
	
	protected IQDLOperator subscribeToSource(IQDLOperator sink, Object sourceObj) {
		IQDLOperator source = null;
		int sourceOutPort = DEFAULT_SOURCE_OUT_PORT;

		if (sourceObj instanceof QDLSubscribableWithPort) {
			source = ((QDLSubscribableWithPort)sourceObj).getOp();
			sourceOutPort = ((QDLSubscribableWithPort)sourceObj).getPort();
		} else if (sourceObj instanceof IQDLOperator) {
			source = ((IQDLOperator)sourceObj);
		}
		sink.subscribeToSource(source, sourceOutPort);
		return source;
	}
	
	protected List<IQDLOperator> subscribeSink(Collection<?> sources, Collection<?> sinks) {
		List<IQDLOperator> result = new ArrayList<>();
		for (Object source : sources) {			
			for (Object sink : sinks) {
				if (source instanceof Collection && sink instanceof Collection) {
					result.addAll(subscribeSink((Collection<?>)source, (Collection<?>)sink));
				} else if (source instanceof Collection) {
					result.add(subscribeSink((Collection<?>)source, (IQDLOperator)sink));
				} else if (sink instanceof Collection) {
					result.addAll(subscribeSink(source, (Collection<?>)sink));
				} else {
					result.add(subscribeSink(source,  (IQDLOperator)sink));
				}
			}
		}
		return result;
	}
	
	
	protected IQDLOperator subscribeSink(Collection<?> sources, IQDLOperator sink) {
		for (Object obj : sources) {
			if (obj instanceof Collection) {
				subscribeSink((Collection<?>)obj, sink);
			} else {
				subscribeSink(obj, sink);
			} 			
		}
		return sink;
	}
	
	protected List<IQDLOperator> subscribeSink(Object source, Collection<?> sinks) {
		List<IQDLOperator> result = new ArrayList<>();
		for (Object sink : sinks) {
			if (sink instanceof Collection) {
				result.addAll(subscribeSink(source, (Collection<?>) sink));
			} else  {
				result.add(subscribeSink(source, (IQDLOperator) sink));
			} 			
		}
		return result;
	}
	
	protected IQDLOperator subscribeSink(Object sourceObj, IQDLOperator sink) {
		IQDLOperator source = null;
		int sourceOutPort = DEFAULT_SOURCE_OUT_PORT;
		
		if (sourceObj instanceof QDLSubscribableWithPort) {
			source = ((QDLSubscribableWithPort)sourceObj).getOp();
			sourceOutPort = ((QDLSubscribableWithPort)sourceObj).getPort();
		} else if (sourceObj instanceof IQDLOperator) {
			source = ((IQDLOperator)sourceObj);
		}
		source.subscribeSink(sink, sourceOutPort);

		return sink;
	}

}
