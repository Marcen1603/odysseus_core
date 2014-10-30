package de.uniol.inf.is.odysseus.peer.smarthome.server.logicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.peer.smarthome.server.physicaloperator.ActivityConfigurationTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "ActivityConfigurationACCESS", doc = "Returns the activity configuration of an entity.", category = { LogicalOperatorCategory.SOURCE })
public class ActivityConfigurationAccessAO extends AbstractAccessAO {

	private static final long serialVersionUID = 1L;
	private String activityName;
	private String entityName;

	public ActivityConfigurationAccessAO() {
		super();
		setTransportHandler(ActivityConfigurationTransportHandler.NAME);
		setWrapper(Constants.GENERIC_PULL);
		setDataHandler("Tuple");
		setProtocolHandler("none");

		List<SDFAttribute> schema = new LinkedList<>();
		schema.add(new SDFAttribute(null, "ConfigEntityName", SDFDatatype.STRING,
				null));
		schema.add(new SDFAttribute(null, "ConfigActivityName", SDFDatatype.STRING,
				null));
		setAttributes(schema);
	}

	public ActivityConfigurationAccessAO(ActivityConfigurationAccessAO other) {
		super(other);
		this.entityName = other.entityName;
		this.activityName = other.activityName;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ActivityConfigurationAccessAO(this);
	}

	@Parameter(name = "entity", doc = "The entity name", type = StringParameter.class, optional = false)
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityName() {
		return this.entityName;
	}

	@Parameter(name = "activity", doc = "The activity name", type = StringParameter.class, optional = false)
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityName() {
		return this.activityName;
	}

	@Override
	public boolean isSourceOperator() {
		return true;
	}

	@Override
	public boolean isSinkOperator() {
		return false;
	}
}
