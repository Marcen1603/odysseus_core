package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class CoalescePO<M extends ITimeInterval> extends
		AggregatePO<M, IStreamObject<? extends M>, IStreamObject<M>> {

	int lastGroupID = -1;
	PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> currentPartialAggregates = null;
	
	public CoalescePO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(IStreamObject<? extends M> object, int port) {
		// 1st check if the same group as last one
		Integer currentGroupID = getGroupProcessor().getGroupID(object);
		
		// TODO: Think about metadata
		
		if (currentPartialAggregates != null && currentGroupID == lastGroupID) {
			calcMerge(currentPartialAggregates, object);
		}else{
			PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(currentPartialAggregates);
			// create IStreamObject
			IStreamObject<M> out = getGroupProcessor().createOutputElement(lastGroupID, result);
			M metadata = currentPartialAggregates.getMetadata();
			metadata.setEnd(object.getMetadata().getEnd());
			out.setMetadata(metadata);
			transfer(out);
			// init new PartialAggregate
			currentPartialAggregates = calcInit(object);
			currentPartialAggregates.setMetadata(object.getMetadata());
		}
	}

	@Override
	public AbstractPipe<IStreamObject<? extends M>, IStreamObject<M>> clone() {
		throw new RuntimeException("Sorry, currently not implemented!");
	}

}
