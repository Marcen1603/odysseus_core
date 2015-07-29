package de.uniol.inf.is.odysseus.iql.qdl.types.impl.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.QDLSubscribableWithPort;


public abstract class AbstractQDLQuery implements IQDLQuery {
	private final int DEFAULT_SOURCE_OUT_PORT = 0;
	
	private List<IPair<String, Object>> metadata = new ArrayList<>();
	
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
	public List<IPair<String, Object>> getMetadata() {
		return metadata;
	}

	@Override
	public void addMetadata(String key, Object value) {
		metadata.add(new Pair<String, Object>(key, value));
	}
	
	
	protected DefaultQDLSource<StreamAO> getSource(String name) {
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
		DefaultQDLSource<StreamAO> result = new DefaultQDLSource<>(streamAO);
		return result;
	}
	
	public List<IQDLOperator<?>> subscribeToSource(Collection<?> sinks, Collection<?> sources) {
		List<IQDLOperator<?>> result = new ArrayList<>();
		for (Object sink : sinks) {			
			for (Object source : sources) {
				if (sink instanceof Collection && source instanceof Collection) {
					result.addAll(subscribeToSource((Collection<?>)sink, (Collection<?>)source));
				} else if (sink instanceof Collection) {
					result.add(subscribeToSource((Collection<?>)sink, source));
				} else if (source instanceof Collection) {
					result.addAll(subscribeToSource((IQDLOperator<?>)sink, (Collection<?>)source));
				} else {
					result.add(subscribeToSource((IQDLOperator<?>)sink, source));
				}
			}
		}
		return result;
	}
	
	
	public List<IQDLOperator<?>> subscribeToSource(IQDLOperator<?> sink, Collection<?> sources) {
		List<IQDLOperator<?>> result = new ArrayList<>();
		for (Object source : sources) {
			if (source instanceof Collection) {
				result.addAll(subscribeToSource(sink, (Collection<?>)source));
			} else {
				result.add(subscribeToSource(sink, source));
			} 			
		}
		return result;
	}
	
	public IQDLOperator<?> subscribeToSource(Collection<?> sinks, Object source) {
		for (Object sink : sinks) {
			if (sink instanceof Collection) {
				subscribeToSource((Collection<?>) sink, source);
			} else  {
				subscribeToSource((IQDLOperator<?>)sink, source);
			} 			
		}
		if (source instanceof QDLSubscribableWithPort) {
			return ((QDLSubscribableWithPort)source).getOp();
		} else if (source instanceof IQDLOperator) {
			return ((IQDLOperator<?>)source);
		} else {
			return null;
		}
	}
	
	public IQDLOperator<?> subscribeToSource(IQDLOperator<?> sink, Object sourceObj) {
		IQDLOperator<?> source = null;
		int sourceOutPort = DEFAULT_SOURCE_OUT_PORT;

		if (source instanceof QDLSubscribableWithPort) {
			source = ((QDLSubscribableWithPort)source).getOp();
			sourceOutPort = ((QDLSubscribableWithPort)sourceObj).getPort();
		} else if (sourceObj instanceof IQDLOperator) {
			source = ((IQDLOperator<?>)sourceObj);
		}
		sink.subscribeToSource(source, sink.getLogicalOperator().getSubscribedToSource().size(), sourceOutPort);
		return source;
	}
	
	public List<IQDLOperator<?>> subscribeSink(Collection<?> sources, Collection<?> sinks) {
		List<IQDLOperator<?>> result = new ArrayList<>();
		for (Object source : sources) {			
			for (Object sink : sinks) {
				if (source instanceof Collection && sink instanceof Collection) {
					result.addAll(subscribeSink((Collection<?>)source, (Collection<?>)sink));
				} else if (source instanceof Collection) {
					result.add(subscribeSink((Collection<?>)source, (IQDLOperator<?>)sink));
				} else if (sink instanceof Collection) {
					result.addAll(subscribeSink(source, (Collection<?>)sink));
				} else {
					result.add(subscribeSink(source,  (IQDLOperator<?>)sink));
				}
			}
		}
		return result;
	}
	
	
	public IQDLOperator<?> subscribeSink(Collection<?> sources, IQDLOperator<?> sink) {
		for (Object obj : sources) {
			if (obj instanceof Collection) {
				subscribeSink((Collection<?>)obj, sink);
			} else {
				subscribeSink(obj, sink);
			} 			
		}
		return sink;
	}
	
	public List<IQDLOperator<?>> subscribeSink(Object source, Collection<?> sinks) {
		List<IQDLOperator<?>> result = new ArrayList<>();
		for (Object sink : sinks) {
			if (sink instanceof Collection) {
				result.addAll(subscribeSink(source, (Collection<?>) sink));
			} else  {
				result.add(subscribeSink(source, (IQDLOperator<?>) sink));
			} 			
		}
		return result;
	}
	
	public IQDLOperator<?> subscribeSink(Object sourceObj, IQDLOperator<?> sink) {
		IQDLOperator<?> source = null;
		int sourceOutPort = DEFAULT_SOURCE_OUT_PORT;
		
		if (sourceObj instanceof QDLSubscribableWithPort) {
			source = ((QDLSubscribableWithPort)sourceObj).getOp();
			sourceOutPort = ((QDLSubscribableWithPort)sourceObj).getPort();
		} else if (sourceObj instanceof IQDLOperator) {
			source = ((IQDLOperator<?>)sourceObj);
		}
		source.subscribeSink(sink, sink.getLogicalOperator().getSubscriptions().size(), sourceOutPort);

		return sink;
	}

}
