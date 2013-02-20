package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class PredicateCoalescePO<M extends ITimeInterval> extends
		AbstractCoalescePO<M> implements IHasPredicate {

	@SuppressWarnings("rawtypes")
	private IPredicate predicate;

	public PredicateCoalescePO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,@SuppressWarnings("rawtypes")IPredicate predicate,
			 ITransferArea<IStreamObject<?>, IStreamObject<?>> transferArea) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations, transferArea);
		this.predicate = predicate;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public void process_open() throws OpenFailedException {
		this.predicate.init();
		getGroupProcessor().init();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(IStreamObject<? extends M> object, int port) {

		// The created object is a combiniation of all objects before
		// --> so the new object needs the start timestamp of the first
		// participating object and the other metadata of the last
		// participating object (maybe a metadata merge function should be used)
		
		if (currentPartialAggregates == null) {
			currentPartialAggregates = calcInit(object);
		} else {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> newP = calcMerge(
					currentPartialAggregates, object);
			newP.setMetadata(currentPartialAggregates.getMetadata());
			currentPartialAggregates = newP;
		}
		if (predicate.evaluate(object)) {
			PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(currentPartialAggregates);
			// create IStreamObject
			IStreamObject<M> out = getGroupProcessor().createOutputElement(0,
					result);
			M metadata = object.getMetadata();
			metadata.setStart(currentPartialAggregates.getMetadata().getStart());
			out.setMetadata(metadata);
			transfer(out);
			currentPartialAggregates = null;
		}

	}

	@Override
	public AbstractPipe<IStreamObject<? extends M>, IStreamObject<M>> clone() {
		throw new RuntimeException("Sorry, currently not implemented!");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IPredicate getPredicate() {
		return predicate;
	}

}
