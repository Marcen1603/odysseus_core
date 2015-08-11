package de.uniol.inf.is.odysseus.iql.qdl.parser;



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
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.basic.typing.ParameterFactory;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;

public class OdysseusScriptGenerator {
	
	@Inject
	private QDLTypeFactory factory;

	public String createOdysseusScript(IQDLQuery query, IDataDictionary dd, ISession session) {
		query.setDataDictionary(dd);
		query.setSession(session);		
		
		Collection<IQDLOperator> operators = query.execute();
		for (IQDLOperator operator : operators) {
			setParameters(operator, session, dd);
		}
		Collection<IQDLOperator> roots = getRoots(operators);
		TopAO topAO = createTopAO(roots);
		String pql = QDLServiceBinding.getPQLGenerator().generatePQLStatement(topAO);	
		String script = setPreParserKeywords(query, pql);
		return script;
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

	
	@SuppressWarnings({ "unchecked" })
	private void setParameters(IQDLOperator operator, ISession session, IDataDictionary dd) {		
		Class<? extends ILogicalOperator> opClass = factory.getOperatorBuilder(operator.getLogicalOperator().getName()).getOperatorClass();
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
		
				Class<?> valueType = ParameterFactory.getParameterValue(valueParameterType);
				Class<?> keyType = null;

				if (isMap) {
					keyType = ParameterFactory.getParameterValue(keyParameterType);
				}
				
				Object parameterValue = getParameterValue(parameters.get(parameterName.toLowerCase()), valueParameterType, valueType, keyParameterType, keyType, isList, isMap, operator.getLogicalOperator(), session, dd);
				try {
					writeMethod.invoke(operator.getLogicalOperator(), parameterValue);
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
				if (ParameterFactory.isComplexParameterType(valueParameterType)) {
					result.add(ParameterFactory.toParameter(element, valueParameterType, operator, session, dd));
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
				if (ParameterFactory.isComplexParameterType(keyParameterType)) {
					key = ParameterFactory.toParameter(e.getKey(), keyParameterType, operator, session, dd);
				} else {
					key = e.getKey();
				}
				if (ParameterFactory.isComplexParameterType(valueParameterType)) {
					value = ParameterFactory.toParameter(e.getValue(), valueParameterType, operator, session, dd);
				} else {
					value = e.getValue();
				}
				result.put(key, value);
			}
			return result;
		} else {
			return ParameterFactory.toParameter(parameterValue, valueParameterType, operator, session, dd);
		}		
	}
	

	public TopAO createTopAO(Collection<IQDLOperator> sources) {
		TopAO topAO = new TopAO();
		int i = 0; 
		for (IQDLOperator op : sources) {
			topAO.subscribeToSource(op.getLogicalOperator(), i++, 0, op.getLogicalOperator().getOutputSchema());
		}
		return topAO;
	}

	
	private Collection<IQDLOperator> getRoots(Collection<IQDLOperator> operators) {
		Collection<IQDLOperator> roots = new HashSet<>();
		for (IQDLOperator op : operators) {
			if (op.getLogicalOperator().getSubscriptions().size() == 0) {
				roots.add(op);
			}
		}
		return roots;
	}
}
