package de.uniol.inf.is.odysseus.server.intervalapproach.state;

import java.io.Serializable;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;

/**
 * The current state of an {@link AggregateTIPO} is defined by 
 * its transfer area and its groups.
 * 
 * @author Michael Brand
 *
 */
public class AggregateTIPOState<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>> implements Serializable {
	
	private static final long serialVersionUID = 9088231287860150949L;

	private ITransferArea<W,W> transferArea;
	
	private Map<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups;

	public ITransferArea<W,W> getTransferArea() {
		return transferArea;
	}

	public void setTransferArea(ITransferArea<W,W> transferArea) {
		this.transferArea = transferArea;
	}

	public Map<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> getGroups() {
		return groups;
	}

	public void setGroups(Map<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups) {
		this.groups = groups;
	}
	
}