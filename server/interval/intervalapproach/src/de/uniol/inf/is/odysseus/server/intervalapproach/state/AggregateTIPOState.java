package de.uniol.inf.is.odysseus.server.intervalapproach.state;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IMerger;
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
public class AggregateTIPOState<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>> implements Serializable, IOperatorState {
	
	private static final long serialVersionUID = 9088231287860150949L;

	private ITransferArea<W,W> transferArea;
	
	private Map<Long, DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groups;
	private Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IEvaluator<R, W>> eval = new HashMap<FESortedClonablePair<SDFSchema, AggregateFunction>, IEvaluator<R, W>>();
	private Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IInitializer<R>> init = new HashMap<FESortedClonablePair<SDFSchema, AggregateFunction>, IInitializer<R>>();
	private Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IMerger<R>> merger = new HashMap<FESortedClonablePair<SDFSchema, AggregateFunction>, IMerger<R>>();
	
	
	
	public Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IEvaluator<R, W>> getEval() {
		return eval;
	}
	
	public Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IMerger<R>> getMerger() {
		return merger;
	}
	
	public void setMerger(Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IMerger<R>> merger) {
		this.merger = merger;
	}
	
	public Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IInitializer<R>> getInit() {
		return init;
	}
	
	public void setInit(Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IInitializer<R>> init) {
		this.init = init;
	}
	
	public void setEval(Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IEvaluator<R, W>> eval) {
		this.eval = eval;
	}

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