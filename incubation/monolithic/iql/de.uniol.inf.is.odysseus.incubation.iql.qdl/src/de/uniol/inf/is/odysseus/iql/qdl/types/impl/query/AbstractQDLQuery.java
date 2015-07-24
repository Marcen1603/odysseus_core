package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.QDLSubscribableWithPort;


public abstract class AbstractQDLQuery implements IQDLQuery {
	private final int DEFAULT_SOURCE_OUT_PORT = 0;
	
	private Map<String, List<Object>> metadata = new HashMap<>();
	
	private Map<String, StreamAO> sources = new HashMap<>();

	private ISession session;
	
	private IDataDictionary dataDictionary;

	@Override
	public void setSession(ISession session) {
		this.session = session;
	}
	
	@Override
	public void setDataDictionary(IDataDictionary dataDictionary) {
		this.dataDictionary = dataDictionary;
	}

	@Override
	public Map<String, List<Object>> getMetadata() {
		return metadata;
	}

	@Override
	public void addMetadata(String key, Object value) {
		List<Object> valueList = metadata.get(key);
		if (valueList == null) {
			valueList = new ArrayList<>();
		}
		valueList.add(value);
	}
	
	
	protected StreamAO getSource(String name) {
		String sourceName = name.toLowerCase();
		StreamAO streamAO = sources.get(sourceName);
		if (streamAO == null) {
			SourceParameter source = new SourceParameter();
			source.setName("Source");
			source.setInputValue(sourceName);
			source.setDataDictionary(dataDictionary);		
			source.setCaller(session);
			
			AccessAO accessAO = source.getValue();
			streamAO = new StreamAO();
			streamAO.setSource(accessAO);
			streamAO.setName(sourceName);
			
			sources.put(name, streamAO);
		}		
		return streamAO;
	}
	
	public List<ILogicalOperator> subscribeToSource(Collection<?> sinks, Collection<?> sources) {
		List<ILogicalOperator> result = new ArrayList<>();
		for (Object sink : sinks) {			
			for (Object source : sources) {
				if (sink instanceof Collection && source instanceof Collection) {
					result.addAll(subscribeToSource((Collection<?>)sink, (Collection<?>)source));
				} else if (sink instanceof Collection) {
					result.add(subscribeToSource((Collection<?>)sink, source));
				} else if (source instanceof Collection) {
					result.addAll(subscribeToSource((ILogicalOperator)sink, (Collection<?>)source));
				} else {
					result.add(subscribeToSource((ILogicalOperator)sink, source));
				}
			}
		}
		return result;
	}
	
	
	public List<ILogicalOperator> subscribeToSource(ILogicalOperator sink, Collection<?> sources) {
		List<ILogicalOperator> result = new ArrayList<>();
		for (Object source : sources) {
			if (source instanceof Collection) {
				result.addAll(subscribeToSource(sink, (Collection<?>)source));
			} else {
				result.add(subscribeToSource(sink, source));
			} 			
		}
		return result;
	}
	
	public ILogicalOperator subscribeToSource(Collection<?> sinks, Object source) {
		for (Object sink : sinks) {
			if (sink instanceof Collection) {
				subscribeToSource((Collection<?>) sink, source);
			} else  {
				subscribeToSource((ILogicalOperator)sink, source);
			} 			
		}
		if (source instanceof QDLSubscribableWithPort) {
			return ((QDLSubscribableWithPort)source).getOp();
		} else if (source instanceof ILogicalOperator) {
			return ((ILogicalOperator)source);
		} else {
			return null;
		}
	}
	
	public ILogicalOperator subscribeToSource(ILogicalOperator sink, Object sourceObj) {
		ILogicalOperator source = null;
		int sourceOutPort = DEFAULT_SOURCE_OUT_PORT;

		if (source instanceof QDLSubscribableWithPort) {
			source = ((QDLSubscribableWithPort)source).getOp();
			sourceOutPort = ((QDLSubscribableWithPort)sourceObj).getPort();
		} else if (sourceObj instanceof ILogicalOperator) {
			source = ((ILogicalOperator)sourceObj);
		}
		sink.subscribeToSource(source, sink.getSubscribedToSource().size(), sourceOutPort, source.getOutputSchema(sourceOutPort));
		return source;
	}
	
	public List<ILogicalOperator> subscribeSink(Collection<?> sources, Collection<?> sinks) {
		List<ILogicalOperator> result = new ArrayList<>();
		for (Object source : sources) {			
			for (Object sink : sinks) {
				if (source instanceof Collection && sink instanceof Collection) {
					result.addAll(subscribeSink((Collection<?>)source, (Collection<?>)sink));
				} else if (source instanceof Collection) {
					result.add(subscribeSink((Collection<?>)source, (ILogicalOperator)sink));
				} else if (sink instanceof Collection) {
					result.addAll(subscribeSink(source, (Collection<?>)sink));
				} else {
					result.add(subscribeSink(source,  (ILogicalOperator)sink));
				}
			}
		}
		return result;
	}
	
	
	public ILogicalOperator subscribeSink(Collection<?> sources, ILogicalOperator sink) {
		for (Object obj : sources) {
			if (obj instanceof Collection) {
				subscribeSink((Collection<?>)obj, sink);
			} else {
				subscribeSink(obj, sink);
			} 			
		}
		return sink;
	}
	
	public List<ILogicalOperator> subscribeSink(Object source, Collection<?> sinks) {
		List<ILogicalOperator> result = new ArrayList<>();
		for (Object sink : sinks) {
			if (sink instanceof Collection) {
				result.addAll(subscribeSink(source, (Collection<?>) sink));
			} else  {
				result.add(subscribeSink(source, (ILogicalOperator) sink));
			} 			
		}
		return result;
	}
	
	public ILogicalOperator subscribeSink(Object sourceObj, ILogicalOperator sink) {
		ILogicalOperator source = null;
		int sourceOutPort = DEFAULT_SOURCE_OUT_PORT;
		
		if (sourceObj instanceof QDLSubscribableWithPort) {
			source = ((QDLSubscribableWithPort)sourceObj).getOp();
			sourceOutPort = ((QDLSubscribableWithPort)sourceObj).getPort();
		} else if (sourceObj instanceof ILogicalOperator) {
			source = ((ILogicalOperator)sourceObj);
		}
		source.subscribeSink(sink, sink.getSubscribedToSource().size(), sourceOutPort, source.getOutputSchema(sourceOutPort));
		return sink;
	}

}
