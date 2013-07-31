package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.pql.generator.AbstractPQLStatementGenerator;

public class StandardPQLStatementGenerator extends AbstractPQLStatementGenerator<ILogicalOperator> {

	@Override
	protected String generateParameters(ILogicalOperator operator) {
		Map<String, String> parameterMap = removeNullValues(operator.getParameterInfos());
		parameterMap.put("NAME", "'" + operator.getName() + "'");
		
		StringBuilder sb = new StringBuilder();
		sb.append(toPQLString(parameterMap));
		return sb.toString();
	}

	private String toPQLString(Map<String, String> parameterMap) {
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

	@Override
	public Class<ILogicalOperator> getOperatorClass() {
		return ILogicalOperator.class;
	}
}
