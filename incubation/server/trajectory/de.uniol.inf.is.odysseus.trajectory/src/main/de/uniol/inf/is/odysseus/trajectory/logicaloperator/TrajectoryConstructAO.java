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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder.NestAggregateItem;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder.NestAggregateItemParameter;

@LogicalOperator(name = "TRAJECTORYCONSTRUCT", minInputPorts = 1, maxInputPorts = 1, doc="Construct Trajectories", category={LogicalOperatorCategory.ADVANCED})
public class TrajectoryConstructAO extends UnaryLogicalOp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8226843002104428660L;
		
		
	private boolean subtrajectories = false;
	
	private SDFAttribute trajectoryId;
	
	private NestAggregateItem positionMapping;
	
	
	public TrajectoryConstructAO() {
	}
	
	public TrajectoryConstructAO(TrajectoryConstructAO trajectoryConstructAO) {
		super(trajectoryConstructAO);
		
		this.subtrajectories = trajectoryConstructAO.subtrajectories;
		this.trajectoryId = trajectoryConstructAO.trajectoryId;
		this.positionMapping = trajectoryConstructAO.positionMapping;
	}
	
	
//#######################################
	// Own parameters
	
	@Parameter(name = "SUBTRAJECTORIES", type = BooleanParameter.class)
	public void setSubtrajectories(final boolean subtrajectories) {
		this.subtrajectories = subtrajectories;
	}
	
	@Parameter(name = "TRAJECTORYID", type = ResolvedSDFAttributeParameter.class)
	public void setTrajectoryId(final SDFAttribute trajectoryId) {
		this.trajectoryId = trajectoryId;
	}
	
	@Parameter(name = "POSITIONMAP", type = NestAggregateItemParameter.class)
	public void setPositionMapping(final NestAggregateItem positionMapping) {
		this.positionMapping = positionMapping;
	}
	
	
	public boolean getSubtrajectories() {
		return this.subtrajectories;
	}
	
	public SDFAttribute getTrajectoryId() {
		return this.trajectoryId;
	}
	
	public NestAggregateItem getPositionMapping() {
		return this.positionMapping;
	}
	
	
	/**
	 * Output schema
	 */
	public final static SDFSchema OUTPUT_SCHEMA = new SDFSchema(
			TrajectoryConstructAO.class.getName(), 
			Tuple.class, 
			new SDFAttribute(null, "VehicleId", SDFDatatype.STRING, null),
			new SDFAttribute(null, "TrajectoryId", SDFDatatype.INTEGER, null),
			new SDFAttribute(null, "Positions", SDFSpatialDatatype.LIST , null)
	);
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return OUTPUT_SCHEMA;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryConstructAO(this);
	}
	
}
