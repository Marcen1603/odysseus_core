package de.uniol.inf.is.odysseus.action.physicaloperator;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.ActionParameter;
import de.uniol.inf.is.odysseus.action.output.ActionParameter.ParameterType;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class EventDetectionSink<T> extends AbstractSink<T>{
	private Map<Action, ArrayList<ActionParameter>> actionParameterMap;

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		//extract parameters
		for (Entry<Action, ArrayList<ActionParameter>> entry : actionParameterMap.entrySet()){
			ArrayList<ActionParameter> parameterList = entry.getValue();
			Object[] parameters = new Object[parameterList.size()];
			int index = 0;
			for (ActionParameter param : parameterList){
				if (param.getType().equals(ParameterType.Value)){
					parameters[index] = param.getValue();
				}else {
					//TODO extract attribute from source
				}
				index++;
			}
			entry.getKey().executeMethod(parameters);
		}
	}
}
