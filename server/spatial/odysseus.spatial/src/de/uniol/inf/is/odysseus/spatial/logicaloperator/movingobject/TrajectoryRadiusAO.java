package de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "MovingObjectTrajectoryRadius", doc = "Does a radius query on the given trajectories.", category = {
		LogicalOperatorCategory.SPATIAL })
public class TrajectoryRadiusAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 5056150871620243720L;

	private boolean queryAllMovingObjects = true;
	private List<String> movingObjectsToQuery;

	public TrajectoryRadiusAO() {
		super();
		this.setMovingObjectsToQuery(new ArrayList<>());
	}

	public TrajectoryRadiusAO(TrajectoryRadiusAO ao) {
		super(ao);
		this.queryAllMovingObjects = ao.isQueryAllMovingObjects();
		this.movingObjectsToQuery = ao.getMovingObjectsToQuery();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryRadiusAO(this);
	}

	public boolean isQueryAllMovingObjects() {
		return queryAllMovingObjects;
	}

	@Parameter(name = "queryAllMovingObjects", optional = false, type = BooleanParameter.class, isList = false, doc = "If set to true, the radius query will be done for all known moving objects.")
	public void setQueryAllMovingObjects(boolean queryAllMovingObjects) {
		this.queryAllMovingObjects = queryAllMovingObjects;
	}

	public List<String> getMovingObjectsToQuery() {
		return movingObjectsToQuery;
	}

	@Parameter(name = "movingObjectIDs", optional = true, type = StringParameter.class, isList = true, doc = "IDs if the moving objects to do the radius query (used if not set to query all IDs).")
	public void setMovingObjectsToQuery(List<String> movingObjectsToQuery) {
		this.movingObjectsToQuery = movingObjectsToQuery;
	}

}
