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
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.IQLSubscription;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;

public class OdysseusScriptGenerator {
	
	@Inject
	private IQDLTypeDictionary typeDictionary;

	public ILogicalQuery createLogicalQuery(IQDLQuery query, IDataDictionary dd, ISession session) {
		
		Collection<IQDLOperator> operators = query.execute();
		Map<IQDLOperator, ILogicalOperator> operatorsMap = new HashMap<>();
		Map<String, StreamAO> sourcesMap = new HashMap<>();

		for (IQDLOperator operator : operators) {
			if (operator.isSource()) {
				operatorsMap.put(operator, getSource(operator.getName(), sourcesMap, dd , session));
			} else {
				operatorsMap.put(operator, QDLServiceBinding.createOperator(operator.getName()));
			}
		}
		
		Collection<IQDLOperator> roots = getRoots(operators);
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
		ILogicalQuery logQuery = createLogicalQuery(query, dd, session);
		String pql = QDLServiceBinding.getPQLGenerator().generatePQLStatement(logQuery.getLogicalPlan());	
		String script = setPreParserKeywords(query, pql);
		return script;
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
			root.subscribeSink(topAO, i++, 0, root.getOutputSchema());
		}
		return topAO;
	}
	
	
	private String setPreParserKeywords(IQDLQuery query, String pql) {
		StringBuilder b = new StringBuilder();
		b.append("#PARSER "+"PQL"+System.lineSeparator());	
		boolean queryCmd = false;
		for (IPair<String, Object> metadata : query.getMetadata()) {
			if (metadata.getE1().equalsIgnoreCase("query")) {
				queryCmd = true;
			} else if (metadata.getE1().equalsIgnoreCase("addquery")) {
				queryCmd = true;
			} else if (metadata.getE1().equalsIgnoreCase("runquery")) {
				queryCmd = true;
			}
			b.append("#"+metadata.getE1().toUpperCase() +" ");		
			if (metadata.getE2() != null) {
				b.append(getPreParserKeywordValue(metadata.getE2())+System.lineSeparator());		
			} else {
				b.append(System.lineSeparator());		
			}
		}
		if (!queryCmd) {
			b.append("#ADDQUERY"+System.lineSeparator());	
		}
		b.append(pql);
		System.out.println(b.toString());
		return b.toString();
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
	

}
