package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class StartStopPredicateCoalescePO<M extends ITimeInterval> extends
		AggregatePO<M, IStreamObject<M>, IStreamObject<M>> {

	final static int START = 0;
	final static int END = 1;

	@SuppressWarnings("rawtypes")
	final private List<IPredicate> predicates = new LinkedList<IPredicate>();
	final private Map<Object, PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<M>>, M>> currentPAs = new HashMap<>();

	public StartStopPredicateCoalescePO(SDFSchema inputSchema,
			SDFSchema outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			@SuppressWarnings("rawtypes") IPredicate startPredicate,
			@SuppressWarnings("rawtypes") IPredicate endPredicate) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations, false, null);
		predicates.add(startPredicate);
		predicates.add(endPredicate);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		getGroupProcessor().init();
		currentPAs.clear();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void process_next(IStreamObject<M> object, int port) {
		Object groupID = getGroupProcessor().getGroupID(object);
		PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<M>>, M> pas = currentPAs
				.get(groupID);
		// when calcuation is running
		if (pas != null) {
			if (predicates.get(END).evaluate(object)) {
				PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(
						pas, false);
				IStreamObject<M> out = getGroupProcessor().createOutputElement(
						groupID, result);
				out.setMetadata(pas.getMetadata());
				transfer(out);

				// Clear partial Aggregates
				currentPAs.remove(groupID);
			} else {
				// merge
				PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<M>>, M> newP = calcMerge(
						pas, object, true);
				M newMeta = (M) object.getMetadata().clone();
				// keep start
				newMeta.setStart(pas.getMetadata().getStart());
				newP.setMetadata(newMeta);
				// update pa
				currentPAs.put(groupID, newP);

				// TODO: createHeartbeat(newMeta.getStart());

			}
		} else {
			if (predicates.get(START).evaluate(object)) {
				pas = calcInit(object);
				pas.setMetadata((M) object.getMetadata().clone());
				currentPAs.put(groupID, pas);
			}
		}

	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO handle punctuations correclty
		//	sendPunctuation(punctuation);
	}

	@Override
	protected void process_close() {
		dumpGroups();
	}

	@Override
	protected void process_done(int port) {
		dumpGroups();
	}

	private void dumpGroups() {
		synchronized (currentPAs) {
			for (Entry<Object, PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<M>>, M>> e : currentPAs
					.entrySet()) {
				PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<M>>, M> currentPartialAggregates = e
						.getValue();
				PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(
						currentPartialAggregates, false);
				IStreamObject<M> out = getGroupProcessor().createOutputElement(
						e.getKey(), result);
				out.setMetadata(currentPartialAggregates.getMetadata());
				transfer(out);
			}
			currentPAs.clear();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof StartStopPredicateCoalescePO)){
			return false;
		}
		
		StartStopPredicateCoalescePO po = (StartStopPredicateCoalescePO) ipo;
		
		if (this.predicates.size() != po.predicates.size()){
			return false;
		}
		
		Iterator<IPredicate> otherIter = po.predicates.iterator();
		for (IPredicate p: predicates){
			if (!p.equals(otherIter.next())){
				return false;
			}
		}
		
		
		return super.isSemanticallyEqual(ipo);
	}
}
