package de.uniol.inf.is.odysseus.action.operator;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.action.exception.OperatorException;
import de.uniol.inf.is.odysseus.action.operator.AttributeExtractor.Datatype;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.ActionAttribute;
import de.uniol.inf.is.odysseus.action.output.ActionParameter;
import de.uniol.inf.is.odysseus.action.output.ActionParameter.ParameterType;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

/**
 * Physical Operator calling a list of Actions when an Object is processed
 * @author Simon Flandergan
 *
 * @param <T>
 */
public class EventDetectionPO<T> extends AbstractSink<T>{
	private Map<Action, ArrayList<ActionParameter>> actions;
	private Datatype type;

	public EventDetectionPO(Map<Action, ArrayList<ActionParameter>> actions, String type) throws OperatorException {
		super();
		this.actions = actions;
		this.type = AttributeExtractor.determineDataType(type);
	}


	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		//extract parameters
		for (Entry<Action, ArrayList<ActionParameter>> entry : actions.entrySet()){
			ArrayList<ActionParameter> parameterList = entry.getValue();
			Object[] parameters = new Object[parameterList.size()];
			int index = 0;
			for (ActionParameter param : parameterList){
				if (param.getType().equals(ParameterType.Value)){
					//static value
					parameters[index] = param.getValue();
				}else {
					//value must be extracted from attribute
					Object identifier = ((ActionAttribute)param.getValue()).getIdentifier();
					parameters[index] = AttributeExtractor.extractAttribute(identifier, object, this.type);
				}
				index++;
			}
			entry.getKey().executeMethod(parameters);
		}
	}
}
