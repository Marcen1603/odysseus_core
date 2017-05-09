package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class PredicateCoalescePO<M extends ITimeInterval> extends
		AbstractCoalescePO<M> implements IHasPredicate {

	@SuppressWarnings("rawtypes")
	private IPredicate predicate;

	public PredicateCoalescePO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			@SuppressWarnings("rawtypes") IPredicate predicate) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
		this.predicate = predicate;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public void process_open() throws OpenFailedException {
		getGroupProcessor().init();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_done(int port) {
		if (currentPartialAggregates != null) {
			PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(
					currentPartialAggregates, false);
			IStreamObject<M> out = getGroupProcessor().createOutputElement(0L,
					result);
			out.setMetadata((M) currentPartialAggregates.getMetadata().clone());
			transfer(out);
			currentPartialAggregates = null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(IStreamObject<M> object, int port) {
		super.process_next(object, port);

		// The created object is a combiniation of all objects before
		// --> so the new object needs the start timestamp of the first
		// participating object and the other metadata of the last
		// participating object (maybe a metadata merge function should be used)

		if (currentPartialAggregates == null) {
			currentPartialAggregates = calcInit(object);
			currentPartialAggregates.setMetadata((M) object.getMetadata()
					.clone());
		} else {
			// TODO: Is it really neceessary to create a new partial aggregate?
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<M>>, M> newP = calcMerge(
					currentPartialAggregates, object, true);
			newP.setMetadata((M) currentPartialAggregates.getMetadata().clone());
			currentPartialAggregates = newP;
		}
		if (predicate.evaluate(object)) {
			PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(
					currentPartialAggregates, false);
			// create IStreamObject
			IStreamObject<M> out = getGroupProcessor().createOutputElement(0L,
					result);
			M metadata = (M) object.getMetadata().clone();
			metadata.setStart(currentPartialAggregates.getMetadata().getStart());
			out.setMetadata(metadata);
			transfer(out);
			sendPunctuations(getWatermark());
			currentPartialAggregates = null;
		} else {
			createHeartbeat(object.getMetadata().getStart());
		}

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		IPunctuation outPunctuation = predicate.processPunctuation(punctuation);
		super.processPunctuation(outPunctuation, port);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IPredicate getPredicate() {
		return predicate;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {

		if (! (ipo instanceof PredicateCoalescePO)){
			return false;
		}

		@SuppressWarnings("rawtypes")
		PredicateCoalescePO po = (PredicateCoalescePO) ipo;

		if (! this.predicate.equals(po)){
			return false;
		}

		return super.isSemanticallyEqual(ipo);
	}

	@Override
	public void setPredicate(IPredicate<?> predicate) {
		// TODO Auto-generated method stub
		
	}

}
