package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * This is a special, latency optimized version of the aggregate operator
 * elements are written without valid time interval, so there is no need to wait
 * 
 * @author Marco Grawunder
 *
 * @param
 * 			<Q>
 * @param <R>
 * @param <W>
 */

public class AggregateTIPO2<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends AggregateTIPO<Q, R, W> {

	public AggregateTIPO2(SDFSchema inputSchema, SDFSchema outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations, boolean fastGrouping,
			IMetadataMergeFunction<Q> metadataMerge) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations, fastGrouping, metadataMerge);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	protected void produceResults(List<PairMap<SDFSchema, AggregateFunction, W, Q>> results, Long groupID,
			IGroupProcessor<R, W> g) {
		// Remark: These are the results from already evaluated events, because
		// of the overwritten method
		// preCalcEval only dirty events (i.e. events that are not written) are
		// inside this list
		// --> all elements can be send to transfer area
		for (PairMap<SDFSchema, AggregateFunction, W, Q> e : results) {
			W out = g.createOutputElement(groupID, e);
			@SuppressWarnings("unchecked")
			Q newMeta = (Q) e.getMetadata().clone();
			// remove time interval!
			newMeta.setEnd(PointInTime.INFINITY);
			out.setMetadata(newMeta);
			//transferArea.transfer(out);
			transfer(out);
		}

	}

	@Override
	protected void produceResults(List<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> results,
			Long groupID, IGroupProcessor<R, W> g, PointInTime trigger,
			Map<Long, ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> groupsToProcess, int inPort) {

		// result contains Elements that are ready (i.e. end time stamp before
		// trigger)
		
		// now add elements that contains the current trigger:
		for (Entry<Long, ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>>> entry : groupsToProcess
				.entrySet()) {

			ITimeIntervalSweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa = entry
					.getValue();
			results.addAll(sa.queryContains(trigger));

		}
		Iterator<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> resultIter = results.iterator();
		while (resultIter.hasNext()) {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> e = resultIter.next();
			W out = null;
			// Only create output for dirty elements
			Iterator<Entry<FESortedClonablePair<SDFSchema, AggregateFunction>, IPartialAggregate<R>>> iter = e
					.entrySet().iterator();
			if (iter.hasNext()) {
				if (iter.next().getValue().isDirty()) {
					if (outputPA) {
						out = g.createOutputElement2(groupID, e);
					} else {
						PairMap<SDFSchema, AggregateFunction, W, ? extends ITimeInterval> r = calcEval(e, true, false);
						out = g.createOutputElement(groupID, r);
					}
					@SuppressWarnings("unchecked")
					Q newMeta = (Q) e.getMetadata().clone();
					// remove time interval!
					newMeta.setEnd(PointInTime.INFINITY);
					out.setMetadata(newMeta);
					//transferArea.transfer(out);
					transfer(out);
				}
			}
		}
		//transferArea.newHeartbeat(trigger, inPort);

	}

	@Override
	@SuppressWarnings("unchecked")
	protected void preCalcEval(_Point<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> p2,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q> lastPartialAggregate,
			List<PairMap<SDFSchema, AggregateFunction, W, Q>> returnValues) {

		Set<Entry<FESortedClonablePair<SDFSchema, AggregateFunction>, IPartialAggregate<R>>> entrySet = lastPartialAggregate
				.entrySet();
		// Only if pa is not already written (== isDirty)
		if (entrySet.size() > 0 && entrySet.iterator().next().getValue().isDirty()) {
			PairMap<SDFSchema, AggregateFunction, W, Q> v = calcEval(lastPartialAggregate, false, false);
			v.setMetadata((Q) lastPartialAggregate.getMetadata().clone());
			v.getMetadata().setEnd(p2.getPoint());
			returnValues.add(v);
		}
	}

}
