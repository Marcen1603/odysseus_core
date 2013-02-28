package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
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

	protected PriorityQueue<IPunctuation> punctuationsOutputQueue = new PriorityQueue<IPunctuation>(
			11, new Comparator<IPunctuation>() {
				@Override
				public int compare(IPunctuation left, IPunctuation right) {
					return left.getTime().compareTo(right.getTime());
				}
			});
	private PointInTime lastStart = null;

	public AbstractCoalescePO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// if there is a calculation, we need to keep the punctuations
		if (currentPartialAggregates != null) {
			punctuationsOutputQueue.add(punctuation);
		} else { // else send
			sendPunctuation(punctuation);
		}
	}
	
	@Override
	protected void process_next(IStreamObject<? extends M> object, int port) {
		lastStart = object.getMetadata().getStart();
	}
	
	protected void sendPunctuations() {
		IPunctuation punc = punctuationsOutputQueue.peek();
		while (punc !=null && punc.getTime().before(lastStart)){
			sendPunctuation(punctuationsOutputQueue.poll());
			punc = punctuationsOutputQueue.peek();
		}
	}


	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

}
