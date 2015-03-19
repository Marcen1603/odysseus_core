package de.uniol.inf.is.odysseus.rcp.viewer.server.opdetail;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.UnionPO;
import de.uniol.inf.is.odysseus.rcp.AbstractKeyValueUpdaterProvider;

public class UnionOperatorDetailProvider extends
		AbstractKeyValueUpdaterProvider<UnionPO<?>> {

	@Override
	public String getTitle() {
		return "Union";
	}

	@Override
	protected Map<String, String> getKeyValuePairs(UnionPO<?> operator) {
		Map<String, String> map = new HashMap<>();
		map.put("OutputQueueSize", operator.getElementsStored1()+"");
		map.put("Watermark", operator.getWatermark()+"");
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends UnionPO<?>> getOperatorType() {
		return (Class<? extends UnionPO<?>>) UnionPO.class;
	}

}
