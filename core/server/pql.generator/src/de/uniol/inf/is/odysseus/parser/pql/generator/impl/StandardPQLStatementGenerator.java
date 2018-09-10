package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.pql.generator.AbstractPQLStatementGenerator;

public class StandardPQLStatementGenerator<T extends ILogicalOperator> extends AbstractPQLStatementGenerator<T> {

	@Override
	protected String generateParameters(T operator) {
		Map<String, String> parameterMap = removeNullValues(determineParameterMap(operator));
		parameterMap.put("NAME", "'" + operator.getName() + "'");
		
		return toPQLString(parameterMap);
	}
	
	protected Map<String, String> determineParameterMap(T operator) {
		return operator.getParameterInfos();
	}

	private static String toPQLString(Map<String, String> parameterMap) {
		StringBuilder sb = new StringBuilder();
		if (!parameterMap.isEmpty()) {
			String[] keys = parameterMap.keySet().toArray(new String[0]);
			for (int i = 0; i < keys.length; i++) {
				sb.append(keys[i]).append("=").append(parameterMap.get(keys[i]));
				if (i < keys.length - 1) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}
	
	private static Map<String, String> removeNullValues(Map<String, String> parameterInfos) {
		Map<String, String> result = Maps.newHashMap();
		for (String key : parameterInfos.keySet()) {
			if (parameterInfos.get(key) != null) {
				result.put(key, parameterInfos.get(key));
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getOperatorClass() {
		return (Class<T>) ILogicalOperator.class;
	}
}
