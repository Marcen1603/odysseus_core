package de.uniol.inf.is.odysseus.iql.qdl.executor;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.Operator;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQueryExecutor;
import de.uniol.inf.is.odysseus.iql.qdl.types.source.Source;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscribable;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscriber;
import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscription;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class OdysseusScriptGenerator {
	private static final String QUERY_COMMAND = "queryCmd";

	public List<ILogicalQuery> createLogicalQueries(IQDLQuery query,IDataDictionary dd, ISession session) {		
		QDLQueryExecutor executor = new QDLQueryExecutor(dd, session);
		query.setExecutor(executor);
		Collection<Operator> operators = query.execute();
		List<ILogicalQuery> result = new ArrayList<>();
		result.addAll(executor.getQueries());
		if (isAutoCreate(query)) {
			Collection<Operator> topOps = getTopOperators(operators);
			ILogicalQuery logQuery = createLogicalQuery(topOps, dd, session);
			logQuery.setName(new Resource(session.getUser().getName(),query.getName()));
			result.add(logQuery);
		}
		return result;
	}
	
	private boolean isAutoCreate(IQDLQuery query) {
		for (IPair<String, Object> pair: query.getMetadata()) {
			if (pair.getE1().equalsIgnoreCase(IQDLLookUp.AUTO_CREATE)) {
				return (boolean) pair.getE2();
			}
		}
		return true;
 	}
	
	public ILogicalQuery createLogicalQuery(Operator topOp, IDataDictionary dd, ISession session) {
		Collection<Operator> topOps = new HashSet<>();
		topOps.add(topOp);
		return createLogicalQuery(topOps, dd, session);
	}
	
	public ILogicalQuery createLogicalQuery(Collection<Operator> topOps, IDataDictionary dd, ISession session) {	
		Map<Operator, ILogicalOperator> operatorsMap = new HashMap<>();
		Map<String, IOperatorBuilder> operatorBuildersMap = new HashMap<>();
		Map<Source, ILogicalOperator> sourcesMap = new HashMap<>();
		
		Collection<Source> sources = getSources(topOps);		
		for (Source source : sources) {
			sourcesMap.put(source, getSource(source.getName(), dd , session));
		}
		for (IOperatorBuilder builder : QDLServiceBinding.getOperatorBuilderFactory().getOperatorBuilder()) {
			operatorBuildersMap.put(builder.getName().toLowerCase(), builder);
		}
		
		Collection<Operator> visitedOperators = new HashSet<>();
		for (Operator topOp : topOps) {
			createLogicalGraph(topOp, operatorsMap, operatorBuildersMap, sourcesMap, visitedOperators, dd , session);
		}
		
		TopAO topAO = createTopAO(topOps, operatorsMap);
		
		ILogicalQuery result = new LogicalQuery();
		result.setLogicalPlan(topAO, true);
		
		return result;
	}

	public String createOdysseusScript(IQDLQuery query, IDataDictionary dd, ISession session) {
		List<ILogicalQuery> queries = createLogicalQueries(query, dd, session);
		if (queries.size() == 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("#PARSER "+"PQL"+System.lineSeparator());
		List<String> singleMetadata = new ArrayList<>();
		String queryCmd = null;
		for (IPair<String, Object> metadata : query.getMetadata()) {
			if (isPreParserKeyword(metadata.getE1())) {
				if (metadata.getE1().equalsIgnoreCase("query")) {
					if (metadata.getE2() == null || metadata.getE2() == Boolean.TRUE) {
						queryCmd = "#QUERY";
					}
				} else if (metadata.getE1().equalsIgnoreCase("addquery")) {
					if (metadata.getE2() == null || metadata.getE2() == Boolean.TRUE) {
						queryCmd = "#ADDQUERY";
					}
				} else if (metadata.getE1().equalsIgnoreCase("runquery")) {
					if (metadata.getE2() == null || metadata.getE2() == Boolean.TRUE) {
						queryCmd = "#RUNQUERY";
					}
				} else if (metadata.getE2() != null){
					builder.append("#"+metadata.getE1().toUpperCase() +" "+getPreParserKeywordValue(metadata.getE2())+System.lineSeparator());
				} else {
					singleMetadata.add("#"+metadata.getE1().toUpperCase() +System.lineSeparator());
				}				
			}
		}
		for (ILogicalQuery logQuery : queries) {
			if (logQuery.getName() != null) {
				builder.append("#QNAME "+logQuery.getName()+System.lineSeparator());
			}
			for (String metadata : singleMetadata) {
				builder.append(metadata);
			}
			Object queryCommand = logQuery.getParameter(QUERY_COMMAND);
			if (queryCommand != null) {
				builder.append(queryCommand+System.lineSeparator());	
			} else if(queryCmd != null) {
				builder.append(queryCmd+System.lineSeparator());	
			} else {
				builder.append("#ADDQUERY"+System.lineSeparator());	
			}
			builder.append(QDLServiceBinding.getPQLGenerator().generatePQLStatement(logQuery.getLogicalPlan()));
		}	
		return builder.toString();
	}
	
	private boolean isPreParserKeyword(String key) {
		for (IPreParserKeywordProvider provider : QDLServiceBinding.getPreParserKeywordProviders()) {
			for (Entry<String, Class<? extends IPreParserKeyword>> entry : provider.getKeywords().entrySet()) {
				if (entry.getKey().equalsIgnoreCase(key)) {
					return true;
				}
			}
		}
		return false;
	}

	private ILogicalOperator getSource(String name, IDataDictionary dd, ISession session) {
		String sourceName = name.toLowerCase();
		SourceParameter source = new SourceParameter();
		source.setName("Source");
		source.setInputValue(sourceName);
		source.setDataDictionary(dd);		
		source.setCaller(session);
		
		AccessAO accessAO = source.getValue();
		StreamAO streamAO  = new StreamAO();
		streamAO.setSource(accessAO);
		streamAO.setName(sourceName);
		return streamAO;
	}

	
	private Collection<Operator> getTopOperators(Collection<Operator> operators) {
		Collection<Operator> result = new HashSet<>();
		for (Operator op : operators) {
			if (op instanceof Subscribable) {
				Subscribable subscribable = (Subscribable) op;
				if (subscribable.getSubscriptions().size() == 0) {
					result.add(op);
				}
			}			
		}
		return result;
	}
	
	private void createLogicalGraph(Operator operator, Map<Operator, ILogicalOperator> operatorsMap, Map<String, IOperatorBuilder> operatorBuildersMap, Map<Source, ILogicalOperator> sourcesMap, Collection<Operator> visitedOperators, IDataDictionary dd, ISession session) {
		if (!visitedOperators.contains(operator)) {
			visitedOperators.add(operator);
			if (operator instanceof Subscriber) {
				Subscriber subscriber = (Subscriber) operator;
				for (Subscription subs : subscriber.getSubscriptionsToSource()) {
					Subscribable source = subs.getSource();
					if (source instanceof Operator) {
						Operator sourceOperator = (Operator) source;
						createLogicalGraph(sourceOperator, operatorsMap,operatorBuildersMap, sourcesMap, visitedOperators, dd, session);
					} 
				}
				createLogicalOperator(operator, operatorsMap,operatorBuildersMap, sourcesMap, visitedOperators, dd, session);
			}			
		}
	}
	
	private void createLogicalOperator(Operator operator, Map<Operator, ILogicalOperator> operatorsMap, Map<String, IOperatorBuilder> operatorBuildersMap, Map<Source, ILogicalOperator> sourcesMap, Collection<Operator> visitedOperators, IDataDictionary dd, ISession session) {
		IOperatorBuilder builder = operatorBuildersMap.get(operator.getName().toLowerCase()).cleanCopy();
		builder.setCaller(session);
		builder.setDataDictionary(dd);
		if (operator instanceof Subscriber) {
			Subscriber subscriber = (Subscriber) operator;
			for (Subscription subs : subscriber.getSubscriptionsToSource()) {
				Subscribable source = subs.getSource();
				if (source instanceof Operator) {
					ILogicalOperator sourceOp = operatorsMap.get(subs.getSource());
					builder.setInputOperator(subs.getSinkInPort(), sourceOp, subs.getSourceOutPort());				
				} else if (source instanceof Source) {
					ILogicalOperator sourceOp = sourcesMap.get(subs.getSource());
					builder.setInputOperator(subs.getSinkInPort(), sourceOp, subs.getSourceOutPort());				
				}
			}
		}
		setParameters(operator, builder, session, dd);
		ILogicalOperator op = null;
		try {
			op = builder.createOperator();
		}catch (Exception e) {
			e.printStackTrace();
		}
		operatorsMap.put(operator, op);
		
	}

	private void setParameters(Operator operator, IOperatorBuilder builder, ISession session, IDataDictionary dd) {		
		Map<String, Object> parameters = operator.getParameters();
		for (IParameter<?> parameter : builder.getParameters()) {
			String parameterName = parameter.getName();
			Object value = parameters.get(parameterName.toLowerCase());
			if (value instanceof Integer) {
				value = new Long((int)value);
			}
			parameter.setInputValue(parameters.get(parameterName.toLowerCase()));
		}
	}
		
	private TopAO createTopAO(Collection<Operator> topOps, Map<Operator, ILogicalOperator> operatorsMap) {
		TopAO topAO = new TopAO();
		int i = 0; 
		for (Operator op : topOps) {
			ILogicalOperator topOp = operatorsMap.get(op);
			topAO.subscribeToSource(topOp, i++, 0, topOp.getOutputSchema());
		}
		return topAO;
	}
	
		
	@SuppressWarnings("unchecked")
	private String getPreParserKeywordValue(Object obj) {
		if (obj instanceof String) {
			return "\""+obj.toString()+"\"";
		} else if (obj instanceof Character) {
			return "'"+obj.toString()+"'";
		} else if (obj instanceof List) {
			List<Object> list = (List<Object>) obj;
			StringBuilder b = new StringBuilder();
			for (Object element : list) {
				b.append(getPreParserKeywordValue(element)+" ");
			}
			return b.toString();
		} else if (obj instanceof Map) {
			Map<Object, Object> map = (Map<Object, Object>) obj;
			StringBuilder b = new StringBuilder();
			for (Entry<Object, Object> entry : map.entrySet()) {
				b.append(getPreParserKeywordValue(entry.getKey())+" "+getPreParserKeywordValue(entry.getValue()));
			}
			return b.toString();
		} else {
			return obj.toString();
		}
	}
	
	private Collection<Source> getSources(Collection<Operator> topOps) {
		Collection<Source> result = new HashSet<>();
		for (Operator topOp : topOps) {
			findSources(topOp, result);
		}
		return result;
	}
	
	private void findSources(Operator operator, Collection<Source> visitedSources) {
		if (operator instanceof Subscriber) {
			Subscriber subscriber = (Subscriber) operator;
			for (Subscription subs : subscriber.getSubscriptionsToSource()) {
				Subscribable source = subs.getSource();
				if (source instanceof Operator) {
					Operator sourceOp = (Operator) source;
					findSources(sourceOp, visitedSources);					
				}else if (source instanceof Source) {
					visitedSources.add((Source) source);
				}
			}
		}		
	}

	
	private class QDLQueryExecutor implements IQDLQueryExecutor {
		private List<ILogicalQuery> queries = new ArrayList<>();
		private IDataDictionary dd;
		private ISession session;

		public QDLQueryExecutor(IDataDictionary dd, ISession session) {
			this.dd = dd;
			this.session = session;
		}
		
		@Override
		public void create(Operator operator) {
			ILogicalQuery query = createLogicalQuery(operator, dd, session);
			query.setParameter(QUERY_COMMAND, "#ADDQUERY");
			queries.add(query);
		}

		@Override
		public void createWithMultipleSinks(List<Operator> operators) {
			ILogicalQuery query = createLogicalQuery(operators, dd, session);
			query.setParameter(QUERY_COMMAND, "#ADDQUERY");
			queries.add(query);
		}

		@Override
		public void start(Operator operator) {
			ILogicalQuery query = createLogicalQuery(operator, dd, session);
			query.setParameter(QUERY_COMMAND, "#RUNQUERY");
			queries.add(query);
		}

		@Override
		public void startWithMultipleSinks(List<Operator> operators) {
			ILogicalQuery query = createLogicalQuery(operators, dd, session);
			query.setParameter(QUERY_COMMAND, "#RUNQUERY");
			queries.add(query);
		}
		
		public List<ILogicalQuery> getQueries() {
			return this.queries;
		}

		@Override
		public void create(String name, Operator operator) {
			ILogicalQuery query = createLogicalQuery(operator, dd, session);
			query.setParameter(QUERY_COMMAND, "#ADDQUERY");
			query.setName(new Resource(session.getUser().getName(),name));
			queries.add(query);			
		}

		@Override
		public void createWithMultipleSinks(String name,List<Operator> operators) {
			ILogicalQuery query = createLogicalQuery(operators, dd, session);
			query.setParameter(QUERY_COMMAND, "#ADDQUERY");
			query.setName(new Resource(session.getUser().getName(),name));
			queries.add(query);			
		}

		@Override
		public void start(String name, Operator operator) {
			ILogicalQuery query = createLogicalQuery(operator, dd, session);
			query.setParameter(QUERY_COMMAND, "#RUNQUERY");
			query.setName(new Resource(session.getUser().getName(),name));
			queries.add(query);			
		}

		@Override
		public void startWithMultipleSinks(String name,	List<Operator> operators) {
			ILogicalQuery query = createLogicalQuery(operators, dd, session);
			query.setParameter(QUERY_COMMAND, "#RUNQUERY");
			query.setName(new Resource(session.getUser().getName(),name));
			queries.add(query);			
		}		
	}	

}
