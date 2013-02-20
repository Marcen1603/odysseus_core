package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class AbstractCoalescePO<M extends ITimeInterval> extends
		AggregatePO<M, IStreamObject<? extends M>, IStreamObject<M>> {

	PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> currentPartialAggregates = null;

	public AbstractCoalescePO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (currentPartialAggregates == null){
			sendPunctuation(punctuation);
		}
	}
	
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

}
