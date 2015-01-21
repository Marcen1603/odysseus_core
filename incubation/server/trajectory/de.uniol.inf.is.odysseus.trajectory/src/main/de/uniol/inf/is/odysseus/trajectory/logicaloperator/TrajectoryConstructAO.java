package de.uniol.inf.is.odysseus.trajectory.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

@LogicalOperator(name = "TRAJECTORYCONSTRUCT", minInputPorts = 1, maxInputPorts = 1, doc="Construct Trajectories", category={LogicalOperatorCategory.ADVANCED})
public class TrajectoryConstructAO extends UnaryLogicalOp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8226843002104428660L;
	
	public final static String VEHICLE_ID_ATTR_NAME = "VehicleId";
	
	public final static String START_TIMESTAMP_ATTR_NAME = "StartTimestamp";
	
	public final static String END_TIMESTAMP_ATTR_NAME = "EndTimestamp";
	
	public final static String POINTS_ATTR_NAME = "Points";
	
	
	/**
	 * Output schema
	 */
	private final static SDFSchema OUTPUT_SCHEMA = new SDFSchema(
			TrajectoryConstructAO.class.getName(), 
			Tuple.class, 
			new SDFAttribute(null, VEHICLE_ID_ATTR_NAME, SDFDatatype.STRING, null),
			new SDFAttribute(null, START_TIMESTAMP_ATTR_NAME, SDFDatatype.STRING, null),
			new SDFAttribute(null, END_TIMESTAMP_ATTR_NAME, SDFDatatype.STRING, null),
			new SDFAttribute(null, POINTS_ATTR_NAME, SDFSpatialDatatype.LIST , null)
	);
	
	
	private boolean subtrajectories = false;
	
	
	public TrajectoryConstructAO() {
	}
	
	public TrajectoryConstructAO(boolean subtrajectories) {
		this.subtrajectories = subtrajectories;
	}
	
	public TrajectoryConstructAO(TrajectoryConstructAO trajectoryConstructAO) {
		super(trajectoryConstructAO);
		
		this.subtrajectories = trajectoryConstructAO.subtrajectories;
	}
	
	
//#######################################
	// Own parameters
	
	@Parameter(name = "SUBTRAJECTORIES", type = BooleanParameter.class, isList = false, optional = true)
	public void setSubtrajectories(final boolean subtrajectories) {
		this.subtrajectories = subtrajectories;
	}
	
	public boolean getSubtrajectories() {
		return this.subtrajectories;
	}
	
	
	
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return OUTPUT_SCHEMA;
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryConstructAO(this);
	}
	
}
