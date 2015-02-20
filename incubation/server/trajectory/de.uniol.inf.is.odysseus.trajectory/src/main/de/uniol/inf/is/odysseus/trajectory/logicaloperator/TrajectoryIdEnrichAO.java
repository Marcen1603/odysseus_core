package de.uniol.inf.is.odysseus.trajectory.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * The logical operator for enriching trajectories with Ids.
 * 
 * @author marcus
 *
 */
@LogicalOperator(name = "PLEASEDONTUSEMEBECAUSEIAMANOPERATORONLYFORACERTAINUSECASEYESYOUARERIGHTANDBECAUSEOFTHISIHAVETHISNAMENOTTOCNAMECLASHWITHOTHEROPERATORSYEAHMAYBEIAMTHEOPERATORWITHTHELONGESTINNAMEINWHOLEFUCKINGODYSSEUS", minInputPorts = 1, maxInputPorts = 1, doc="Enrich Trajectory with Ids", category={LogicalOperatorCategory.ADVANCED})
public class TrajectoryIdEnrichAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6559476897670445930L;

	public TrajectoryIdEnrichAO() {	}
	
	public TrajectoryIdEnrichAO(final TrajectoryIdEnrichAO trajectoryIdEnricherAO) {	
		super(trajectoryIdEnricherAO);
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(final int port) {
		return TrajectoryConstructAO.OUTPUT_SCHEMA;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryIdEnrichAO(this);
	}
}
