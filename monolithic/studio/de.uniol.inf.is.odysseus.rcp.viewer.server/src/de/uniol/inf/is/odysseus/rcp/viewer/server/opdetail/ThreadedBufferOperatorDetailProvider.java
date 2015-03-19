package de.uniol.inf.is.odysseus.rcp.viewer.server.opdetail;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.ThreadedBufferPO;
import de.uniol.inf.is.odysseus.rcp.AbstractKeyValueUpdaterProvider;

public class ThreadedBufferOperatorDetailProvider extends
		AbstractKeyValueUpdaterProvider<ThreadedBufferPO<?>> {

	@Override
	public String getTitle() {
		return "ThreadedBuffer";
	}

	@Override
	protected Map<String, String> getKeyValuePairs(ThreadedBufferPO<?> operator) {
		Map<String, String> map = new HashMap<>();
		map.put("CurrentSize", operator.getElementsStored1()+"");
		map.put("InputQueue", operator.getInputBufferSize()+"");
		map.put("OutputQueue", operator.getOutputBufferSize()+"");
		map.put("started",operator.isRunning()+"");
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends ThreadedBufferPO<?>> getOperatorType() {
		return (Class<? extends ThreadedBufferPO<?>>) ThreadedBufferPO.class;
	}

}
