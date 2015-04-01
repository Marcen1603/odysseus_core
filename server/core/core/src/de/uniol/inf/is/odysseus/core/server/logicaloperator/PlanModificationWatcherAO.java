package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * A logical operator that can be used to monitor plan modifications
 * 
 * @author Marco Grawunder
 *
 */
@LogicalOperator(name="PlanModificationWatcher", maxInputPorts=0, minInputPorts=0, doc="TODO", category = { LogicalOperatorCategory.PLAN})
public class PlanModificationWatcherAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 8034681550356402521L;
	private static SDFSchema outputSchema;
	
	
	public PlanModificationWatcherAO() {
	}
		
	public PlanModificationWatcherAO(
			PlanModificationWatcherAO planModificationWatcherAO) {
		super(planModificationWatcherAO);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {		// TODO Auto-generated method stub
		if (outputSchema == null){
			ArrayList<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			attributes.add(new SDFAttribute("", "ts", SDFDatatype.TIMESTAMP, null));
			attributes.add(new SDFAttribute("", "qid", SDFDatatype.LONG, null));
			attributes.add(new SDFAttribute("", "type", SDFDatatype.STRING, null)); // PlanModificationEventType?
			
			outputSchema = SDFSchemaFactory.createNewSchema("", (Class<? extends IStreamObject<?>>) Tuple.class, attributes);
		}
		return outputSchema;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PlanModificationWatcherAO(this);
	}

}
