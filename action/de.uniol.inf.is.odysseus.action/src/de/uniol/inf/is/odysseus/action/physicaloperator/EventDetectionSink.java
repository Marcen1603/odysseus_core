package de.uniol.inf.is.odysseus.action.physicaloperator;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class EventDetectionSink<T> extends AbstractSink<T>{
	private Map<Action, String[]> actionParameterMap;

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		//extract parameters
		for (Entry<Action, String[]> entry : actionParameterMap.entrySet()){
			//TODO fetch attributes from tuple
			entry.getKey().executeMethod(null);
		}
	}
}
