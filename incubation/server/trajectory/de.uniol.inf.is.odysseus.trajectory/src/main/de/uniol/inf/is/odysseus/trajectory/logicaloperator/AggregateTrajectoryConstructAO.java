package de.uniol.inf.is.odysseus.trajectory.logicaloperator;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder.NestAggregateItemParameter;

/**
 * 
 * TrajectoryConstructAO is the logical operator for creating routes from GPS measurements.
 * To do so TrajectoryConstruct extends {@link AggregateAO} and forbids setting the "AGGREGATIONS" and
 * and "GROUP_BY" Parameters. The grouping automatically is done by setting the "TRAJECTORY_ID" parameter
 * which points to the attribute name where the grouping shall happen. Furthermore the operator 
 * demands to point to the attribute which shall be nested. This has to be the position data of the tuple.
 * 
 * @author marcus
 *
 */
@LogicalOperator(name = "TTT", minInputPorts = 1, maxInputPorts = 1, doc="Construct Trajectories", category={LogicalOperatorCategory.ADVANCED})
public class AggregateTrajectoryConstructAO extends AggregateAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6402497964848377680L;
	
	public AggregateTrajectoryConstructAO() {
	}
	
	public AggregateTrajectoryConstructAO(AggregateTrajectoryConstructAO trajectoryConstructAO) {
		super(trajectoryConstructAO);
	}
	
//#######################################
	// Own parameters
	
	/**
	 * Sets the name of the attribute where a trajectory is identified.
	 * @param trajectoryId the name of the attribute where a trajectory is identified
	 */
	@Parameter(name = "TRAJECTORY_ID", type = ResolvedSDFAttributeParameter.class)
	public void setTrajectoryId(final SDFAttribute trajectoryId) {
		this.setGroupingAttributes(Arrays.asList(trajectoryId));
	}
	
	/**
	 * Sets the name of the attribute where the positions are stored.
	 * @param positionAttributeName the name of the attribute where the positions are stored
	 */
	@Parameter(name = "POSITION_ATTRIBUTE_NAME", type = NestAggregateItemParameter.class)
	public void setPositionAttributeName(final AggregateItem positionAttributeName) {
		this.setAggregationItems(Arrays.asList(positionAttributeName));
	}
	
//#######################################//
	// Overrides Parameters that they can't be set in the query //
	
	/**
	 * 
	 */
	@Override
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		super.setGroupingAttributes(attributes);
	}
	
	/**
	 * 
	 */
	@Override
	public void setAggregationItems(List<AggregateItem> aggregations) {
		super.setAggregationItems(aggregations);
	}
	
//#######################################
	
	@Override
	public AggregateAO clone() {
		return new AggregateTrajectoryConstructAO(this);
	}
}
