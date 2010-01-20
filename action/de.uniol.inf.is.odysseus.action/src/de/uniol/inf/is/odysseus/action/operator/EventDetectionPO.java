package de.uniol.inf.is.odysseus.action.operator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.IActionParameter;
import de.uniol.inf.is.odysseus.action.output.StreamAttributeParameter;
import de.uniol.inf.is.odysseus.action.output.IActionParameter.ParameterType;
import de.uniol.inf.is.odysseus.action.services.dataExtraction.IDataExtractor;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.services.exception.DataextractionException;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

/**
 * Physical Operator calling a list of Actions when an Object is processed
 * @author Simon Flandergan
 *
 * @param <T>
 */
public class EventDetectionPO<T> extends AbstractSink<T>{
	private Map<Action, List<IActionParameter>> actions;
	private String type;
	private static IDataExtractor dataExtractor;

	public EventDetectionPO(Map<Action, List<IActionParameter>> actions, String type) {
		super();
		this.actions = actions;
		this.type = type;
	}
	
	public void bindDataExtractor(IDataExtractor extractor){
		dataExtractor = extractor;
	}


	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		//extract parameters
		for (Entry<Action, List<IActionParameter>> entry : actions.entrySet()){
			List<IActionParameter> parameterList = entry.getValue();
			Object[] parameters = new Object[parameterList.size()];
			int index = 0;
			for (IActionParameter param : parameterList){
				if (param.getType().equals(ParameterType.Value)){
					//static value
					parameters[index] = param.getValue();
				}else {
					//value must be extracted from attribute
					Object identifier = ((StreamAttributeParameter)param.getValue());
					try {
						parameters[index] = dataExtractor.extractAttribute(identifier, object, this.type);
					} catch (DataextractionException e) {
						parameters[index] = null;
						System.err.println("Attribute: "+identifier+" wasn't extracted. \n" +
								"Reason: "+ e.getMessage());
					}
				}
				index++;
			}
			try {
				entry.getKey().executeMethod(parameters);
			} catch (ActuatorException e) {
				//Shouldnt happen, because method & parameters are checked during creation of action
				System.err.println(e.getMessage());
			}
		}
	}
	
	public Map<Action, List<IActionParameter>> getActions() {
		return actions;
	}
}
