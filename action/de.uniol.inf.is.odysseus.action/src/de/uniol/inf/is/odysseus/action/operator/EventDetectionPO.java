package de.uniol.inf.is.odysseus.action.operator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.action.dataExtraction.DataExtractor;
import de.uniol.inf.is.odysseus.action.exception.DataextractionException;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.IActionParameter;
import de.uniol.inf.is.odysseus.action.output.StreamAttributeParameter;
import de.uniol.inf.is.odysseus.action.output.IActionParameter.ParameterType;
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

	public EventDetectionPO(Map<Action, List<IActionParameter>> actions, String type) throws DataextractionException {
		super();
		this.actions = actions;
		this.type = type;
	}


	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		DataExtractor extractor = DataExtractor.getInstance();
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
						parameters[index] = extractor.extractAttribute(identifier, object, this.type);
					} catch (DataextractionException e) {
						parameters[index] = null;
						System.err.println("Attribute: "+identifier+" wasn't extracted. \n" +
								"Reason: "+ e.getMessage());
					}
				}
				index++;
			}
			entry.getKey().executeMethod(parameters);
		}
	}
}
