package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryConstructAO;
import de.uniol.inf.is.odysseus.trajectory.transform.TTrajectoryCompareAORule;

/**
 * This is a physical operator for {@link TrajectoryConstructAO}. This class actually isn't needed
 * because the superclass {@link AggregateTIPO} could be instantiated in {@link TTrajectoryCompareAORule}
 * instead.
 * @author marcus
 *
 * @param <Q>
 * @param <R>
 * @param <W>
 */
public class TrajectoryConstructPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>> 
		extends AggregateTIPO<Q,R,W> {

	/**
	 * 
	 * @param inputSchema
	 * @param outputSchema
	 * @param groupingAttributes
	 * @param aggregations
	 * @param fastGrouping
	 */
	public TrajectoryConstructPO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			boolean fastGrouping) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations, fastGrouping);
	}

	/**
	 * 
	 * @param aggregatePO
	 */
	public TrajectoryConstructPO(AggregateTIPO<Q, R, W> aggregatePO) {
		super(aggregatePO);
	}
}
