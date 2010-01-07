package de.uniol.inf.is.odysseus.action.operator;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.IActionParameter;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Logical Operator for an EventDetectionSink
 * @author Simon Flandergan
 *
 */
public class EventDetectionAO extends AbstractLogicalOperator {
	private Map<Action, List<IActionParameter>> actions;
	
	private static final long serialVersionUID = 5876348180345982247L;
	
	public EventDetectionAO(Map<Action, List<IActionParameter>> actionsToExecute) {
		this.actions = actionsToExecute;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		//FIXME darf null sein oder schema der subscription liefern???
		//does not provide any output
		return null;
	}
	
	/**
	 * Return actions, which should be executed
	 * @return
	 */
	public Map<Action, List<IActionParameter>> getActions() {
		return actions;
	}

}
