package de.uniol.inf.is.odysseus.iql.qdl.executor;



import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.IQLSubscription;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQueryExecutor;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

public class OdysseusScriptGenerator {
	private static final String QUERY_COMMAND = "queryCmd";
	
	@Inject
	private IQDLTypeDictionary typeDictionary;

	
	public List<ILogicalQuery> createLogicalQueries(IQDLQuery query,IDataDictionary dd, ISession session) {
		QDLQueryExecutor executor = new QDLQueryExecutor(dd, session);
		query.setExecutor(executor);
		Collection<IQDLOperator> operators = query.execute();
		List<ILogicalQuery> result = new ArrayList<>();
		result.addAll(executor.getQueries());
		if (isAutoCreate(query)) {
			Collection<IQDLOperator> roots = getRoots(operators);
			ILogicalQuery logQuery = createLogicalQuery(roots, dd, session);
			logQuery.setName(query.getName());
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
	
	public ILogicalQuery createLogicalQuery(IQDLOperator root, IDataDictionary dd, ISession session) {
		Collection<IQDLOperator> roots = new HashSet<>();
		roots.add(root);
		return createLogicalQuery(roots, dd, session);
	}
	
	public ILogicalQuery createLogicalQuery(Collection<IQDLOperator> roots, IDataDictionary dd, ISession session) {	
		Map<IQDLOperator, ILogicalOperator> operatorsMap = new HashMap<>();
		Map<String, StreamAO> sourcesMap = new HashMap<>();
		
		Collection<IQDLOperator> operators = getOperators(roots);
		
		for (IQDLOperator operator : operators) {
			if (operator.isSource()) {
				operatorsMap.put(operator, getSource(operator.getName(), sourcesMap, dd , session));
			} else {
				operatorsMap.put(operator, QDLServiceBinding.createOperator(operator.getName()));
			}
		}
		
		Collection<IQDLOperator> visitedOperators = new HashSet<>();
		for (IQDLOperator root : roots) {
			createLogicalGraph(root, operatorsMap,visitedOperators, dd , session);
		}
		
		TopAO topAO = createTopAO(roots, operatorsMap);
		
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
				if (metadata.getE2() != null) {
					builder.append("#"+metadata.getE1().toUpperCase() +" "+getPreParserKeywordValue(metadata.getE2())+System.lineSeparator());
				} else {
					if (metadata.getE1().equalsIgnoreCase("query")) {
						queryCmd = "query";
					} else if (metadata.getE1().equalsIgnoreCase("addquery")) {
						queryCmd = "addquery";
					} else if (metadata.getE1().equalsIgnoreCase("runquery")) {
						queryCmd = "runquery";
					} else {
						singleMetadata.add("#"+metadata.getE1().toUpperCase() +System.lineSeparator());
					}
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
		System.out.println(builder.toString());
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

	private ILogicalOperator getSource(String name, Map<String, StreamAO> sourcesMap, IDataDictionary dd, ISession session) {
		String sourceName = name.toLowerCase();
		StreamAO streamAO = sourcesMap.get(sourceName);
		if (streamAO == null) {
			SourceParameter source = new SourceParameter();
			source.setName("Source");
			source.setInputValue(sourceName);
			source.setDataDictionary(dd);		
			source.setCaller(session);
			
			AccessAO accessAO = source.getValue();
			streamAO = new StreamAO();
			streamAO.setSource(accessAO);
			streamAO.setName(sourceName);
			
			sourcesMap.put(name, streamAO);
		}	
		return streamAO;
	}

	
	private Collection<IQDLOperator> getRoots(Collection<IQDLOperator> operators) {
		Collection<IQDLOperator> roots = new HashSet<>();
		for (IQDLOperator op : operators) {
			if (!op.isSource() && op.getSubscriptions().size() == 0) {
				roots.add(op);
			}
		}
		return roots;
	}
	
	private void createLogicalGraph(IQDLOperator root, Map<IQDLOperator, ILogicalOperator> operatorsMap, Collection<IQDLOperator> visitedOperators, IDataDictionary dd, ISession session) {
		if (!visitedOperators.contains(root)) {
			visitedOperators.add(root);
			for (IQLSubscription subs : root.getSubscriptionsToSource()) {
				createLogicalGraph(subs.getSource(), operatorsMap, visitedOperators, dd, session);
				ILogicalOperator sourceOp = operatorsMap.get(subs.getSource());
				ILogicalOperator targetOp = operatorsMap.get(subs.getTarget());
				targetOp.subscribeToSource(sourceOp, subs.getSinkInPort(), subs.getSourceOutPort(),  sourceOp.getOutputSchema(subs.getSourceOutPort()));
			}
			if (!root.isSource()) {
				setParameters(root, operatorsMap.get(root), session, dd);
			}
		}
	}
	
	
	@SuppressWarnings({ "unchecked" })
	private void setParameters(IQDLOperator operator, ILogicalOperator logicalOp, ISession session, IDataDictionary dd) {		
		Class<? extends ILogicalOperator> opClass = typeDictionary.getOperatorBuilder(logicalOp.getName()).getOperatorClass();
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(opClass, Object.class);
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return;
		}
			
		Map<String, Object> parameters = operator.getParameters();
		for (PropertyDescriptor curProperty : beanInfo.getPropertyDescriptors()) {
			Method writeMethod = curProperty.getWriteMethod();
			Method readMethod = curProperty.getReadMethod();
			if (readMethod != null && writeMethod != null && writeMethod.isAnnotationPresent(Parameter.class)) {
				Parameter parameterAnnotation = writeMethod.getAnnotation(Parameter.class);
				String parameterName = parameterAnnotation.name();
				if (Strings.isNullOrEmpty(parameterName)) {
					parameterName = curProperty.getName();
				}
				if (!parameters.containsKey(parameterName.toLowerCase())) {
					continue;
				}
				Class<? extends IParameter<?>> valueParameterType = (Class<? extends IParameter<?>>) parameterAnnotation.type();
				Class<? extends IParameter<?>> keyParameterType = (Class<? extends IParameter<?>>) parameterAnnotation.keytype();

				boolean isList = parameterAnnotation.isList();
				boolean isMap = parameterAnnotation.isMap();
		
				Class<?> valueType = typeDictionary.getParameterValue(valueParameterType);
				Class<?> keyType = null;

				if (isMap) {
					keyType = typeDictionary.getParameterValue(keyParameterType);
				}
				
				Object parameterValue = getParameterValue(parameters.get(parameterName.toLowerCase()), valueParameterType, valueType, keyParameterType, keyType, isList, isMap, logicalOp, session, dd);
				try {
					writeMethod.invoke(logicalOp, parameterValue);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
 			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	private Object getParameterValue(Object parameterValue, Class<? extends IParameter<?>> valueParameterType, Class<?> valueType, Class<? extends IParameter<?>> keyParameterType, Class<?> keyType, boolean isList, boolean isMap, ILogicalOperator operator, ISession session, IDataDictionary dd) {
		if (isList) {
			List<Object> result = new ArrayList<>();
			for (Object element : (List<?>) parameterValue) {
				if (isComplexParameterType(valueType)) {
					result.add(toParameter(element, valueParameterType, operator, session, dd));
				} else {
					result.add(element);
				}
			}
			return result;
		} else if (isMap) {
			Map<Object, Object> result = new HashMap<>();
			for (Entry<Object, Object> e : ((Map<Object, Object>) parameterValue).entrySet()) {
				Object key = null;
				Object value = null;
				if (isComplexParameterType(keyType)) {
					key = toParameter(e.getKey(), keyParameterType, operator, session, dd);
				} else {
					key = e.getKey();
				}
				if (isComplexParameterType(valueType)) {
					value = toParameter(e.getValue(), valueParameterType, operator, session, dd);
				} else {
					value = e.getValue();
				}
				result.put(key, value);
			}
			return result;
		} else {
			return toParameter(parameterValue, valueParameterType, operator, session, dd);
		}		
	}
	
	private boolean isComplexParameterType(Class<?> parameterValue) {
		if (parameterValue != null) {
			if (parameterValue == Byte.class) {
				return false;
			} else if (parameterValue == Short.class) {
				return false;
			} else if (parameterValue == Integer.class) {
				return false;
			} else if (parameterValue == Long.class) {
				return false;
			} else if (parameterValue == Float.class) {
				return false;
			} else if (parameterValue == Double.class) {
				return false;
			} else if (parameterValue == Boolean.class) {
				return false;
			} else if (parameterValue == Character.class) {
				return false;
			} else if (parameterValue == String.class) {
				return false;
			} else if (parameterValue.isPrimitive()) {
				return false;
			}
		}
		return true;
	}
	
	private Object toParameter(Object parameterValue,	Class<? extends IParameter<?>> parameterType, ILogicalOperator operator, ISession session, IDataDictionary dd) {
		try {
			IParameter<?> parameter = parameterType.newInstance();
			parameter.setInputValue(parameterValue);
			parameter.setCaller(session);
			parameter.setDataDictionary(dd);
			List<SDFSchema> schemata = new ArrayList<>();
			for (LogicalSubscription subs : operator.getSubscribedToSource()) {
				schemata.add(subs.getSchema());
			}
			IAttributeResolver attributeResolver = new DirectAttributeResolver(schemata);
			parameter.setAttributeResolver(attributeResolver);
			return parameter.getValue();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private TopAO createTopAO(Collection<IQDLOperator> roots, Map<IQDLOperator, ILogicalOperator> operatorsMap) {
		TopAO topAO = new TopAO();
		int i = 0; 
		for (IQDLOperator op : roots) {
			ILogicalOperator root = operatorsMap.get(op);
			topAO.subscribeToSource(root, i++, 0, root.getOutputSchema());
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
	
	private Collection<IQDLOperator> getOperators(Collection<IQDLOperator> roots) {
		Collection<IQDLOperator> operators = new HashSet<>();
		for (IQDLOperator root : roots) {
			collectOperators(root, operators);
		}
		return operators;
	}
	
	private void collectOperators(IQDLOperator operator, Collection<IQDLOperator> visitedOperators) {
		visitedOperators.add(operator);
		for (IQLSubscription subs : operator.getSubscriptionsToSource()) {
			if (!visitedOperators.contains(subs.getSource())) {
				collectOperators(subs.getSource(), visitedOperators);
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
		public void create(IQDLOperator operator) {
			ILogicalQuery query = createLogicalQuery(operator, dd, session);
			query.setParameter(QUERY_COMMAND, "#ADDQUERY");
			queries.add(query);
		}

		@Override
		public void createWithMultipleSinks(List<IQDLOperator> operators) {
			ILogicalQuery query = createLogicalQuery(operators, dd, session);
			query.setParameter(QUERY_COMMAND, "#ADDQUERY");
			queries.add(query);
		}

		@Override
		public void start(IQDLOperator operator) {
			ILogicalQuery query = createLogicalQuery(operator, dd, session);
			query.setParameter(QUERY_COMMAND, "#RUNQUERY");
			queries.add(query);
		}

		@Override
		public void startWithMultipleSinks(List<IQDLOperator> operators) {
			ILogicalQuery query = createLogicalQuery(operators, dd, session);
			query.setParameter(QUERY_COMMAND, "#RUNQUERY");
			queries.add(query);
		}
		
		public List<ILogicalQuery> getQueries() {
			return this.queries;
		}

		@Override
		public void create(String name, IQDLOperator operator) {
			ILogicalQuery query = createLogicalQuery(operator, dd, session);
			query.setParameter(QUERY_COMMAND, "#ADDQUERY");
			query.setName(name);
			queries.add(query);			
		}

		@Override
		public void createWithMultipleSinks(String name,List<IQDLOperator> operators) {
			ILogicalQuery query = createLogicalQuery(operators, dd, session);
			query.setParameter(QUERY_COMMAND, "#ADDQUERY");
			query.setName(name);
			queries.add(query);			
		}

		@Override
		public void start(String name, IQDLOperator operator) {
			ILogicalQuery query = createLogicalQuery(operator, dd, session);
			query.setParameter(QUERY_COMMAND, "#RUNQUERY");
			query.setName(name);
			queries.add(query);			
		}

		@Override
		public void startWithMultipleSinks(String name,	List<IQDLOperator> operators) {
			ILogicalQuery query = createLogicalQuery(operators, dd, session);
			query.setParameter(QUERY_COMMAND, "#RUNQUERY");
			query.setName(name);
			queries.add(query);			
		}		
	}
	
	
	
//	
//	private String setPreParserKeywords(IQDLQuery query, String pql) {
//		StringBuilder b = new StringBuilder();
//		b.append("#PARSER "+"PQL"+System.lineSeparator());	
//		boolean queryCmd = false;
//		b.append("#QNAME "+query.getName()+System.lineSeparator());	
//		for (IPair<String, Object> metadata : query.getMetadata()) {
//			if (metadata.getE1().equalsIgnoreCase("query")) {
//				queryCmd = true;
//			} else if (metadata.getE1().equalsIgnoreCase("addquery")) {
//				queryCmd = true;
//			} else if (metadata.getE1().equalsIgnoreCase("runquery")) {
//				queryCmd = true;
//			}
//			b.append("#"+metadata.getE1().toUpperCase() +" ");		
//			if (metadata.getE2() != null) {
//				b.append(getPreParserKeywordValue(metadata.getE2())+System.lineSeparator());		
//			} else {
//				b.append(System.lineSeparator());		
//			}
//		}
//		if (!queryCmd) {
//			b.append("#ADDQUERY"+System.lineSeparator());	
//		}
//		b.append(pql);
//		System.out.println(b.toString());
//		return b.toString();
//	}
	

}
