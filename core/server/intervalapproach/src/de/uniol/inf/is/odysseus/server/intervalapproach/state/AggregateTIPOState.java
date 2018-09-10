package de.uniol.inf.is.odysseus.server.intervalapproach.state;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * The current state of an {@link AggregateTIPO} is defined by 
 * its transfer area and its groups.
 * 
 * @author Michael Brand
 *
 */
public class AggregateTIPOState<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>> extends AbstractOperatorState  {
	
	private static final long serialVersionUID = 9088231287860150949L;

	private ITransferArea<W,W> transferArea;
	
	private Map<Object, ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups;
	private Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IAggregateFunction<R,W>> init = new HashMap<>();
			
	public Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IAggregateFunction<R,W>> getAggregateFunctions() {
		return init;
	}
	
	public void setAggregateFunctions(Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IAggregateFunction<R,W>> init) {
		this.init = init;
	}
	
	public ITransferArea<W,W> getTransferArea() {
		return transferArea;
	}

	public void setTransferArea(ITransferArea<W,W> transferArea) {
		this.transferArea = transferArea;
	}

	public Map<Object, ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> getGroups() {
		return groups;
	}

	public void setGroups(Map<Object, ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups) {
		this.groups = groups;
	}

	@Override
	public Serializable getSerializedState() {
		return this;
	}

	@Override
	public long estimateSizeInBytes() {
		//As this state does not tend to be really big we return the real size
		return getSizeInBytesOfSerializable(this);
	}
	
}