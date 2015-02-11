package de.uniol.inf.is.odysseus.trajectory.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

@LogicalOperator(name = "NONANANA", minInputPorts = 1, maxInputPorts = 1, doc="Construct Trajectories", category={LogicalOperatorCategory.ADVANCED})
public class TrajectoryIdEnricherAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6559476897670445930L;

	public TrajectoryIdEnricherAO() {	}
	
	public TrajectoryIdEnricherAO(TrajectoryIdEnricherAO trajectoryIdEnricherAO) {	}
	
	/**
	 * Output schema
	 */
	public final static SDFSchema OUTPUT_SCHEMA = new SDFSchema(
			TrajectoryConstructAO.class.getName(), 
			Tuple.class, 
			new SDFAttribute(null, "VehicleId", SDFDatatype.STRING, null),
			new SDFAttribute(null, "TrajectoryId", SDFDatatype.INTEGER, null),
			new SDFAttribute(null, "Points", SDFSpatialDatatype.LIST , null)
	);
	
	@Override
	public SDFSchema getOutputSchemaIntern(int port) {
		return TrajectoryConstructAO.OUTPUT_SCHEMA;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryIdEnricherAO(this);
	}
}
