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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class CoalescePO<M extends ITimeInterval> extends
		AggregatePO<M, IStreamObject<? extends M>, IStreamObject<M>> implements
		IHasPredicate {

	int lastGroupID = -1;
	PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> currentPartialAggregates = null;

	@SuppressWarnings("rawtypes")
	private IPredicate predicate;

	public CoalescePO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	public void setPredicate(@SuppressWarnings("rawtypes") IPredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public void process_open() throws OpenFailedException {
		if (this.predicate != null) {
			this.predicate.init();
		}
		getGroupProcessor().init();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(IStreamObject<? extends M> object, int port) {
		if (predicate == null) { // Mode grouping

			// 1st check if the same group as last one
			Integer currentGroupID = getGroupProcessor().getGroupID(object);

			// TODO: Think about metadata

			if (currentPartialAggregates != null
					&& currentGroupID == lastGroupID) {
				PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> newP = calcMerge(currentPartialAggregates, object);
				newP.setMetadata(currentPartialAggregates.getMetadata());
				currentPartialAggregates = newP;
			} else {
				if (currentPartialAggregates != null) {
					PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(currentPartialAggregates);
					// create IStreamObject
					IStreamObject<M> out = getGroupProcessor()
							.createOutputElement(lastGroupID, result);
					M metadata = currentPartialAggregates.getMetadata();
					metadata.setEnd(object.getMetadata().getStart());
					out.setMetadata(metadata);
					transfer(out);
				}
				// init new PartialAggregate
				currentPartialAggregates = calcInit(object);
				currentPartialAggregates.setMetadata(object.getMetadata());
			}
			lastGroupID = currentGroupID;
		} else { // Mode predicate
			if (currentPartialAggregates == null) {
				currentPartialAggregates = calcInit(object);
				currentPartialAggregates.setMetadata(object.getMetadata());
			}else{
				PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> newP = calcMerge(currentPartialAggregates, object);
				newP.setMetadata(currentPartialAggregates.getMetadata());
				currentPartialAggregates = newP;
			}
			if (predicate.evaluate(object)) {
				PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(currentPartialAggregates);
				// create IStreamObject
				IStreamObject<M> out = getGroupProcessor()
						.createOutputElement(lastGroupID, result);
				M metadata = currentPartialAggregates.getMetadata();
				metadata.setEnd(object.getMetadata().getEnd());
				out.setMetadata(metadata);
				transfer(out);
				currentPartialAggregates = null;
			}

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
