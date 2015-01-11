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
	
	
	private boolean subtrajectories = false;
	
	/**
	 * Output schema
	 */
	private static final SDFSchema OUTPUT_SCHEMA = new SDFSchema(
			TrajectoryConstructAO.class.getName(), 
			Tuple.class, 
			new SDFAttribute(null, "VehicleId", SDFDatatype.STRING, null),
			new SDFAttribute(null, "Points", SDFSpatialDatatype.LIST , null)
	);
	
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
