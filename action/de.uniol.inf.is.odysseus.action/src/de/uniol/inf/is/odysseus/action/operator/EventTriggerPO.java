package de.uniol.inf.is.odysseus.action.operator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.IActionParameter;
import de.uniol.inf.is.odysseus.action.output.IActionParameter.ParameterType;
import de.uniol.inf.is.odysseus.action.services.dataExtraction.IDataExtractor;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

/**
 * Physical Operator calling a list of Actions when an Object is processed
 * @author Simon Flandergan
 *
 * @param <T>
 */
public class EventTriggerPO<T> extends AbstractSink<T>{
	private Map<Action, List<IActionParameter>> actions;
	private String type;
	private Logger logger;
	private static IDataExtractor dataExtractor;

	/**
	 * Public Constructor used by OSGI
	 */
	public EventTriggerPO(){
		
	}
	
	/**
	 * Public Constructor for Transformation rule
	 * @param actions
	 * @param type
	 */
	public EventTriggerPO(Map<Action, List<IActionParameter>> actions, String type) {
		super();
		this.actions = actions;
		this.type = type;
		this.logger = LoggerFactory.getLogger(EventTriggerPO.class);
	}
	
	/**
	 * Method called by OSGi to fulfill binding
	 * @param extractor
	 */
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
					Object identifier = param.getValue();
					try {
						parameters[index] = dataExtractor.extractAttribute(object, identifier, this.type);
					} catch (Exception e) {
						this.logger.error("Attribute: "+identifier+" wasn't extracted. \n" +
								"Reason: "+ e.getMessage());
						return;
					} 
				}
				index++;
			}
			try {
				entry.getKey().executeMethod(parameters);
			} catch (ActuatorException e) {
				//Shouldnt happen, because method & parameters are checked during creation of action
				this.logger.error("Method execution failed: "+e.getMessage());
			}
		}
	}
	
	public Map<Action, List<IActionParameter>> getActions() {
		return actions;
	}
}
