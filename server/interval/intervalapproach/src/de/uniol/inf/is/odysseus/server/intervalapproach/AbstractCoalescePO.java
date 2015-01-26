package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class AbstractCoalescePO<M extends ITimeInterval> extends AggregatePO<M, IStreamObject<? extends M>, IStreamObject<M>> {

	PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> currentPartialAggregates = null;

	protected PriorityQueue<IPunctuation> punctuationsOutputQueue = new PriorityQueue<IPunctuation>(11, new Comparator<IPunctuation>() {
		@Override
		public int compare(IPunctuation left, IPunctuation right) {
			return left.getTime().compareTo(right.getTime());
		}
	});
	private PointInTime lastStart = null;

	private long heartbeatRate = -1;

	private long counter = 0;

	public AbstractCoalescePO(SDFSchema inputSchema, SDFSchema outputSchema, List<SDFAttribute> groupingAttributes, Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations, false);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// if there is a calculation, we need to keep the punctuations
		if (currentPartialAggregates != null) {
			punctuationsOutputQueue.add(punctuation);
			sendPunctuations();
		} else { // else send
			sendPunctuation(punctuation);
		}
	}

	@Override
	protected void process_next(IStreamObject<? extends M> object, int port) {
		lastStart = object.getMetadata().getStart();
	}

	protected void createHeartbeat(PointInTime time) {
		if (heartbeatRate > 0 && counter == heartbeatRate) {
			counter = 0;
			punctuationsOutputQueue.add(Heartbeat.createNewHeartbeat(time));
			sendPunctuations();
		}
		counter++;

	}

	protected void sendPunctuations() {
		IPunctuation punc = punctuationsOutputQueue.peek();
		while (punc != null && punc.getTime().before(lastStart)) {
			sendPunctuation(punctuationsOutputQueue.poll());
			punc = punctuationsOutputQueue.peek();
		}
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	public long getHeartbeatRate() {
		return heartbeatRate;
	}

	public void setHeartbeatRate(long heartbeatRate) {
		this.heartbeatRate = heartbeatRate;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}
}
