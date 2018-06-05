package de.uniol.inf.is.odysseus.rcp.opdetailprovider;

import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.AbstractKeyValueGeneralProvider;

public class ParameterInfoOperatorDetailProvider extends AbstractKeyValueGeneralProvider {

	@Override
	public String getTitle() {
		return "ParameterInfo";
	}

	@Override
	protected Map<String, String> getKeyValuePairs(IPhysicalOperator operator) {
		Map<String, String> map = Maps.newHashMap();
		
		for( String parameterInfoKey : operator.getParameterInfos().keySet()) {
			map.put(parameterInfoKey, operator.getParameterInfos().get(parameterInfoKey));
		}

		return map;
	}
}
